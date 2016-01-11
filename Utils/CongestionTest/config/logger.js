/**
 * Created by AsherS on 8/24/15.
 */


var path = require('path');
var mkdirp = require('mkdirp');


var logger = function (file, level, logToConsole) {

    var logFullPath = path.resolve(file);
    mkdirp.sync(path.dirname(logFullPath));

    var log4js = require( "log4js" );
    var appenders = [
        {
            "category": "Tester",
            "type": "file",
            "filename": logFullPath
        }
    ];

    if (logToConsole)
    {
        appenders.push({
            "type": "console",
            "layout": {
                "type": "pattern",
                "pattern": "%m"
            }
        });
    }

    var log4jsConfiguration = {
        "appenders": appenders,
        "replaceConsole": false,
        "levels" : {
            "Tester": level
        }
    };

    log4js.configure(log4jsConfiguration);

    // Support log rotate - this is the signal that is used
    process.on('SIGUSR1', function() {
        log4js.clearAppenders();
        log4js.configure(log4jsConfiguration);
    });

    var res = log4js.getLogger("Tester");
    return res;
};



var loggerDecorator = require('./log-decorator');

module.exports = function(file, level, logToConsole){
    return logger(file, level, logToConsole);
};