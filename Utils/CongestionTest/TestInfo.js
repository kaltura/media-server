


var FFMpegTask=require('./ffmpegModule/FFMpegTask.js').FFMpegTask;
var kle = require('./API/KalturaLiveEntries.js').KalturaLiveEntries;
var q = require('q');
var _ = require('underscore');
var LoggerEx = require('./utils').LoggerEx;
var config = require('config');
var request = require('request');

function WowzaTestInfo(info) {

    this._info=info;
    this.id=info.id;
}

WowzaTestInfo.prototype.getRtmpUrl=function() {
    return q.resolve(this._info.rtmpUrls);
}


WowzaTestInfo.prototype.getMasterManifest=function(logger) {
    var _this=this;
    return kle.parseMasterM3U8(logger,_this._info.m3u8Url,false).then(function(flavors) {

        return q.resolve(_this._info.m3u8Url);
    });
}

WowzaTestInfo.prototype.getLiveStatus=function(logger) {
    var _this=this;
    return kle.parseMasterM3U8(logger,_this._info.m3u8Url,false).then(function(flavors) {
        if (flavors.length===0) {
            return q.resolve(0);
        }
        return kle.parseChunkListM3U8(logger,flavors[0].m3u8)
                    .then(function (res) {

                        if (res.length >= config.entryTest.minimumChunksInChunkList) {
                            return q.resolve(1);
                        } else {
                            return q.resolve(2);
                        }
                    },function() {
                       return q.resolve(2);
                    });
    }).catch(function() {
        return q.resolve(0);
    });
}




function KalturaTestInfo(entryId) {

    this.id=entryId;
}

KalturaTestInfo.prototype.getRtmpUrl=function() {

    return  kle.getEntry(this.id)
        .then(function(entry) {
            return q.resolve([entry.primaryBroadcastingUrl + "/" + entry.streamName]);
        })
        .then(function(urls){

            var newList=_.map(urls,function(url,index) {
                    _.each(config.rtmpReplace,function(value,key) {
                        url=url.replace(key,value);
                    });

                    return url.substring(0,url.length-2)+(index+1);
                }
            );
            return q.resolve(newList);
        });
}


KalturaTestInfo.prototype.getMasterManifest=function(logger) {
     return  kle.getEntry(this.id)
        .then(function(entry) {
            var hlsconfig = _.filter(entry.liveStreamConfigurations, function (config) {
                return config.protocol === "hls";
            })[0];

            return  q.resolve(hlsconfig.url);
        });
}

KalturaTestInfo.prototype.getLiveStatus=function(logger) {
    return  kle.getEntry(this.id)
        .then(function(entry) {
            return entry.liveStatus;
        });
}


exports.KalturaTestInfo=KalturaTestInfo;
exports.WowzaTestInfo=WowzaTestInfo;
