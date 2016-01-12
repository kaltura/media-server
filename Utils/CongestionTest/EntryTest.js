
var FFMpegTask=require('./ffmpegModule/FFMpegTask.js').FFMpegTask;
var kle = require('./API/KalturaLiveEntries.js').KalturaLiveEntries;
var q = require('q');
var _ = require('underscore');
var modulelogger = require('./logger')(module);
var loggerDecorator = require('./config/log-decorator');
var config = require('config');
var request = require('request');


function EntryTest(entryId) {

    // private variables
    var that = this;


    var id= _.isObject(entryId) ? entryId.id : entryId;
    var task=null;
    var logger = loggerDecorator(modulelogger, messageDecoration);
    logger.info("Generate new instance for ",id);



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

    function waitForLiveState(expectedState,timeOut) {


        return retryPromise(function () {
            logger.debug("calling getMasterManifest on entryId",entryId);

            //case of d
            if (_.isObject(entryId)) {

                return kle.parseMasterM3U8(entryId.m3u8Url).then(function(flavors) {
                    return q.resolve(entryId.m3u8Url);
                });
            }
            return getMasterManifest(entryId).then(function (masterClipList) {

                logger.debug("getMasterManifest returned "+masterClipList);
                if (expectedState) {
                    if (masterClipList) {
                        return q.resolve(masterClipList);
                    }
                    else {  //try agin in some interval
                        return q.reject();
                    }
                } else {

                    if (masterClipList) {
                        return q.reject();
                    }
                    else {  //try agin in some interval
                        return q.resolve();
                    }
                }
            });
        }, config.entryTest.poolInterval, (1000*timeOut)/config.entryTest.poolInterval);
    }

    function getAllChuncksHeader(flavor)
    {
        return kle.parseChunkListM3U8(flavor)
            .then(function (res) {

                _.each(res,function(chunk){
                    if (chunk.duration<=0) {
                        logger.error("Incorrect duration for chunk ",chunk.duration);
                    }
                });
                var Chunk=res[0];
                logger.info("getting ts  ",Chunk.ts);

                return kle.getTS(Chunk.ts)
                    .then(function(headers){
                            logger.info("successfully downloaded " +Chunk.ts);
                            return q.resolve();
                        })
                    .catch(function(err) {
                            logger.error("err: Failed to download "+Chunk.ts+" : "+ err.message);
                            return q.reject(err);
                        });
            });
    }

    function messageDecoration(msg) {
        return "[" + id + "] " + msg;
    }

    function GetRTMPurl(){
        if (_.isObject(entryId)) {
            return q.resolve(entryId.rtmpUrl);
        }
        return  kle.getEntry(entryId)
            .then(function(entry) {
              return q.resolve(entry.primaryBroadcastingUrl+"/"+entry.streamName);
            })
            .then(function(path){

                var str=path;
                _.each(config.rtmpReplace,function(value,key) {
                    str=str.replace(key,value);
                });

                return q.resolve(str);
            });
    }

    var stopffmpegTask=function() {

        if (task) {
            logger.info("stopping FFMPEG");
            task.stop();
            logger.info("FFMPEG stopped");
            task=null;
        }
    }
    // public functions
    that.start=function() {
        var startTime=new Date();
        logger.info("Getting rtmp url: ");
       return GetRTMPurl()
           .then(
            function(url){
                logger.info("Got url: ",url);

                var rtmpUrl=url.substring(0,url.length-2)+"1";
                task=new FFMpegTask(id, [ "-re","-i",config.sourceFileNames[0],"-c:v","copy","-c:a","libmp3lame","-f","flv","-rtmp_live","1",rtmpUrl]);
                logger.info("Starting FFMpeg streaming for ", url);
                return task.start();
            })
            .then(function() {
                var finishTime = new Date();
                logger.info("FFMpeg started, Operation took "+ (finishTime - startTime) + " ms");
                return waitForLiveState(true,config.entryTest.maxTimeToBeLive);
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
               logger.error("Test Failed!!! ",JSON.stringify(err));

               stopffmpegTask();
               return q.reject(err);
           });
    }

    that.stop=function() {
        stopffmpegTask();
        var startTime = new Date();
        return waitForLiveState(false,config.entryTest.maxTimeToBeOffline)
            .then(function() {

                var finishTime = new Date();
                logger.info("Live stopped from API, Operation took "+ (finishTime.getTime() - startTime.getTime()) + " ms");
                return q.resolve();
                });

    };


}

module.exports=EntryTest;
