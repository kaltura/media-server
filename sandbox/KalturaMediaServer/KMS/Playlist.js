/**
 * Created by igors on 1/19/16.
 */
// http://kalsegsec-a.akamaihd.net/dc-1/m/ny-live-publish2/kLive/smil:1_whaa08se_mbr.smil/chunklist_b475136.m3u8

var q = require('q');
var log4js = require('log4js');
var lse = require('./LiveStreamEntry.js');
var http = require('http');
var config = require('config');
var utils = require('../utils/utils.js');
var log4js = require('log4js');

var logger = log4js.getLogger('Playlist');

var StreamingConstants = {
    chunklist:'chunklist',
    playlist:'playlist',
    ext:'.m3u8',
    liveAppName:'live',
    rtmpPort: 1935
};

function PlaylistEntry(opts) {
    var _this = this;
    _this.time = Date.now();
    _this.seqn = -1;
    _this.status = lse.liveEntryStatus.BROADCASTING;
    _this.URI = opts.uri;
    _this.streamId = opts.uri.match(/.*\/([^/]+)/)[1];
    _this.checkChunklist = opts.checkChunklist;
    _this.playlist = '';
    _this.wSession = '';
    _this.format = function (name) {
        return this.URI + name + this.wSession + StreamingConstants.ext;
    };
    return _this;
}

function PlayList(entryId,serverURI,config) {
    var _this = this;

    var fmtServerAndPort = function(server){
        return server.indexOf(':') > 0 ? server : (server + ':' + StreamingConstants.rtmpPort);
    };

    var formatURI = function(var1,var2){
        return 'http://' + fmtServerAndPort(serverURI)  + '/' + StreamingConstants.liveAppName +'/'+var1 + entryId+'_' + var2 + '/';
    }

    //_this._playlist = '';
    _this._streams = [];

    var updatePlaylistByChunkList = function(chunklist,entry){
        logger.debug("updatePlaylistByChunkList. entry %s", entry.streamId);

        var re = /#EXTINF:(.*),/g,
            chunk,
            sum = 0,
            count = 0;

        while( chunk = re.exec(chunklist) ) {
            sum += parseFloat(chunk[1]);
            if (++count >= config.minLiveChunks) {

                  logger.debug("updatePlaylistByChunkList. entry %s num of chunks %d", entry.streamId, count);

                  entry.status = lse.liveEntryStatus.PLAYABLE;
                  if (sum >= config.minLiveDurationSec) {
                      var mediaSeq = chunklist.match(/#EXT-X-MEDIA-SEQUENCE:(\d+)/);
                      if (mediaSeq) {
                          if (entry.seqn == mediaSeq[1]) {
                              if (entry.time + config.minTimeBeforeStoppedSec * 1000 < Date.now()) {
                                  logger.warn("updatePlaylistByChunkList. entry %s. last entry.seqn %d isn't updated for %d => stopped",
                                      entry.streamId, entry.seqn, entry.time + config.minTimeBeforeStoppedSec);
                                  entry.status = lse.liveEntryStatus.STOPPED;
                              }
                          } else {
                              logger.debug("updatePlaylistByChunkList. entry %s. last entry.seqn %d => palyable", entry.streamId, entry.seqn);
                              entry.time = Date.now();
                              entry.seqn = mediaSeq[1];
                              entry.status = lse.liveEntryStatus.PLAYABLE;
                          }
                      }
                  }
                }

        }

    };

    var examinePlaylist = function(entry){

        if(entry.wSession && !entry.checkChunklist){
            return q.resolve('');

        }

        logger.debug("examinePlaylist. entry %s", entry.streamId);

        var matched = entry.playlist.match(/(chunklist(_.*).m3u8)/);

        if(matched && !entry.wSession){
            entry.wSession = matched[2];
        }
        return utils.httpGet(entry.format(StreamingConstants.chunklist))
           .then(function (rr) {
               updatePlaylistByChunkList(rr.body,entry);
               return q.resolve('');
           });
    }

    var addPlaylist = function(uri,checkChunklist){
        logger.debug("addPlaylist. uri %s", uri);

         if (_this._streams.every( function(item){return item.URI != uri}) ) {
             _this._streams.push( new PlaylistEntry({uri:uri,checkChunklist:checkChunklist}) );
          }
        return q.resolve(0);
    }

    //:1935/live/ngrp:1_n6sqf1kv_1_all/playlist.m3u8

    var refreshPlaylist = function(entry){
        var _q = entry.playlist ? examinePlaylist(entry) : utils.httpGet(entry.format(StreamingConstants.playlist))
            .then(function (r) {
                if(!r.body){
                    strErr = r.response.statusCode;
                    if(r.response.statusMessage){
                        strErr += ' : ' + r.response.statusMessage;
                    }
                    throw(strErr);
                } else if(!entry.playlist && r.body.indexOf('#EXT-X-STREAM-INF') > -1) {
                    entry.playlist = r.body;
                }
                return examinePlaylist(entry);
            });
        return _q.catch( function(err){
                return q.resolve(err);
            });

    };


    return {
        addStream:function(streamName){
            return addPlaylist('http://' + fmtServerAndPort(serverURI) + '/'+ StreamingConstants.liveAppName +'/' + streamName + '/',true);
        },
        addInjest:function(flavor){
            return addPlaylist(formatURI('',flavor),true);
        },
        refresh: function () {
            var arr = _this._streams.filter(function(entry){return entry.checkChunklist || !entry.playlist});

            logger.info("refresh %d entries", arr.length);
            return q.all(arr.map(refreshPlaylist));
        },
        getPlaylist:function(){
            var entries = _this._streams.filter(function(entry){ return (entry.status == lse.liveEntryStatus.PLAYABLE); });
            return entries.reduce(function(prev,entry){
                var found;
                if(found = /(\n#EXT-X-STREAM-INF:.*\n+.*)/.exec(entry.playlist)) {
                    //prev += found[1];
                    var lines = found[1].split('\n');
                    lines[2] = entry.URI + lines[2];
                    prev += lines.join('\n');
                }
                return prev;
            },'#EXTM3U\n#EXT-X-VERSION:3');
        },
        getStatus:function(){
            var retval = lse.liveEntryStatus.BROADCASTING;
            for (e in _this._streams) {
                var entry = _this._streams[e];
                if(entry.checkChunklist) {
                    switch (entry.status) {
                        case lse.liveEntryStatus.STOPPED:
                        case lse.liveEntryStatus.BROADCASTING:
                            logger.info("getStatus: " + entry.status);
                            //return entry.status;
                            break;
                        case lse.liveEntryStatus.PLAYABLE:
                            return entry.status;
                            //retval = entry.status;
                            break;
                    }
                }
            }
            logger.info("getStatus: " + retval);
            return retval;
        }
    }
}

exports.PlayList = PlayList;
