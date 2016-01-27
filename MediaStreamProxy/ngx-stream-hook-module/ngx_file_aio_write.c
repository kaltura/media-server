/*
 * ngx_file_aio_write.c
 *
 *  Created on: Dec 23, 2015
 *      Author: igor shevach
 */

#include <ngx_config.h>
#include <ngx_core.h>
#include <ngx_event.h>
#include <linux/fs.h>
#include "ngx_file_aio.h"


extern int            ngx_eventfd;
extern aio_context_t  ngx_aio_ctx;


static void
ngx_dump_file_aio_cleanup_handler(ngx_event_t *ev);

static void
ngx_dump_file_aio_cleanup(ngx_file_aio_t *faio);

static int
io_submit(aio_context_t ctx, long n, struct iocb **paiocb)
{
    return syscall(SYS_io_submit, ctx, n, paiocb);
}

static int
io_cancel(aio_context_t ctx, struct iocb *paiocb)
{
	struct io_event ev;
    return syscall(SYS_io_cancel, ctx, paiocb,&ev);
}

ngx_int_t
ngx_append_on(ngx_file_t *file){
	ngx_int_t flags = fcntl (file->fd, F_GETFL, 0);
		flags |= O_APPEND;
		if( -1 == fcntl (file->fd, F_SETFL, flags) ){
			ngx_errno = errno;
			return NGX_ERROR;
		}
		return NGX_OK;
}

ngx_int_t
ngx_append_off(ngx_file_t *file){

	ngx_int_t flags = fcntl (file->fd, F_GETFL, 0);
	flags &= ~O_APPEND;
	if( -1 == fcntl (file->fd, F_SETFL, flags) ){
		ngx_errno = errno;
		return NGX_ERROR;
	}
	return NGX_OK;
}

size_t
ngx_directio_memalign(ngx_file_t *file)
{
	int  align;
	if( -1 == ioctl(file->fd,BLKSSZGET,&align) ){
		return 512;
	}
	return align;
}

ssize_t
ngx_file_aio_destroy(ngx_file_aio_t *faio)
{
	if(faio){
		ngx_event_aio_t  *aio;
		ngx_event_t      *ev;
		ngx_file_t *file = &faio->tf.file;

		int err = 0;

		if (!ngx_file_aio) {
			return NGX_OK;
		}

		aio = file->aio;

		if (aio == NULL){
			return NGX_ERROR;
		}

		ev = &aio->event;

		ev->closed = 1;

		if(faio->num_of_ios == 0){
			ngx_dump_file_aio_cleanup(faio);
		} else {
			ngx_log_debug1(NGX_LOG_DEBUG_CORE, file->log, 0,
					"aio destroyed %V", &file->name);
			if( io_cancel(ngx_aio_ctx,&aio->aiocb) == -1 ){
				err = ngx_errno;
				return NGX_ERROR;
			} else  {
				faio->num_of_ios = 0;
				ngx_dump_file_aio_cleanup(faio);
			}
		}
	}
	return NGX_OK;
}

ssize_t
ngx_file_aio_write(ngx_file_aio_t *faio, u_char *buf, size_t size, off_t offset,
    ngx_pool_t *pool, ngx_event_handler_pt handler)
{
    ngx_err_t         err;
    struct iocb      *piocb[1];
    ngx_event_t      *ev;
    ngx_event_aio_t  *aio;
    ngx_file_t *file = &faio->tf.file;

/*    size_t i = 0;
    for(;i < size;i++){
    	buf[i] = buf[i];
    }
*/
    if (!ngx_file_aio) {
        return ngx_write_file(file, buf, size, offset);
    }

	 aio = file->aio;

    if (file->aio == NULL && ngx_file_aio_init(file, pool) != NGX_OK) {
        return NGX_ERROR;
    }

    aio = file->aio;
    ev = &aio->event;

    if (ev->closed){
    	   ngx_log_error(NGX_LOG_CRIT, file->log, ngx_errno,
    	                      "aio write \"%s\" attempt to write on closed file!", file->name.data);
    	   return NGX_ERROR;
    }

    if (!ev->ready) {
     	//ngx_log_error(NGX_LOG_CRIT,
        ngx_log_debug(NGX_LOG_DEBUG_CORE,
        		file->log, 0,
                      "previous aio hasn't finished yet for  \"%V\" req: [buf=%d sz=%d off=%d]  aio: [buf=%d n=%d off=%d] aio->res=%d", &file->name,
                      buf, size, offset,
                      aio->aiocb.aio_buf,
                      aio->aiocb.aio_nbytes,
                      aio->aiocb.aio_offset,
                      aio->res);
        return NGX_AGAIN;
    }

    ngx_log_debug4(NGX_LOG_DEBUG_CORE, file->log, 0,
                   "aio complete:%d @%O:%z %V",
                   ev->complete, offset, size, &file->name);

    if (ev->complete) {
        ev->active = 0;
        ev->complete = 0;

        if (aio->res >= 0) {
            ngx_set_errno(0);
            return aio->res;
        }

        ngx_set_errno(-aio->res);

        ngx_log_error(NGX_LOG_CRIT, file->log, ngx_errno,
                      "aio write \"%s\" failed", file->name.data);

        return NGX_ERROR;
    }

    ngx_memzero(&aio->aiocb, sizeof(struct iocb));

    aio->aiocb.aio_data = (uint64_t) (uintptr_t) ev;
    aio->aiocb.aio_lio_opcode = IOCB_CMD_PWRITE;
    aio->aiocb.aio_fildes = file->fd;
    aio->aiocb.aio_buf = (uint64_t) (uintptr_t) buf;
    aio->aiocb.aio_nbytes = size;
    aio->aiocb.aio_offset = offset;
    aio->aiocb.aio_flags = IOCB_FLAG_RESFD;
    aio->aiocb.aio_resfd = ngx_eventfd;

   	//ngx_log_error(NGX_LOG_ERR,
   	ngx_log_debug(NGX_LOG_DEBUG_STREAM,
   			faio->tf.file.log, 0, "ngx_file_aio_write %s aio: [%d,%d,%d] ",
   			faio->tf.file.name.data,aio->aiocb.aio_buf,aio->aiocb.aio_nbytes, aio->aiocb.aio_offset);

    aio->handler = handler;

    ev->handler = ngx_dump_file_aio_cleanup_handler;

    piocb[0] = &aio->aiocb;

	aio->res = 0;

    if (io_submit(ngx_aio_ctx, 1, piocb) == 1) {
        ev->active = 1;
        ev->ready = 0;
        ev->complete = 0;
        faio->num_of_ios++;
        return NGX_AGAIN;
    }

    err = ngx_errno;

    if (err == NGX_EAGAIN) {
        return ngx_write_file(file, buf, size, offset);
    }

    ngx_log_error(NGX_LOG_CRIT, file->log, err,
                  "io_submit(\"%V\") failed", &file->name);

    if (err == NGX_ENOSYS) {
        ngx_file_aio = 0;
        return ngx_write_file(file, buf, size, offset);
    }

    return NGX_ERROR;
}

ssize_t
ngx_file_aio_writev(ngx_file_aio_t *faio, struct iovec *vec, size_t size, off_t offset,
    ngx_pool_t *pool, ngx_event_handler_pt handler)
{
    ngx_err_t         err;
    struct iocb      *piocb[1];
    ngx_event_t      *ev;
    ngx_event_aio_t  *aio;
    ngx_file_t *file = &faio->tf.file;

/*    size_t i = 0;
    for(;i < size;i++){
    	buf[i] = buf[i];
    }
*/
    if (!ngx_file_aio) {
    	fallback:

    	if (lseek(file->fd, offset, SEEK_SET) == -1) {
    		ngx_log_error(NGX_LOG_CRIT, file->log, ngx_errno,
    				"lseek() \"%s\" failed", file->name.data);
    		return NGX_ERROR;
    	}
    	return writev(file->fd, vec, size);
    }

	 aio = file->aio;

    if (file->aio == NULL && ngx_file_aio_init(file, pool) != NGX_OK) {
        return NGX_ERROR;
    }

    aio = file->aio;
    ev = &aio->event;

    if (ev->closed){
    	   ngx_log_error(NGX_LOG_CRIT, file->log, ngx_errno,
    	                      "aio write \"%s\" attempt to write on closed file!", file->name.data);
    	   return NGX_ERROR;
    }

    if (!ev->ready) {
     	//ngx_log_error(NGX_LOG_CRIT,
        ngx_log_debug(NGX_LOG_DEBUG_CORE,
        		file->log, 0,
                      "previous aio hasn't finished yet for  \"%V\" req: [buf=%d sz=%d off=%d]  aio: [buf=%d n=%d off=%d] aio->res=%d", &file->name,
                      vec, size, offset,
                      aio->aiocb.aio_buf,
                      aio->aiocb.aio_nbytes,
                      aio->aiocb.aio_offset,
                      aio->res);
        return NGX_AGAIN;
    }

    ngx_log_debug4(NGX_LOG_DEBUG_CORE, file->log, 0,
                   "aio complete:%d @%O:%z %V",
                   ev->complete, offset, size, &file->name);

    if (ev->complete) {
        ev->active = 0;
        ev->complete = 0;

        if (aio->res >= 0) {
            ngx_set_errno(0);
            return aio->res;
        }

        ngx_set_errno(-aio->res);

        ngx_log_error(NGX_LOG_CRIT, file->log, ngx_errno,
                      "aio write \"%s\" failed", file->name.data);

        return NGX_ERROR;
    }

    ngx_memzero(&aio->aiocb, sizeof(struct iocb));

    aio->aiocb.aio_data = (uint64_t) (uintptr_t) ev;
    aio->aiocb.aio_lio_opcode = IOCB_CMD_PWRITEV;
    aio->aiocb.aio_fildes = file->fd;
    aio->aiocb.aio_buf = (uint64_t) (uintptr_t) vec;
    aio->aiocb.aio_nbytes = size;
    aio->aiocb.aio_offset = offset;
    aio->aiocb.aio_flags = IOCB_FLAG_RESFD;
    aio->aiocb.aio_resfd = ngx_eventfd;

   	//ngx_log_error(NGX_LOG_ERR,
   	ngx_log_debug(NGX_LOG_DEBUG_STREAM,
   			faio->tf.file.log, 0, "ngx_file_aio_write %s aio: [%d,%d,%d] ",
   			faio->tf.file.name.data,aio->aiocb.aio_buf,aio->aiocb.aio_nbytes, aio->aiocb.aio_offset);

    aio->handler = handler;

    ev->handler = ngx_dump_file_aio_cleanup_handler;

    piocb[0] = &aio->aiocb;

	aio->res = 0;

    if (io_submit(ngx_aio_ctx, 1, piocb) == 1) {
        ev->active = 1;
        ev->ready = 0;
        ev->complete = 0;
        faio->num_of_ios++;
        return NGX_AGAIN;
    }

    err = ngx_errno;

    if (err == NGX_EAGAIN) {
    	goto fallback;
    }

    ngx_log_error(NGX_LOG_CRIT, file->log, err,
                  "io_submit(\"%V\") failed", &file->name);

    if (err == NGX_ENOSYS) {
        ngx_file_aio = 0;
     	goto fallback;
    }

    return NGX_ERROR;
}


static void
ngx_dump_file_aio_cleanup_handler(ngx_event_t *ev)
{
	ngx_event_aio_t  *aio = ev->data;
	ngx_file_aio_t *faio = (ngx_file_aio_t*)aio->file;

	faio->num_of_ios--;

	if( ev->closed ){
		ngx_dump_file_aio_cleanup(faio);
	} else {
		aio->handler(ev);
	}
}

static void
ngx_dump_file_aio_cleanup(ngx_file_aio_t *faio)
{
	faio->cleanup(faio);
}

