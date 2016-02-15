<?php

require_once('../app/clients/php5/KalturaClient.php');

$kaltura_system_ini = "/etc/kaltura.d/system.ini";
$broadcast_file = "../app/configurations/broadcast.ini";

/**
 * @param $ini_content
 * @param $admin_secret
 * @return KalturaClient
 */
function initClient ($ini_content, $admin_secret) {
    $config = new KalturaConfiguration();
    $config->serviceUrl = $ini_content["SERVICE_URL"].'/';
    $client = new KalturaClient($config);
    $result = $client->session->start($admin_secret, null, KalturaSessionType::ADMIN, -5, null, null);
    $client->setKs($result);
    return $client;
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
    (array)$res = $client->serverNode->add($serverNode); // if there's a problem, we'd exit, otherwise it's ok to return true in the end
    $client->serverNode->enable($res->id);
    print '['.$ini_content["RED5_HOST"]."] registered with id: ".$res->id.PHP_EOL;
}

function logToFIle ($message) {
    print $message;
}

/**
 * @return mixed
 */
function getMediaAdminsecretFromXml () {
    $xml_file_path=__DIR__."/../../../usr/local/WowzaStreamingEngine/conf/Server.xml";
    $xml = simplexml_load_file($xml_file_path);
    return $xml->Server->Properties->Property[1]->Value;
}
// main
$system_ini_content = parse_ini_file($kaltura_system_ini);
$xml_admin_secret = getMediaAdminsecretFromXml();
$new_client = initClient($system_ini_content, $xml_admin_secret);
registerMediaServer ($system_ini_content, $new_client);
