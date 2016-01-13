'use strict';
var child_process=require("child_process");
var LoggerEx=require('./../utils.js').LoggerEx;
var Utils=require('./../utils.js');

var config = require('config');
var Q=require('q');



function FFMpegTask(id,params) {

    this._logger = LoggerEx("FFMpegTask", id);
    //this._ffmpeg = __dirname + "/../../bin/ffmpeg";
    this._ffmpeg = __dirname + "/../ffmpeg";
    this._process=null;

    this._lastMessageTime=null;
    this._progressInfo={ frames:0, clock:new Date()};
    this._progressTimeout=10*1000;//10 seconds
    this._ffmpegParams=params;
}



FFMpegTask.prototype.start=function() {


    var def= Q.defer();

    var _this=this;

    _this._logger.debug("Spawnning new ffmpeg process: %s %s",this._ffmpeg, this._ffmpegParams.join(' '));

    this._process= child_process.spawn(this._ffmpeg, this._ffmpegParams);

    Utils.emitLines(this._process.stderr);
    Utils.emitLines(this._process.stdout);

    _this._logger.info("Spawned a new ffmpeg process (pid=%d)",this._process.pid);

    var parseffmpegLine=function(line) {
        if ( _this._progressInfo.frames===0 ) {
            var time=new Date();
            if ( time-_this._progressInfo.clock>1000) {//update every second
                _this._progressInfo.clock = time;
                def.notify("starting");
            }
        }
        var matches=line.match(/frame=(.*)\sfps=(.*)\sq=(.*)time=(.*)\sbitrate=(.*)$/);
        if (matches && matches.length===6) {
            var newProgressInfo = {
                frames: parseInt(matches[1]),
                fps: parseFloat(matches[2]),
                time: matches[4].trim(),
                bitrate: matches[5].trim(),
                clock: new Date()
            };

            if (newProgressInfo.frames>_this._progressInfo.frames) {
                var isFirst=( _this._progressInfo.frames===0);
                _this._progressInfo=newProgressInfo;
                if (isFirst) {
                    def.resolve(true);
                }
            }
        }
    }

    function processLine(line) {

        _this._logger.debug("[FFMPEG] %s",line);
        parseffmpegLine(line);
        _this._lastMessageTime=new Date();
    }

    function processError (err) {
        _this._logger.warn("[FFMPEG] exited with error: ",err);
        _this._process=null;
        var isFirst=( _this._progressInfo.frames===0);
        if (isFirst) {
            def.reject("failed to start ffmpeg");
        }
    }


    this._process.stderr.on('line', processLine);
    this._process.stdout.on('line', processLine);


    this._process.on('exit', processError);
    this._process.on('error', processError);

    this._waitForStart=def.promise;


    setTimeout(function() {

        if ( _this._progressInfo.frames===0) {

            _this.stop();
            def.reject("failed to start ffmpeg (timeout) ");
        }

    },config.entryTest.ffmpegTimeout*1000);

    return this._waitForStart;
}

FFMpegTask.prototype.stop=function() {
    this._logger.info("stopping")

    if (this._process) {
        this._logger.info("stopping process ",this._process.pid);
        this._process.kill();
        this._process = null;
    }

}


FFMpegTask.prototype.isAlive=function() {

    if (!this._process) {
        this._logger.warn("process is not alive anymore!")
        return false;
    }

    var now=new Date();

    if (now-this._lastMessageTime>this._progressTimeout) {
        this._logger.warn("process last message was at %s  not alive!!!",this._lastMessageTime);
        return false;
    }


    if (now-this._progressInfo.clock>this._progressTimeout){
        this._logger.warn("process is not progressing anymore! (last %s)",this._progressInfo.clock);
        return false;
    }

    return true;

}


FFMpegTask.prototype.getStatus=function() {
    var retVal={};

    if (this._process) {
        retVal.pid = this._process.pid;
    }

    retVal.ffmpegProgressInfo=this._progressInfo;

    return retVal;
}


exports.FFMpegTask=FFMpegTask;
