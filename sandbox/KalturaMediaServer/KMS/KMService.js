'use strict';

var express = require('express');
var http = require('http');
var Q = require('q');
var log4js = require('log4js');
var utils=require("../Utils/utils.js");

var lse = require("./LiveStreamEntry.js");

function KMService() {
    var _this = this;
    _this._logger = log4js.getLogger('KMService');
    _this._app = express();

    return _this;
}

var ApiMethods = {
    appinit:'appinit',
    appstop:'appstop',
    authenticate:'authenticate',
    publish:'publish',
    unpublish:'unpublish'
};


KMService.prototype.init=function(config,pluginConfig) {

    var _this = this;

    _this._pluginConfig = pluginConfig;

    this._app.get( "/crossdomain.xml",  function ( req, res ) {
        utils.httpGet('http://localhost/crossdomain.xml')
            .then(function(r){

                for( var p in r.response.headers){
                    if(r.response.headers.propertyIsEnumerable(p) && r.response.headers.hasOwnProperty(p)) {
                        res.setHeader(p, r.response.headers[p]);
                    }
                }
                res.send(r.body);

            })
            .catch(function(e){
                res.statusCode = 404;
            })
            .finally(function(){
                res.end();
            })
     /*   var xml = '<?xml version="1.0"?>\n<!DOCTYPE cross-domain-policy SYSTEM' +
            ' "http://www.macromedia.com/xml/dtds/cross-domain-policy.dtd">\n<cross-domain-policy>\n';
        xml += '<allow-access-from domain="*" to-ports="*"/>\n';
        xml += '</cross-domain-policy>\n';

        req.setEncoding('utf8');
        res.writeHead( 200, {'Content-Type': 'text/xml'} );
        res.end( xml );*/
    });

    //_this._app.post('*', function (req, res) {
    //    _this._logger.warn("begin Request for: ", url);
    //});

    // this._app.get('*/chunklist.m3u8', function (req, res) {
    //
    //    _this._logger.info("get begin Request for: ", req.url);
    //
    //    var entryId = /.*\/(.*)\/chunklist.m3u8/.exec(req.url);
    //    (entryId ?  lse.getLiveStreamEntry({entryId: entryId[1]}, _this._pluginConfig)
    //        : q.reject('entry not found'))
    //        .then( function(liveEntry){
    //            return utils.httpGet('http:/' + req.url) ;
    //        })
    //        .then(function(chunklist){
    //            res.setHeader('Content-Type','application/vnd.apple.mpegurl');
    //            res.setHeader('Cache-Control','no-cache');
    //            match = chunklist.match(/media(_w\d+).ts/);
    //            if( match ){
    //                chunklist = chunklist.replace(match[1],'');
    //            }
    //            res.send(chunklist);
    //        })
    //        .catch(function(err){
    //            res.statusMessage = err;
    //            res.statusCode = 404;})
    //        .finally(function(){
    //            res.end();
    //        });
    //});


    this._app.get('*/playlist.m3u8', function (req, res) {

        _this._logger.info("get begin Request for: ", req.url);

        var entryId = /.*\/(.*)\/playlist.m3u8/.exec(req.url);
        (entryId ?  lse.getLiveStreamEntry({entryId: entryId[1]}, _this._pluginConfig)
           : q.reject('entry not found'))
                .then( function(liveEntry){
                    return liveEntry.getPlaylist();
                })
               .then(function(playlist){
                    res.setHeader('Content-Type','application/vnd.apple.mpegurl');
                    res.setHeader('Cache-Control','no-cache');
                    res.send(playlist);
                    })
               .catch(function(err){
                      res.statusMessage = err;
                      res.statusCode = 404;})
               .finally(function(){
                    res.end();
                });
    });

    this._app.get('\/api_v3\/index.php\/service\/wowza_liveConversionProfile\/action\/serve\/streamName\/*', function (req, res) {

           _this._logger.info("get begin Request for: ", req.url);

           var entryId = req.url.match(/\/serve\/streamName\/(.*)_(\d+)\/f\/transcode.xml/);
           if(entryId){
               var remoteAddress = req.headers['x-forwarded-for'] ?  req.headers['x-forwarded-for'] : req.socket.remoteAddress;
               lse.getLiveStreamEntry({entryId:entryId[1],flavourId:entryId[2],serverNode:remoteAddress},_this._pluginConfig)
                   .then( function(liveEntry){
                       return liveEntry.getTranscodeTemplate(req.url);
                   })
                   .then(function(template){
                       res.setHeader('Content-Type','application/xml');
                       res.send(template);
                       res.end();
                       })
                   .catch(function(err){
                       _this._logger.warn("request for wowza_liveConversionProfile failed " + err);
                       res.statusCode = 404;
                       res.end();
                   });
           }

     });


    _this._app.post('\/[W|w]owzaSE\/*', function (req, res) {

        _this._logger.info("post begin Request for: ", req.url);

        var json="";
        req.on('data',function(data){
            json += data;
        }) ;

        req.on('end',function(){

            var disconnectEntry = function(m,e){
                _this._logger.warn(" disconnecting stream: %s due to error: %s", m.streamName, e);
                var headers = {};
                headers['Accept'] = 'application/json; charset=utf-8';
                var urlDel = 'http://'+ m.hostName + ':8087/v2/servers/_defaultServer_/vhosts/_defaultVHost_/applications/live/instances/_defaultInst_/incomingstreams/'+m.streamName+'/actions/disconnectStream';
                utils.httpPut(urlDel,headers).catch( function(err){
                    _this._logger.warn("failed to delete stream entry %s error %s",m.streamName, err);
                });
            };

            var http_error = function(code,str){
                _this._logger.warn("Request for: %s . error: %d  %s",req.url,code,str);
                res.statusCode = code;
                res.statusMessage = str;
                res.end();
            };

            var handleLiveEntry = function(m){
                // during authentication phase there is no published stream
                var reg = (m.method == ApiMethods.authenticate) ? /.*(\?)p=(.*)&e=(.*)&i=([0|1])&t=(.*)/ : /.*_(\d+)\?p=(.*)&e=(.*)&i=([0|1])&t=(.*)/ ;
                var result = m.streamName.match(reg);
                if(!result){
                    http_error(404,'bad streamName syntax');
                } else {

                    m.flavourId = result[1];
                    m.partnerId = result[2];
                    m.entryId=result[3];
                    m.mediaServerIndex=result[4];
                    m.token = result[5];

                    _this._logger.info("stream: %s. method: %s ",m.streamName,m.method);

                    lse.getLiveStreamEntry(m,_this._pluginConfig)
                         .then( function(liveEntry) {
                            return liveEntry.execute(m);
                            })
                         .catch(function (e) {
                             _this._logger.warn("stream: %s. method: %s reason: %s",m.streamName,m.method,e);
                            //  disconnectEntry(m);
                            //res.send('{error:555}');
                            //res.end();
                           })
                        .finally(function(){
                            res.end();
                            });

                }
            }

            var handleAppEvent = function(m){
                switch(m.method) {
                    case ApiMethods.appinit:
                        lse.reinitLiveStreams();
                        break;
                    case ApiMethods.appstop:
                        lse.reinitLiveStreams();
                        break;
                }
            }


            var m = JSON.parse(json);
            if(m){
                if (!m.streamName || !m.method || !m.hostName ){
                    http_error(404,'empty required field');
                } else {
                   switch(m.method)
                    {
                    case ApiMethods.appinit:
                    case ApiMethods.appstop:
                            handleAppEvent(m);
                            break;
                     case ApiMethods.authenticate:
                     case ApiMethods.publish:
                     case ApiMethods.unpublish:
                            handleLiveEntry(m);
                            break;
                        default:
                            http_error(404,'unknown method ' + m.method);
                            return;
                            break;
                    }
                  }
            } else {
                http_error(404, 'bad json string');
            }
        });

        /*
        url = 'http://cdnapi.kaltura.com/api_v3/index.php/service/wowza_liveConversionProfile/action/serve/partnerId/1883581/streamName/1_n6sqf1kv_1/f/transcode.xml';
        utils.httpGet(url, req.headers).then(function (r) {
            for (var p in r.response.headers) {
                res.setHeader(p, r.response.headers[p]);
            }
            res.send(r.body);
            res.end();
            _this._logger.debug("finished Request");
        });*/
        res.end();
    });

}

KMService.prototype.start=function() {

    var _this = this;

    _this._logger.warn("Start listen on port %d",this._pluginConfig.listenPort);

    _this._app.listen(this._pluginConfig.listenPort);

}


exports.KMService=KMService;
exports.ApiMethods = ApiMethods;


//http://localhost:7774/http://cdnapi.kaltura.com/p/1742181/sp/174218100/playManifest/entryId/1_5zhvairc/flavorIds/1_xxyxt9ix,1_nvofucoj,1_87caz5fe,1_j7p3okcn/format/applehttp/protocol/http/a.m3u8
//http://localhost:7774/http://cfvod.kaltura.com/fhls/p/1742181/sp/174218100/serveFlavor/entryId/1_5zhvairc/v/1/flavorId/1_xxyxt9ix/name/a.mp4/index.m3u8