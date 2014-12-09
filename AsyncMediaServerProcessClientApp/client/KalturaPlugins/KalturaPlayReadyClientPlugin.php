<?php
// ===================================================================================================
//                           _  __     _ _
//                          | |/ /__ _| | |_ _  _ _ _ __ _
//                          | ' </ _` | |  _| || | '_/ _` |
//                          |_|\_\__,_|_|\__|\_,_|_| \__,_|
//
// This file is part of the Kaltura Collaborative Media Suite which allows users
// to do with audio, video, and animation what Wiki platfroms allow them to do with
// text.
//
// Copyright (C) 2006-2011  Kaltura Inc.
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
// @ignore
// ===================================================================================================

/**
 * @package Kaltura
 * @subpackage Client
 */
require_once(dirname(__FILE__) . "/../KalturaClientBase.php");
require_once(dirname(__FILE__) . "/../KalturaEnums.php");
require_once(dirname(__FILE__) . "/../KalturaTypes.php");
require_once(dirname(__FILE__) . "/KalturaDrmClientPlugin.php");

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyAnalogVideoOPL
{
	const MIN_100 = 100;
	const MIN_150 = 150;
	const MIN_200 = 200;
}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyCompressedDigitalVideoOPL
{
	const MIN_400 = 400;
	const MIN_500 = 500;
}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyDigitalAudioOPL
{
	const MIN_100 = 100;
	const MIN_150 = 150;
	const MIN_200 = 200;
	const MIN_250 = 250;
	const MIN_300 = 300;
}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyLicenseRemovalPolicy
{
	const FIXED_FROM_EXPIRATION = 1;
	const ENTRY_SCHEDULING_END = 2;
	const NONE = 3;
}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyMinimumLicenseSecurityLevel
{
	const NON_COMMERCIAL_QUALITY = 150;
	const COMMERCIAL_QUALITY = 2000;
}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyUncompressedDigitalVideoOPL
{
	const MIN_100 = 100;
	const MIN_250 = 250;
	const MIN_270 = 270;
	const MIN_300 = 300;
}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyAnalogVideoOPId
{
	const EXPLICIT_ANALOG_TV = "2098DE8D-7DDD-4BAB-96C6-32EBB6FABEA3";
	const BEST_EFFORT_EXPLICIT_ANALOG_TV = "225CD36F-F132-49EF-BA8C-C91EA28E4369";
	const IMAGE_CONSTRAINT_VIDEO = "811C5110-46C8-4C6E-8163-C0482A15D47E";
	const AGC_AND_COLOR_STRIPE = "C3FD11C6-F8B7-4D20-B008-1DB17D61F2DA";
	const IMAGE_CONSTRAINT_MONITOR = "D783A191-E083-4BAF-B2DA-E69F910B3772";
}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyCopyEnablerType
{
	const CSS = "3CAF2814-A7AB-467C-B4DF-54ACC56C66DC";
	const PRINTER = "3CF2E054-F4D5-46cd-85A6-FCD152AD5FBE";
	const DEVICE = "6848955D-516B-4EB0-90E8-8F6D5A77B85F";
	const CLIPBOARD = "6E76C588-C3A9-47ea-A875-546D5209FF38";
	const SDC = "79F78A0D-0B69-401e-8A90-8BEF30BCE192";
	const SDC_PREVIEW = "81BD9AD4-A720-4ea1-B510-5D4E6FFB6A4D";
	const AACS = "C3CF56E0-7FF2-4491-809F-53E21D3ABF07";
	const HELIX = "CCB0B4E3-8B46-409e-A998-82556E3F5AF4";
	const CPRM = "CDD801AD-A577-48DB-950E-46D5F1592FAE";
	const PC = "CE480EDE-516B-40B3-90E1-D6CFC47630C5";
	const SDC_LIMITED = "E6785609-64CC-4bfa-B82D-6B619733B746";
	const ORANGE_BOOK_CD = "EC930B7D-1F2D-4682-A38B-8AB977721D0D";
}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyDigitalAudioOPId
{
	const SCMS = "6D5CFA59-C250-4426-930E-FAC72C8FCFA6";
}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyPlayEnablerType
{
	const HELIX = "002F9772-38A0-43E5-9F79-0F6361DCC62A";
	const HDCP_WIVU = "1B4542E3-B5CF-4C99-B3BA-829AF46C92F8";
	const AIRPLAY = "5ABF0F0D-DC29-4B82-9982-FD8E57525BFC";
	const UNKNOWN = "786627D8-C2A6-44BE-8F88-08AE255B01A";
	const HDCP_MIRACAST = "A340C256-0941-4D4C-AD1D-0B6735C0CB24";
	const UNKNOWN_520 = "B621D91F-EDCC-4035-8D4B-DC71760D43E9";
	const DTCP = "D685030B-0F4F-43A6-BBAD-356F1EA0049A";
}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyProfileOrderBy
{
	const ID_ASC = "+id";
	const NAME_ASC = "+name";
	const ID_DESC = "-id";
	const NAME_DESC = "-name";
}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyAnalogVideoOPIdHolder extends KalturaObjectBase
{
	/**
	 * The type of the play enabler
	 * 	 
	 *
	 * @var KalturaPlayReadyAnalogVideoOPId
	 */
	public $type = null;


}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyContentKey extends KalturaObjectBase
{
	/**
	 * Guid - key id of the specific content 
	 * 	 
	 *
	 * @var string
	 */
	public $keyId = null;

	/**
	 * License content key 64 bit encoded
	 * 	 
	 *
	 * @var string
	 */
	public $contentKey = null;


}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyCopyEnablerHolder extends KalturaObjectBase
{
	/**
	 * The type of the copy enabler
	 * 	 
	 *
	 * @var KalturaPlayReadyCopyEnablerType
	 */
	public $type = null;


}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyDigitalAudioOPIdHolder extends KalturaObjectBase
{
	/**
	 * The type of the play enabler
	 * 	 
	 *
	 * @var KalturaPlayReadyDigitalAudioOPId
	 */
	public $type = null;


}

/**
 * @package Kaltura
 * @subpackage Client
 */
abstract class KalturaPlayReadyRight extends KalturaObjectBase
{

}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyPolicy extends KalturaDrmPolicy
{
	/**
	 * 
	 *
	 * @var int
	 */
	public $gracePeriod = null;

	/**
	 * 
	 *
	 * @var KalturaPlayReadyLicenseRemovalPolicy
	 */
	public $licenseRemovalPolicy = null;

	/**
	 * 
	 *
	 * @var int
	 */
	public $licenseRemovalDuration = null;

	/**
	 * 
	 *
	 * @var KalturaPlayReadyMinimumLicenseSecurityLevel
	 */
	public $minSecurityLevel = null;

	/**
	 * 
	 *
	 * @var array of KalturaPlayReadyRight
	 */
	public $rights;


}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyLicenseDetails extends KalturaObjectBase
{
	/**
	 * PlayReady policy object
	 * 	 
	 *
	 * @var KalturaPlayReadyPolicy
	 */
	public $policy;

	/**
	 * License begin date
	 * 	 
	 *
	 * @var int
	 */
	public $beginDate = null;

	/**
	 * License expiration date
	 * 	 
	 *
	 * @var int
	 */
	public $expirationDate = null;

	/**
	 * License removal date
	 * 	 
	 *
	 * @var int
	 */
	public $removalDate = null;


}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyPlayEnablerHolder extends KalturaObjectBase
{
	/**
	 * The type of the play enabler
	 * 	 
	 *
	 * @var KalturaPlayReadyPlayEnablerType
	 */
	public $type = null;


}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaAccessControlPlayReadyPolicyAction extends KalturaRuleAction
{
	/**
	 * Play ready policy id 
	 * 	 
	 *
	 * @var int
	 */
	public $policyId = null;


}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyCopyRight extends KalturaPlayReadyRight
{
	/**
	 * 
	 *
	 * @var int
	 */
	public $copyCount = null;

	/**
	 * 
	 *
	 * @var array of KalturaPlayReadyCopyEnablerHolder
	 */
	public $copyEnablers;


}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyPlayRight extends KalturaPlayReadyRight
{
	/**
	 * 
	 *
	 * @var KalturaPlayReadyAnalogVideoOPL
	 */
	public $analogVideoOPL = null;

	/**
	 * 
	 *
	 * @var array of KalturaPlayReadyAnalogVideoOPIdHolder
	 */
	public $analogVideoOutputProtectionList;

	/**
	 * 
	 *
	 * @var KalturaPlayReadyDigitalAudioOPL
	 */
	public $compressedDigitalAudioOPL = null;

	/**
	 * 
	 *
	 * @var KalturaPlayReadyCompressedDigitalVideoOPL
	 */
	public $compressedDigitalVideoOPL = null;

	/**
	 * 
	 *
	 * @var array of KalturaPlayReadyDigitalAudioOPIdHolder
	 */
	public $digitalAudioOutputProtectionList;

	/**
	 * 
	 *
	 * @var KalturaPlayReadyDigitalAudioOPL
	 */
	public $uncompressedDigitalAudioOPL = null;

	/**
	 * 
	 *
	 * @var KalturaPlayReadyUncompressedDigitalVideoOPL
	 */
	public $uncompressedDigitalVideoOPL = null;

	/**
	 * 
	 *
	 * @var int
	 */
	public $firstPlayExpiration = null;

	/**
	 * 
	 *
	 * @var array of KalturaPlayReadyPlayEnablerHolder
	 */
	public $playEnablers;


}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyProfile extends KalturaDrmProfile
{
	/**
	 * 
	 *
	 * @var string
	 */
	public $keySeed = null;


}

/**
 * @package Kaltura
 * @subpackage Client
 */
abstract class KalturaPlayReadyPolicyBaseFilter extends KalturaDrmPolicyFilter
{

}

/**
 * @package Kaltura
 * @subpackage Client
 */
abstract class KalturaPlayReadyProfileBaseFilter extends KalturaDrmProfileFilter
{

}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyPolicyFilter extends KalturaPlayReadyPolicyBaseFilter
{

}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyProfileFilter extends KalturaPlayReadyProfileBaseFilter
{

}


/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyDrmService extends KalturaServiceBase
{
	function __construct(KalturaClient $client = null)
	{
		parent::__construct($client);
	}

	/**
	 * Generate key id and content key for PlayReady encryption
	 * 
	 * @return KalturaPlayReadyContentKey
	 */
	function generateKey()
	{
		$kparams = array();
		$this->client->queueServiceActionCall("playready_playreadydrm", "generateKey", $kparams);
		if ($this->client->isMultiRequest())
			return $this->client->getMultiRequestResult();
		$resultObject = $this->client->doQueue();
		$this->client->throwExceptionIfError($resultObject);
		$this->client->validateObjectType($resultObject, "KalturaPlayReadyContentKey");
		return $resultObject;
	}

	/**
	 * Get content keys for input key ids
	 * 
	 * @param string $keyIds - comma separated key id's
	 * @return array
	 */
	function getContentKeys($keyIds)
	{
		$kparams = array();
		$this->client->addParam($kparams, "keyIds", $keyIds);
		$this->client->queueServiceActionCall("playready_playreadydrm", "getContentKeys", $kparams);
		if ($this->client->isMultiRequest())
			return $this->client->getMultiRequestResult();
		$resultObject = $this->client->doQueue();
		$this->client->throwExceptionIfError($resultObject);
		$this->client->validateObjectType($resultObject, "array");
		return $resultObject;
	}

	/**
	 * Get content key and key id for the given entry
	 * 
	 * @param string $entryId 
	 * @param bool $createIfMissing 
	 * @return KalturaPlayReadyContentKey
	 */
	function getEntryContentKey($entryId, $createIfMissing = false)
	{
		$kparams = array();
		$this->client->addParam($kparams, "entryId", $entryId);
		$this->client->addParam($kparams, "createIfMissing", $createIfMissing);
		$this->client->queueServiceActionCall("playready_playreadydrm", "getEntryContentKey", $kparams);
		if ($this->client->isMultiRequest())
			return $this->client->getMultiRequestResult();
		$resultObject = $this->client->doQueue();
		$this->client->throwExceptionIfError($resultObject);
		$this->client->validateObjectType($resultObject, "KalturaPlayReadyContentKey");
		return $resultObject;
	}

	/**
	 * Get Play Ready policy and dates for license creation
	 * 
	 * @param string $keyId 
	 * @param string $deviceId 
	 * @param int $deviceType 
	 * @param string $entryId 
	 * @param string $referrer 64base encoded
	 * @return KalturaPlayReadyLicenseDetails
	 */
	function getLicenseDetails($keyId, $deviceId, $deviceType, $entryId = null, $referrer = null)
	{
		$kparams = array();
		$this->client->addParam($kparams, "keyId", $keyId);
		$this->client->addParam($kparams, "deviceId", $deviceId);
		$this->client->addParam($kparams, "deviceType", $deviceType);
		$this->client->addParam($kparams, "entryId", $entryId);
		$this->client->addParam($kparams, "referrer", $referrer);
		$this->client->queueServiceActionCall("playready_playreadydrm", "getLicenseDetails", $kparams);
		if ($this->client->isMultiRequest())
			return $this->client->getMultiRequestResult();
		$resultObject = $this->client->doQueue();
		$this->client->throwExceptionIfError($resultObject);
		$this->client->validateObjectType($resultObject, "KalturaPlayReadyLicenseDetails");
		return $resultObject;
	}
}
/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaPlayReadyClientPlugin extends KalturaClientPlugin
{
	/**
	 * @var KalturaPlayReadyDrmService
	 */
	public $playReadyDrm = null;

	protected function __construct(KalturaClient $client)
	{
		parent::__construct($client);
		$this->playReadyDrm = new KalturaPlayReadyDrmService($client);
	}

	/**
	 * @return KalturaPlayReadyClientPlugin
	 */
	public static function get(KalturaClient $client)
	{
		return new KalturaPlayReadyClientPlugin($client);
	}

	/**
	 * @return array<KalturaServiceBase>
	 */
	public function getServices()
	{
		$services = array(
			'playReadyDrm' => $this->playReadyDrm,
		);
		return $services;
	}

	/**
	 * @return string
	 */
	public function getName()
	{
		return 'playReady';
	}
}

