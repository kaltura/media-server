/**
 * Created by igors on 1/17/16.
 */

var kas = require("../KalturaApi/KalturaService.js"),
 q = require('q'),
 log4js = require('log4js'),
 kms = require('./KMService.js'),
 pl = require('./Playlist.js'),
 utils = require('../utils/utils.js'),
 xml2js = require('xml2js');

var logger = log4js.getLogger('LiveStreamEntry');

var liveEntryStatus = {
    STOPPED:0,
    BROADCASTING:1,
    PLAYABLE:2
};

var LiveEntryMethods = {
  authenticate:'authenticate',
  publish:'registerMediaServer',
  unpublish:'unregisterMediaServer'
};

var makeUniqueKey = function(m){
    return m.entryId;
}

var liveStreamEntries = [];

var methodNotimplemented = function(){
    return q.reject('error: not implemented');
}

var methodNoop = function(){
    return q.resolve();
}

function StoppedState(liveEntry){
    return {
        name: 'StoppedState',
        authenticate: function(m){
            liveEntry._logger.info("authenticate %s", m.entryId);

            liveEntry._params = {
                service: 'liveStream',
                action: LiveEntryMethods.authenticate,
                ignoreNull: true,
                entryId: m.entryId,
                token: m.token
            };
            liveEntry._state = new AuthenticatedState(liveEntry);
            return liveEntry.exec_i();
        },
        publish: methodNotimplemented,
        unpublish: methodNotimplemented,
        error:function(){},
        success:function(){},
        getTranscodeTemplate:methodNotimplemented
    };
};

function AuthenticatedState(liveEntry){
    return {
        name: 'AuthenticatedState',
        authenticate: methodNoop,
        publish: function(m){

            liveEntry._logger.info("publish %s flavour %d", liveEntry._params.entryId,m.flavourId);

            liveEntry._state = new BroadcastingState(liveEntry);
            liveEntry._params =  {
                service: 'liveStream',
                action: LiveEntryMethods.publish,
                partnerId: m.partnerId,
                hostname: m.hostName,
                clientTag: m.serverNode,
                mediaServerIndex: m.mediaServerIndex,
                entryId: m.entryId,
                ignoreNull: true,
                liveEntryStatus: liveEntryStatus.BROADCASTING
            };
            liveEntry._playlist.addInjest(m.flavourId);
            return liveEntry.exec_i();
        },
        unpublish: methodNotimplemented,
        error:liveEntry.stop,
        success:function(){},
        getTranscodeTemplate:liveEntry.getTranscodeTemplateReal
    };
};

function BroadcastingState(liveEntry){
    return {
        name: 'BroadcastingState',
        authenticate: methodNoop,
        publish: function(m){
            liveEntry._logger.info("publish %s flavour %d", liveEntry._params.entryId,m.flavourId);
            liveEntry._playlist.addInjest(m.flavourId);
            return q.resolve('');
        },
        unpublish: function(m){
            liveEntry._logger.info("unpublish %s", m.entryId);
            liveEntry._params.method = LiveEntryMethods.unpublish;
            liveEntry._state = new StoppedState(liveEntry);
            return liveEntry.exec_i();
        },
        error:liveEntry.stop,
        success:liveEntry.onKAPICallComplete,
        getTranscodeTemplate:liveEntry.getTranscodeTemplateReal
    };
};

function LiveStreamEntry(m) {
    var _this = this;

    _this._logger = logger;

    _this.stop = function(err){
        _this._params = undefined;
        if(_this._timer) {
            clearTimeout(_this._timer);
            _this._timer = undefined;
        }
        _this._state = new StoppedState(_this);
        if(err){
            _this._logger.warn("live entry %s stops with error %s", m.entryId, err);
            var key = makeUniqueKey(m);
            delete liveStreamEntries[key];
        }
    };



    _this.onKAPICallComplete = function() {

        _this._logger.info("onKAPICallComplete set timer", m.entryId);

        _this._timer = setTimeout(function(){
            if(_this._timer) {
                clearTimeout(_this._timer);
                _this._timer = undefined;
            }
            _this._playlist.refresh().then( function() {

                _this._logger.info("keepalive entry %s", m.entryId);
                _this._params.status = _this._playlist.getStatus();

                if(_this._params.status == liveEntryStatus.STOPPED){
                    _this._logger.info("onTimer entry %s. no more streaming! send unpublish", m.entryId);
                    _this._state.unpublish().finally(_this.stop)
                } else {
                    _this.exec_i().catch(_this.stop);
                }
            });
        }, m.config.keepaliveTimeout);
    };

    _this.exec_i = function() {

        return kas.KalturaService.call(_this._params).then(function () {
              _this._logger.info("reported ok");
            _this._state.success();
            return q.resolve(_this);
         }, function (err) {
              _this._logger.warn("fail to report " + err);
            _this._state.error();
              return q.reject(e);
         });
    };

    _this.getTranscodeTemplateReal=function(url){

        var onSuccess = function(xml){

            var parser = new xml2js.Parser();
            var retVal;
            parser.parseString(xml, function (err, result) {
                try {

                    if(err)
                     throw(err);

                     if(!result)
                         throw  'null reference!';

                    result.Root.Transcode.forEach(function (xCode) {
                        xCode.StreamNameGroups[0].StreamNameGroup.filter(function (streamGroup) {
                             if(streamGroup.Name[0] == 'all') {
                                streamGroup.Members[0].Member.forEach(function (member) {
                                    var arr = xCode.Encodes[0].Encode.every(function (e) {
                                        if (member.EncodeName[0] == e.Name[0]) {
                                            _this._playlist.addStream(e.StreamName[0]);
                                            return false;
                                        }
                                        return true;
                                    })
                                });
                                return false;
                             }
                             return true;
                        });
                    });
                    retVal = q.resolve(xml);
                }
                catch(err)
                {
                    retVal = q.reject(err);
                }
            });
            return retVal;
        };

        var partnerId = m.partnerId ? m.partnerId : kas.KalturaService.getConfigValue('partnerId');

        var arr = url.split('streamName');

        var newUrl =  kas.KalturaService.getConfigValue('serviceUrl') + arr[0] + 'partnerId/' + partnerId + '/streamName' + arr[1];

        return utils.httpGet(
            newUrl
            //'http://cdnapi.kaltura.com/api_v3/index.php/service/wowza_liveConversionProfile/action/serve/partnerId/1883581/streamName/1_n6sqf1kv_1/f/transcode.xml'
            //'/Users/igors/Documents/transcode.xml'
             )
            .then( function(r){
                return onSuccess(r.body);
            })
            .catch(function(e) {
                return q.reject(e);
            });

        var params = {
            service:'baseEntry',
            action:'get',
            entryId: m.entryId,
            ignoreNull: true,
        };

        return kas.KalturaService.call(params).then( function(entry) {

            var params = {
                service: 'wowza_liveConversionProfile',
                action: 'serve',
                partnerId: entry.partnerId,
                streamName: m.entryId,
                ignoreNull: true,
            };
            return kas.KalturaService.callAnonymous(params).then(onSuccess);
        });
    }

    _this.stop();

    _this._playlist = new pl.PlayList(m.entryId, m.serverNode,m.config);

    _this.getStreamingServer=function(){
        return m.hostName;
    }

    return _this;
}

LiveStreamEntry.prototype.getTranscodeTemplate = function(url){

    var _this = this;
     return _this.getTranscodeTemplateReal(url);
}

LiveStreamEntry.prototype.execute = function(m) {

    var _this = this;

    switch (m.method) {
        case kms.ApiMethods.authenticate:
            return _this._state.authenticate(m);
        case kms.ApiMethods.publish:
            return _this._state.publish(m);
        case kms.ApiMethods.unpublish:
            return _this._state.unpublish(m);
        default:
            return methodNotimplemented();
    }
 }

LiveStreamEntry.prototype.getPlaylist = function(m) {
    var _this = this;
    if(_this._playlist.getStatus() == liveEntryStatus.PLAYABLE) {
        return q.resolve(_this._playlist.getPlaylist());
    } else {
        return q.reject(_this._playlist ? 'not enough chunks' : 'playlist is empty');
    }
}

function getLiveStreamEntry(m,config){
    var key = makeUniqueKey(m);
    var le = liveStreamEntries[key];
    if ( le == null || m.method == LiveEntryMethods.authenticate ){
        logger.info("getLiveStreamEntry. create new entry with key " + key);
        m.config = config;
        if(le){
            try {
                le.stop('server restarted');
            }
            catch(e){
                logger.warn("getLiveStreamEntry. error during unpublish:  " + e);
            }
        }
        le = new LiveStreamEntry(m);
        liveStreamEntries[key] = le;
        //return le.initFromMediaServer();
    }
    return q.resolve(le);
}

function reinitLiveStreams(){
    for( key in liveStreamEntries){
        liveStreamEntries[key].stop();
        delete liveStreamEntries[key];
    }
}


exports.reinitLiveStreams=reinitLiveStreams;

exports.getLiveStreamEntry=getLiveStreamEntry;

exports.liveEntryStatus = liveEntryStatus;