var config = require('config');
var kle = require('./API/KalturaLiveEntries.js').KalturaLiveEntries;
var q = require('q');
var _ = require('underscore');
var EntryTest=require('./EntryTest.js');



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



var logger = require('./logger')("main");


var streamEntry=function(entryId,delay,duration) {

    var delay = 1+Math.random()*delay;

    logger.info("Going to start ",entryId," in ",delay,' seconds');
    return q.delay(delay*1000).then(function() {
        var entryTestInstance = new EntryTest(entryId);

        logger.info("Starting",entryId);
        return entryTestInstance.start().then(function () {
            return q.delay(duration * 1000);
        }).then(function () {
            return entryTestInstance.stop();
        }).then(function () {
            return q.resolve(true);
        }).catch(function (err) {
            //test failed
            logger.info("Test of entry ",entryId," failed!")
            return q.resolve(false);
        });
    }).then(function(result) {
        return streamEntry(entryId,delay,duration);
    });
};

var fixedEntries=config.entires.slice(0,config.test.fixedEntries.count);
fixedEntries.forEach(function(entryId){

    streamEntry(entryId, 5, config.test.fixedEntries.duration);
});


var changingEntries=config.entires.slice(config.test.fixedEntries.count,config.test.fixedEntries+config.test.changingEntries.count-1);

changingEntries.forEach(function(entryId){

    streamEntry(entryId, config.test.changingEntries.duration, config.test.changingEntries.duration);
});


console.warn(changingEntries);
(function wait () {
    setTimeout(wait, 1000);
})();