/*
 * ngx_file_aio.c
 *
 *  Created on: Dec 23, 2015
 *      Author: igor shevach
 */

#include <ngx_config.h>
#include <ngx_core.h>
#include "ngx_file_aio.h"
#include "ngx_file_aio_write.h"

static ngx_inline  u_char*get_old_start_ptr(ngx_buf_aio_t *buf){
	return buf->old_start;
}

static ngx_inline void reset_old_start_ptr(ngx_buf_aio_t *buf,u_char *val) {
	if(get_old_start_ptr(buf) != NULL) {
		ngx_free(buf->old_start);
	}
	buf->old_start = val;
}

static ngx_inline size_t
ngx_dump_file_buf_calc_space(ngx_buf_aio_t *b, ngx_int_t total ){

	size_t space = b->end - b->start;

	if( b->pos == b->last && b->last == b->end ){
		b->pos = b->last = b->start;
	} else if(b->pos > b->last){
		space = b->pos - b->last;
	} else {
		space = b->end - b->last;
		if(total && b->pos > b->start){
			space += b->pos - b->start - 1;
		}
	}
	return space;
}


#ifdef MEM_PROF
	#define check_bounds(val,s,e)\
		if((s) > (val) || (val) > (e) )  raise(SIGSEGV)
#else
	#define check_bounds(val,s,e)
#endif


static ngx_inline void
advance_cpy_p( ngx_buf_aio_t *b,u_char **p,u_char *buf,size_t size) {
	if(!size)
		return;
	size_t space = ngx_min(size,(size_t)(b->end - *p));
	if(buf){
		ngx_memcpy(*p,buf,space);
		buf+=space;
	}
	size -= space;
	*p += space;
	if(size > 0){
		space = ngx_min(size,(size_t)(b->end - b->start));
		if(buf){
			ngx_memcpy(b->start,buf,space);
		}
		*p = b->start + space;
	}
}


static ngx_inline void
advance_cpy( ngx_buf_aio_t *b,u_char *buf,size_t size) {
	advance_cpy_p(b,&b->last,buf,size);
}

#define advance_pointer(buf,by) advance_cpy(buf,NULL,by)

#define assign(s,field,val) \
		check_bounds(val,(s)->start,(s)->end);\
		(s)->field = val


static ngx_pool_t *file_aio_pool = NULL;

static void
ngx_dump_file_aio_event_handler(ngx_event_t *ev);

static void
ngx_dump_file_aio_write_aligned(ngx_file_aio_t *faio);

static ngx_int_t
ngx_dump_file_aio_buf_check_grow(ngx_file_aio_t *faio,size_t required);

static void
ngx_dump_file_aio_cleanup(ngx_file_aio_t *faio);

static ngx_file_aio_t *
ngx_file_aio_alloc(ngx_log_t *log);


static void
ngx_dump_file_aio_cleanup(ngx_file_aio_t *faio)
{
	if( faio && faio->num_of_ios == 0) {
		ngx_close_file(faio->tf.file.fd);
		scramble_free(0xAB,file_aio_pool,faio->tf.file.aio);
		ngx_free(faio->buf.start);
		reset_old_start_ptr(&faio->buf,0);
	}
}

ngx_int_t
ngx_dump_file_aio_buf_create_circular(ngx_file_aio_t *faio,size_t bufsz)
{
	ngx_int_t rc;
	ngx_file_t *c = &faio->tf.file;
	ngx_file_t *file = &faio->tf.file;

	if(faio){
		ngx_uint_t align = faio->align;
		ngx_buf_aio_t  *b = &faio->buf;
		size_t pa_size = size_align(bufsz + ngx_pagesize - 1,~(ngx_pagesize-1)), limit = faio->ctx->conf->buf_limit_size;
		bufsz = pa_size + ~align+1;

		if(bufsz > limit){
			ngx_log_error(NGX_LOG_ERR,faio->tf.file.log, 0,
					"ngx_dump_file_aio_buf_create_circular \"%*s\" buffer size reached high memory limit %d > %d",
					file->name.len,file->name.data,bufsz,limit);
			return NGX_ERROR;
		}

		ngx_log_debug(NGX_LOG_DEBUG_STREAM,	//ngx_log_error(NGX_LOG_ERR,
				c->log, 0 , "ngx_dump_file_aio_buf_create_circular  \"%s\" %d", file->name.data,bufsz);


		u_char *bs = b->start,*bp = b->pos, *bl = b->last, *be = b->end;

		reset_old_start_ptr(b,b->start);
		ngx_log_debug(NGX_LOG_DEBUG_STREAM,
		//ngx_log_error(NGX_LOG_ERR,
				faio->tf.file.log, 0 ,  "ngx_dump_file_aio_buf_create_circular  \"%s\" [0, %d, %d, %d] aio: [%d,%d,%d] old ptr: %d",
				file->name.data,
				bp - bs,bl- bs,be - bs,
				file->aio ? file->aio->aiocb.aio_buf - (ngx_uint_t)bs : 0,
						file->aio ? file->aio->aiocb.aio_buf - (ngx_uint_t)bs + file->aio->aiocb.aio_nbytes : 0,
								file->aio ? file->aio->aiocb.aio_offset : 0,
										get_old_start_ptr(b));
		_PTR(b->start = ngx_memalign(~align+1,bufsz,c->log));

		b->pos = b->last = b->start;
		b->end = b->start + pa_size;

		if(bs){
			if(bp < bl){
				advance_cpy(b,bp,bl-bp);
			} else if( bl < bp ){
				advance_cpy(b,bp,be-bp);
				advance_cpy(b,bs,bl-bs);
			}
		}
		return NGX_OK;
	}
	error:
	return NGX_ERROR;
}

static ngx_file_aio_t *
ngx_file_aio_alloc(ngx_log_t *log){

	if(file_aio_pool == NULL){
		file_aio_pool = ngx_create_pool(NGX_DEFAULT_POOL_SIZE,log);
	}
	if(file_aio_pool == NULL){
		return NULL;
	}
	return ngx_pcalloc(file_aio_pool,sizeof(ngx_file_aio_t));
}

static const ngx_int_t dateTimePatternLen = sizeof("1000-10-10") - 1;


ngx_file_aio_t *
ngx_dump_file_aio_create(ngx_connection_t *c,
		ngx_stream_dump_file_handler_t *f,
		ngx_stream_dump_srv_conf_t *dscf,
		size_t bufSize){
	ngx_int_t rc;
	ngx_file_aio_t *faio;

	_PTR(faio = ngx_file_aio_alloc(c->pool->log));
	faio->ctx = f;
	faio->cleanup = (ngx_pool_cleanup_pt)ngx_dump_file_aio_cleanup;
#if defined (NGX_HAVE_O_DIRECT)
	faio->tf.file.directio = 1;
#endif
	faio->tf.file.fd = NGX_INVALID_FILE;
	faio->tf.file.log = c->log;
	faio->tf.pool = c->pool;
	faio->tf.path = ngx_pcalloc(faio->tf.pool,sizeof(*faio->tf.path));
	faio->tf.warn = "stream is saved to a temporary file";
	faio->tf.log_level = c->log->log_level;
	faio->tf.persistent = 1;
	faio->tf.clean = 0;
	faio->tf.access = 0666;

	faio->tf.path->name.len = dscf->dump_dir->name.len + dateTimePatternLen + 2 + 3;

	_PTR( faio->tf.path->name.data = ngx_pnalloc(c->pool,faio->tf.path->name.len + 1) );



	ngx_sprintf(faio->tf.path->name.data,"%V/%*s/%02d/%Z",&dscf->dump_dir->name,
			dateTimePatternLen,ngx_cached_http_log_iso8601.data,( ngx_time() / 3600 ) % 24 );

	ngx_log_debug2(NGX_LOG_DEBUG_STREAM, c->log, 0, "create_dump_file %p . path=<%s>",c->data,faio->tf.path->name.data);

	_S(ngx_create_full_path(faio->tf.path->name.data,0700));

	_S(ngx_create_temp_file(&faio->tf.file, faio->tf.path, faio->tf.pool,
			faio->tf.persistent, faio->tf.clean, faio->tf.access));

	//_S(ngx_append_on(&dump->faio->tf.file));
	if(faio->tf.file.directio){
		faio->align = ~(ngx_directio_memalign(&faio->tf.file) - 1);
		_S(ngx_directio_on(faio->tf.file.fd));
	} else {
		faio->align = ~0;
	}

	_S( ngx_dump_file_aio_buf_create_circular(faio,bufSize));

	return faio;
error:
	if( rc != NGX_OK  ){
		if(faio) {
			ngx_file_aio_destroy(faio);
		}
	}
	return NULL;
}


ssize_t
ngx_dump_file_aio_write(ngx_file_aio_t *faio, u_char *buf, size_t size){
	ssize_t n = 0;
	ngx_buf_aio_t  *b = &faio->buf;
	size = ngx_dump_file_aio_buf_check_grow(faio,size);

	if((ngx_int_t)size <= 0){
		return 0;
	}

	advance_cpy(b,buf,size);

	ngx_dump_file_aio_write_aligned(faio);

	return n;
}

static void
ngx_dump_file_aio_event_handler(ngx_event_t *ev)
{
	ngx_event_aio_t  *aio =  ev->data;
	ngx_file_aio_t  *faio = (void*)aio->file;
	ngx_buf_aio_t  *b = &faio->buf;

	if (aio->res > 0) {

		advance_cpy_p(b,&b->pos,NULL,aio->res);
		b->in_flight -= aio->res;
		faio->tf.file.offset = aio->aiocb.aio_offset + aio->res;
		if(b->in_flight == 0){
			reset_old_start_ptr(b,0);
		} else {
			ngx_log_error(NGX_LOG_ERR,
			//ngx_log_debug(NGX_LOG_DEBUG_STREAM,
					faio->tf.file.log, 0, "ngx_dump_file_aio_event_handler %s [%d , %d , %d , %d] - buf=%d size=%d fo: %d tx: %d ios:%d inflight: %d",
					faio->tf.file.name.data, 0 , b->pos - b->start, b->last - b->start, b->end - b->start,
					aio->aiocb.aio_buf,
					aio->aiocb.aio_nbytes, faio->tf.file.offset,
					aio->res,
					faio->num_of_ios,
					b->in_flight);
		}
	}
	// without setting this next write will return immediately without doing anything
	ev->complete = 0;
	if( NGX_OK == ngx_dump_file_handle_write(faio->ctx,faio,aio->res) ){
		ngx_dump_file_aio_write_aligned(faio);
	}
}

static ngx_int_t
ngx_dump_file_aio_buf_check_grow(ngx_file_aio_t *faio,size_t required){
	ngx_int_t rc;
	ngx_file_t *c = &faio->tf.file;
	size_t size = ngx_dump_file_buf_calc_space(&faio->buf,1);

	if( size < required ) {
		_S(ngx_dump_file_aio_buf_create_circular(faio,2 * (faio->buf.end - faio->buf.start)));
	}
	return required;
	error:
	ngx_log_error(NGX_LOG_ERR,c->log, 0, "ngx_dump_file_aio_buf_check_grow. \"%V\" error %d",
			&c->name, rc);
	return ngx_dump_file_handle_error(faio->ctx,faio,NGX_ERROR);
}


static void
ngx_dump_file_aio_write_aligned(ngx_file_aio_t *faio)
{
	// event handler *ready* field is volatile and cannot be trusted...
	if(faio->num_of_ios > 0){
		return;
	}


	ngx_buf_aio_t  *b = &faio->buf;
	ngx_int_t rc;
	ngx_memzero(faio->vec, sizeof(faio->vec));
	size_t cnt = 0,sent = 0;
	u_char *pos = b->pos;

	while( cnt < 2 )
	{
		size_t size = 0;
		if( pos <= b->last ){
			size = b->last - pos;
		} else {
			size = b->end - pos;
			if(size == 0){
				pos = b->start;
				continue;
			}
		}

		if(faio->align){
			size = size_align(size,~(ngx_pagesize-1));
		}

		if(size == 0){
			//ngx_log_error(NGX_LOG_ERR, faio->tf.file.log, 0, "ngx_dump_file_aio_write_aligned. 0 size");
			break;
		}

		u_char *last  = pos + size;

		if(last <= pos){
			//	ngx_log_error(NGX_LOG_ERR, faio->tf.file.log, 0, "ngx_dump_file_aio_write_aligned.",last-start,b->pos-start,size);
			break;
		}

		sent += size;

		faio->vec[cnt].iov_base = pos;
		faio->vec[cnt].iov_len = size;

		cnt++;

		if( pos <= b->last )
			break;
		else
			pos = b->start;
	}

	if( 0 == cnt)
		return;

	ngx_log_debug(NGX_LOG_DEBUG_STREAM,
			//ngx_log_error(NGX_LOG_ERR,
			faio->tf.file.log, 0, "ngx_dump_file_aio_write_aligned. %s [%d , %d , %d , %d] - %d fo: %d ios:%d cnt: %d",
			faio->tf.file.name.data, 0 , b->pos - b->start, b->last - b->start, b->end - b->start, sent,faio->tf.file.offset,
			faio->num_of_ios, cnt);

	rc = ngx_file_aio_writev(faio,
			(struct iovec*)&faio->vec,
			cnt,
			faio->tf.file.offset,
			file_aio_pool,ngx_dump_file_aio_event_handler);

	if ( rc <= 0 ){
		switch(rc){
		case NGX_OK:
			//ngx_log_error(NGX_LOG_ERR, faio->tf.file.log, 0, "ngx_dump_file_aio_write_aligned. -> NGX_OK");
			break;
		case NGX_AGAIN:
			if(faio->num_of_ios > 0){
				b->in_flight += faio->vec[0].iov_len + ( cnt == 1 ? 0 : faio->vec[1].iov_len);
			}
			break;
		default:
			ngx_log_error(NGX_LOG_ERR, faio->tf.file.log, 0, "ngx_dump_file_aio_write_aligned. error %d",rc);
			break;
		};
	}
}




