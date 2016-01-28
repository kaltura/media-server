# deployment instructions

### prerequisites:
- clean centos 6.7 machine.
 
### installation: 

 
- install nginx latest version
- modify Wowza Streaming Engine VHost.xml VHost:HostPortList:HostPort:Port value to listen on port other than 1935 to any other port.
- restart WowzaStreamingEngine
- copy https://github.com/kaltura/media-server/sandbox/MediaStreamProxy/conf/nginx.conf to /etc/nginx.conf
- in /etc/nginx.conf:stream:server section change proxy_pass to <WowzaSE IP>:<WowzaPort> where WowzaPort is the value set in step 2.
     WowzaSE IP can be 127.0.0.1 or localhost in case of nginx being placed on same machine.
- in /etc/nginx.conf:stream:server section modify dump_stream to a path located on partition with enough disk space; make sure the path
     have necessary permissons for nginx service to create files (nginx runs as user kaltura).
- take care for storage cleanup and nginx being up and running since it will serve the incoming rtmp connection in place of wowzaSE.
- replace /etc/nginx with prebuilt instance.
- from tty run: service nginx restart.
- in case you need to modify configuration consult with  https://github.com/kaltura/media-server/MediaStreamProxy/README.md.
- use https://github.com/kaltura/media-server/sandbox/MediaStreamProxy/bin/rtmp2mp4.sh to convert rtmp dump file(s) to mp4 file or
     https://github.com/kaltura/media-server/sandbox/MediaStreamProxy/util/flv/rtmp2flv.py to flv (NB: seeks are not supported).
