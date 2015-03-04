<?php

//Define consts
define("MAX_EXECUTION_ATTEMPTS", 5);

define("FILE_STATUS_DONE", "done");
define("FILE_STATUS_IN_PROGRESS", "in_progress");
define("FILE_STATUS_ERROR", "error");
define("FILE_STATUS_RETRY", "retry");

//Read config file
$configFilePath = $argv[1];
$config = parse_ini_file($configFilePath);

//Create REquired Directory For Script Execution
if(!createDirIfNotExists($config['dirName'] . "/error/"))
{
	die();
}

if(!createDirIfNotExists($config['dirName'] . "/complete/"))
{
	die();
}

//Load configuration
$files = glob($config['dirName'] . '/*.xml');
if (!count($files))
{
	logMsg("No new files found at this time.");
	die();
}

logMsg(count ($files) . " files found.");

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
$ks = $client->generateSessionV2($config['adminSecret'], '', KalturaSessionType::ADMIN, $clientConfig->partnerId, 86400, "disableentitlement");
$client->setKs($ks);

foreach ($files as $f)
{	
	$fileStatus = FILE_STATUS_IN_PROGRESS;
	while ($fileStatus == FILE_STATUS_IN_PROGRESS)
	{
		$taskXml = new SimpleXMLElement(file_get_contents($f));
		if ($taskXml->getName() == 'upload')
		{
			try {
				$fileStatus = handleUploadXMLResource($taskXml, $client);
			}
			catch(Exception $e) {
				$fileStatus = handleException($e, $f, strval($taskXml->filepath), strval($taskXml->entryId));
			}
		}
		
	}
	
	if($filaeStatus == FILE_STATUS_DONE)
	{
		moveFile($f, $config['dirName'] . "/complete/" . basename($f));
	}
}

function handleUploadXMLResource (SimpleXMLElement $uploadXML, KalturaClient $client)
{
	global $config;
	$entryId = strval($uploadXML->entryId);
	$assetId = strval($uploadXML->assetId);
	$partnerId = strval ($uploadXML->partnerId);
	$partnerAdminSecret = strval ($uploadXML->adminSecret);
	$duration = floatval($uploadXML->duration);
	$isLastChunk = (strval($uploadXML->isLastChunk) == 'true') ? 1 : 0;
	$index = intval($uploadXML->index);
	$filepath = strval($uploadXML->filepath);
	$workmode = strval($uploadXML->workMode);

	if(file_exists($config['dirName'] . "/error/" . $entryId . "/")){
		throw new Exception("append recording: entry [$entryId] asset [$assetId] index [$index] filePath [$filepath] duration [$duration] isLastChunk [$isLastChunk], append will not run, entry is in error list");
	}
	
	logMsg("append recording: entry [$entryId] asset [$assetId] index [$index] filePath [$filepath] duration [$duration] isLastChunk [$isLastChunk]");
		
	$clientConfig = $client->getConfig();
	$clientConfig->partnerId = $partnerId;
	$client->setConfig($clientConfig);
	
	logMsg("Getting live entry [$entryId]");
	$liveEntry = $client->liveStream->get($entryId);
	
	logMsg("Getting content resource for live entry [$entryId]");
	$resource = getContentResource($filepath, $liveEntry, $workmode, $client);
	
	logMsg("Calling append recording for Entry [$entryId]");
	$updatedEntry = $client->liveStream->appendRecording($entryId, $assetId, $index, $resource, $duration, $isLastChunk);
	
	return FILE_STATUS_DONE;
}

/**
 *	Function returns contentResource of the appropriate type to the workMode
 *	@param string $filePath
 *	@param KalturaLiveStreamEntry $liveEntry
 *	@param string $workMode
 * 
 *  @throws Exception if creation of contnet resource failed
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
				throw new Exception("Response Token retuned with error, failed to creating content resource.");
			}
		}
				
		return $resource;
	}
	
	throw new Exception("Error getting content resource");
}

function handleException($e, $xmlFilePath, $filePathOnDisc, $entryId)
{
	logException($e);

	global $config;
	$baseFileName = basename($xmlFilePath,".xml");
	
	switch (get_class($e)) {
		case 'KalturaClientException':
			$fileParts = explode(".", $baseFileName);
			$fileName = $fileParts[0];
			$fileExecutionAttempts = $fileParts[1];
			
			if($fileExecutionAttempts == null)
				$fileExecutionAttempts = 0;
			else
				$fileExecutionAttempts++;

			if($fileExecutionAttempts == MAX_EXECUTION_ATTEMPTS)
			{
				moveFileToErrorDir($entryId, $xmlFilePath, $baseFileName . ".xml", $filePathOnDisc);
				return FILE_STATUS_ERROR;
			}

			moveFile($xmlFilePath, $config['dirName'] . $fileName . "." . $fileExecutionAttempts . ".xml");
			return FILE_STATUS_RETRY;
			break;

		default:
			moveFileToErrorDir($entryId, $xmlFilePath, $baseFileName . ".xml", $filePathOnDisc);
			return FILE_STATUS_ERROR;
			break;
	}
}

function moveFileToErrorDir($entryId, $xmlOldFilePath, $xmlNewFileName, $mediaFilePath)
{
	logMsg("Moving File To Error Dir [$entryId] [$xmlOldFilePath] [$mediaFilePath]");

	$entryErrorDir = createErrorDirForEntry($entryId);

	if(!$entryErrorDir){
		throw new Exception("Failed to create entry error dir for entry [$entryId]");
	}
	
	moveFile($xmlOldFilePath, $entryErrorDir . $xmlNewFileName);
	moveFile($mediaFilePath, $entryErrorDir . basename($mediaFilePath));
}

function createErrorDirForEntry($entryId)
{
	global $config;
	$entryErrorDir = $config['dirName'] . "/error/" . $entryId . "/";
	
	logMsg("Creating error dir for entry [$entryId]");
	
	if(!createDirIfNotExists($entryErrorDir))
		return null;

	return $entryErrorDir;
}

function createDirIfNotExists($dirName)
{
	$res = true;

	if(file_exists($dirName)){
		return $res;
	}

	if (!file_exists($dirName)) 
		$res = mkdir($dirName, 0644, true);

	if(!$res) 
	{
    	$error = error_get_last();
    	logMsg("Failed to create error dir with error " . implode(" ", $error));
    }

	return $res;
}

function moveFile($currFilePath, $newFilePath)
{
	logMsg("Moving file from [$currFilePath] to [$newFilePath]");
	rename($currFilePath, $newFilePath);
}

function logException($e, $messgae = "")
{
	logMsg("Exception of class " . get_class($e) . ": [{$e->getMessage()}]. " . $messgae);
}

function logMsg($str)
{
	echo(date("Y-m-d H:i:s") . ": " . $str . PHP_EOL);
}
