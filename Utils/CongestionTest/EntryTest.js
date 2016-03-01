
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
    var masterM3U8=null;


    var broadcastingTime=0;
    var playingTime=0;
    var stoppingTime=0;
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



    function waitForLiveStatus(expectedStatus,timeOut) {


        logger.info("waiting for live stream to be ",expectedStatus);
        return retryPromise(function () {
            logger.debug("calling getLiveStatus");


            return testInfo.getLiveStatus(logger).then(function (liveStatus) {

                logger.debug("getLiveStatus returned "+liveStatus);
                if (expectedStatus.indexOf(liveStatus)>-1) {
                        return q.resolve(liveStatus);
                }
                else {
                    return q.reject();
                }
            });
        }, config.entryTest.poolInterval, (1000*timeOut)/config.entryTest.poolInterval);
    }

    function getAllChuncksHeader(flavorUrl)
    {
        return kle.parseChunkListM3U8(logger,flavorUrl)
            .then(function (res) {

                if (res.length<config.entryTest.minimumChunksInChunkList) {

                    var err="chunk list  "+res.length+"<"+config.entryTest.minimumChunksInChunkList+ " flavorUrl "+flavorUrl;
                    logger.warn(err);
                    return q.reject(err);
                }
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

    function verifyHls() {

        var promises=_.map(masterM3U8,function(flavor) {
            logger.info("About to get manifest from flavor ",flavor.bitrate,' url: ',flavor.m3u8);
            return getAllChuncksHeader(flavor.m3u8);
        });

        return q.all(promises);

    }
    function getMasterManifest(masterM3U8Url) {
        logger.info("Testing ",masterM3U8Url);

        return retryPromise(function () {

            return kle.parseMasterM3U8(logger,masterM3U8Url,false);

        }, 1000, 10).then(function(res){
            masterM3U8=res;
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
        logger.info("Getting rtmp url...");

       return testInfo.getRtmpUrl()
           .then(function(urls){
                logger.info("Got url: ",urls);
                task=new FFMpegTask(id, [ "-re","-i",config.sourceFileNames[0],"-c:v","copy","-c:a","libmp3lame","-f","flv","-rtmp_live","1",urls[0]]);
                logger.info("Starting FFMpeg streaming for ", urls);
                return task.start();
            })
            .then(function() {
                var finishTime = new Date();
                logger.info("FFMpeg started, Operation took "+ (finishTime - startTime) + " ms");
                return waitForLiveStatus([1,2],config.entryTest.maxTimeToBeBroadcasting);
            })
           .then(function(newState) {

               if (newState===2) {
                   var finishTime = new Date();
                   logger.info("got state BROADCASTING after %s ms waiting for playing", (finishTime - startTime));

                   broadcastingTime= (finishTime - startTime);

                   return waitForLiveStatus([1], config.entryTest.maxTimeToBeLive);
               }
            })
            .then(function() {
               var finishTime = new Date();
               logger.info("got state PLAYING after %s ms waiting for playing", (finishTime - startTime));
               playingTime= (finishTime - startTime);
               return testInfo.getMasterManifest(logger);
            }).then(function (masterClipListUrl) {
                var finishTime = new Date();
                logger.info("Received Url ",masterClipListUrl," from wowza, Operation took ", (finishTime - startTime) , " ms");
                return getMasterManifest(masterClipListUrl);
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

    that.verifyAlive=function() {
        logger.info("Verify stream is alive");

        return waitForLiveStatus([1])
            .then(function() {
                return verifyHls();
            });
    }

    that.stop=function() {
        stopffmpegTask();
        var startTime = new Date();
        return waitForLiveStatus([0],config.entryTest.maxTimeToBeOffline)
            .then(function() {

                var finishTime = new Date();
                logger.info("Live stopped from API, Operation took "+ (finishTime - startTime) + " ms");
                stoppingTime=finishTime-startTime;
                return q.resolve();
                });

    };

    that.getResults=function() {

        return {
            broadcastingTime: broadcastingTime,
            playingTime: playingTime,
            stoppingTime: stoppingTime
        };
    };


}

module.exports=EntryTest;
