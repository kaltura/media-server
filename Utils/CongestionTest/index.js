var config = require('config');
var kle = require('./API/KalturaLiveEntries.js').KalturaLiveEntries;
var q = require('q');
var _ = require('underscore');
var EntryTest=require('./EntryTest.js');
var LoggerEx = require('./utils').LoggerEx;
var WowzaTestInfo = require('./testInfo.js').WowzaTestInfo;
var KalturaTestInfo = require('./testInfo.js').KalturaTestInfo;

/*

kle.getConversionProfiles().then(function(res) {
    console.warn("Conversion profiles for partner",config.KalturaService.partnerId,":",
    _.map(res,function(profile) { return "'"+profile.name+"' (id="+profile.id+")";}).join(", "));
});


kle.getEntries(true).then(function(res) {
    console.warn("Currently broadcasting live Entries for partner ",config.KalturaService.partnerId,":",
    _.map(res,function(entry) { return "'"+entry.name+"' (id="+entry.id+")";}).join(", "));
});

kle.getEntries(false).then(function(res) {
    console.warn("Currently  live Entries for partner ",config.KalturaService.partnerId,":",
    _.map(res,function(entry) { return "'"+entry.name+"' (id="+entry.id+")";}).join(", "));
});



kle.getEntries(false).then(function(res) {
    res.forEach(function(entry){
        var EntryTestInstance=new EntryTest(entry.id);
        EntryTestInstance.start().then(function() {
            return q.delay(5*1000);
        }).then(function() {
            return EntryTestInstance.stop();
        }).catch(function() {
            //test failed
            return q.reject();
        });
    });
});


*/


var logger = LoggerEx("MainTest","");

var streamEntry=function(testInfo,minDelay,maxDelay,duration) {

    var testObj=null;
    if (_.isObject(testInfo)) {
        testObj=new WowzaTestInfo(testInfo);
    } else {
        testObj=new KalturaTestInfo(testInfo);
    }
    var id= testObj.id;

    var delay = minDelay + Math.random()*(maxDelay-minDelay);

    logger.info("Going to start ",id," in ",delay,' seconds');
    return q.delay(delay*1000).then(function() {
        var entryTestInstance = new EntryTest(testObj);

        logger.info("Starting",id);
        return entryTestInstance.start().then(function () {
            logger.info("Entry",id,"strated succsefully, waiting ",duration," seconds")
            return q.delay(duration * 1000);
        }).then(function () {
            logger.info("Entry",id,"stopping");
            return entryTestInstance.stop();
        }).then(function () {
            logger.info("Test of entry ",id," was success!")
            return q.resolve(true);
        }).catch(function (err) {
            //test failed
            logger.info("Test of entry ",id," failed!")
            return q.resolve(false);
        });
    }).then(function(result) {
        return streamEntry(testInfo,minDelay,maxDelay,duration);
    });
};

var entries=config.entires;

/*
entries=[];

for (var i=0;i<3;i++) {

    entries.push({
        "id": i,
        "rtmpUrls":["rtmp://localhost:1935/live/"+i+"/abc123_1"],
        "m3u8Url":"http://localhost:1935/live/"+i+"/abc123_1/playlist.m3u8"
    });
}*/

var fixedEntries=entries.slice(0,config.test.fixedEntries.count);
fixedEntries.forEach(function(entryId){
    streamEntry(entryId, config.test.fixedEntries.minDelay,config.test.fixedEntries.maxDelay, config.test.fixedEntries.duration);
});


var changingEntries=entries.slice(config.test.fixedEntries.count,config.test.fixedEntries+config.test.changingEntries.count-1);

changingEntries.forEach(function(entryId){

    streamEntry(entryId, config.test.changingEntries.minDelay,config.test.changingEntries.maxDelay,  config.test.changingEntries.duration);
});


(function wait () {
    setTimeout(wait, 1000);
})();