var $q = require('q');
var request = require('request');
var config = require('config');
var _ = require('underscore');
var url= require('url');
var kalturaAPI=require('./KalturaAPI.js').KalturaAPI;


function KalturaLiveEntries() {

}
/**
 *
 * @param liveValue
 */
KalturaLiveEntries.prototype.getEntries=function(liveValue){

    var obj={
        service: "liveStream",
        action: "list",
        "filter:orderBy": "-createdAt",

    };

    if (liveValue!==undefined) {
        obj["filter:isLive"]=liveValue;
    }
    return kalturaAPI.call(obj).then(function (res) {
        return $q.resolve(res.objects);
    }, function (error) {
        return $q.reject(error);
    });
}

/**
 *
 * @param liveValue
 */
KalturaLiveEntries.prototype.getEntry=function(entryId){

    var obj={
        service: "liveStream",
        action: "get",
        entryId: entryId

    };

    return kalturaAPI.call(obj).then(function (res) {
        return $q.resolve(res);
    }, function (error) {
        return $q.reject(error);
    });
}

KalturaLiveEntries.prototype.createEntry=function(name,description,conversionProfileId,recording,dvr){
    return kalturaAPI.call({
        service: "liveStream",
        action: "add",
        "liveStreamEntry:objectType": "KalturaLiveStreamEntry",
        "sourceType": "32",
        "liveStreamEntry:name": name,
        "liveStreamEntry:description": description,
        "liveStreamEntry:conversionProfileId": conversionProfileId,
        "liveStreamEntry:recordStatus": recording ? 1 : 0,
        "liveStreamEntry:dvrStatus": dvr ? 1 : 0,
        "liveStreamEntry:dvrWindow": 120,
        "liveStreamEntry:mediaType": 201
    }).then(function (res) {
        return $q.resolve(res);
    }, function (error) {
        return $q.reject(error);
    });
}

KalturaLiveEntries.prototype.deleteEntry=function(id){
    return kalturaAPI.call({
        service: "liveStream",
        action: "delete",
        "entryId": id
    }).then(function (res) {
        return $q.resolve(res);
    }, function (error) {
        return $q.reject(error);
    });
}

KalturaLiveEntries.prototype.getConversionProfiles=function(){
    return kalturaAPI.call({  service: "conversionProfile",
                    "filter:typeEqual": 2,
                    action: "list"}).then(function (res) {
        return $q.resolve(res.objects);
    }, function (error) {
        return $q.reject(error);
    });
}


KalturaLiveEntries.prototype.getBroadcastingUrls=function(){

    return this.getEntries().then(function(res) {
       return $q.resolve(_.map(res,function(entry) { return entry.primaryBroadcastingUrl+"/"+entry.streamName; }));
    });
}




KalturaLiveEntries.prototype.parseMasterM3U8=function(masterUrl) {
    return $q.Promise( function(resolve,reject) {
        request.get({
            url: masterUrl
        }, function (error, response, result) {

            var re = /BANDWIDTH=([^,]*),*RESOLUTION=(.*).*\n(.*.m3u8)/gm;

            var res = [];
            var m;

            while ((m = re.exec(result)) !== null) {
                if (m.index === re.lastIndex) {
                    re.lastIndex++;
                }

                res.push({
                    bitrate: m[1],
                    resolution: m[2],
                    m3u8: url.resolve(masterUrl, m[3])
                });
            }


            resolve(res);
        });
    });
}


KalturaLiveEntries.prototype.parseChunkListM3U8=function(chunksUrl) {
    return $q.Promise( function(resolve,reject) {
        request.get({
            url: chunksUrl
        }, function (error, response, result) {

            var re = /EXTINF:(.*)\n(.*.ts)/gm;

            var res = [];
            var m;

            while ((m = re.exec(result)) !== null) {
                if (m.index === re.lastIndex) {
                    re.lastIndex++;
                }

                res.push({
                    duration: m[1],
                    ts: url.resolve(chunksUrl, m[2])
                });
            }


            resolve(res);
        });
    });
}

KalturaLiveEntries.prototype.getTS=function(chunkUrl) {
    return $q.Promise( function(resolve,reject) {
        request.head
        ({
            url: chunkUrl
        }, function (error, response, result) {
            if (error)
            {
                console.log("err1: "+err);
                reject(error);
                return;
            }
            resolve(response.headers);
        });
    })
}
var ks=module.exports.KalturaLiveEntries=new KalturaLiveEntries();