var exec = require('cordova/exec');

exports.startFriendlyScoreConnect = function (arg0, success, error) {
    exec(success, error, 'FriendlyScoreConnectPlugin', 'startFriendlyScoreConnect', [arg0]);
};
