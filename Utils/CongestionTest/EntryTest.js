
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

    var runffmpeg=true;

    var id= testInfo.id;
    var task=null;
    var logger = LoggerEx("EntryTest", id);
    var masterM3U8=null;


    var broadcastingTime=0;
    var playingTime=0;
    var stoppingTime=0;

    var lastTsList={};
    logger.info("Generate new instance for ",id);



    function retryPromise(fn,intervalRetry,maxRetries) {
        logger.debug("retryPromise");
        return fn()
            .catch(function() { // if it fails
                return q.delay(intervalRetry) // delay
                    // retry with more time
                    .then(function(){
                        if (maxRetries <= 0) {
                            return q.reject("maxRetries <= 0");
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
        }, config.entryTest.poolInterval, (1000*timeOut)/config.entryTest.poolInterval)
            .catch(function(err) {
                throw new  Error("Timeout! getLiveStatus didn't return expected status "+expectedStatus+ " for more than "+timeOut+ " seconds");
            });
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
                        logger.error("Incorrect duration (%s) for chunk %s ",chunk.duration,chunk.ts);
                        throw new Error("Incorrect duration for chunk "+chunk.ts+ " duration="+chunk.duration);
                    }
                });

                if (lastTsList[flavorUrl]) {
                   var diff= _.difference(lastTsList[flavorUrl],res);
                    if (diff.length===0) {
                        logger.error("No progress in chunklist for flavor (%s) ",flavorUrl);
                        throw new Error("No progress in chunklist for flavor "+flavorUrl);
                    }
                }
                lastTsList[flavorUrl]=res;

                var promises=_.map([res[0],res[res.length-1]],function(chunk) {
                    logger.info("getting ts ", chunk.ts);

                    return kle.getTS(chunk.ts)
                        .then(function () {
                            logger.info("successfully downloaded %s" , chunk.ts);
                            return q.resolve();
                        })
                        .catch(function (err) {
                            logger.error("err: Failed to download %s (%s)", chunk.ts, err);
                            return q.reject(err);
                        });
                });
                return q.all(promises);
            });
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

    function buildFFmpegCommand(rtmpInfo) {
       // rtmpInfo.bitrates=rtmpInfo.bitrates[0];
     //   rtmpInfo.flavorParamsIds="32";
        var inputFiles=_.reduce(rtmpInfo.bitrates,function(old,bitrate, index) {
            return old.concat(["-i", config.env.sourceFileNames[index]]);
        },[]);
        var map=_.reduce(rtmpInfo.bitrates,function(old,bitrate, index) {
             var rtmps = _.reduce(rtmpInfo.urls, function (old, value) {
                 //libmp3lame
                 return old.concat(["-c:v", "copy", "-c:a", "copy", "-f", "flv", "-rtmp_live", "1", value+(index+1)]);
             }, ["-map",index]);

            return old.concat(rtmps);
        },[]);

        return ['-re'].concat(inputFiles,map);
    }
    // public functions
    that.start=function() {
        var startTime=new Date();
        logger.info("Getting rtmp url...");

       return testInfo.getRtmpInfo()
            .then(function(rtmpInfo){
                logger.info("Got rtmpInfo: %j",rtmpInfo);

               if (runffmpeg) {
                   var cmd=buildFFmpegCommand(rtmpInfo);
                   task = new FFMpegTask(id, cmd);
                   logger.info("Starting FFMpeg streaming (",cmd,")");
                   return task.start();
               }
            }).then(function() {
                var finishTime = new Date();
                logger.info("FFMpeg started, Operation took "+ (finishTime - startTime) + " ms");
                return waitForLiveStatus([1,2],config.entryTest.maxTimeToBeBroadcasting);
            }).then(function(newState) {

               if (newState===2) {
                   var finishTime = new Date();
                   logger.info("got state BROADCASTING after %s ms waiting for playing", (finishTime - startTime));

                   broadcastingTime= (finishTime - startTime);

                   return waitForLiveStatus([1], config.entryTest.maxTimeToBePlaying);
               }
            }).then(function() {
               var finishTime = new Date();
               logger.info("got state PLAYING after %s ms waiting for playing", (finishTime - startTime));
               playingTime= (finishTime - startTime);
               return testInfo.getMasterManifestUrl(logger);
            }).then(function (masterClipListUrl) {
                var finishTime = new Date();
                logger.info("Received Url ",masterClipListUrl," Operation took ", (finishTime - startTime) , " ms");
                return getMasterManifest(masterClipListUrl);
            }).then(function(){
               logger.info("Parsed master manifest successfully, starting was successfull");
               return q.resolve();
            }).catch(function(err) {
               logger.error("Test Failed!!! ",err);

               stopffmpegTask();
               return q.reject(err);
           });
    }

    that.verifyAlive=function(wait) {
        logger.info("Verify stream is alive");


        return waitForLiveStatus([1])
            .then(function() {
                var promises=_.map(masterM3U8,function(flavor) {
                    logger.info("About to get manifest from flavor ",flavor.bitrate,' url: ',flavor.m3u8);
                    return getAllChuncksHeader(flavor.m3u8);
                });

                if (wait) {
                    return retryPromise(function() {
                        return q.all(promises);
                    },1000,5).catch(function(err) {
                        logger.info("Chunklist not ready yet");
                        return q.reject("Timeout waiting for chunklist");
                    });
                } else {
                    return q.all(promises);
                }
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
