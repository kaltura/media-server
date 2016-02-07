<?php

require_once('../app/infra/general/BaseEnum.php');
require_once('../app/alpha/lib/enums/serverNodeType.php');
require_once('../app/alpha/lib/IKalturaPluginEnum.php');
require_once('../app/plugins/media/wowza/lib/WowzaMediaServerNodeType.php');
require_once('../app/clients/php5/KalturaClient.php');

define ("ERROR",1);
define ("OK", 0);

$kaltura_system_ini = "/etc/kaltura.d/system.ini";
$broadcast_file = "../app/configurations/broadcast.ini";
$admin_secret_query = 'SELECT admin_secret FROM kaltura.partner WHERE id= -5';

/**
 * @param $file_name
 * @return array
 */
function readIniFile ($file_name) {
    if (! is_readable($file_name)) {
        print "The file [$file_name] could not be read. Existing".PHP_EOL;
        exit ("ERROR");
    }
    return (parse_ini_file($file_name, true));
}

/**
 * @param $ini_content
 */
function checkForNeededVals($ini_content) {
    $needd_vals = array("DB1_HOST", "DB1_NAME", "SUPER_USER", "SUPER_USER_PASSWD");
    foreach ($needd_vals as $value) {
        if ( empty($ini_content[$value] ) ) {
            print "The value for $value is empty or the value does not exist in the system.ini file. Please chack and re-run.".PHP_EOL;
            exit ("ERROR");
        }
    }
}

/**
 * @param $ini_content
 * @param $needed_query
 * @return array
 */
function getMediaAdminSecret($ini_content, $needed_query)
{
    $link = new mysqli($ini_content["DB1_HOST"], $ini_content["DB1_USER"], $ini_content["DB1_PASS"],$ini_content["DB1_NAME"],  $ini_content["DB1_PORT"]);
    if (!empty ($link->connect_error)) {
        print "[".$link->connect_error."]".PHP_EOL;
        exit ("ERROR");
    }
    $query_result = mysqli_query($link, $needed_query);// or die('error getting data');
    $admin_secret = $query_result->fetch_assoc();
    $admin_secret = $admin_secret["admin_secret"];
    return $admin_secret;
}

/**
 * @param $ini_content
 * @param $admin_secret
 * @return KalturaClient
 */
function initClient ($ini_content, $admin_secret) {
    $config = new KalturaConfiguration();
    $config->serviceUrl = $ini_content["SERVICE_URL"].'/';
    $client = new KalturaClient($config);
    $userId = null;
    $type = KalturaSessionType::ADMIN;
    $partnerId = -5;
    $expiry = null;
    $privileges = null;
    $result = $client->session->start($admin_secret, $userId, $type, $partnerId, $expiry, $privileges);
    if (!$result) {
        print "Error creating session.";
        exit ("ERROR");
    }
    return $client;
}


/**
 * @param $client
 * @param $admin_secret
 * @return mixed
 */
function getKsFromClient ($client, $admin_secret) {
    $result = $client->session->start($admin_secret, null, KalturaSessionType::ADMIN, -5, null, null);
    $client->setKs($result);
    if (!$result) {
        print "Error creating session.";
        exit ("ERROR");
    }
    return $result;
}


/**
 * @param $ini_content
 * @param $client
 */
function registerMediaServer ($ini_content, $client) {
    $media_server_tag = "_media_server";
    $media_server_default_port = 1935;
    $media_server_default_protocol = "http";

    $config = new KalturaConfiguration();
    $config->serviceUrl = $ini_content["SERVICE_URL"].'/';
    $serverNode = new KalturaWowzaMediaServerNode();
    $serverNode->name = $ini_content["RED5_HOST"].$media_server_tag;
    $serverNode->systemName = '';
    $serverNode->description = '';
    $serverNode->hostName = $ini_content["RED5_HOST"];
    $serverNode->liveServicePort = $media_server_default_port;
    $serverNode->liveServiceProtocol = $media_server_default_protocol;
    try {
        (array)$res = $client->serverNode->add($serverNode); // if there's a problem, we'd exit, otherwise it's ok to return true in the end
    } catch (KalturaException $ex) {
        print "The server already exists / registered: ".$ex->getMessage().PHP_EOL;
        exit ("ERROR");
    }
    $client->serverNode->enable($res->id);
    print '['.$ini_content["RED5_HOST"]."] registered with id: ".$res->id.PHP_EOL;
}

// main
$system_ini_content = readIniFile($kaltura_system_ini);
checkForNeededVals($system_ini_content);
$admin_secret = getMediaAdminSecret($system_ini_content, $admin_secret_query);
$new_client = initClient($system_ini_content, $admin_secret);
$ks =  getKsFromClient($new_client,$admin_secret);
registerMediaServer ($system_ini_content, $new_client);
exit ("OK");
