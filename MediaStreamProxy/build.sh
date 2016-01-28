SCRIPT_PATH=`readlink -f $0`
SCRIPT_DIR=`dirname $SCRIPT_PATH`
NGX_DUMP_MODULE=`readlink -f $SCRIPT_DIR/ngx-stream-hook-module`

withFFmpeg=${withFFmpeg:-}
nginxVer=${nginxVer:-1.9.5}

function parseCMD()
{
        while [[ "$#" -gt "0" ]]
        do
                local arg=$1
                echo "arg=$arg"
                shift
                case $arg in
                 --with-ffmpeg)
                        withFFmpeg=$arg
                 ;;
                 -nginx-ver=*)
                        nginxVer=${arg:11}
                        echo "nginxVer=$nginxVer argno=$#"
                ;;
                *)
                        echo "unrecognized option $arg"
                ;;
                esac
        done
}

parseCMD $@

function configureOnDemand()
{
	local dir=$1
	local cmd=$2

	cd $dir
	if [ -r "$dir/configure_cmd" ] 
	then
		local old_cmd=`cat $NGINX_BUILD_DIR/configure_cmd`
	else
		local old_cmd=		
	fi

	if [ "$old_cmd" != "$cmd" ]  
	then
		echo "configureOnDemand. executing $cmd"
		$cmd
	fi
	
	echo "compile `basename $1`"

	make

	if [ "$?" -eq "0" ]
	then
		echo "configureOnDemand. save configure $dir/configure_cmd"
		echo $cmd > "$dir/configure_cmd"
	else
		echo "configureOnDemand. error $? deleting $dir/configure_cmd"
		rm -f "$dir/configure_cmd" &> /dev/null
		exit 0
	fi
}

echo "NGX_DUMP_MODULE => $NGX_DUMP_MODULE"

NGINX_BUILD_DIR=${NGINX_BUILD_DIR:-/root/nginx/}

if [ ! -f "/var/tmp/release-$nginxVer.zip" ]
then
	cd /var/tmp
	wget -nc https://github.com/nginx/nginx/archive/release-$nginxVer.zip
	unzip release-$nginxVer.zip -d $NGINX_BUILD_DIR
fi

NGINX_CONF_CMD="./auto/configure --prefix=/etc/nginx --sbin-path=/usr/sbin/nginx --conf-path=/etc/nginx/nginx.conf --error-log-path=/var/log/nginx/error.log --http-log-path=/var/log/nginx/access.log --pid-path=/var/run/nginx.pid --lock-path=/var/run/nginx.lock --http-client-body-temp-path=/var/cache/nginx/client_temp --http-proxy-temp-path=/var/cache/nginx/proxy_temp --http-fastcgi-temp-path=/var/cache/nginx/fastcgi_temp --http-uwsgi-temp-path=/var/cache/nginx/uwsgi_temp --http-scgi-temp-path=/var/cache/nginx/scgi_temp --user=kaltura --group=kaltura --with-http_ssl_module --with-http_realip_module --with-http_addition_module --with-http_sub_module --with-http_dav_module --with-http_flv_module --with-http_mp4_module --with-http_gunzip_module --with-http_gzip_static_module --with-http_random_index_module --with-http_secure_link_module --with-http_stub_status_module --with-http_auth_request_module --with-mail --with-mail_ssl_module --with-file-aio --with-ipv6 --with-debug  --with-threads --with-stream --add-module=$NGX_DUMP_MODULE"

configureOnDemand "$NGINX_BUILD_DIR" "$NGINX_CONF_CMD"

if [ -n "$withFFmpeg" ]
then
	FFMPEG_BUILD_DIR=/root/ffmpeg

	if [ ! -d  "$FFMPEG_BUILD_DIR" ]
	then
		echo "get ffmpeg code"
		git clone git://source.ffmpeg.org/ffmpeg.git $FFMPEG_BUILD_DIR
	else
		echo "sync ffmpeg code"
		cd $FFMPEG_BUILD_DIR
		git pull
	fi

	FFMPEG_CONF_CMD="./configure  --disable-everything   --enable-muxer=flv --enable-muxer=mpegts --disable-doc   --enable-protocol=file --enable-protocol=udp --enable-protocol=hls --enable-protocol=http --enable-protocol=https  --enable-demuxer=hls --enable-demuxer=mpegts  --enable-muxer=rtp_mpegts  --enable-protocol=rtmp --enable-parser=h264 --enable-parser=aac --enable-decoder=aac --enable-demuxer=flv --enable-muxer=mp4"

	configureOnDemand "$FFMPEG_BUILD_DIR" "$FFMPEG_CONF_CMD"
fi