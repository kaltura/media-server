<?php

$configFilePath = $argv[1];
$config = parse_ini_file($configFilePath);

//Load configuration
$files = glob($config['dirName'] . '/*.xml');
if (!count($files))
{
	die ('No files at this time.');
}

var_dump(count ($files) . ' files found.');

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
		$partnerId = strval ($uploadXML->partnerId);
		$partnerAdminSecret = strval ($uploadXML->adminSecret);
		$duration = intval ($uploadXML->duration);
		$index = intval($uploadXML->index);
		$filepath = strval($uploadXML->filepath);
		$workmode = strval($uploadXML->workMode);
		
		var_dump("append recording: entry [$entryId] index [$index] filePath [$filepath] duration [$duration]");
			
		$clientConfig = $client->getConfig();
		$clientConfig->partnerId = $partnerId;
		$client->setConfig($clientConfig);
		
		try 
		{
			$liveEntry = $client->liveStream->get($entryId);
		}
		catch (Exception $e)
		{
			var_dump ("An error occured retrieving entry with id [$entryId]. Error message [" . $e->getMessage() . "]");
			return false;
		}
		
		$resource = getContentResource($filepath, $liveEntry, $workmode,$client);
		if (!$resource)
			return false;
		
		try {
			$updatedEntry = $client->liveStream->appendRecording($entryId, $index, $resource, $duration);
		}
		catch (Exception $e)
		{
			var_dump('Append live recording error: ' . $e->getMessage());
			return false;
		}
		
		appendRecording($liveEntry, $client);
		
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
				var_dump("Content resource creation error: " . $tokenResponse->getMessage());
				return null;
			}
				
			return $resource;
			
		} catch (Exception $e) {
			var_dump("Content resource creation error: " . $e->getMessage());
		}
	}
	
	return null;
}

function appendRecording (KalturaLiveStreamEntry $liveEntry, KalturaClient $client){
	var_dump("creating media entry for live entry [" . $liveEntry->id . "]");

	$resource = new KalturaEntryResource();
	$resource->entryId = $liveEntry->id;

	$recordedEntryId = $liveEntry->recordedEntryId;
	if (!$recordedEntryId) {
		var_dump("recorded media entry is null for entry [" . $liveEntry->id . "]: reloading");
		$liveEntry = reloadEntry($liveEntry->id, $liveEntry->partnerId, $client);
		$recordedEntryId = $liveEntry->recordedEntryId;
	}

	if (!$recordedEntryId) {
		var_dump("recorded media entry is null for entry [" . $liveEntry->id . "]: creating media entry");
		$mediaEntry = createMediaEntry($liveEntry, $client);
		if (!$mediaEntry)
		{
			var_dump("recorded media entry is null for entry [" . $liveEntry->id . "]: creating media entry failed");
			return;
		}
		$recordedEntryId = $mediaEntry->id;
	}

	try {
		$client->media->cancelReplace($recordedEntryId);
		$mediaEntry = $client->media->updateContent($recordedEntryId, $resource);

		if (!is_null($mediaEntry->replacingEntryId))
			$client->media->approveReplace($recordedEntryId);

	} catch (Exception $e) {
		var_dump("failed to add content resource [$recordedEntryId]: " . $e->getMessage());
	}
}

function  reloadEntry($entryId, KalturaClient $client) {
	try {
		$liveStreamEntry = $client->liveStream->get($entryId);
	} catch (Exception $e) {
		var_dump("KalturaLiveStreamManager::reloadEntry unable to get entry [$entryId]: " . $e->getMessage());
		return null;
	}

	return $liveStreamEntry;
}
	

function createMediaEntry(KalturaLiveEntry $liveEntry, KalturaClient $client) {
	var_dump("creating media entry for live entry [{$liveEntry->id}]");

	if ($liveEntry->recordedEntryId) {
		try {
			$mediaEntry = $client->media->get($liveEntry->recordedEntryId);
		} catch (Exception $e) {
			var_dump("failed to get recorded media entry [{$liveEntry->recordedEntryId}]: " . $e->getMessage());
		}
	}

	if (!$mediaEntry) {
		$mediaEntry = new KalturaMediaEntry();
		$mediaEntry->rootEntryId = $liveEntry->id;
		$mediaEntry->name = $liveEntry->name;
		$mediaEntry->description = $liveEntry->description;
		$mediaEntry->sourceType = KalturaSourceType::RECORDED_LIVE;
		$mediaEntry->mediaType = KalturaMediaType::VIDEO;
		$mediaEntry->accessControlId = $liveEntry->accessControlId;
		$mediaEntry->userId = $liveEntry->userId;

		try {
			$mediaEntry = $client->media->add($mediaEntry);
		} catch (Exception $e) {
			var_dump("failed to create media entry: " . $e->getMessage());
			return null;
		}
		var_dump("created media entry [" . $mediaEntry->id . "] for live entry [" . $liveEntry->id . "]");
	}

	try {
		$class = get_class($liveEntry);
		$updateLiveEntry = new $class();
	} catch (Exception $e) {
		var_dump("failed to instantiate [" . get_class($liveEntry).+ "]: " . $e->getMessage());
		return null;
	}
	$updateLiveEntry->recordedEntryId = $mediaEntry->id;
	try {
		$client->baseEntry->update($liveEntry->id, $updateLiveEntry);
	} catch (Exception $e) {
		var_dump("failed to upload file: " . $e->getMessage());
	}

	return $mediaEntry;
}
	
