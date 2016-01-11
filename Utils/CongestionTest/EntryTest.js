
var FFMpegTask=require('./ffmpegModule/FFMpegTask.js').FFMpegTask;
var fileName="/Users/ronyadgar/Downloads/123.mp4";
var kle = require('./API/KalturaLiveEntries.js').KalturaLiveEntries;
var q = require('q');
var _ = require('underscore');
var modulelogger = require('./logger')(module);
var loggerDecorator = require('./config/log-decorator');
var MaxRetrty=100;
var IntervalRetry=1000;

function EntryTest(entryId) {

    // private variables
    var that = this;

    var task=null;
    var logger = loggerDecorator(modulelogger, messageDecoration);
    logger.info("Generate new instance for ",entryId);



    function getMasterManifest(entryId){    //take into consideration the case that IsLive() is failed
        return  kle.getEntry(entryId)
            .then(function(entry) {
                var hlsconfig = _.filter(entry.liveStreamConfigurations, function (config) {
                    return config.protocol === "hls";
                })[0];

                return  q.resolve(hlsconfig.url);
            });
    }

    function retryPromise(fn,intervalRetry,maxRetries) {
        logger.debug("retryPromise");
        return fn()
            .catch(function() { // if it fails
                return q.delay(intervalRetry) // delay
                    // retry with more time
                    .then(function(){
                        if (maxRetries <= 0) {
                            throw new Error("Failed to download master manifest after "+maxRetries+" retries");
                        }
                        return retryPromise(fn,intervalRetry,maxRetries-1);
                    });
            });
    }

    function waitForLiveState(expectedState) {
        return retryPromise(function () {
            logger.debug("calling getMasterManifest on entryId"+entryId);
            return getMasterManifest(entryId).then(function (masterClipList) {

                logger.debug("getMasterManifest returned "+masterClipList);
                if (expectedState) {
                    if (masterClipList) {
                        return q.resolve(masterClipList);
                    }
                    else {  //try agin in some interval
                        return q.reject()
                    }
                } else {

                    if (masterClipList) {
                        return q.reject();
                    }
                    else {  //try agin in some interval
                        return q.resolve()
                    }
                }
            });
        }, IntervalRetry, MaxRetrty);
    }

    function getAllChuncksHeader(flavor)
    {
        return kle.parseChunkListM3U8(flavor)
            .then(function (res) {
                var Chunk=res[0];
                return kle.getTS(Chunk.ts)
                    .then(function(msg){
                            logger.info("successfully downloaded " +Chunk.ts);
                            return q.resolve();
                        }
                        ,
                        function(err){
                            logger.error("err: Failed to download "+Chunk.ts+" : "+ err.message);
                            return q.reject();
                        })
            })
    }

    function messageDecoration(msg) {
        return "[" + entryId + "]"  + msg;
    };

    function GetRTMPurl(){
        return  kle.getEntry(entryId)
            .then(function(entry) {
              return q.resolve(entry.primaryBroadcastingUrl+"/"+entry.streamName)
            })
            .then(function(path){
                var str= path.replace('ny-publish','il-wowza-68.dev');
                str=str.substring(0,str.length-2)+"1";
                return q.resolve(str);
            })
    }

    // public functions
    that.start=function() {
        var startTime=new Date();
       return GetRTMPurl()
           .then(
            function(url_res){
                url=url_res;
                task=new FFMpegTask( [ "-re","-i",fileName,"-c:v","copy","-c:a","libmp3lame","-f","flv","-rtmp_live","1",url]);
                logger.info("Starting FFMpeg streaming for ", url);
                return task.start();
            })
            .then(function() {
                var finishTime = new Date();
                logger.info("FFMpeg started, Operation took "+ (finishTime - startTime) + " ms");
                return waitForLiveState(true);
            })
            .then(function(url){
                var finishTime = new Date();
                logger.info("Recive Url from Wowza, Operation took ", (finishTime - startTime) , " ms");
                return kle.parseMasterM3U8(url);
            })
            .then(function(res){
                logger.info("Get master manifest succssefully");
                var promises=_.map(res,function(flavor) {
                    logger.info("About to get manifest from flavor "+flavor.bitrate);
                    return getAllChuncksHeader(flavor.m3u8);
                });

                return q.all(promises);
            })
            .then(function(){
                logger.info("Tested all chunks succssefully");
                return q.resolve();
            }).catch(function(err) {
               logger.error("ERROR: "+err.message);
               return q.reject();
           })
    }

    that.stop=function() {
        if (task) {
            task.stop();
            logger.info("stopping FFMPEG");
        }
        var startTime = new Date();
        return waitForLiveState(false)
            .then(function() {

                var finishTime = new Date();
                logger.info("Live stopped from API, Operation took "+ (finishTime.getTime() - startTime.getTime()) + " ms");
                return q.resolve();
                });

    }


}

module.exports=EntryTest;
