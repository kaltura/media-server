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
require_once(dirname(__FILE__) . "/KalturaDropFolderClientPlugin.php");

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaWebexDropFolder extends KalturaDropFolder
{
	/**
	 * 
	 *
	 * @var string
	 */
	public $webexUserId = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $webexPassword = null;

	/**
	 * 
	 *
	 * @var int
	 */
	public $webexSiteId = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $webexPartnerId = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $webexServiceUrl = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $webexHostIdMetadataFieldName = null;


}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaWebexDropFolderFile extends KalturaDropFolderFile
{
	/**
	 * 
	 *
	 * @var int
	 */
	public $recordingId = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $webexHostId = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $description = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $confId = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $contentUrl = null;


}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaWebexDropFolderContentProcessorJobData extends KalturaDropFolderContentProcessorJobData
{
	/**
	 * 
	 *
	 * @var string
	 */
	public $description = null;

	/**
	 * 
	 *
	 * @var string
	 */
	public $webexHostId = null;


}

/**
 * @package Kaltura
 * @subpackage Client
 */
class KalturaWebexDropFolderClientPlugin extends KalturaClientPlugin
{
	protected function __construct(KalturaClient $client)
	{
		parent::__construct($client);
	}

	/**
	 * @return KalturaWebexDropFolderClientPlugin
	 */
	public static function get(KalturaClient $client)
	{
		return new KalturaWebexDropFolderClientPlugin($client);
	}

	/**
	 * @return array<KalturaServiceBase>
	 */
	public function getServices()
	{
		$services = array(
		);
		return $services;
	}

	/**
	 * @return string
	 */
	public function getName()
	{
		return 'WebexDropFolder';
	}
}

