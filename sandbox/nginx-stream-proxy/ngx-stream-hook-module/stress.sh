#!/bin/bash

SCRIPT_NAME=`readlink -f ${BASH_SOURCE[0]}`
SCRIPT_DIR=`dirname $SCRIPT_NAME`

. $SCRIPT_DIR/check.sh

function error()
{
	echo "error: $@"
	exit 1
}

echo "nginx stress test"

proxy=${1:-}
[ -n "$proxy" ]  || error "server name empty!"
file=${2:-}
[ -n "$file" ]  || error "input file not specified!"
max_streams=${3:-0}
[ "$max_streams" -gt 0 ]  || error "unspecified number of streams"
max_iterations=${4:-0}
[ "$max_iterations" -eq "0" ] && echo "running test forever..." || echo "run exactly $max_iterations iterations"

echo "proxy=$proxy file=$file max_streams=$max_streams"

url_prefix="rtmp://$proxy/live/$$/"

ffmpegs=(`find / -type f -name ffmpeg`)

for ffmpeg in $ffmpegs
do
	[ -z "`$ffmpeg -formats` | grep 'E flv'" ] && break
done

if [ -x "$ffmpeg" ] 
then
	echo "ffmpeg exe = $ffmpeg"
else
	echo "no ffmpeg found!"
	exit 1
fi	

let workers=0

#kill all ffmpeg instances whose parents aren't running
function kill_orphaned()
{
	local	tasks=(`ps -fA | grep $ffmpeg | grep -vE grep | awk '{ print $3 " " $4; } '`)
	for i in ${#tasks[@]}
	do
		if (( $i % 2 )) 
		then
			local parent_pid=${tasks[$i]}
		else
			local child_pid=${tasks[$i]}
			[ -z "`ps  $parent_pid | awk ' NR > 1 { print $0 }'`" ] && kill -9 $child_pid
		fi
	done
}

 kill_orphaned

function get_workers()
{ 
	workers=`ps -fA | grep $ffmpeg | grep -vE grep | awk -v PID=$$ 'BEGIN {CNT=0} $3 == PID { CNT++ } END{ print CNT;}'`
	workers=${workers:-0}
}

let g_exit=0

function onExit()
{
	if (( !g_exit ))
	then
		echo "stress interrupted. exiting..."
		kill -15 `ps -fA | grep $ffmpeg | grep -vE grep | awk -v PID=$$ '$3 == PID { print $3}'` &> /dev/null 
		sleep 1s
		kill -9 `ps -fA | grep $ffmpeg | grep -vE grep | awk -v PID=$$ '$3 == PID { print $3}'` &> /dev/null 
		g_exit=1
	fi
}

trap onExit EXIT SIGINT SIGTERM SIGSTOP

LAST_TS=`date -u +%s`
function run_purger
{
	local next_ts=`date -u +%s`
	if [ "$next_ts" -gt "$LAST_TS" ]
	then
		$SCRIPT_DIR/nginx_file_purger 
		LAST_TS=$((next_ts+60)) 	
	fi
}

while (( !g_exit ))
do
	get_workers
	echo "monitoring: running $workers / $max_streams"
	#service kaltura-nginx status &> /dev/null
	#if [ "$?"  -ne "0" ]
	#then
	#	 break
	#fi
	while (( workers < max_streams )) 
	do
		cmd_exec="$ffmpeg -re -i $file -c:v copy -c:a copy -f flv -rtmp_live live $url_prefix"stress_test_"$workers"
		echo "spawning new worker... <$cmd_exec>"
		let workers+=1 
		$cmd_exec &> /dev/null &
	done
	if [ "$max_iterations" -ne "0" ]
	then
		let max_iterations-=1
		[ "$max_iterations" -eq "0" ] && break;
	fi
	sleep 10s
	run_purger
done

runCheck

echo "stress done..."
