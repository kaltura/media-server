#!/bin/bash

SCRIPT_NAME=`readlink -f ${BASH_SOURCE[0]}`
SCRIPT_DIR=`dirname $SCRIPT_NAME`

function error()
{
	echo "!error: $@"
	#exit 1
}


function onExit()
{
    [ -f "$outfile" ] && rm -rf $outfile	
    #[ -f "$outfile.flv" ] && rm -f "$outfile.flv"	
    echo "on exit: $outfile"
}

trap onExit EXIT

RTMP2FLV=`readlink -f $SCRIPT_DIR/../../rtmpProxy/flv/rtmp2flv.py`
TEMP_DIR=/var/tmp/
# rtmp dump file path structure: 
# <dump dir>/<YYYY>-<mm>-<dd>/<entryid>/<ddd.ddd.ddd.ddd-d+><time of day msecs>-<file split index>.rtmp 
#
REGEX="([^\/]+)\/([^\/]+)-([[:digit:]]+)-([[:digit:]]+).rtmp$" #"([^\/+])/(.*)-[[:digit:]]+.rtmp"

# convertRtmp2FlvFile: stitches together files and 
#
function convertRtmp2FlvFile()
{
	which rtmp2flv.py &> /dev/null || echo "rtmp2flv.py" 
	local files=($line)
	#echo "files=${files[@]}"
	if [[ "${files[0]}" =~ $REGEX ]] 
	then
		local total_msecs=${BASH_REMATCH[3]}
		local msecs=$((total_msecs % 1000))
		local secs=$((total_msecs / 1000 % 60))
		local mins=$((total_msecs / (1000 * 60) % 60))
		local hrs=$((total_msecs / (1000 * 60 * 60) % 24))

		outfile="$TEMP_DIR${BASH_REMATCH[1]}_$hrs-$mins-$secs.$msecs_${BASH_REMATCH[2]}.rtmp" 
		if [ -z "$errorDir" ]
		then
			errorDir="`dirname $outfile`/errors"
			if [ ! -d "$errorDir" ] 
			then 
				 mkdir -p $errorDir || error "mkdir -p $errorDir"
			fi
			rm -f $errorDir/*
		fi
		cat $line > $outfile || error "cat $line > $outfile"
		echo -e "makeRtmpFile. stitched: " && ls -l $outfile
		if ! $RTMP2FLV $outfile > "$outfile.flv"
		then 
			 mv $outfile.flv $errorDir 
			 #error "$RTMP2FLV $outfile > $errorDir/`basename $outfile`.flv"  
		else
			echo "makeRtmpFile. created $outfile.flv" && ls -l "$outfile.flv"
		fi
		#rm -f $outfile
	else
		error "makeRtmpFile - bad input <$0>"
	fi
}

function runCheck()
{
	echo "runCheck"

	$SCRIPT_DIR/nginx_file_purger

	 find $TEMP_DIR -name "*.flv" -delete

	if [[ `grep "dump_stream" /etc/nginx/nginx.conf`  =~ dump_stream(\s*?)(.*)\; ]] 
	then
		 	DUMP_DIR=${BASH_REMATCH[2]}
			echo "dump dir = $DUMP_DIR"
			ls -rt  $DUMP_DIR/*/*.rtmp | awk 'BEGIN{FS="-"}  { key=$1"-"$2"-"$3"-"$4;  arr[key]=arr[key]" "$0; } END{ for (i in arr){ print arr[i]}}' | 	  while read line; do  convertRtmp2FlvFile $line; done 

	else
		error "dump_stream directive not found in /etc/nginx/nginx.conf"
	fi
}
