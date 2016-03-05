var Q = require('q');
var fs = require('fs');
var path = require('path');
var Url = require('url');
var log4js = require('log4js');
var mkdirp = require('mkdirp');

mkdirp.sync("./logs");
log4js.configure('config/logConfig.json', {});


exports.LoggerEx = function(loggerName,id) {

    var logger = log4js.getLogger(loggerName);

    if (id) {
        var loggerEx = {};

        function modify(func) {

            loggerEx[func] = function () {
                if (arguments && arguments.length > 0) {
                    arguments[0] = "[" + id + "] " + arguments[0];
                }
                return logger[func].apply(logger, arguments);
            }
        }


        modify("debug");
        modify("warn");
        modify("info");
        modify("error");

        loggerEx.logger = logger;

        return loggerEx;
    } else {
        return logger;
    }
}





exports.emitLines=function(stream) {
    var backlog = ''
    stream.on('data', function (data) {
        try {
            // console.log("data " + data);
            backlog += new String(data);
            while(true) {
                var n = backlog.indexOf('\n');
                if (n===-1) {
                    n = backlog.indexOf('\r');
                }
                if (n<0) {
                    break;
                }

                var line=backlog.substring(0, n);
                if (line.length>0) {
                    stream.emit('line', line);
                }
                backlog = backlog.substring(n + 1)
                n = backlog.indexOf('\n')
            }
        }catch (e) {
            console.warn(e);
        }
    })
    stream.on('end', function () {
        if (backlog) {
            stream.emit('line', backlog);
        }
    });
}

var repeatPromise=function(logger,fn,intervalRetry,maxRetries) {
    return fn()
        .then(function(res) {
            if (maxRetries <= 0) {
                return Q.resolve(res);
            }
            logger.info("Waiting another ",intervalRetry," ms until next test ",maxRetries, "retries left");
            return Q.delay(intervalRetry) // delay
                // retry with more time
                .then(function(){
                    return repeatPromise(logger,fn,intervalRetry,maxRetries-1);
                });
        });
}

exports.repeatPromise=repeatPromise;