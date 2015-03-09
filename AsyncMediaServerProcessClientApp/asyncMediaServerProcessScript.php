<?php

//Define consts
define("DEFAULT_MAX_RETRY_ATTEMPTS", 5);

//Validate input
if(count($argv) < 2){
	logMsg("Mandatory config_file_path not provided. Usage: php asyncMediaServerProcessScript.php [config_file_path] {max_retry_attempts}");
	die();
}

//Read config file
$configFilePath = $argv[1];
$config = parse_ini_file($configFilePath);

//Set max retry attempts
$maxRetryAttempts = DEFAULT_MAX_RETRY_ATTEMPTS;
if( isset($argv[2]) && is_numeric($argv[2]) ){
	$maxRetryAttempts = $argv[2];
}

//Configure required base dir's
$baseErrorDir = $config['dirName'] . "/error/";
$baseCompleteDir = $config['dirName'] . "/complete/";

//Create Required Directories For Script Execution
if(!file_exists($baseErrorDir) && !createDirIfNotExists($baseErrorDir)) {
	logMsg("Failed to create error dir");
	die();
}

if(!file_exists($baseCompleteDir) && !createDirIfNotExists($baseCompleteDir)) {
	logMsg("Failed to create complete dir");
	die();
}

//Load configuration
$files = glob($config['dirName'] . '/*.xml');
if (!count($files)) {
	logMsg("No new files found at this time.");
	die();
}
logMsg(count ($files) . " files found.");

//Sort files - ascending creation date
foreach ($files as $f) {
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
	$taskXml = new SimpleXMLElement(file_get_contents($f));
	if ($taskXml->getName() == 'upload')
	{
		try {
			handleUploadXMLResource($client, $taskXml, $baseErrorDir);
			moveFile($f, $baseCompleteDir . basename($f));
		}
		catch(KalturaClientException $e) {
			logException($e);
			$baseFileName = basename($f,".xml");
			$fileParts = explode(".", $baseFileName);
			$fileName = $fileParts[0];
			$fileExecutionAttempts = $fileParts[1];
	
			if($fileExecutionAttempts == null)
				$fileExecutionAttempts = 0;
			else
				$fileExecutionAttempts++;

			if($fileExecutionAttempts == $maxRetryAttempts) {
				moveFileToErrorDir(strval($taskXml->entryId), $baseErrorDir, $f, $baseFileName . ".xml", strval($taskXml->filepath));
			}

			moveFile($f, $config['dirName'] . $fileName . "." . $fileExecutionAttempts . ".xml");
		}
		catch(Exception $e) {
			logException($e);
			moveFileToErrorDir(strval($taskXml->entryId), $baseErrorDir, $f, basename($f), strval($taskXml->filepath));
		}
	}
}

function handleUploadXMLResource (KalturaClient $client, SimpleXMLElement $uploadXML, $baseErrorDir)
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

	if(file_exists($baseErrorDir . $entryId . "/")){
		throw new Exception("append recording: entry [$entryId] asset [$assetId] index [$index] filePath [$filepath] duration [$duration] isLastChunk [$isLastChunk], append will not run, entry is in error list");
	}

	logMsg("Started append recording: entry [$entryId] asset [$assetId] index [$index] filePath [$filepath] duration [$duration] isLastChunk [$isLastChunk]");

	#Start the Multi-Request
	$client->startMultiRequest();

	$resource = getContentResource($client, $workmode, $filepath);

	$appendRecording = $client->liveStream->appendRecording($entryId, $assetId, $index, $resource, $duration, $isLastChunk);

	$responses = $client->doMultiRequest();

	validateMultiRequestResponse($responses);

	logMsg("Finished append recording: entry [$entryId] asset [$assetId] index [$index] filePath [$filepath] duration [$duration] isLastChunk [$isLastChunk]");
}

/**
 *	Function returns contentResource of the appropriate type to the workMode
 *	@param KalturaClinet $client
 *	@param string $filePath
 *	@param string $workMode
 *
 *	@return KalturaDataCenterContentResource
 */
function getContentResource (KalturaClient $client, $workMode, $filepath) 
{
	if ($workMode == 'kaltura') {
		$resource = new KalturaServerFileResource();
		$resource->localFilePath = $filepath;
		return $resource;
	}
	else 
	{
		$uploadToken = $client->uploadToken->add(new KalturaUploadToken());
		$uploadToken = $client->uploadToken->upload($uploadToken->id, realpath($filepath));

		$resource = new KalturaUploadedFileTokenResource();
		$resource->token = $uploadToken->id;
			
		return $resource;		
	}
}

function validateMultiRequestResponse($responses)
{
	foreach ($responses as $key => $value) {
		if(is_array($value) && isset($value['code'])){
			throw new Exception("Request index [$key] returned error code [{$value['code']}] and messgae [{$value['message']}]");
		}
	}
}

function moveFileToErrorDir($entryId, $baseErrorDir, $xmlOldFilePath, $xmlNewFileName, $mediaFilePath)
{
	logMsg("Moving File To Error Dir [$entryId] [$xmlOldFilePath] [$mediaFilePath]");

	$entryErrorDir = createErrorDirForEntry($baseErrorDir, $entryId);

	if(!$entryErrorDir){
		throw new Exception("Failed to create entry error dir for entry [$entryId]");
	}
	
	moveFile($xmlOldFilePath, $entryErrorDir . $xmlNewFileName);
	moveFile($mediaFilePath, $entryErrorDir . basename($mediaFilePath));
}

function createErrorDirForEntry($baseErrorDir, $entryId)
{
	$entryErrorDir = $baseErrorDir . $entryId . "/";

	if(file_exists($entryErrorDir))
		return $entryErrorDir;

	logMsg("Creating error dir for entry [$entryId]");
	
	if(!createDirIfNotExists($entryErrorDir))
		return null;

	return $entryErrorDir;
}

function createDirIfNotExists($dirName)
{
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
