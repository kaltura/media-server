#ifndef _NGX_FILE_AIO_WRITE_H_INCLUDED_
#define _NGX_FILE_AIO_WRITE_H_INCLUDED_


ssize_t
ngx_file_aio_write(ngx_file_aio_t *file, u_char *buf, size_t size, off_t offset,
    ngx_pool_t *pool, ngx_event_handler_pt handler);

ssize_t
ngx_file_aio_writev(ngx_file_aio_t *faio, struct iovec *vec, size_t size, off_t offset,
    ngx_pool_t *pool, ngx_event_handler_pt handler);

ngx_int_t
ngx_append_on(ngx_file_t *file);

ngx_int_t
ngx_append_off(ngx_file_t *file);

size_t
ngx_directio_memalign(ngx_file_t *file);

#endif
