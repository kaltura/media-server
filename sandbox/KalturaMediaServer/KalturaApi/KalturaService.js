var $q              = require('q'),
    fs              = require('fs'),
    ini             = require('ini'),
    config          = require('config'),
    log4js          = require('log4js'),
    request         = require('request'),
    genKSV2         = require('../utils/generateKalturaSession.js');

function KalturaService() {
    this._clientConfig              = loadClientConfig();
    this._loginPromise              = null;
    this._id                        = 0;
    this._multiRequestPromises      = [];
    this._multiRequestParams        = null;
    this._logger                    = log4js.getLogger('KalturaService');
}

KalturaService.prototype.call=function(params) {
    var _this = this;

    if (this._multiRequestParams) {
        return _this._kcall(params);
    } else {
        return this.login().then(function () {
            return _this._kcall(params);
        });
    }
}

KalturaService.prototype.callAnonymous=function(params) {
    var _this = this;
    delete _this._ks;
    return _this._kcall(params,undefined);
}


KalturaService.prototype.login = function () {
    var _this = this;
    if (this._loginPromise) {
        return this._loginPromise;
    }

    this._loginPromise = this.genKs()
        .then(function (result) {
            _this._ks = result;
            return $q.resolve(result);
        },
        function (res) {
            return $q.reject(res);
        });

    return this._loginPromise;
}

KalturaService.prototype.genKs=function () {

    var _this = this;

    return $q.Promise( function(resolve, reject) {
        genKSV2.generateKalturaSessionV2(_this._clientConfig.adminSecret, '2', _this._clientConfig.partnerId, {expiry: Date.now() + 60}, function (KalturaSessionKey) {
            if (!KalturaSessionKey) {
                reject("generateKalturaSessionV2 returned empty KS string");
            }

            resolve(KalturaSessionKey);
        });
    });
};

KalturaService.prototype.getConfigValue=function(key){
    return  this._clientConfig[key];
}

KalturaService.prototype._kcall=function(params,ingoreMR) {

    var _this=this;
    //we chain the requests in a multi-request calls
    if (this._multiRequestParams && !ingoreMR) {

        var  multiRequestCount=this._multiRequestPromises.length+1;
        for(var propertyName in params) {
            //clone the property
            Object.defineProperty(this._multiRequestParams, multiRequestCount+":"+propertyName,
                Object.getOwnPropertyDescriptor(params, propertyName));
        }
        return $q.Promise(function (success, failure) {
            _this._multiRequestPromises.push({ success: success, failure:failure});
        });
    }

    params.format = 1; //return JSON
    if (this._ks)
      params.ks = this._ks;

    this._id++;

    return $q.Promise( function(resolve,reject) {
        request.post({
            url: _this._clientConfig.serviceUrl + '/api_v3/index.php',
            json: true,
            body: params
        }, function (error, response, result) {
            if (result && result.objectType==="KalturaAPIException") {
                return reject( result );
            }
            if(error) {
                return reject(error);
            }
            return resolve(result);
        });
    });
}

KalturaService.prototype.startMultirequest=function () {
    this._multiRequestPromises=[];
    this._multiRequestParams = { service: "multirequest", action: null };
}

KalturaService.prototype.execMultirequest=function() {

    var _this=this;

    var  doCall=function() {

        var params=_this._multiRequestParams;
        var oldMultiRequestPromises=_this._multiRequestPromises;
        _this._multiRequestParams=null;
        _this._multiRequestPromises=[];

        return _this._kcall(params).then(function (result) {

            for (var i = 0; i < result.length; i++) {

                if (result[i] && result[i].code) {
                    return $q.reject(result);
                }

                oldMultiRequestPromises[i].success(result[i]);
            }
            return $q.resolve(result);
        });
    }

    return doCall();
}

var loadClientConfig=function(){
    var kesIniPath = '/opt/kaltura/app/configurations/ecdn.ini';

    var data = fs.readFileSync(kesIniPath).toString();

    var ecdnConfig = ini.parse(data);
    if (!ecdnConfig) {
        return null;
    }

    var partnerId = ecdnConfig.PARTNER_ID;
    var adminSecret = ecdnConfig.ADMIN_SECRET;
    var serviceUrl = ecdnConfig.apphome_url;

    if (!partnerId || !adminSecret || !serviceUrl) {
        return null;
    }

    return {partnerId: partnerId, adminSecret: adminSecret, serviceUrl: serviceUrl};
}

exports.KalturaService= new KalturaService();