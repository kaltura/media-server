# deployment instructions

### prerequisites:
- clean centos 6.7 machine.
 
### installation: 

 
1. install nginx latest version
2. copy https://github.com/kaltura/media-server/sandbox/nginx-stream-proxy/bin/nginx to /etc/
3. copy https://github.com/kaltura/media-server/sandbox/nginx-stream-proxy/ngx-stream-hook-module/nginx.conf to /etc/nginx.conf
4. from tty run: service nginx restart.
5. in case you need to modify configuration consult with  https://github.com/kaltura/media-server/sandbox/README.md.
6. use https://github.com/kaltura/media-server/sandbox/nginx-stream-proxy/bin/rtmp2mp4.sh to convert rtmp dump file(s) to mp4 file or
     https://github.com/kaltura/media-server/sandbox/nginx-stream-proxy/flv/rtmp2flv.py to flv (NB: seeks are not supported).
