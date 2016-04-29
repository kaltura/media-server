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
KalturaLiveEntries.prototype.getEntry=function(entryId , serverIndex){

    var obj={
        serverIndex : serverIndex,
        service: "liveStream",
        action: "list",
        "filter:idEqual": entryId

    };

    return kalturaAPI.call(obj).then(function (res) {
        return $q.resolve([res[0].objects[0], res[1]]);
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




KalturaLiveEntries.prototype.parseMasterM3U8=function(logger,masterUrl,failOnError) {
    return $q.Promise( function(resolve,reject) {
        request.get({
            url: masterUrl,
            followAllRedirects: true
        }, function (error, response, result) {

            logger.debug("parseMasterM3U8: Got response from",masterUrl,":",result);

            var re = /BANDWIDTH=([\d.]*)(?:,RESOLUTION=([\d]*))?.*\n(.*.m3u8)/gm;

            var res = [];
            var m;

            while ((m = re.exec(result)) !== null) {
                if (m.index === re.lastIndex) {
                    re.lastIndex++;
                }

                res.push({
                    bitrate: m[1],
                    resolution: m[2],
                    m3u8: url.resolve(response.request.href, m[3])
                });
            }

            if (failOnError && (error || response.statusCode!==200 || res.length==0)) {
                reject("Empty master manifest");
            } else {
                resolve(res);
            }
        });
    });
}


KalturaLiveEntries.prototype.parseChunkListM3U8=function(logger,chunksUrl) {
    return $q.Promise( function(resolve,reject) {
        request.get({
            url: chunksUrl
        }, function (error, response, result) {

            try {
                logger.debug("Got response from",chunksUrl,":",result);
                var re = /EXTINF:([\d.]*),\n(.*.ts)/gm;

                var res = [];
                var m;

                while ((m = re.exec(result)) !== null) {
                    if (m.index === re.lastIndex) {
                        re.lastIndex++;
                    }

                    res.push({
                        duration: parseFloat(m[1]),
                        ts: url.resolve(chunksUrl, m[2])
                    });
                }


                resolve(res);
            } catch(err) {
                reject(err);
            }
        });
    });
}

KalturaLiveEntries.prototype.getTS=function(chunkUrl) {
    return $q.Promise( function(resolve,reject) {
        request.head({
            url: chunkUrl
        }, function (error, response, result) {
            if (error || response.statusCode!==200 ||
                parseInt(response.headers["content-length"])===0) {
                reject(error);
                return;
            }
            resolve(true);
        });
    });
}
var ks=module.exports.KalturaLiveEntries=new KalturaLiveEntries();