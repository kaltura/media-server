    var logLevel= "DEBUG";
    var logToConsole= true;
    var logFileName= "Test.log";
    var logger = require('./config/logger')(logFileName, logLevel,logToConsole);

module.exports = function(){
    return logger;
};