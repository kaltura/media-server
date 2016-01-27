/**
 * Created by igors on 8/30/15.
 */


var njsCrypto = require('crypto'),
//    url = requires('url'),
    Buf = require('buffer');


var Constants = { FIELD_EXPIRY : '_e',
    FIELD_TYPE : '_t',
    FIELD_USER : '_u',
    RANDOM_SIZE : 16,
    ALGO :'aes-128-cbc' };

var generateKalturaSessionV2 = function ($adminSecretForSigning,$type,$partnerId,options,callback) {

       njsCrypto.randomBytes(Constants.RANDOM_SIZE, function (ex, $rand) {
            if (ex) throw ex;
            //console.log('Have %d bytes of random data: %s', $rand.length, $rand);

            // build fields array
            $fields = [];
            $fields[Constants.FIELD_EXPIRY] = options.expiry;
            $fields[Constants.FIELD_TYPE] = $type;
            $fields[Constants.FIELD_USER] = options.userId;

           if(options.privileges) {
               var tokens = options.privileges.split(',');
               if (tokens.length > 1) {
                   for (var $privilege in tokens) {
                       $privilege = $privilege.trim();
                       if (!$privilege)
                           continue;
                       if ($privilege == '*')
                           $privilege = 'all:*';
                       var $splittedPrivilege = $privilege.split(':');
                       if ($splittedPrivilege.length > 2)
                           $fields[$splittedPrivilege[1]] = $splittedPrivilege[1];
                       else
                           $fields[$splittedPrivilege[1]] = '';
                   }
               }
           }
            // build fields string
            //TODO use url format
            var $fieldsStr = "";
            var firstItem = false;
            for (var j in $fields) {
                if (!firstItem) {
                    firstItem = true;
                } else {
                    $fieldsStr += '&';
                }
                $fieldsStr += j + '=' + $fields[j];
            }
            var fieldsBuf = new Buffer($fieldsStr, 'ascii');
           // console.log("fieldsBuf=%s",fieldsBuf.toString('base64'));

            var rnd = new Buffer($rand); /*new Buffer(Constants.RANDOM_SIZE);//new Buffer($rand);
             for(var i =0 ;i < Constants.RANDOM_SIZE; i++){
             rnd[i] = i % 10;
             }
             */
            fieldsBuf = Buffer.concat([rnd,fieldsBuf]);
           // console.log("rand=%s\nconcat=%s",rnd.toString('base64'),fieldsBuf.toString('base64'));

            var sha1 = njsCrypto.createHash('sha1');
            sha1.update(fieldsBuf);
            var sha1Buf = new Buffer(sha1.digest());

            //console.log("sha1=%s",sha1Buf.toString('base64'));
            var message = Buffer.concat([sha1Buf, fieldsBuf]);

            var header = 'v2|'+ $partnerId +'|';

          //  console.log("message=%s", message.toString('base64'));

            var keyBuf = new Buffer($adminSecretForSigning, 'ascii');
            var sha1Key = njsCrypto.createHash('sha1');
            sha1Key.update(keyBuf);
            var key = new Buffer(sha1Key.digest());

            key = key.slice(0, Constants.RANDOM_SIZE);
            var iv = new Buffer(Constants.RANDOM_SIZE,'binary');
            iv.fill(0);

            var cipher = njsCrypto.createCipheriv("aes-128-cbc", key, iv);

            cipher.setAutoPadding(false);

            if(message.length % 16) {
                var padding = new Buffer(16 - message.length % 16, 'binary');
                padding.fill(0);
                message = Buffer.concat( [message, padding]);
            }

            var plaintext = message.toString('base64');//"This is my super secret password";

            var ciphertext = cipher.update(plaintext,'base64');
            // ciphertext += cipher.final();

          //  console.log("Cipher text is:");
          //  console.log("<%s>",new Buffer(ciphertext,'utf8').toString('base64'));
/*
            var decipher = function(ciphertext) {

                var decipher = njsCrypto.createDecipheriv("aes-128-cbc", key, iv);
                decipher.setAutoPadding(false);
                var deciphertext = decipher.update(ciphertext);

                console.log("");
                console.log("Deciphered text is:");
                console.log(deciphertext.toString());
                return deciphertext;
            };
*/
            // glue together header and encrypted string
            // adapt for transfet inside http request
            var $decodedKs = Buffer.concat([new Buffer(header), new Buffer(ciphertext)])
                .toString('base64')
                .split('+').join('-')
                .split('/').join('_');


            $decodedKs = $decodedKs.split('+').join('-').split('/').join('_');

            callback($decodedKs);
        });
   };

if(process.argv.length > 2) {

    if (process.argv.length < 5) {
        throw {message: "wrong number of arguments"};
    }

    var adminSecret = process.argv[2],
        type = process.argv[3],
        partnerId = process.argv[4],
        expiry = process.argv[5],
        userId = process.argv.length > 6 ? process.argv[6] : "",
        privileges = process.argv.length > 7 ? process.argv[7] : "";

    var c = generateKalturaSessionV2(adminSecret, type, partnerId,
        {expiry: expiry, userId: userId, privileges: privileges},
        function (encryptedMessage) {
            process.stdout.write(encryptedMessage);
        });
} else {
    exports.generateKalturaSessionV2=generateKalturaSessionV2;
}








