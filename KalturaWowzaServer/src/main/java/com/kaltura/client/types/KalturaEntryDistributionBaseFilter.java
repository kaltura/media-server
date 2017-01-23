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
import com.kaltura.client.enums.KalturaEntryDistributionStatus;
import com.kaltura.client.enums.KalturaEntryDistributionFlag;
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
public abstract class KalturaEntryDistributionBaseFilter extends KalturaRelatedFilter {
    public int idEqual = Integer.MIN_VALUE;
    public String idIn;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtLessThanOrEqual = Integer.MIN_VALUE;
    public int submittedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int submittedAtLessThanOrEqual = Integer.MIN_VALUE;
    public String entryIdEqual;
    public String entryIdIn;
    public int distributionProfileIdEqual = Integer.MIN_VALUE;
    public String distributionProfileIdIn;
    public KalturaEntryDistributionStatus statusEqual;
    public String statusIn;
    public KalturaEntryDistributionFlag dirtyStatusEqual;
    public String dirtyStatusIn;
    public int sunriseGreaterThanOrEqual = Integer.MIN_VALUE;
    public int sunriseLessThanOrEqual = Integer.MIN_VALUE;
    public int sunsetGreaterThanOrEqual = Integer.MIN_VALUE;
    public int sunsetLessThanOrEqual = Integer.MIN_VALUE;

    public KalturaEntryDistributionBaseFilter() {
    }

    public KalturaEntryDistributionBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("idEqual")) {
                this.idEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("idIn")) {
                this.idIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("createdAtGreaterThanOrEqual")) {
                this.createdAtGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("createdAtLessThanOrEqual")) {
                this.createdAtLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAtGreaterThanOrEqual")) {
                this.updatedAtGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAtLessThanOrEqual")) {
                this.updatedAtLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("submittedAtGreaterThanOrEqual")) {
                this.submittedAtGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("submittedAtLessThanOrEqual")) {
                this.submittedAtLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("entryIdEqual")) {
                this.entryIdEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("entryIdIn")) {
                this.entryIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("distributionProfileIdEqual")) {
                this.distributionProfileIdEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("distributionProfileIdIn")) {
                this.distributionProfileIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("statusEqual")) {
                this.statusEqual = KalturaEntryDistributionStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("dirtyStatusEqual")) {
                this.dirtyStatusEqual = KalturaEntryDistributionFlag.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("dirtyStatusIn")) {
                this.dirtyStatusIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sunriseGreaterThanOrEqual")) {
                this.sunriseGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("sunriseLessThanOrEqual")) {
                this.sunriseLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("sunsetGreaterThanOrEqual")) {
                this.sunsetGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("sunsetLessThanOrEqual")) {
                this.sunsetLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaEntryDistributionBaseFilter");
        kparams.add("idEqual", this.idEqual);
        kparams.add("idIn", this.idIn);
        kparams.add("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.add("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.add("updatedAtGreaterThanOrEqual", this.updatedAtGreaterThanOrEqual);
        kparams.add("updatedAtLessThanOrEqual", this.updatedAtLessThanOrEqual);
        kparams.add("submittedAtGreaterThanOrEqual", this.submittedAtGreaterThanOrEqual);
        kparams.add("submittedAtLessThanOrEqual", this.submittedAtLessThanOrEqual);
        kparams.add("entryIdEqual", this.entryIdEqual);
        kparams.add("entryIdIn", this.entryIdIn);
        kparams.add("distributionProfileIdEqual", this.distributionProfileIdEqual);
        kparams.add("distributionProfileIdIn", this.distributionProfileIdIn);
        kparams.add("statusEqual", this.statusEqual);
        kparams.add("statusIn", this.statusIn);
        kparams.add("dirtyStatusEqual", this.dirtyStatusEqual);
        kparams.add("dirtyStatusIn", this.dirtyStatusIn);
        kparams.add("sunriseGreaterThanOrEqual", this.sunriseGreaterThanOrEqual);
        kparams.add("sunriseLessThanOrEqual", this.sunriseLessThanOrEqual);
        kparams.add("sunsetGreaterThanOrEqual", this.sunsetGreaterThanOrEqual);
        kparams.add("sunsetLessThanOrEqual", this.sunsetLessThanOrEqual);
        return kparams;
    }

}

