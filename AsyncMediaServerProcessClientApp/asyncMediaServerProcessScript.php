<?php

//Define consts
define("MAX_RETRY_ATTEMPTS", 3);
define("MAX_EXECUTION_ATTEMPTS", 5);
define("RETRY_SLEEP_INTERVAL", 2);

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
$ks = $client->generateSessionV2($config['adminSecret'], '', KalturaSessionType::ADMIN, $clientConfig->partnerId, 86400, null);
$client->setKs($ks);

foreach ($files as $f)
{	
	$fileStatus = FILE_STATUS_IN_PROGRESS;
	$retryAttempts = 0;
	while ($fileStatus == FILE_STATUS_IN_PROGRESS)
	{
		$taskXml = new SimpleXMLElement(file_get_contents($f));
		if ($taskXml->getName() == 'upload')
		{
			try {
				$fileStatus = handleUploadXMLResource($taskXml, $client);
			}
			catch(Exception $e) {
				$fileStatus = handleException($f, strval($taskXml->filepath), $e, $retryAttempts);
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

	if(file_exists($config['dirName'] . "/error/" . $entryId . "/"))
	{
		$e = new Exception("append recording: entry [$entryId] asset [$assetId] index [$index] filePath [$filepath] duration [$duration] isLastChunk [$isLastChunk], append will not run, entry is in error list");
		logException($e);
	}
	
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
		logException($e, "error occured while retrieving entry with id [$entryId].");
	}
	
	$resource = getContentResource($filepath, $liveEntry, $workmode, $client);
	
	try {
		$updatedEntry = $client->liveStream->appendRecording($entryId, $assetId, $index, $resource, $duration, $isLastChunk);
	}
	catch (Exception $e)
	{
		logException($e, "Append live recording error.");
	}
	
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
					logException($tokenResponse, "Response Token retuned with error, failed to creating content resource.");
				}
			}
				
			return $resource;
			
		} catch (Exception $e) {
			logException($e, "Content resource creation error.");
		}
	}
	
	$e = new Exception("Error getting content resource");
	logException($e);
	
}

function handleException($xmlFilePath, $filePathOnDisc, $e, &$retryAttempts)
{
	$retryAttempts++;
	$baseFileName = basename($xmlFilePath,".xml");
	$filePathArray = explode("_", $baseFileName);
	$fileExecutionAttempts = end($filePathArray);
	$entryId = $filePathArray[0] . "_" . $filePathArray[1];
	global $config;
	
	switch (get_class($e)) {
		case 'KalturaClientException':
			if($retryAttempts == MAX_RETRY_ATTEMPTS)
			{
				if($fileExecutionAttempts == MAX_EXECUTION_ATTEMPTS)
				{
					moveFileToErrorDir($entryId, $xmlFilePath, $baseFileName . ".xml", $filePathOnDisc, basename($filePathOnDisc));
					return FILE_STATUS_ERROR;
				}
				else if($fileExecutionAttempts > MAX_EXECUTION_ATTEMPTS)
				{
					moveFile($xmlFilePath, $config['dirName'] . "/" . $baseFileName . "_0.xml");
					return FILE_STATUS_RETRY;
				}
				else
				{
					$filePathArray[count($filePathArray)-1] = $fileExecutionAttempts+1;
					moveFile($xmlFilePath, $config['dirName'] . implode("_", $filePathArray) . ".xml");
					return FILE_STATUS_RETRY;
				}
			}
			else
			{
				sleep(RETRY_SLEEP_INTERVAL);
				return FILE_STATUS_IN_PROGRESS;
			}
			break;

		default:
			logMsg("Inside Default exception with entry $entryId");
			moveFileToErrorDir($entryId, $xmlFilePath, $baseFileName . ".xml", $filePathOnDisc, basename($filePathOnDisc));
			return FILE_STATUS_ERROR;
			break;
	}
}

function moveFileToErrorDir($entryId, $xmlOldFilePath, $xmlFileName, $mediaOldFilePath = null, $mediaFileName = null)
{
	logMsg("Moving File To Error Dir [$entryId] [$xmlOldFilePath] [$xmlFileName] [$mediaOldFilePath] [$mediaFileName]");

	$entryErrorDir = createErrorDirForEntry($entryId);
	
	if(!$entryErrorDir)
	{
		logMsg("Error dir could not be create, entry [$entryId] files will not be moved to error");
		return null;
	}
	
	moveFile($xmlOldFilePath, $entryErrorDir . $xmlFileName);
	if($mediaOldFilePath && $mediaFileName)
		moveFile($mediaOldFilePath, $entryErrorDir . $mediaFileName);
}

function createErrorDirForEntry($entryId)
{
	global $config;

	$entryErrorDir = $config['dirName'] . "/error/" . $entryId . "/";
	if(!createDirIfNotExists($entryErrorDir))
		return null;

	return $entryErrorDir;
}

function createDirIfNotExists($dirName)
{
	$res = true;

	if(file_exists($dirName))
		return $res;

	if (!file_exists($dirName)) 
		$res = mkdir($dirName, 0644, true);

	if(!$res) 
	{
    	$error = error_get_last();
    	logMsg("Failed to create error dir with error " . implode(" __ ", $error));
    }

	return $res;
}

function moveFile($currFilePath, $newFilePath)
{
	rename($currFilePath, $newFilePath);
}

function logException($e, $messgae = "")
{
	logMsg("Exception of class " . get_class($e) . ": [{$e->getMessage()}]. " . $messgae);
	throw $e;
}

function logMsg($str)
{
	echo(date("Y-m-d H:i:s") . ": " . $str . PHP_EOL);
}
