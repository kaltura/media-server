
var FFMpegTask=require('./ffmpegModule/FFMpegTask.js').FFMpegTask;
var kle = require('./API/KalturaLiveEntries.js').KalturaLiveEntries;
var q = require('q');
var _ = require('underscore');
var LoggerEx = require('./utils').LoggerEx;
var config = require('config');
var request = require('request');


function EntryTest(testInfo) {

    // private variables
    var that = this;


    var id= testInfo.id;
    var task=null;
    var logger = LoggerEx("EntryTest", id);

    logger.info("Generate new instance for ",id);



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


        logger.info("waiting for live stream to be active");
        return retryPromise(function () {
            logger.debug("calling getMasterManifest");


            return testInfo.getMasterManifest().then(function (masterClipList) {

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


    function GetRTMPurl(){
        return testInfo.getRtmpUrl();
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
        logger.info("Getting rtmp url...");
       return GetRTMPurl()
           .then(
            function(urls){
                logger.info("Got url: ",urls);

                task=new FFMpegTask(id, [ "-re","-i",config.sourceFileNames[0],"-c:v","copy","-c:a","libmp3lame","-f","flv","-rtmp_live","1",urls[0]]);
                logger.info("Starting FFMpeg streaming for ", urls);
                return task.start();
            })
            .then(function() {
                var finishTime = new Date();
                logger.info("FFMpeg started, Operation took "+ (finishTime - startTime) + " ms");
                return waitForLiveState(true,config.entryTest.maxTimeToBeLive);
            })
            .then(function(url){
                var finishTime = new Date();
                logger.info("Recive Url ",url," from Wowza, Operation took ", (finishTime - startTime) , " ms");
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
