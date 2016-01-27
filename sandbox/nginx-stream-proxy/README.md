#ngx-stream-dump module

## intent

This module was developed in order to provide for means of capturing transport streams notably rtmp, and ability to restream it again
for debugging purposes.

## compiling instructions

+ download source code for nginx 1.9
+ run script rebuild_nginx_1.9.sh. nginx source dir is assumed to be ~/nginx or in the NGINX_BUILD_DIR env var

## deployment

 run nginx as you would normally in either standalone or service mode.
 use nginx.conf file from this repository to configure the actual nginx installation.
 modify directives parameters to suit your needs.
 
## directives

#### dump_stream
* **syntax**: `dump_stream`
* **default**: 
* **context**: `location`

#### dump_stream
* **syntax**: `dump_stream [path];`
* **default**: 
* **context**: `location`

enables stream dump for current location.
recorded chunks will be stored under `[path]` in the folders formatted as `YYYY-MM-dd`.
the stream may have multiple chunks accompanied by index file named by first chunk file name with extension `.index`
chunk file name is formed as **`<entryid>-<time of the day in msec>-<chunk index>.rtmp`** where

* ***entryid***: first published entry id captured in the rtmp stream.  
* ***time of the day in msec***: exact time in msec when session begins. there might be multiple sessions during that day... 
* ***chunk index***: serial number of a chunk starting from 0. 

#### dump_file_max_size
* **syntax**: `dump_file_max_size 20000000;`
* **default**: 
* **context**: `location`

stream has been partitioned into smaller files whose size is limited by this directive

#### dump_file_max_duration
* **syntax**: `dump_file_max_duration 3d;`
* **default**: 
* **context**: `location`

stream partitioned into smaller files limited by duration

#### dump_once
* **syntax**: `dump_once off;`
* **default**: `off`
* **context**: `location`

record only first chunk of size and/or duration as defined by either/or **dump_file_max_duration** or **dump_file_max_size**
  
#### dump_index_file_resolution_msec
* **syntax**: `dump_index_file_resolution_msec 10ms;`
* **default**: 
* **context**: `location`

index file will add records at this resolution. meanwile it will sum the traffic

#### dump_buffer_grow_limit
* **syntax**: `dump_buffer_grow_limit 2M;`
* **default**: 
* **context**: `location`
	
internal memory limit used by dump module. if reached it stops dumping and detaches from session. 
proxying prceedes unimpeeded. 	
	
## utilities

accompanying scripts: 
* **sandbox/rtmp/rtmpReStream.py** : takes rtmp chunk(s) and index file and rtmp server and reproduces original streaming timing.
* **sandbox/rtmpProxy/rtmp2flv.py** : converts rtmp dump into flv file. extracts all streams published into separate flv files.





