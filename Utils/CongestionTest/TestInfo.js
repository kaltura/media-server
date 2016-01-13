


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


WowzaTestInfo.prototype.getMasterManifest=function(logger,expectedState) {
    var _this=this;
    if (expectedState===false) {
        return q.resolve();
    }
    return kle.parseMasterM3U8(logger,_this._info.m3u8Url,false).then(function(flavors) {
        return q.resolve(_this._info.m3u8Url);
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

                    return url.substring(0,url.length-2)+index;
                }
            );
            return q.resolve(newList);
        });
}


KalturaTestInfo.prototype.getMasterManifest=function(logger,expectedState) {
     return  kle.getEntry(this.id)
        .then(function(entry) {
            var hlsconfig = _.filter(entry.liveStreamConfigurations, function (config) {
                return config.protocol === "hls";
            })[0];

            return  q.resolve(hlsconfig.url);
        });
}


exports.KalturaTestInfo=KalturaTestInfo;
exports.WowzaTestInfo=WowzaTestInfo;
