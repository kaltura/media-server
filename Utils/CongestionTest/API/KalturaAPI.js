var $q = require('q');
var request = require('request');
var config = require('config');
var url= require('url');

function KalturaAPI() {


    this._connectionInfo=config.KalturaService;
    this._loginPromise = null;


    this._multiRequestPromises=[];

    this._multiRequestParams=null;

}





KalturaAPI.prototype.call=function(params) {
    var _this=this;
    if (this._multiRequestParams || this._ks) {
        return _this._kcall(params);
    } else {
        return this.login().then(function () {
            return _this._kcall(params);
        });
    }
}


KalturaAPI.prototype.login = function () {

    var _this=this;


    if (this._loginPromise) {
        return this._loginPromise;
    }

    if (!this._ks) {

        this._loginPromise  = this._kcall({
            service: "session",
            action: "start",
            type: 2,
            userId: this._connectionInfo.userId,
            secret: this._connectionInfo.adminSecret,
            partnerId: this._connectionInfo.partnerId
        },true).then(function (result) {
                _this._ks = result;
                console.info("loggedin with user '" + _this._connectionInfo.userId + "' in with ks=" + result);
                return $q.resolve(result);
            },
            function (res) {
                return $q.reject(res);
            });
    }
    else {
        this._loginPromise = $q.resolve(this.ks);
    }

    return this._loginPromise;
}


KalturaAPI.prototype._kcall=function(params,ingoreMR) {

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


    var startTime=new Date();

    params.format = 1; //return JSON
    if (this._ks)
        params.ks = this._ks;




    return $q.Promise( function(resolve,reject) {

        request.post({
            url: _this._connectionInfo.serverAddress + '/api_v3/index.php',
            json: true,
            body: params
        }, function (error, response, result) {



            if (result && result.objectType==="KalturaAPIException") {
                return reject( result );
            }
            return resolve( result );
        });


    });


}

KalturaAPI.prototype.startMultirequest=function () {
    this._multiRequestPromises=[];
    this._multiRequestParams = { service: "multirequest", action: null };
}

KalturaAPI.prototype.execMultirequest=function() {

    var _this=this;

    var  doCall=function() {

        var params=_this._multiRequestParams;
        var oldMultiRequestPromises=_this._multiRequestPromises;
        _this._multiRequestParams=null;
        _this._multiRequestPromises=[];

        return _this._kcall(params).then(function (result) {

            for (var i = 0; i < result.length; i++) {

                if (result[i] && result[i].code) {
                    //klog.warn("Error from multirequest #{0} (params={1}) message={2}",i,JSON.stringify(params), JSON.stringify(result[i]));
                    return $q.reject(result);
                }

                oldMultiRequestPromises[i].success(result[i]);
            }
            return $q.resolve(result);
        });
    }


    if (_this._ks) {
        return doCall();
    } else {
        return this.login().then(function () {
            return doCall();
        });
    }

}

var kalturaAPI=new KalturaAPI();
module.exports.KalturaAPI=kalturaAPI;

