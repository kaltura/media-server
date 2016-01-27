#ifndef _NGX_FILE_AIO_H_INCLUDED_
#define _NGX_FILE_AIO_H_INCLUDED_

#include "ngx_stream_dump_common.h"

typedef struct ngx_buf_aio_s{
	u_char *start,
		*end,
		*pos,
		*last,
		*old_start;
	size_t in_flight;
}ngx_buf_aio_t;

struct ngx_file_aio_s{
	ngx_temp_file_t  tf;
	ngx_buf_aio_t	 buf;
	ngx_int_t		 num_of_ios;
	size_t	 		 align;
	ngx_stream_dump_file_handler_t *ctx;
	struct iovec vec[2];
	ngx_pool_cleanup_pt	cleanup;
};


ssize_t
ngx_dump_file_aio_write(ngx_file_aio_t *faio, u_char *buf, size_t size);

ngx_file_aio_t *
ngx_dump_file_aio_create(ngx_connection_t *c,
		ngx_stream_dump_file_handler_t *f,
		ngx_stream_dump_srv_conf_t *dscf,
		size_t bufSize);

ssize_t
ngx_file_aio_destroy(ngx_file_aio_t *file);

#endif
