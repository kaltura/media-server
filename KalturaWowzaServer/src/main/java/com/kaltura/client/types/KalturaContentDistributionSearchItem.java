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
import com.kaltura.client.enums.KalturaEntryDistributionSunStatus;
import com.kaltura.client.enums.KalturaEntryDistributionFlag;
import com.kaltura.client.enums.KalturaEntryDistributionStatus;
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
public class KalturaContentDistributionSearchItem extends KalturaSearchItem {
    public boolean noDistributionProfiles;
    public int distributionProfileId = Integer.MIN_VALUE;
    public KalturaEntryDistributionSunStatus distributionSunStatus;
    public KalturaEntryDistributionFlag entryDistributionFlag;
    public KalturaEntryDistributionStatus entryDistributionStatus;
    public boolean hasEntryDistributionValidationErrors;
	/**  Comma seperated validation error types  */
    public String entryDistributionValidationErrors;

    public KalturaContentDistributionSearchItem() {
    }

    public KalturaContentDistributionSearchItem(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("noDistributionProfiles")) {
                this.noDistributionProfiles = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("distributionProfileId")) {
                this.distributionProfileId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("distributionSunStatus")) {
                this.distributionSunStatus = KalturaEntryDistributionSunStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("entryDistributionFlag")) {
                this.entryDistributionFlag = KalturaEntryDistributionFlag.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("entryDistributionStatus")) {
                this.entryDistributionStatus = KalturaEntryDistributionStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("hasEntryDistributionValidationErrors")) {
                this.hasEntryDistributionValidationErrors = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("entryDistributionValidationErrors")) {
                this.entryDistributionValidationErrors = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaContentDistributionSearchItem");
        kparams.add("noDistributionProfiles", this.noDistributionProfiles);
        kparams.add("distributionProfileId", this.distributionProfileId);
        kparams.add("distributionSunStatus", this.distributionSunStatus);
        kparams.add("entryDistributionFlag", this.entryDistributionFlag);
        kparams.add("entryDistributionStatus", this.entryDistributionStatus);
        kparams.add("hasEntryDistributionValidationErrors", this.hasEntryDistributionValidationErrors);
        kparams.add("entryDistributionValidationErrors", this.entryDistributionValidationErrors);
        return kparams;
    }

}

