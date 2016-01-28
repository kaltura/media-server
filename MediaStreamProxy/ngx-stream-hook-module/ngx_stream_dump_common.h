#ifndef _NGX_STREAM_DUMP_CMN_H_INCLUDED_
#define _NGX_STREAM_DUMP_CMN_H_INCLUDED_

#include <ngx_stream.h>

/*
 *  file dump handler
 * */
typedef struct {
	ngx_stream_handler_pt   original_pt;
	ngx_path_t			   *dump_dir;
	off_t				   max_file_size,
						   min_file_size;
	time_t				   max_file_duration,
						   min_file_duration;
	ngx_flag_t			   dump_once;
	size_t				   buf_limit_size;
	ngx_msec_t			   index_write_time_resolution;
} ngx_stream_dump_srv_conf_t;


/*stream init states*/
typedef enum {
	NGX_DUMP_FILE_PARSE_STREAM_AMF_MSG = 0,
	NGX_DUMP_FILE_PARSE_STREAM_NAME,
	NGX_DUMP_FILE_PARSE_STREAM_DONE,
	NGX_DUMP_FILE_PARSE_STREAM_ERROR
} NGX_DUMP_FILE_PARSE_rtmp_stm_parse_state;

typedef struct ngx_file_aio_s ngx_file_aio_t;
typedef struct ngx_dump_file_stream_parser_s ngx_dump_file_stream_parser_t;

typedef struct ngx_stream_dump_file_handler_s{
	ngx_file_aio_t  *faio;
	ngx_file_aio_t  *faio_index;
	ngx_msec_t		last_index_write;
	off_t			throuput;
	ngx_int_t		stream_split_index;
	time_t			created;
	NGX_DUMP_FILE_PARSE_rtmp_stm_parse_state rtmp_stm_parse_state;
	ngx_dump_file_stream_parser_t *parser;
	ngx_stream_dump_srv_conf_t *conf;
	ngx_str_t		   connection_addr;
}ngx_stream_dump_file_handler_t;

ngx_int_t
ngx_dump_file_handle_write(ngx_stream_dump_file_handler_t *ctx, ngx_file_aio_t *f, ngx_int_t status);

ngx_int_t
ngx_dump_file_handle_error(ngx_stream_dump_file_handler_t *ctx, ngx_file_aio_t *f, ngx_int_t error);


#define MEM_PROF
#define _S(expr) \
		rc = expr;\
		if( rc != NGX_OK ){\
			ngx_log_error(NGX_LOG_ERR,c->log, 0, "%s:%d "#expr" error %d", __FILE__,__LINE__, rc );\
			goto error;\
		}

#define _PTR(expr) \
		if( !(expr) ){\
			rc = NGX_ERROR;\
			ngx_log_error(NGX_LOG_ERR,c->log, 0,  "%s:%d  "#expr" -> NULL pointer",__FILE__,__LINE__ );\
			goto error;\
		}

#ifndef MEM_PROF
	#define scramble_free_n(pat,p,m,n) ngx_pfree(p,m)
	#define scramble_free(pat,p,m) ngx_pfree(p,m)
#else
	#define scramble_free_n(pat,p,m,n) \
		ngx_memset(m,pat,n);\
		ngx_pfree(p,m)
	#define scramble_free(pat,p,m) scramble_free_n(pat,p,m,sizeof(*m))
#endif

#define size_align(sz,align) ((align) ? ((sz) & (align)) : (sz))
#define pointer_align(ptr,align) (u_char*)size_align((ngx_uint_t)ptr,(ngx_uint_t)align)
#define pointer_start_align(ptr,align) pointer_align((u_char*)ptr+(~(align)+1),align)

#endif

