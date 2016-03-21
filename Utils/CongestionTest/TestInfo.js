


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
    this.flavorsToStream=entryObj.flavorsToStream || 3;
}

KalturaTestInfo.prototype.getRtmpInfo=function() {

    var self=this;
    return  kle.getEntry(this.id)
        .then(function(entry) {
            var urls=[];
            entry.bitrates=entry.bitrates.splice(0,self.flavorsToStream);
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

                    return url.substring(0,url.length-2);
                }
            );
            return q.resolve(results);
        });
}


KalturaTestInfo.prototype.getMasterManifestUrl=function(logger) {
    var playManifest=config.KalturaService.serverAddress[0]+"/p/"+config.KalturaService.partnerId+"/sp/"+config.KalturaService.partnerId+"00/playManifest/entryId/"+this.id+"/format/applehttp/protocol/http/a.m3u8";
    return q.resolve(playManifest);
}

KalturaTestInfo.prototype.checkDualDC=function(entries) {
    if (entries.length<=1)
        return;

    if (entries[0].liveStatus!==entries[1].liveStatus)
         throw  new Error("Both dc are not in sync"+entries[0].liveStatus+ " "+entries[1].liveStatus);

    function hash_hls_urls(entry){
        var hlsconfig = _.map( _.filter(entry.liveStreamConfigurations, function (config) {
            return config.protocol === "hls";
        }),function(config) {
            return config.url+"_"+config.backupUrl;
        });

        hlsconfig.sort();


        return hlsconfig.join("_");
    }
    if (hash_hls_urls(entries[0])!==hash_hls_urls(entries[1])) {

        throw  new Error("Both dc are not in sync"+entries[0].liveStatus+ " "+entries[1].liveStatus)
    }
}

KalturaTestInfo.prototype.getLiveStatus=function(logger) {
    var self=this;
    if (config.KalturaService.serverAddress.length==1){
        return kle.getEntry(this.id)
            .then(function(entry) {
            return entry.liveStatus;
        });
    }
    return q.all([kle.getEntry(this.id,0),kle.getEntry(this.id,1)])
        .then(function(entries) {
            self.checkDualDC(entries);
            return entries[0].liveStatus;
        });
}


exports.KalturaTestInfo=KalturaTestInfo;
exports.WowzaTestInfo=WowzaTestInfo;
