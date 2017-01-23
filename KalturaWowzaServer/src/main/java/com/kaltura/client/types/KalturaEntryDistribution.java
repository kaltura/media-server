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
package com.kaltura.client.types;

import org.w3c.dom.Element;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.enums.KalturaEntryDistributionStatus;
import com.kaltura.client.enums.KalturaEntryDistributionSunStatus;
import com.kaltura.client.enums.KalturaEntryDistributionFlag;
import com.kaltura.client.enums.KalturaBatchJobErrorTypes;
import com.kaltura.client.enums.KalturaNullableBoolean;
import java.util.ArrayList;
import com.kaltura.client.utils.ParseUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * This class was generated using exec.php
 * against an XML schema provided by Kaltura.
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

@SuppressWarnings("serial")
public class KalturaEntryDistribution extends KalturaObjectBase {
	/**  Auto generated unique id  */
    public int id = Integer.MIN_VALUE;
	/**  Entry distribution creation date as Unix timestamp (In seconds)  */
    public int createdAt = Integer.MIN_VALUE;
	/**  Entry distribution last update date as Unix timestamp (In seconds)  */
    public int updatedAt = Integer.MIN_VALUE;
	/**  Entry distribution submission date as Unix timestamp (In seconds)  */
    public int submittedAt = Integer.MIN_VALUE;
    public String entryId;
    public int partnerId = Integer.MIN_VALUE;
    public int distributionProfileId = Integer.MIN_VALUE;
    public KalturaEntryDistributionStatus status;
    public KalturaEntryDistributionSunStatus sunStatus;
    public KalturaEntryDistributionFlag dirtyStatus;
	/**  Comma separated thumbnail asset ids  */
    public String thumbAssetIds;
	/**  Comma separated flavor asset ids  */
    public String flavorAssetIds;
	/**  Comma separated asset ids  */
    public String assetIds;
	/**  Entry distribution publish time as Unix timestamp (In seconds)  */
    public int sunrise = Integer.MIN_VALUE;
	/**  Entry distribution un-publish time as Unix timestamp (In seconds)  */
    public int sunset = Integer.MIN_VALUE;
	/**  The id as returned from the distributed destination  */
    public String remoteId;
	/**  The plays as retrieved from the remote destination reports  */
    public int plays = Integer.MIN_VALUE;
	/**  The views as retrieved from the remote destination reports  */
    public int views = Integer.MIN_VALUE;
    public ArrayList<KalturaDistributionValidationError> validationErrors;
    public KalturaBatchJobErrorTypes errorType;
    public int errorNumber = Integer.MIN_VALUE;
    public String errorDescription;
    public KalturaNullableBoolean hasSubmitResultsLog;
    public KalturaNullableBoolean hasSubmitSentDataLog;
    public KalturaNullableBoolean hasUpdateResultsLog;
    public KalturaNullableBoolean hasUpdateSentDataLog;
    public KalturaNullableBoolean hasDeleteResultsLog;
    public KalturaNullableBoolean hasDeleteSentDataLog;

    public KalturaEntryDistribution() {
    }

    public KalturaEntryDistribution(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAt")) {
                this.updatedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("submittedAt")) {
                this.submittedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("entryId")) {
                this.entryId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerId")) {
                this.partnerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("distributionProfileId")) {
                this.distributionProfileId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaEntryDistributionStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("sunStatus")) {
                this.sunStatus = KalturaEntryDistributionSunStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("dirtyStatus")) {
                this.dirtyStatus = KalturaEntryDistributionFlag.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("thumbAssetIds")) {
                this.thumbAssetIds = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("flavorAssetIds")) {
                this.flavorAssetIds = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("assetIds")) {
                this.assetIds = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sunrise")) {
                this.sunrise = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("sunset")) {
                this.sunset = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("remoteId")) {
                this.remoteId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("plays")) {
                this.plays = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("views")) {
                this.views = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("validationErrors")) {
                this.validationErrors = ParseUtils.parseArray(KalturaDistributionValidationError.class, aNode);
                continue;
            } else if (nodeName.equals("errorType")) {
                this.errorType = KalturaBatchJobErrorTypes.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("errorNumber")) {
                this.errorNumber = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("errorDescription")) {
                this.errorDescription = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("hasSubmitResultsLog")) {
                this.hasSubmitResultsLog = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("hasSubmitSentDataLog")) {
                this.hasSubmitSentDataLog = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("hasUpdateResultsLog")) {
                this.hasUpdateResultsLog = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("hasUpdateSentDataLog")) {
                this.hasUpdateSentDataLog = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("hasDeleteResultsLog")) {
                this.hasDeleteResultsLog = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("hasDeleteSentDataLog")) {
                this.hasDeleteSentDataLog = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaEntryDistribution");
        kparams.add("entryId", this.entryId);
        kparams.add("distributionProfileId", this.distributionProfileId);
        kparams.add("thumbAssetIds", this.thumbAssetIds);
        kparams.add("flavorAssetIds", this.flavorAssetIds);
        kparams.add("assetIds", this.assetIds);
        kparams.add("sunrise", this.sunrise);
        kparams.add("sunset", this.sunset);
        kparams.add("validationErrors", this.validationErrors);
        return kparams;
    }

}

