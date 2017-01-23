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

/**
 * This class was generated using exec.php
 * against an XML schema provided by Kaltura.
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

/**  Manage the connection between Conversion Profiles and Asset Params  */
@SuppressWarnings("serial")
public class KalturaConversionProfileAssetParamsService extends KalturaServiceBase {
    public KalturaConversionProfileAssetParamsService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaConversionProfileAssetParamsListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaConversionProfileAssetParamsListResponse list(KalturaConversionProfileAssetParamsFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

	/**  Lists asset parmas of conversion profile by ID  */
    public KalturaConversionProfileAssetParamsListResponse list(KalturaConversionProfileAssetParamsFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("filter", filter);
        kparams.add("pager", pager);
        this.kalturaClient.queueServiceCall("conversionprofileassetparams", "list", kparams, KalturaConversionProfileAssetParamsListResponse.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaConversionProfileAssetParamsListResponse.class, resultXmlElement);
    }

	/**  Update asset parmas of conversion profile by ID  */
    public KalturaConversionProfileAssetParams update(int conversionProfileId, int assetParamsId, KalturaConversionProfileAssetParams conversionProfileAssetParams) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("conversionProfileId", conversionProfileId);
        kparams.add("assetParamsId", assetParamsId);
        kparams.add("conversionProfileAssetParams", conversionProfileAssetParams);
        this.kalturaClient.queueServiceCall("conversionprofileassetparams", "update", kparams, KalturaConversionProfileAssetParams.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaConversionProfileAssetParams.class, resultXmlElement);
    }
}
