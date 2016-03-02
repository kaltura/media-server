


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

WowzaTestInfo.prototype.getRtmpInfo=function() {
    return q.resolve(this._info.rtmpInfo);
}


WowzaTestInfo.prototype.getMasterManifestUrl=function(logger) {
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




function KalturaTestInfo(entryObj) {

    this.id=entryObj.entryId;
    this.useBackup=entryObj.useBackup;
}

KalturaTestInfo.prototype.getRtmpInfo=function() {

    var self=this;
    return  kle.getEntry(this.id)
        .then(function(entry) {
            var urls=[];
            if (entry.primaryBroadcastingUrl) {
                urls.push(entry.primaryBroadcastingUrl+ "/" + entry.streamName);
            }
            if (self.useBackup && entry.secondaryBroadcastingUrl ) {
                urls.push(entry.secondaryBroadcastingUrl+ "/" + entry.streamName);
            }
            return q.resolve( { urls: urls, bitrates: entry.bitrates, flavorParamsIds: entry.flavorParamsIds});
        })
        .then(function(results){

            results.urls= _.map(results.urls,function(url,index) {
                    _.each(config.rtmpReplace,function(value,key) {
                        url=url.replace(key,value);
                    });

                    return url.substring(0,url.length-2)+(1);
                }
            );
            return q.resolve(results);
        });
}


KalturaTestInfo.prototype.getMasterManifestUrl=function(logger) {

    var playManifest=config.KalturaService.serverAddress+"/p/"+config.KalturaService.partnerId+"/sp/"+config.KalturaService.partnerId+"00/playManifest/entryId/"+this.id+"/format/applehttp/protocol/http/a.m3u8";
    return q.resolve(playManifest);
    /*
    return kle.getPlayManifest(

     return  kle.getEntry(this.id)
        .then(function(entry) {

             var hlsconfig = _.filter(entry.liveStreamConfigurations, function (config) {
                return config.protocol === "hls";
            })[0];

            return  q.resolve(hlsconfig.url);
        });*/

}

KalturaTestInfo.prototype.getLiveStatus=function(logger) {
    return  kle.getEntry(this.id)
        .then(function(entry) {
            return entry.liveStatus;
        });
}


exports.KalturaTestInfo=KalturaTestInfo;
exports.WowzaTestInfo=WowzaTestInfo;
