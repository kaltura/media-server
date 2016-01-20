/*
 * ngx-stream-hook-module.c
 *
 *  Created on: Dec 23, 2015
 *      Author: igor shevach
 */

#include <ngx_config.h>
#include <ngx_core.h>
#include "ngx_file_aio.h"

/*
 *  stream name parser
 * */

//AMF header for publish. transaction id can be dismissed (masked with zeroes)

typedef struct amf_pattern_s {
	ngx_str_t pattern;
	ngx_str_t mask;
}amf_pattern_t;
static u_char amf0 [] = {2,0,7,'p','u','b','l','i','s','h',0,0,0,0,0,0,0,0,0,5,2},
		amf0_mask [] = {0xf,0xf,0xf,0xf,0xf,0xf,0xf,0xf,0xf,0xf,0xf,0,0,0,0,0,0,0,0,0xf,0xf},
		amf3 [] = {6,0,7,'p','u','b','l','i','s','h',0,0,0,0,0,0,0,0,0,1,6},
		amf3_mask [] = {0xf,0xf,0xf,0xf,0xf,0xf,0xf,0xf,0xf,0xf,0xf,0,0,0,0,0,0,0,0,0xf,0xf};
static amf_pattern_t publish_amf0_pattern = { {sizeof(amf0),amf0}, {sizeof(amf0_mask),amf0_mask} },
					 publish_amf3_pattern = { {sizeof(amf3),amf3}, {sizeof(amf3_mask),amf3_mask} };

struct ngx_dump_file_stream_parser_s{
	size_t 	  cur_index;
	size_t    cur_index3;
	ngx_str_t stream_name;
};

static ngx_stream_dump_file_handler_t *
ngx_dump_file_create(ngx_connection_t *c,ngx_stream_dump_srv_conf_t *dscf);

static void
ngx_dump_file_close(ngx_stream_dump_file_handler_t *f);

static ssize_t
ngx_dump_file(ngx_stream_dump_file_handler_t *f, u_char *buf, size_t size);

static ssize_t
ngx_dump_file_chain_dummy(ngx_stream_dump_file_handler_t *f, ngx_chain_t *in);

#if 0
static ssize_t
ngx_dump_file_write_index_new_file(ngx_stream_dump_file_handler_t *f,ngx_str_t filename);
#endif


static void *
ngx_stream_dump_create_srv_conf(ngx_conf_t *cf);

/*
 * parser
 * */
static ngx_int_t
ngx_dump_file_input_process(ngx_stream_dump_file_handler_t *f, u_char *buf, size_t size);

static ngx_int_t
ngx_dump_file_input_parse_amf(ngx_stream_dump_file_handler_t *f, u_char *buf, size_t size);

static ngx_int_t
ngx_dump_file_input_parse_stream_name(ngx_stream_dump_file_handler_t *f, u_char *buf, size_t size);

static ngx_int_t
ngx_dump_file_check_reopen(ngx_stream_dump_file_handler_t *f);

static void
ngx_dump_file_format_next_file_name(ngx_stream_dump_file_handler_t *f);


/*
 * dump connection manager
 * */

typedef struct {
	ngx_stream_dump_file_handler_t  *dump;
	ngx_recv_pt       recv;
	ngx_recv_chain_pt recv_chain;
	ngx_pool_t       *pool;
} ngx_stream_dump_connection_t;

//module  data
static ngx_path_init_t  default_path = {
		ngx_string(NGX_HTTP_CLIENT_TEMP_PATH), { 0, 0, 0 }
};

static ngx_command_t cmd_set_dump_dir = { ngx_null_string,
		NGX_STREAM_SRV_CONF|NGX_CONF_TAKE1234,
		ngx_conf_set_path_slot,
		NGX_STREAM_SRV_CONF_OFFSET,
		offsetof(ngx_stream_dump_srv_conf_t, dump_dir),
		NULL
};

static const char *fileExtStr="-%04d%s%Z";

static void *
ngx_stream_dump_create_srv_conf(ngx_conf_t *cf);

static char *
ngx_stream_dump_merge_srv_conf(ngx_conf_t *cf, void *parent, void *child);

static void
ngx_stream_dump_handler(ngx_stream_session_t *s);

static char *
ngx_stream_dump_stream_install_hook(ngx_conf_t *cf, ngx_command_t *cmd, void *conf);


static ngx_command_t  ngx_stream_dump_commands[] = {
		{ ngx_string("dump_stream"),
				NGX_STREAM_SRV_CONF|NGX_CONF_TAKE1,
				ngx_stream_dump_stream_install_hook,
				NGX_STREAM_SRV_CONF_OFFSET,
				0,
				NULL },
		{ ngx_string("dump_file_max_size"),
				NGX_STREAM_SRV_CONF|NGX_CONF_TAKE1,
				ngx_conf_set_off_slot,
				NGX_STREAM_SRV_CONF_OFFSET,
				offsetof(ngx_stream_dump_srv_conf_t, max_file_size),
				NULL },
		{ ngx_string("dump_file_min_size"),
            	NGX_STREAM_SRV_CONF|NGX_CONF_TAKE1,
                ngx_conf_set_off_slot,
                NGX_STREAM_SRV_CONF_OFFSET,
                offsetof(ngx_stream_dump_srv_conf_t, min_file_size),
                NULL },
		{ ngx_string("dump_file_max_duration"),
				NGX_STREAM_SRV_CONF|NGX_CONF_TAKE1,
				ngx_conf_set_sec_slot,
				NGX_STREAM_SRV_CONF_OFFSET,
				offsetof(ngx_stream_dump_srv_conf_t, max_file_duration),
				NULL },
		{ ngx_string("dump_file_min_duration"),
        		NGX_STREAM_SRV_CONF|NGX_CONF_TAKE1,
        		ngx_conf_set_sec_slot,
        		NGX_STREAM_SRV_CONF_OFFSET,
        		offsetof(ngx_stream_dump_srv_conf_t, min_file_duration),
        		NULL },
		{ ngx_string("dump_once"),
				NGX_STREAM_SRV_CONF|NGX_CONF_TAKE1,
				ngx_conf_set_flag_slot,
				NGX_STREAM_SRV_CONF_OFFSET,
				offsetof(ngx_stream_dump_srv_conf_t, dump_once),
				NULL },
		{ ngx_string("dump_buffer_grow_limit"),
				NGX_STREAM_SRV_CONF|NGX_CONF_TAKE1,
				ngx_conf_set_size_slot,
				NGX_STREAM_SRV_CONF_OFFSET,
				offsetof(ngx_stream_dump_srv_conf_t, buf_limit_size),
				NULL },
		{ ngx_string("dump_index_file_resolution_msec"),
				NGX_STREAM_SRV_CONF|NGX_CONF_TAKE1,
				ngx_conf_set_msec_slot,
				NGX_STREAM_SRV_CONF_OFFSET,
				offsetof(ngx_stream_dump_srv_conf_t, index_write_time_resolution),
				NULL },
		ngx_null_command
};

static ngx_stream_module_t  ngx_stream_dump_module_ctx = {
		NULL,                                  /* postconfiguration */

		NULL,                                  /* create main configuration */
		NULL,                                  /* init main configuration */

		ngx_stream_dump_create_srv_conf,      /* create server configuration */
		ngx_stream_dump_merge_srv_conf        /* merge server configuration */
};

ngx_module_t  ngx_stream_dump_module = {
		NGX_MODULE_V1,
		&ngx_stream_dump_module_ctx,          /* module context */
		ngx_stream_dump_commands,             /* module directives */
		NGX_STREAM_MODULE,                     /* module type */
		NULL,                                  /* init master */
		NULL,                                  /* init module */
		NULL,                                  /* init process */
		NULL,                                  /* init thread */
		NULL,                                  /* exit thread */
		NULL,                                  /* exit process */
		NULL,                                  /* exit master */
		NGX_MODULE_V1_PADDING
};

static void *
ngx_stream_dump_create_srv_conf(ngx_conf_t *cf)
{
	ngx_stream_dump_srv_conf_t  *conf = ngx_pcalloc(cf->pool, sizeof(*conf));
	if (conf == NULL) {
		return NULL;
	}

	conf->original_pt = NGX_CONF_UNSET_PTR;
	conf->min_file_size = NGX_CONF_UNSET;
	conf->max_file_size = NGX_CONF_UNSET;
	conf->min_file_duration = NGX_CONF_UNSET;
	conf->max_file_duration = NGX_CONF_UNSET;
	conf->dump_once = NGX_CONF_UNSET;
	conf->buf_limit_size = NGX_CONF_UNSET_SIZE;//(1024*1024 + ngx_pagesize - 1) & ~(ngx_pagesize - 1);
	conf->index_write_time_resolution = NGX_CONF_UNSET_MSEC;

	return conf;
}


static char *
ngx_stream_dump_merge_srv_conf(ngx_conf_t *cf, void *parent, void *child)
{
	ngx_stream_dump_srv_conf_t *prev = parent;
	ngx_stream_dump_srv_conf_t *conf = child;

	ngx_conf_merge_ptr_value(conf->original_pt,
			prev->original_pt, NULL);
	if (ngx_conf_merge_path_value(cf, &conf->dump_dir,
			prev->dump_dir,	&default_path)
			!= NGX_OK)
	{
		return NGX_CONF_ERROR;
	}

	ngx_conf_merge_off_value(conf->max_file_size,prev->max_file_size,NGX_CONF_UNSET);

	ngx_conf_merge_off_value(conf->min_file_size,prev->min_file_size,NGX_CONF_UNSET);

	ngx_conf_merge_value(conf->max_file_duration,prev->max_file_duration,NGX_CONF_UNSET);

	ngx_conf_merge_value(conf->min_file_duration,prev->min_file_duration,NGX_CONF_UNSET);

	ngx_conf_merge_value(conf->dump_once,prev->dump_once,NGX_CONF_UNSET);

	ngx_conf_merge_size_value(conf->buf_limit_size,prev->buf_limit_size,NGX_CONF_UNSET_SIZE);

	ngx_conf_merge_msec_value( conf->index_write_time_resolution, prev->index_write_time_resolution, NGX_CONF_UNSET_MSEC);

	return NGX_CONF_OK;
}

static ssize_t
ngx_recv_hook(ngx_connection_t *c, u_char *buf, size_t size){

	ngx_int_t rc = NGX_OK;
	ngx_stream_session_t *s = c->data;
	ngx_stream_dump_connection_t *d = ngx_stream_get_module_ctx(s, ngx_stream_dump_module);

	ssize_t ret = d->recv(c,buf,size);

	if ( ret > 0){
		if(d->dump){
			rc = ngx_dump_file_input_process(d->dump,buf,ret);
			if( rc != NGX_ERROR){
				//	ngx_log_error(NGX_LOG_ERR, c->log, 0, "ngx_recv_hook recv %d , size %d ",ret,size );
				ngx_dump_file(d->dump,buf,ret);
			} else {
				ngx_log_error(NGX_LOG_ERR, c->log, 0,"ngx_dump_file_input_process. \"%s\" detach from the session. state %d",
						d->dump->faio->tf.file.name.data ? d->dump->faio->tf.file.name.data : (u_char*)"<null>",
						d->dump->rtmp_stm_parse_state);

				c->recv = d->recv;
				c->recv_chain = d->recv_chain;
			}
		}
	} else {
		switch(ret) {
		case NGX_AGAIN: break;
		default:
			switch(ngx_errno){
			case ECONNRESET: break;
			default:
				if(ngx_errno){
					ngx_log_error(NGX_LOG_ERR, c->log, 0, "ngx_recv_hook error: %d errno %d size %d",ret , ngx_errno , size );
				}
				break;
			}
		};
	}
	return ret;
}

static ssize_t
ngx_recv_chain_hook(ngx_connection_t *c, ngx_chain_t *in,
		off_t limit)
{
	ngx_stream_session_t *s = c->data;
	ngx_stream_dump_connection_t *d = ngx_stream_get_module_ctx(s, ngx_stream_dump_module);

	ssize_t ret = d->recv_chain(c,in,limit);

	if (ret > 0){
		if(d->dump){
			ngx_dump_file_chain_dummy(d->dump,in);
		}
	}
	return ret;
}

static void
ngx_dump_free(ngx_stream_dump_connection_t *d){
	if(d && d->dump){
		ngx_dump_file_close(d->dump);
#ifdef MEM_PROF
		ngx_memset(d->dump,0xBE,sizeof(*d->dump));
		ngx_memset(d,0xFA,sizeof(*d));
#endif
		d->dump = NULL;
	}

}

static void
ngx_stream_dump_handler(ngx_stream_session_t *s)
{
	ngx_connection_t                *c;
	ngx_stream_dump_srv_conf_t     *dscf;
	ngx_int_t 				  rc = NGX_ERROR;
	ngx_stream_dump_connection_t  *d;
	ngx_pool_cleanup_t             *cln;

	c = s->connection;

	ngx_log_debug1(NGX_LOG_DEBUG_STREAM, c->log, 0, "ngx_stream_dump_handler %p",s);

	dscf = ngx_stream_get_module_srv_conf(s, ngx_stream_dump_module);

	_PTR( d = ngx_pcalloc(c->pool, sizeof(*d)));
	d->pool = c->pool;

	_PTR( cln = ngx_pool_cleanup_add(c->pool, 0));
	cln->data = d;
	cln->handler = (ngx_pool_cleanup_pt)ngx_dump_free;

	_PTR(d->dump = ngx_dump_file_create(c,dscf));

	ngx_stream_set_ctx(s,d,ngx_stream_dump_module);

	d->recv = c->recv;
	d->recv_chain = c->recv_chain;

	c->recv = ngx_recv_hook;
	c->recv_chain = ngx_recv_chain_hook;

	ngx_log_debug0(NGX_LOG_DEBUG_STREAM, c->log, 0, "ngx_stream_dump_handler");

	(*dscf->original_pt)(s);

	return;

	error:
	ngx_close_connection(c);

}

static char *
ngx_stream_dump_stream_install_hook(ngx_conf_t *cf, ngx_command_t *cmd, void *conf)
{
	ngx_stream_dump_srv_conf_t *pscf = conf;
	ngx_stream_core_srv_conf_t  *cscf;

	cscf = ngx_stream_conf_get_module_srv_conf(cf, ngx_stream_core_module);

	if ( NGX_CONF_OK != ngx_conf_set_path_slot(cf,&cmd_set_dump_dir, conf) ){
		return NGX_CONF_ERROR;
	}

	if ( NULL == cscf->handler ){
		return NGX_CONF_ERROR;
	}

	if( pscf->original_pt == cscf->handler ){
		return NGX_CONF_ERROR;
	}

	pscf->original_pt = cscf->handler;

	cscf->handler = ngx_stream_dump_handler;

	return NGX_CONF_OK;
}

/*
 *  file handler
 * */
static ngx_stream_dump_file_handler_t *
ngx_dump_file_create(ngx_connection_t *c,ngx_stream_dump_srv_conf_t *dscf){

	ngx_stream_dump_file_handler_t  *dump = NULL;
	ngx_int_t 		 rc = NGX_ERROR;
	//TODO: what buffer size is appropriate?
	ssize_t			 bufSize = ngx_pagesize * 8, indexBufSize = ngx_pagesize * 8;

	_PTR( dump = ngx_pcalloc(c->pool, sizeof(ngx_stream_dump_file_handler_t) ) );
	dump->stream_split_index = -1;
	dump->conf = dscf;

	dump->created = ngx_time();
	_PTR(dump->connection_addr.data = ngx_pnalloc(c->pool,NGX_SOCKADDR_STRLEN));
	_PTR(dump->connection_addr.len = ngx_sock_ntop(c->sockaddr,c->socklen, dump->connection_addr.data, NGX_SOCKADDR_STRLEN, 1));
	{
		// replace colon with dash
		u_char *delim = dump->connection_addr.data - 1, *end = dump->connection_addr.data + dump->connection_addr.len;
		while( ++delim < end ){
			if (*delim == ':'){
				*delim = '-';
				break;
			}
		}
	}
	_PTR(dump->faio = ngx_dump_file_aio_create(c,dump,dscf,bufSize));
	_PTR(dump->faio_index = ngx_dump_file_aio_create(c,dump,dscf,indexBufSize));

	return dump;

error:
	if( rc != NGX_OK  ){
		if(dump->faio) {
			ngx_file_aio_destroy(dump->faio);
		}
		if(dump->faio_index) {
			ngx_file_aio_destroy(dump->faio_index);
		}
		ngx_pfree(c->pool,dump);
	}
	return NULL;
}

static ngx_int_t
ngx_check_file_is_valid(ngx_stream_dump_file_handler_t *f)
{
 	if(f->rtmp_stm_parse_state != NGX_DUMP_FILE_PARSE_STREAM_DONE){
    	ngx_log_error(NGX_LOG_ERR,f->faio->tf.file.log, 0, "ngx_dump_file_check_reopen. deleting file %s since stream wasn't parsed yet",
    			f->parser->stream_name.data);
		return 0;
	} else if (f->faio->tf.file.offset  < f->conf->min_file_size ){
	   	ngx_log_error(NGX_LOG_ERR,f->faio->tf.file.log, 0, "ngx_dump_file_check_reopen. deleting file %s since it's size too small (%d vs %d)",
    			f->faio->tf.file.offset,f->conf->min_file_size);
		return 0;
	} else if (ngx_time() - f->created  < f->conf->min_file_duration ){
		ngx_log_error(NGX_LOG_ERR,f->faio->tf.file.log, 0, "ngx_dump_file_check_reopen. deleting file %s since it's duration too short (%d vs %d)",
           			ngx_time() - f->created,f->conf->min_file_duration);
        return 0;
	}
	return 1;
}

static void
ngx_dump_file_close(ngx_stream_dump_file_handler_t *f){
	ngx_file_aio_destroy(f->faio);
	ngx_file_aio_destroy(f->faio_index);
	if(!ngx_check_file_is_valid(f)){
		ngx_log_error(NGX_LOG_ERR, f->faio->tf.file.log, 0, "ngx_dump_file_close: delete temp file - %V", &f->faio->tf.file.name);
		ngx_delete_file(f->faio->tf.file.name.data);
		ngx_delete_file(f->faio_index->tf.file.name.data);
	}
	f->faio = f->faio_index = NULL;
}

static ssize_t
ngx_dump_file_write_index_sz(ngx_stream_dump_file_handler_t *f, size_t size,ngx_int_t force){
	if(size){
		f->throuput += size;
		if(f->last_index_write == 0){
			f->last_index_write = ngx_current_msec;
		}
		if( (force || ngx_current_msec > f->last_index_write + f->conf->index_write_time_resolution)){
			u_char msg[100];
			u_char *end = ngx_sprintf(msg,"%M %d\n",f->last_index_write,f->throuput);
			ngx_dump_file_aio_write(f->faio_index,msg,end - msg);
			f->throuput = 0;
			f->last_index_write = ngx_current_msec;
		}
	}
	return 0;
}

#if 0
static ssize_t
ngx_dump_file_write_index_new_file(ngx_stream_dump_file_handler_t *f,ngx_str_t filename){
	ngx_dump_file_write_index_sz(f,0,f->throuput > 0);
	u_char msg[256];
	u_char *p = filename.data + filename.len;
	while( p >= filename.data && *p != '/')
		p--;
	p++;
	u_char *end = ngx_sprintf(msg,"%M f %*s\n",f->last_index_write,filename.data+filename.len-p,p);
	return ngx_dump_file_aio_write(f->faio_index,msg,end - msg);
}
#endif

static ssize_t
ngx_dump_file(ngx_stream_dump_file_handler_t *f, u_char *buf, size_t size){
	ngx_dump_file_aio_write(f->faio,buf,size);
	ngx_dump_file_write_index_sz(f,size,0);
	return 0;
}

static ssize_t
ngx_dump_file_chain_dummy(ngx_stream_dump_file_handler_t *f, ngx_chain_t *in){
	return 0;
}


ngx_int_t
ngx_dump_file_handle_write(ngx_stream_dump_file_handler_t *ctx, ngx_file_aio_t *f, ngx_int_t written)
{
	ngx_int_t status;
	if(written > 0)
		status = NGX_OK;
	else
		status = written;
	if(ctx && NGX_OK == ngx_dump_file_handle_error(ctx,f,status) ){
		return ngx_dump_file_check_reopen(ctx);
	}
	return NGX_ERROR;
}

ngx_int_t
ngx_dump_file_handle_error(ngx_stream_dump_file_handler_t *ctx, ngx_file_aio_t *f, ngx_int_t error)
{
	switch(error)
	{
	case NGX_OK:
	case NGX_AGAIN:
		return NGX_OK;
	default:
		ctx->rtmp_stm_parse_state = NGX_DUMP_FILE_PARSE_STREAM_ERROR;
		return NGX_ERROR;
	};
}


static ngx_int_t
ngx_dump_file_input_process(ngx_stream_dump_file_handler_t *f, u_char *buf, size_t size)
{
	ngx_log_debug4(NGX_LOG_DEBUG_STREAM, f->faio->tf.file.log, 0, "ngx_dump_file_input_process h=%p , buf=%p, sz=%d state=%d",f,buf,size,f->rtmp_stm_parse_state);

	switch(f->rtmp_stm_parse_state)
	{
	case NGX_DUMP_FILE_PARSE_STREAM_AMF_MSG:
		return ngx_dump_file_input_parse_amf(f,buf,size);
	case NGX_DUMP_FILE_PARSE_STREAM_NAME:
		return ngx_dump_file_input_parse_stream_name(f,buf,size);
	case NGX_DUMP_FILE_PARSE_STREAM_DONE:
		return NGX_DONE;
	case NGX_DUMP_FILE_PARSE_STREAM_ERROR:
	default:
		return NGX_ERROR;
	};
}

static ngx_int_t
ngx_dump_file_input_parse_amf_n(ngx_stream_dump_file_handler_t *f, u_char *buf, size_t size,amf_pattern_t *pPattern,size_t *pIndex)
{
	ngx_log_debug4(NGX_LOG_DEBUG_STREAM, f->faio->tf.file.log, 0, "ngx_dump_file_input_parse_amf %p , %p, %d idx %d",f,buf,size,f->parser->cur_index);

	while(size--){
		u_char ch = *buf++;
		if(  pPattern->mask.data[*pIndex] && pPattern->pattern.data[*pIndex] != ch ){
			*pIndex = 0;
			continue;
		}
		*pIndex = *pIndex+1;
		if( *pIndex == pPattern->pattern.len ){
			*pIndex = 0;
			f->rtmp_stm_parse_state = NGX_DUMP_FILE_PARSE_STREAM_NAME;
			return ngx_dump_file_input_parse_stream_name(f,buf,size);
		}
	}
	return NGX_AGAIN;
}


static ngx_int_t
ngx_dump_file_input_parse_amf(ngx_stream_dump_file_handler_t *f, u_char *buf, size_t size)
{
	ngx_int_t rc;
	ngx_file_t *c = &f->faio->tf.file;

	if(f->parser == NULL){
		_PTR(f->parser = ngx_pcalloc(f->faio->tf.pool,sizeof(*f->parser)));
		ngx_memzero(f->parser,sizeof(*f->parser));
	}
	rc = ngx_dump_file_input_parse_amf_n(f,buf,size,&publish_amf0_pattern,&f->parser->cur_index);
	if( rc == NGX_AGAIN ){
		rc = ngx_dump_file_input_parse_amf_n(f,buf,size,&publish_amf3_pattern,&f->parser->cur_index3);
	}
	return rc;
error:
	f->rtmp_stm_parse_state = NGX_DUMP_FILE_PARSE_STREAM_ERROR;
	return rc;
}

static char file_ext[]=".rtmp";
static ngx_int_t extra = sizeof(file_ext) + 10 + 10 + 1;

static ngx_int_t
ngx_dump_file_input_parse_stream_name(ngx_stream_dump_file_handler_t *f, u_char *buf, size_t size)
{
	ngx_int_t rc;
	ngx_file_t *c = &f->faio->tf.file;

	ngx_log_debug4(NGX_LOG_DEBUG_STREAM,f->faio->tf.file.log, 0, "ngx_dump_file_input_parse_stream_name %p , %p, %d parser %p",f,buf,size,f->parser);

	if(f->parser == NULL){
		return NGX_ERROR;
	}

	if(!size)
		return NGX_AGAIN;

	if( 0 == f->parser->cur_index && size-- ){
		f->parser->stream_name.len = *buf++;
#if (NGX_HAVE_LITTLE_ENDIAN)
		f->parser->stream_name.len =  (f->parser->stream_name.len << 8) & 0xFF;
#endif
		f->parser->cur_index++;
	}

	while(size--){

		if(f->parser->stream_name.data == NULL){
#if (NGX_HAVE_LITTLE_ENDIAN)
			f->parser->stream_name.len += *buf++;
#else
			f->parser->stream_name.len += *buf++ >> 8;
#endif

			if( f->parser->stream_name.len == 0 ){
				ngx_log_error(NGX_LOG_ERR, f->faio->tf.file.log, 0, "ngx_dump_file_input_parse_stream_name. error: zero length stream name!");
				rc = NGX_ERROR;
				goto error;
			}

			// rename to same directory as temp file
			u_char *p = f->faio->tf.file.name.data + f->faio->tf.file.name.len;
			while ( p != f->faio->tf.file.name.data && *--p != '/'  )
				;

			f->parser->cur_index = p - f->faio->tf.file.name.data + 1;

			f->parser->stream_name.len += f->parser->cur_index + extra;

			f->parser->stream_name.len += f->connection_addr.len + 1;

			f->parser->stream_name.len += 1;

			_PTR(f->parser->stream_name.data = ngx_pcalloc(f->faio->tf.pool,f->parser->stream_name.len));

			ngx_memcpy(f->parser->stream_name.data,f->faio->tf.file.name.data,f->parser->cur_index);

			continue;
		}

		f->parser->stream_name.data[f->parser->cur_index] = *buf++;

		// finalize?
		if( f->parser->stream_name.data[f->parser->cur_index] == '?' ||
			++f->parser->cur_index == f->parser->stream_name.len - extra - (f->connection_addr.len + 1) - 1 ){

			f->parser->stream_name.data[f->parser->cur_index++] = (u_char)'/';
			f->parser->stream_name.data[f->parser->cur_index] = (u_char)0;

			_S(ngx_create_full_path(f->parser->stream_name.data,0700));

			ngx_str_t index_file_name;

			// make for *unique* session based on msec value
			f->parser->cur_index = ngx_sprintf(f->parser->stream_name.data + f->parser->cur_index,"%V-%M",
					&f->connection_addr , ngx_current_msec % (24*60*60*1000)) - f->parser->stream_name.data;
			// assign index
			ngx_dump_file_format_next_file_name(f);

			f->rtmp_stm_parse_state = NGX_DUMP_FILE_PARSE_STREAM_DONE;

			ngx_log_debug2(NGX_LOG_DEBUG_STREAM, f->faio->tf.file.log, 0, "ngx_dump_file_input_parse_stream_name %p name: %V",f,&f->parser->stream_name);

			_S(ngx_rename_file(f->faio->tf.file.name.data,f->parser->stream_name.data));

			index_file_name.len = ngx_strlen(f->parser->stream_name.data) + sizeof(".index") + 1;
			index_file_name.data = ngx_palloc(f->faio_index->tf.pool,index_file_name.len);
			ngx_sprintf(index_file_name.data,"%s.index%Z",f->parser->stream_name.data);
			_S(ngx_rename_file(f->faio_index->tf.file.name.data,index_file_name.data));
			f->faio_index->tf.file.name = index_file_name;

			f->faio->tf.file.name = f->parser->stream_name;

			f->stream_split_index++;

			return NGX_DONE;
		}
	}

	return NGX_AGAIN;
error:
	f->rtmp_stm_parse_state = NGX_DUMP_FILE_PARSE_STREAM_ERROR;
	return rc;
}

static void
ngx_dump_file_format_next_file_name(ngx_stream_dump_file_handler_t *f)
{
	ngx_sprintf(&f->parser->stream_name.data[f->parser->cur_index],fileExtStr,1+f->stream_split_index,file_ext);
}

static ngx_int_t
ngx_dump_file_check_reopen(ngx_stream_dump_file_handler_t *f)
{
	ngx_int_t incr;
	ngx_stream_dump_srv_conf_t *dscf = f->conf;

	if( dscf->max_file_duration != NGX_CONF_UNSET && ngx_time() - f->created > dscf->max_file_duration ){
			goto reopen;
	}
	if( dscf->max_file_size != NGX_CONF_UNSET &&  f->faio->tf.file.offset >= dscf->max_file_size){
		goto reopen;
	}
	return NGX_OK;
reopen:

	if(!ngx_check_file_is_valid(f)){
		ngx_delete_file(f->faio->tf.file.name.data);
    	incr = 0;
	} else {
		ngx_close_file(f->faio->tf.file.fd);
    	incr = 1;
	}

    f->faio->tf.file.offset = 0;
    f->created = ngx_time();
   // ngx_dump_file_write_index_new_file(f,f->faio->tf.file.name);
	if( NGX_CONF_UNSET != dscf->dump_once && dscf->dump_once ){
			return NGX_OK;
	}
	ngx_dump_file_format_next_file_name(f);
	f->faio->tf.file.fd = ngx_open_tempfile(f->parser->stream_name.data,1,f->faio->tf.access);
	 if (f->faio->tf.file.fd != NGX_INVALID_FILE){
		 f->stream_split_index += incr;
		f->faio->tf.file.name = f->parser->stream_name;
		 return NGX_OK;
	 }
	 errno = ngx_errno;
	 f->rtmp_stm_parse_state = NGX_DUMP_FILE_PARSE_STREAM_ERROR;
	 ngx_log_error(NGX_LOG_ERR,f->faio->tf.file.log, 0, "ngx_dump_file_check_reopen. failed create file %s -> error %d ",f->parser->stream_name.data,errno );
	 return NGX_ERROR;
}



