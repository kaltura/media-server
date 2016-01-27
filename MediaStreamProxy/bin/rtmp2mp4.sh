# ! /bin/bash
# convert rtmp dump file to mp4

scriptName=`basename $0`

function log(){
	echo "`date` $scriptName $@"
}

function error(){
	local errorCode=$?
	[[ "$errorCode" -eq "0" ]] && log "error: $errorCode. $@" || log "error $@"
	exit 1
}

function check(){
	local cmd=$1
	eval "$cmd" || error "$cmd"
}

function usage()
{
	echo "usage: rtmp2mp4 <path to rtmp file path (including metacharacters)> <output dir to place mp4 file>"
	exit 0
}

[[ "$#" -eq "0" ]] && usage

rtmp2flvScriptPath=$(readlink -e `dirname $0`/../util/flv/rtmp2flv.py)
ffmpegPath=$(readlink -e `dirname $0`/ffmpeg)

rtmp2flv=${rtmp2flv:-$rtmp2flvScriptPath}
ffmpeg=${ffmpeg:-$ffmpegPath}

inputRtmp=$1
outputDir=${2:-pwd}

echo "path to rtmp dump file(s): $inputRtmp, path to rtmp to flv conversion script: $rtmp2flv , ffmpeg: $ffmpeg, output dir: $outputDir"

[ -n "`ls "$rtmp2flvScriptPath"`" ] && [ -x "$ffmpeg" ] && [ -x "$rtmp2flv" ] && [ -n "$outputDir" ] || usage

check 'tempDir=`mktemp -d /var/tmp/tmp.XXXXXXX`'

function cleanup(){
 [ -d "$tempDir" ] && ls -lt $tempDir && echo "cleanup. delete $tempDir" && rm -rf $tempDir 		
}

inputRtmpFileList=(`ls -rt $inputRtmp`)

if [[ "${#inputRtmpFileList[@]}" -gt "1" ]] 
then
	echo "merge all rtmp chunks into single rtmp file. chunks are: ${inputRtmpFileList[@]}"
	check 'tempRtmp=$tempDir/`basename $inputRtmp`'
	check 'cat ${inputRtmpFileList[@]} > $tempRtmp'
	inputRtmp=$tempRtmp
fi


trap cleanup EXIT SIGTERM SIGINT 

[ -d "$tempDir" ] || error "failed to create temp dir"

echo "create flv file (no navigation cue points) from $inputRtmp"

check '$rtmp2flv $inputRtmp $tempDir/'

flvFile=`ls $tempDir/*.flv`

echo "convert flv file to mp4"

#echo "$ffmpeg -i $flvFile -c:v copy -c:a copy $outputDir`basename $flvFile`.mp4"

check '$ffmpeg -i $flvFile -c:v copy -c:a copy $outputDir`basename $flvFile`.mp4'




