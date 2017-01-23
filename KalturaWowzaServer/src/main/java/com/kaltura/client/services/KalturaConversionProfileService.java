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
package com.kaltura.client.services;

import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaServiceBase;
import com.kaltura.client.types.*;
import org.w3c.dom.Element;
import com.kaltura.client.utils.ParseUtils;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.enums.*;

/**
 * This class was generated using exec.php
 * against an XML schema provided by Kaltura.
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

/**  Add &amp; Manage Conversion Profiles  */
@SuppressWarnings("serial")
public class KalturaConversionProfileService extends KalturaServiceBase {
    public KalturaConversionProfileService(KalturaClient client) {
        this.kalturaClient = client;
    }

	/**  Set Conversion Profile to be the partner default  */
    public KalturaConversionProfile setAsDefault(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("id", id);
        this.kalturaClient.queueServiceCall("conversionprofile", "setAsDefault", kparams, KalturaConversionProfile.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaConversionProfile.class, resultXmlElement);
    }

    public KalturaConversionProfile getDefault() throws KalturaApiException {
        return this.getDefault(null);
    }

	/**  Get the partner's default conversion profile  */
    public KalturaConversionProfile getDefault(KalturaConversionProfileType type) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("type", type);
        this.kalturaClient.queueServiceCall("conversionprofile", "getDefault", kparams, KalturaConversionProfile.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaConversionProfile.class, resultXmlElement);
    }

	/**  Add new Conversion Profile  */
    public KalturaConversionProfile add(KalturaConversionProfile conversionProfile) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("conversionProfile", conversionProfile);
        this.kalturaClient.queueServiceCall("conversionprofile", "add", kparams, KalturaConversionProfile.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaConversionProfile.class, resultXmlElement);
    }

	/**  Get Conversion Profile by ID  */
    public KalturaConversionProfile get(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("id", id);
        this.kalturaClient.queueServiceCall("conversionprofile", "get", kparams, KalturaConversionProfile.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaConversionProfile.class, resultXmlElement);
    }

	/**  Update Conversion Profile by ID  */
    public KalturaConversionProfile update(int id, KalturaConversionProfile conversionProfile) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("id", id);
        kparams.add("conversionProfile", conversionProfile);
        this.kalturaClient.queueServiceCall("conversionprofile", "update", kparams, KalturaConversionProfile.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaConversionProfile.class, resultXmlElement);
    }

	/**  Delete Conversion Profile by ID  */
    public void delete(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("id", id);
        this.kalturaClient.queueServiceCall("conversionprofile", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return ;
        this.kalturaClient.doQueue();
    }

    public KalturaConversionProfileListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaConversionProfileListResponse list(KalturaConversionProfileFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

	/**  List Conversion Profiles by filter with paging support  */
    public KalturaConversionProfileListResponse list(KalturaConversionProfileFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("filter", filter);
        kparams.add("pager", pager);
        this.kalturaClient.queueServiceCall("conversionprofile", "list", kparams, KalturaConversionProfileListResponse.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaConversionProfileListResponse.class, resultXmlElement);
    }
}
