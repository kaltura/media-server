


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
};




function KalturaTestInfo(entryObj) {

    this.id=entryObj.entryId;
    this.useBackup=entryObj.useBackup;
    this.flavorsToStream=entryObj.flavorsToStream || 3;
}

KalturaTestInfo.prototype.getRtmpInfo=function() {

    var self=this;
    return  kle.getEntry(this.id)
        .then(function(result) {
            var entry=result[0];
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

KalturaTestInfo.prototype.checkDualDC=function(entries, numOfWowza) {

    if (entries[0].liveStatus!==entries[1].liveStatus){
        return q.reject("Both dc are not in sync on LiveStatus: "+entries[0].liveStatus+ " "+entries[1].liveStatus);
    }


    function hash_hls_urls(entry){
        var hlsconfig = _.map( _.filter(entry.liveStreamConfigurations, function (config) {
            return config.protocol === "hls";
        }),function(config) {
            if (numOfWowza==1){
                return [config.url];
            }
            return [config.url,config.backupUrl];
        });


        return hlsconfig[0].sort().join("_");
    }
    if (hash_hls_urls(entries[0])!==hash_hls_urls(entries[1])) {
       return q.reject(logger.error("Hls config are not in sync in both dc: "+hash_hls_urls(entries[0])+ " "+hash_hls_urls(entries[1])));
    }
    return q.resolve()
}

KalturaTestInfo.prototype.getLiveStatus=function(logger) {
    var self=this;
    self.logger=logger;
    if (config.KalturaService.serverAddress.length==1){
        return kle.getEntry(this.id)
            .then(function(results) {
                self.logger.debug("got live status %d , kaltura session %s",results[0].liveStatus, results["1"]["x-kaltura-session"])
            return results[0].liveStatus;
        });
    }
    var entry_id=this.id;
   var entryConfiguration= _.find(config.entires, function(element){
        return element.entryId===entry_id;
    });
    var numOfWowza= (entryConfiguration.useBackup === true) ? 2 :1;
    return q.all([kle.getEntry(this.id,0),kle.getEntry(this.id,1)])
        .then(function(results) {
            var entries=[results[0][0], results[1][0]];
            return self.checkDualDC(entries, numOfWowza)
                .then(function(){
                    return entries[0].liveStatus;
                })
        });
};


exports.KalturaTestInfo=KalturaTestInfo;
exports.WowzaTestInfo=WowzaTestInfo;
