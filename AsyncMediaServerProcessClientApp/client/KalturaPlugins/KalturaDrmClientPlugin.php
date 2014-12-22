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

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaDrmLicenseExpirationPolicy
{
	const FIXED_DURATION = 1;
	const ENTRY_SCHEDULING_END = 2;
	const UNLIMITED = 3;
}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaDrmPolicyStatus
{
	const ACTIVE = 1;
	const DELETED = 2;
}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaDrmProfileStatus
{
	const ACTIVE = 1;
	const DELETED = 2;
}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaDrmDeviceOrderBy
{
	const CREATED_AT_ASC = "+createdAt";
	const CREATED_AT_DESC = "-createdAt";
}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaDrmLicenseScenario
{
	const PROTECTION = "playReady.PROTECTION";
	const PURCHASE = "playReady.PURCHASE";
	const RENTAL = "playReady.RENTAL";
	const SUBSCRIPTION = "playReady.SUBSCRIPTION";
}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaDrmLicenseType
{
	const NON_PERSISTENT = "playReady.NON_PERSISTENT";
	const PERSISTENT = "playReady.PERSISTENT";
}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaDrmPolicyOrderBy
{
}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaDrmProfileOrderBy
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
class KalturaDrmProviderType
{
	const PLAY_READY = "playReady.PLAY_READY";
	const WIDEVINE = "widevine.WIDEVINE";
}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaDrmPolicy extends KalturaObjectBase
{
	/**
	 * 
	 *
	 * @var int
	 * @readonly
	 */
	public $id = null;

	/**
	 * 
	 *
	 * @var int
	 * @insertonly
	 */
	public $partnerId = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $name = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $systemName = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $description = null;

	/**
	 * 
	 *
	 * @var KalturaDrmProviderType
	 */
	public $provider = null;

	/**
	 * 
	 *
	 * @var KalturaDrmPolicyStatus
	 */
	public $status = null;

	/**
	 * 
	 *
	 * @var KalturaDrmLicenseScenario
	 */
	public $scenario = null;

	/**
	 * 
	 *
	 * @var KalturaDrmLicenseType
	 */
	public $licenseType = null;

	/**
	 * 
	 *
	 * @var KalturaDrmLicenseExpirationPolicy
	 */
	public $licenseExpirationPolicy = null;

	/**
	 * Duration in days the license is effective
	 * 	 
	 *
	 * @var int
	 */
	public $duration = null;

	/**
	 * 
	 *
	 * @var int
	 * @readonly
	 */
	public $createdAt = null;

	/**
	 * 
	 *
	 * @var int
	 * @readonly
	 */
	public $updatedAt = null;


}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaDrmPolicyListResponse extends KalturaObjectBase
{
	/**
	 * 
	 *
	 * @var array of KalturaDrmPolicy
	 * @readonly
	 */
	public $objects;

	/**
	 * 
	 *
	 * @var int
	 * @readonly
	 */
	public $totalCount = null;


}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaDrmProfile extends KalturaObjectBase
{
	/**
	 * 
	 *
	 * @var int
	 * @readonly
	 */
	public $id = null;

	/**
	 * 
	 *
	 * @var int
	 * @insertonly
	 */
	public $partnerId = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $name = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $description = null;

	/**
	 * 
	 *
	 * @var KalturaDrmProviderType
	 */
	public $provider = null;

	/**
	 * 
	 *
	 * @var KalturaDrmProfileStatus
	 */
	public $status = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $licenseServerUrl = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $defaultPolicy = null;

	/**
	 * 
	 *
	 * @var int
	 * @readonly
	 */
	public $createdAt = null;

	/**
	 * 
	 *
	 * @var int
	 * @readonly
	 */
	public $updatedAt = null;


}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaDrmProfileListResponse extends KalturaObjectBase
{
	/**
	 * 
	 *
	 * @var array of KalturaDrmProfile
	 * @readonly
	 */
	public $objects;

	/**
	 * 
	 *
	 * @var int
	 * @readonly
	 */
	public $totalCount = null;


}

/**
 * @package Kaltura
 * @subpackage Client
 */
abstract class KalturaDrmDeviceBaseFilter extends KalturaFilter
{
	/**
	 * 
	 *
	 * @var int
	 */
	public $partnerIdEqual = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $partnerIdIn = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $deviceIdLike = null;

	/**
	 * 
	 *
	 * @var KalturaDrmProviderType
	 */
	public $providerEqual = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $providerIn = null;


}

/**
 * @package Kaltura
 * @subpackage Client
 */
abstract class KalturaDrmPolicyBaseFilter extends KalturaFilter
{
	/**
	 * 
	 *
	 * @var int
	 */
	public $partnerIdEqual = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $partnerIdIn = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $nameLike = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $systemNameLike = null;

	/**
	 * 
	 *
	 * @var KalturaDrmProviderType
	 */
	public $providerEqual = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $providerIn = null;

	/**
	 * 
	 *
	 * @var KalturaDrmPolicyStatus
	 */
	public $statusEqual = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $statusIn = null;

	/**
	 * 
	 *
	 * @var KalturaDrmLicenseScenario
	 */
	public $scenarioEqual = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $scenarioIn = null;


}

/**
 * @package Kaltura
 * @subpackage Client
 */
abstract class KalturaDrmProfileBaseFilter extends KalturaFilter
{
	/**
	 * 
	 *
	 * @var int
	 */
	public $idEqual = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $idIn = null;

	/**
	 * 
	 *
	 * @var int
	 */
	public $partnerIdEqual = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $partnerIdIn = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $nameLike = null;

	/**
	 * 
	 *
	 * @var KalturaDrmProviderType
	 */
	public $providerEqual = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $providerIn = null;

	/**
	 * 
	 *
	 * @var KalturaDrmProfileStatus
	 */
	public $statusEqual = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $statusIn = null;


}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaDrmDeviceFilter extends KalturaDrmDeviceBaseFilter
{

}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaDrmPolicyFilter extends KalturaDrmPolicyBaseFilter
{

}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaDrmProfileFilter extends KalturaDrmProfileBaseFilter
{

}


/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaDrmPolicyService extends KalturaServiceBase
{
	function __construct(KalturaClient $client = null)
	{
		parent::__construct($client);
	}

	/**
	 * Allows you to add a new DrmPolicy object
	 * 
	 * @param KalturaDrmPolicy $drmPolicy 
	 * @return KalturaDrmPolicy
	 */
	function add(KalturaDrmPolicy $drmPolicy)
	{
		$kparams = array();
		$this->client->addParam($kparams, "drmPolicy", $drmPolicy->toParams());
		$this->client->queueServiceActionCall("drm_drmpolicy", "add", $kparams);
		if ($this->client->isMultiRequest())
			return $this->client->getMultiRequestResult();
		$resultObject = $this->client->doQueue();
		$this->client->throwExceptionIfError($resultObject);
		$this->client->validateObjectType($resultObject, "KalturaDrmPolicy");
		return $resultObject;
	}

	/**
	 * Retrieve a KalturaDrmPolicy object by ID
	 * 
	 * @param int $drmPolicyId 
	 * @return KalturaDrmPolicy
	 */
	function get($drmPolicyId)
	{
		$kparams = array();
		$this->client->addParam($kparams, "drmPolicyId", $drmPolicyId);
		$this->client->queueServiceActionCall("drm_drmpolicy", "get", $kparams);
		if ($this->client->isMultiRequest())
			return $this->client->getMultiRequestResult();
		$resultObject = $this->client->doQueue();
		$this->client->throwExceptionIfError($resultObject);
		$this->client->validateObjectType($resultObject, "KalturaDrmPolicy");
		return $resultObject;
	}

	/**
	 * Update an existing KalturaDrmPolicy object
	 * 
	 * @param int $drmPolicyId 
	 * @param KalturaDrmPolicy $drmPolicy Id
	 * @return KalturaDrmPolicy
	 */
	function update($drmPolicyId, KalturaDrmPolicy $drmPolicy)
	{
		$kparams = array();
		$this->client->addParam($kparams, "drmPolicyId", $drmPolicyId);
		$this->client->addParam($kparams, "drmPolicy", $drmPolicy->toParams());
		$this->client->queueServiceActionCall("drm_drmpolicy", "update", $kparams);
		if ($this->client->isMultiRequest())
			return $this->client->getMultiRequestResult();
		$resultObject = $this->client->doQueue();
		$this->client->throwExceptionIfError($resultObject);
		$this->client->validateObjectType($resultObject, "KalturaDrmPolicy");
		return $resultObject;
	}

	/**
	 * Mark the KalturaDrmPolicy object as deleted
	 * 
	 * @param int $drmPolicyId 
	 * @return KalturaDrmPolicy
	 */
	function delete($drmPolicyId)
	{
		$kparams = array();
		$this->client->addParam($kparams, "drmPolicyId", $drmPolicyId);
		$this->client->queueServiceActionCall("drm_drmpolicy", "delete", $kparams);
		if ($this->client->isMultiRequest())
			return $this->client->getMultiRequestResult();
		$resultObject = $this->client->doQueue();
		$this->client->throwExceptionIfError($resultObject);
		$this->client->validateObjectType($resultObject, "KalturaDrmPolicy");
		return $resultObject;
	}

	/**
	 * List KalturaDrmPolicy objects
	 * 
	 * @param KalturaDrmPolicyFilter $filter 
	 * @param KalturaFilterPager $pager 
	 * @return KalturaDrmPolicyListResponse
	 */
	function listAction(KalturaDrmPolicyFilter $filter = null, KalturaFilterPager $pager = null)
	{
		$kparams = array();
		if ($filter !== null)
			$this->client->addParam($kparams, "filter", $filter->toParams());
		if ($pager !== null)
			$this->client->addParam($kparams, "pager", $pager->toParams());
		$this->client->queueServiceActionCall("drm_drmpolicy", "list", $kparams);
		if ($this->client->isMultiRequest())
			return $this->client->getMultiRequestResult();
		$resultObject = $this->client->doQueue();
		$this->client->throwExceptionIfError($resultObject);
		$this->client->validateObjectType($resultObject, "KalturaDrmPolicyListResponse");
		return $resultObject;
	}
}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaDrmProfileService extends KalturaServiceBase
{
	function __construct(KalturaClient $client = null)
	{
		parent::__construct($client);
	}

	/**
	 * Allows you to add a new DrmProfile object
	 * 
	 * @param KalturaDrmProfile $drmProfile 
	 * @return KalturaDrmProfile
	 */
	function add(KalturaDrmProfile $drmProfile)
	{
		$kparams = array();
		$this->client->addParam($kparams, "drmProfile", $drmProfile->toParams());
		$this->client->queueServiceActionCall("drm_drmprofile", "add", $kparams);
		if ($this->client->isMultiRequest())
			return $this->client->getMultiRequestResult();
		$resultObject = $this->client->doQueue();
		$this->client->throwExceptionIfError($resultObject);
		$this->client->validateObjectType($resultObject, "KalturaDrmProfile");
		return $resultObject;
	}

	/**
	 * Retrieve a KalturaDrmProfile object by ID
	 * 
	 * @param int $drmProfileId 
	 * @return KalturaDrmProfile
	 */
	function get($drmProfileId)
	{
		$kparams = array();
		$this->client->addParam($kparams, "drmProfileId", $drmProfileId);
		$this->client->queueServiceActionCall("drm_drmprofile", "get", $kparams);
		if ($this->client->isMultiRequest())
			return $this->client->getMultiRequestResult();
		$resultObject = $this->client->doQueue();
		$this->client->throwExceptionIfError($resultObject);
		$this->client->validateObjectType($resultObject, "KalturaDrmProfile");
		return $resultObject;
	}

	/**
	 * Update an existing KalturaDrmProfile object
	 * 
	 * @param int $drmProfileId 
	 * @param KalturaDrmProfile $drmProfile Id
	 * @return KalturaDrmProfile
	 */
	function update($drmProfileId, KalturaDrmProfile $drmProfile)
	{
		$kparams = array();
		$this->client->addParam($kparams, "drmProfileId", $drmProfileId);
		$this->client->addParam($kparams, "drmProfile", $drmProfile->toParams());
		$this->client->queueServiceActionCall("drm_drmprofile", "update", $kparams);
		if ($this->client->isMultiRequest())
			return $this->client->getMultiRequestResult();
		$resultObject = $this->client->doQueue();
		$this->client->throwExceptionIfError($resultObject);
		$this->client->validateObjectType($resultObject, "KalturaDrmProfile");
		return $resultObject;
	}

	/**
	 * Mark the KalturaDrmProfile object as deleted
	 * 
	 * @param int $drmProfileId 
	 * @return KalturaDrmProfile
	 */
	function delete($drmProfileId)
	{
		$kparams = array();
		$this->client->addParam($kparams, "drmProfileId", $drmProfileId);
		$this->client->queueServiceActionCall("drm_drmprofile", "delete", $kparams);
		if ($this->client->isMultiRequest())
			return $this->client->getMultiRequestResult();
		$resultObject = $this->client->doQueue();
		$this->client->throwExceptionIfError($resultObject);
		$this->client->validateObjectType($resultObject, "KalturaDrmProfile");
		return $resultObject;
	}

	/**
	 * List KalturaDrmProfile objects
	 * 
	 * @param KalturaDrmProfileFilter $filter 
	 * @param KalturaFilterPager $pager 
	 * @return KalturaDrmProfileListResponse
	 */
	function listAction(KalturaDrmProfileFilter $filter = null, KalturaFilterPager $pager = null)
	{
		$kparams = array();
		if ($filter !== null)
			$this->client->addParam($kparams, "filter", $filter->toParams());
		if ($pager !== null)
			$this->client->addParam($kparams, "pager", $pager->toParams());
		$this->client->queueServiceActionCall("drm_drmprofile", "list", $kparams);
		if ($this->client->isMultiRequest())
			return $this->client->getMultiRequestResult();
		$resultObject = $this->client->doQueue();
		$this->client->throwExceptionIfError($resultObject);
		$this->client->validateObjectType($resultObject, "KalturaDrmProfileListResponse");
		return $resultObject;
	}

	/**
	 * Retrieve a KalturaDrmProfile object by provider, if no specific profile defined return default profile
	 * 
	 * @param string $provider 
	 * @return KalturaDrmProfile
	 */
	function getByProvider($provider)
	{
		$kparams = array();
		$this->client->addParam($kparams, "provider", $provider);
		$this->client->queueServiceActionCall("drm_drmprofile", "getByProvider", $kparams);
		if ($this->client->isMultiRequest())
			return $this->client->getMultiRequestResult();
		$resultObject = $this->client->doQueue();
		$this->client->throwExceptionIfError($resultObject);
		$this->client->validateObjectType($resultObject, "KalturaDrmProfile");
		return $resultObject;
	}
}
/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaDrmClientPlugin extends KalturaClientPlugin
{
	/**
	 * @var KalturaDrmPolicyService
	 */
	public $drmPolicy = null;

	/**
	 * @var KalturaDrmProfileService
	 */
	public $drmProfile = null;

	protected function __construct(KalturaClient $client)
	{
		parent::__construct($client);
		$this->drmPolicy = new KalturaDrmPolicyService($client);
		$this->drmProfile = new KalturaDrmProfileService($client);
	}

	/**
	 * @return KalturaDrmClientPlugin
	 */
	public static function get(KalturaClient $client)
	{
		return new KalturaDrmClientPlugin($client);
	}

	/**
	 * @return array<KalturaServiceBase>
	 */
	public function getServices()
	{
		$services = array(
			'drmPolicy' => $this->drmPolicy,
			'drmProfile' => $this->drmProfile,
		);
		return $services;
	}

	/**
	 * @return string
	 */
	public function getName()
	{
		return 'drm';
	}
}

