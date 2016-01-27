/**
 * Created by igors on 1/14/16.
 */
var log4js          = require('log4js'),
    pack            = require('./package.json'),
    fs              = require('fs'),
    config          = require('config'),
    kms             = require('./KMS/KMService.js'),
 LoggerEx           = require('./Utils/utils.js').LoggerEx("Manager");
;

fs.mkdir("./logs",function(e) {
});

log4js.configure('./config/logConfig.json', { reloadSecs: 1000, cwd: './logs'  });

LoggerEx.debug("Starting ", pack.name, " version ", pack.version);

var instance = new kms.KMService();
var serviceConfig = config.get('KMService');
instance.init(config, serviceConfig);
instance.start();
