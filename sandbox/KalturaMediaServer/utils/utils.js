var Q = require('q');
var http = require('http');
var https = require('https');
var fs = require('fs');
var path = require('path');
var Url = require('url');
var zlib = require('zlib');
//var heapdump = require('heapdump');

var log4js = require('log4js');


process.on('uncaughtException', function(err) {
    // handle the error safely
    console.error("uncaughtException: %s %s ",err.message,err.stack);
})
process.on('unhandledRejection', function(reason, p){
    console.log("Possibly Unhandled Rejection at: Promise  %s, reason: %s callstack: %s ", JSON.stringify(p), reason.message,reason.stack);
    //  heapdump.writeSnapshot('./' + Date.now() + '.heapsnapshot');
    //console.log("dump created!");

});

exports.httpDelete=function(url,headers) {

    var options = Url.parse(url);
    if( headers ) {
        options.headers = headers;
    }

    var def = Q.defer();
    var t1=new Date();
    var delete_func=http.request;
    var put_func=http.request;
    options.method = 'DELETE';

    delete_func(options, function (response) {
        // Continuously update stream with data
        var chunks = [];

        response.on('data', function (chunk) {
            chunks.push(chunk);
        });

        response.on('end', function () {
            var t2=new Date();

            var buffer = Buffer.concat(chunks);
            var encoding = response.headers['content-encoding'];

            if (encoding == 'gzip') {
                zlib.gunzip(buffer, function(err, decoded) {
                    if(!err) {
                        delete response.headers['content-encoding'];
                        def.resolve({ body: decoded.toString(), response: response});
                    }
                });
            } else if (encoding == 'deflate') {
                zlib.inflate(buffer, function(err, decoded) {
                    if(!err) {
                        delete response.headers['content-encoding'];
                        def.resolve({ body: decoded.toString(), response: response});
                    }
                });
            } else {
                def.resolve({ body: buffer.toString(), response: response});
            }
        });
    });

    return def.promise;
};


exports.httpPut=function(url,value,headers) {

    var options = Url.parse(url);
    if( headers ) {
        options.headers = headers;
    }

    var def = Q.defer();
    var t1=new Date();
    var put_func=http.request;
    options.method = 'PUT';

    put_func(options, value,function (response) {
        // Continuously update stream with data
        var chunks = [];

        response.on('data', function (chunk) {
            chunks.push(chunk);
        });

        response.on('end', function () {
            var t2=new Date();

            var buffer = Buffer.concat(chunks);
            var encoding = response.headers['content-encoding'];

            if (encoding == 'gzip') {
                zlib.gunzip(buffer, function(err, decoded) {
                    if(!err) {
                        delete response.headers['content-encoding'];
                        def.resolve({ body: decoded.toString(), response: response});
                    }
                });
            } else if (encoding == 'deflate') {
                zlib.inflate(buffer, function(err, decoded) {
                    if(!err) {
                        delete response.headers['content-encoding'];
                        def.resolve({ body: decoded.toString(), response: response});
                    }
                });
            } else {
                def.resolve({ body: buffer.toString(), response: response});
            }
        });
    });

    return def.promise;
};


exports.httpGet=function(url,headers) {

    var options = Url.parse(url);
    if( headers ) {
        options.headers = headers;
    }
    var def = Q.defer();
    var t1=new Date();
    var get_func=http.get;
    if (url.indexOf("https")==0) {
        get_func=https.get;
    }

    get_func(options, function (response) {
        // Continuously update stream with data
        var chunks = [];

        response.on('data', function (chunk) {
            chunks.push(chunk);
        });

        response.on('end', function () {
            var t2=new Date();

            var buffer = Buffer.concat(chunks);
            var encoding = response.headers['content-encoding'];

            if (encoding == 'gzip') {
                zlib.gunzip(buffer, function(err, decoded) {
                    if(!err) {
                        delete response.headers['content-encoding'];
                        def.resolve({ body: decoded.toString(), response: response});
                    }
                });
            } else if (encoding == 'deflate') {
                zlib.inflate(buffer, function(err, decoded) {
                    if(!err) {
                        delete response.headers['content-encoding'];
                        def.resolve({ body: decoded.toString(), response: response});
                    }
                });
            } else {
                def.resolve({ body: buffer.toString(), response: response});
            }
        });
    });

    return def.promise;
};



exports.mkdir=function(directory) {
    var path = directory.replace(/\/$/, '').split('/');

    for (var i = 1; i <= path.length; i++) {
        var segment = path.slice(0, i).join('/');
        !fs.existsSync(segment) ? fs.mkdirSync(segment) : null ;
    }

}



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

        loggerEx.logger = logger;

        return loggerEx;
    } else {
        return logger;
    }
}




exports.ipAddresstoLong = function toInt(ip){
    var ipl=0;
    ip.split('.').forEach(function( octet ) {
        ipl<<=8;
        ipl+=parseInt(octet);
    });
    return(ipl >>>0);
};

exports.ipAddressfromLong = function fromInt(ipl){
    return ( (ipl>>>24) +'.' +
    (ipl>>16 & 255) +'.' +
    (ipl>>8 & 255) +'.' +
    (ipl & 255) );
};




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
            stream.emit('line', backlog)
        }
    });
}


//TODO
exports.progressedResponse=function(promise,success,fail,progress){
    var timeOut=null;
    var alreadyResponsed=false;


    promise.then(function(arg) {

        if (timeOut) {
            clearTimeout(timeOut);
        }
        if (alreadyResponsed) {
            return;
        }

        alreadyResponsed=true;

        success.call(this,arg);

    }, function(arg) {
        if (timeOut) {
            clearTimeout(timeOut);
        }
        if (alreadyResponsed) {
            return;
        }
        alreadyResponsed=true;
        fail.call(this,arg);
    }, function(arg) {
        if (alreadyResponsed) {
            return;
        }

        timeOut=setTimeout( function() {

            if (alreadyResponsed) {
                return;
            }
            alreadyResponsed=true;
            progress.call(this,arg);

        },5000);
    });
}