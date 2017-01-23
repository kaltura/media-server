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
// Copyright (C) 2006-2016  Kaltura Inc.
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
package com.kaltura.client;

import com.kaltura.client.types.KalturaBaseResponseProfile;
import com.kaltura.client.services.KalturaLiveStreamService;
import com.kaltura.client.services.KalturaMetadataService;
import com.kaltura.client.services.KalturaMetadataProfileService;
import com.kaltura.client.services.KalturaConversionProfileAssetParamsService;
import com.kaltura.client.services.KalturaConversionProfileService;
import com.kaltura.client.services.KalturaFlavorAssetService;
import com.kaltura.client.services.KalturaUploadTokenService;
import com.kaltura.client.services.KalturaMediaService;

/**
 * This class was generated using exec.php
 * against an XML schema provided by Kaltura.
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

@SuppressWarnings("serial")
public class KalturaClient extends KalturaClientBase {
	
	public KalturaClient(KalturaConfiguration config) {
		super(config);
		
		this.setClientTag("java:17-01-19");
		this.setApiVersion("3.3.0");
	}

	
	protected KalturaLiveStreamService liveStreamService;
	public KalturaLiveStreamService getLiveStreamService() {
		if(this.liveStreamService == null)
			this.liveStreamService = new KalturaLiveStreamService(this);
	
		return this.liveStreamService;
	}

	protected KalturaMetadataService metadataService;
	public KalturaMetadataService getMetadataService() {
		if(this.metadataService == null)
			this.metadataService = new KalturaMetadataService(this);

		return this.metadataService;
	}
	protected KalturaMetadataProfileService metadataProfileService;
	public KalturaMetadataProfileService getMetadataProfileService() {
		if(this.metadataProfileService == null)
			this.metadataProfileService = new KalturaMetadataProfileService(this);

		return this.metadataProfileService;
	}
	protected KalturaFlavorAssetService flavorAssetService;
	public KalturaFlavorAssetService getFlavorAssetService() {
		if(this.flavorAssetService == null)
			this.flavorAssetService = new KalturaFlavorAssetService(this);

		return this.flavorAssetService;
	}
	protected KalturaConversionProfileAssetParamsService conversionProfileAssetParamsService;
	public KalturaConversionProfileAssetParamsService getConversionProfileAssetParamsService() {
		if(this.conversionProfileAssetParamsService == null)
			this.conversionProfileAssetParamsService = new KalturaConversionProfileAssetParamsService(this);

		return this.conversionProfileAssetParamsService;
	}

	protected KalturaUploadTokenService uploadTokenService;
	public KalturaUploadTokenService getUploadTokenService() {
		if(this.uploadTokenService == null)
			this.uploadTokenService = new KalturaUploadTokenService(this);

		return this.uploadTokenService;
	}
	protected KalturaMediaService mediaService;
	public KalturaMediaService getMediaService() {
		if(this.mediaService == null)
			this.mediaService = new KalturaMediaService(this);

		return this.mediaService;
	}

	/**
	 * @param String $clientTag
	 */
	public void setClientTag(String clientTag){
		this.clientConfiguration.put("clientTag", clientTag);
	}
	
	/**
	 * @return String
	 */
	public String getClientTag(){
		if(this.clientConfiguration.containsKey("clientTag")){
			return (String) this.clientConfiguration.get("clientTag");
		}
		
		return null;
	}
	
	/**
	 * @param String $apiVersion
	 */
	public void setApiVersion(String apiVersion){
		this.clientConfiguration.put("apiVersion", apiVersion);
	}
	
	/**
	 * @return String
	 */
	public String getApiVersion(){
		if(this.clientConfiguration.containsKey("apiVersion")){
			return (String) this.clientConfiguration.get("apiVersion");
		}
		
		return null;
	}
	
	/**
	 * Impersonated partner id
	 * 
	 * @param Integer $partnerId
	 */
	public void setPartnerId(Integer partnerId){
		this.requestConfiguration.put("partnerId", partnerId);
	}
	
	/**
	 * Impersonated partner id
	 * 
	 * @return Integer
	 */
	public Integer getPartnerId(){
		if(this.requestConfiguration.containsKey("partnerId")){
			return (Integer) this.requestConfiguration.get("partnerId");
		}
		
		return null;
	}
	
	/**
	 * Kaltura API session
	 * 
	 * @param String $ks
	 */
	public void setKs(String ks){
		this.requestConfiguration.put("ks", ks);
	}
	
	/**
	 * Kaltura API session
	 * 
	 * @return String
	 */
	public String getKs(){
		if(this.requestConfiguration.containsKey("ks")){
			return (String) this.requestConfiguration.get("ks");
		}
		
		return null;
	}
	
	/**
	 * Kaltura API session
	 * 
	 * @param String $sessionId
	 */
	public void setSessionId(String sessionId){
		this.requestConfiguration.put("ks", sessionId);
	}
	
	/**
	 * Kaltura API session
	 * 
	 * @return String
	 */
	public String getSessionId(){
		if(this.requestConfiguration.containsKey("ks")){
			return (String) this.requestConfiguration.get("ks");
		}
		
		return null;
	}
	
	/**
	 * Response profile - this attribute will be automatically unset after every API call.
	 * 
	 * @param KalturaBaseResponseProfile $responseProfile
	 */
	public void setResponseProfile(KalturaBaseResponseProfile responseProfile){
		this.requestConfiguration.put("responseProfile", responseProfile);
	}
	
	/**
	 * Response profile - this attribute will be automatically unset after every API call.
	 * 
	 * @return KalturaBaseResponseProfile
	 */
	public KalturaBaseResponseProfile getResponseProfile(){
		if(this.requestConfiguration.containsKey("responseProfile")){
			return (KalturaBaseResponseProfile) this.requestConfiguration.get("responseProfile");
		}
		
		return null;
	}
	
	/**
	 * Clear all volatile configuration parameters
	 */
	protected void resetRequest(){
		this.requestConfiguration.remove("responseProfile");
	}
}
