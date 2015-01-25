<?php

$configFilePath = $argv[1];
$config = parse_ini_file($configFilePath);

//Load configuration
$files = glob($config['dirName'] . '/*.xml');
if (!count($files))
{
	logMsg("No files at this time.");
	die();
}

logMsg(count ($files) . " files found.");

$inProgress = array();
if (file_exists('in_progress'))
{
	$content = file_get_contents('in_progress');
	$inProgress = explode('\n', $content);
}

$complete = array();
if (file_exists('complete'))
{
	$content = file_get_contents('complete');
	$complete = explode('\n', $content);
}

//Sort files - ascending creation date
foreach ($files as $f){
  $tmp[realpath($f)] = filectime($f);
}
asort($tmp);
$files = array_keys($tmp);
require_once 'client/KalturaClient.php';

$clientConfig = new KalturaConfiguration();
$clientConfig->serviceUrl = $config['serviceUrl'];
$clientConfig->partnerId = $config['partnerId'];
$client = new KalturaClient($clientConfig);
$ks = $client->generateSessionV2($config['adminSecret'], '', KalturaSessionType::ADMIN, $clientConfig->partnerId, 86400, null);
$client->setKs($ks);

foreach ($files as $f)
{
	if (in_array($f, $inProgress) || in_array($f, $complete))
	{
		continue;
	}
	
	$inProgress[] = $f;
	file_put_contents('in_progress', implode('\n', $inProgress));
	
	$handled = false;
	while (!$handled)
	{
		$taskXml = new SimpleXMLElement(file_get_contents($f));
		if ($taskXml->getName() == 'upload')
		{
			$handled = handleUploadXMLResource($taskXml, $client);	
		}
		
	}
	$complete[] = $f;
	file_put_contents('complete', implode('\n', $complete));
}

function handleUploadXMLResource (SimpleXMLElement $uploadXML, KalturaClient $client)
{
	$entryId = strval($uploadXML->entryId);
	$assetId = strval($uploadXML->assetId);
	$partnerId = strval ($uploadXML->partnerId);
	$partnerAdminSecret = strval ($uploadXML->adminSecret);
	$duration = floatval($uploadXML->duration);
	$isLastChunk = (strval($uploadXML->isLastChunk) == 'true') ? 1 : 0;
	$index = intval($uploadXML->index);
	$filepath = strval($uploadXML->filepath);
	$workmode = strval($uploadXML->workMode);
	
	logMsg("append recording: entry [$entryId] asset [$assetId] index [$index] filePath [$filepath] duration [$duration] isLastChunk [$isLastChunk]");
		
	$clientConfig = $client->getConfig();
	$clientConfig->partnerId = $partnerId;
	$client->setConfig($clientConfig);
	
	try 
	{
		$liveEntry = $client->liveStream->get($entryId);
	}
	catch (Exception $e)
	{
		logMsg("An error occured retrieving entry with id [$entryId]. Error message [" . $e->getMessage() . "]");
		return false;
	}
	
	$resource = getContentResource($filepath, $liveEntry, $workmode, $client);
	if (!$resource)
		return false;
	
	try {
		$updatedEntry = $client->liveStream->appendRecording($entryId, $assetId, $index, $resource, $duration, $isLastChunk);
	}
	catch (Exception $e)
	{
		logMsg("Append live recording error: [" . $e->getMessage() . "]");
		return false;
	}
	
	return true;
}

/**
 *	Function returns contentResource of the appropriate type to the workMode
 *	@param string $filePath
 *	@param KalturaLiveStreamEntry $liveEntry
 *	@param string $workMode
 *
 *	@return KalturaDataCenterContentResource
 */
function getContentResource ($filepath, KalturaLiveStreamEntry $liveEntry, $workMode, KalturaClient $client) 
{
	if ($workMode == 'kaltura') {
		$resource = new KalturaServerFileResource();
		$resource->localFilePath = $filepath;
		return $resource;
	}
	else {
		try {
			$client->startMultiRequest();
			$client->uploadToken->add(new KalturaUploadToken());
			
			$client->uploadToken->upload("{1:result:id}", realpath($filepath));
			$responses = $client->doMultiRequest();
			
			$resource = new KalturaUploadedFileTokenResource();
			$tokenResponse = $responses[1];
			if ($tokenResponse instanceof KalturaUploadToken)
				$resource->token = $tokenResponse->id;
			else {
				if ($tokenResponse instanceof Exception) {
			}
				logMsg("Content resource creation error: [" . $tokenResponse->getMessage() . "]");
				return null;
			}
				
			return $resource;
			
		} catch (Exception $e) {
			logMsg("Content resource creation error: [" . $e->getMessage() . "]");
		}
	}
	
	return null;
}

function logMsg($str)
{
	echo($str . PHP_EOL);
}