/**
 * Created by elad.benedict on 10/8/2015.
 */

module.exports = function decorate(logger, decorator) {

    var decorateLevel = function (loggerInstance, loggingFuncName) {
        var func = loggerInstance[loggingFuncName].bind(loggerInstance);
        loggerInstance[loggingFuncName] = function (msg, err) {
            if (err) {
                func(decorator(msg), err);
            } else {
                func(decorator(msg));
            }
        };
    };

    var l = Object.create(logger);

    decorateLevel(l, 'trace');
    decorateLevel(l, 'debug');
    decorateLevel(l, 'info');
    decorateLevel(l, 'warn');
    decorateLevel(l, 'error');
    decorateLevel(l, 'fatal');
    return l;
};
