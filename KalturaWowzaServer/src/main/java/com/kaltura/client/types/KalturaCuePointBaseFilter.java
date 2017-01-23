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
import com.kaltura.client.enums.KalturaCuePointType;
import com.kaltura.client.enums.KalturaCuePointStatus;
import com.kaltura.client.enums.KalturaNullableBoolean;
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
public abstract class KalturaCuePointBaseFilter extends KalturaRelatedFilter {
    public String idEqual;
    public String idIn;
    public KalturaCuePointType cuePointTypeEqual;
    public String cuePointTypeIn;
    public KalturaCuePointStatus statusEqual;
    public String statusIn;
    public String entryIdEqual;
    public String entryIdIn;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtLessThanOrEqual = Integer.MIN_VALUE;
    public int triggeredAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int triggeredAtLessThanOrEqual = Integer.MIN_VALUE;
    public String tagsLike;
    public String tagsMultiLikeOr;
    public String tagsMultiLikeAnd;
    public int startTimeGreaterThanOrEqual = Integer.MIN_VALUE;
    public int startTimeLessThanOrEqual = Integer.MIN_VALUE;
    public String userIdEqual;
    public String userIdIn;
    public int partnerSortValueEqual = Integer.MIN_VALUE;
    public String partnerSortValueIn;
    public int partnerSortValueGreaterThanOrEqual = Integer.MIN_VALUE;
    public int partnerSortValueLessThanOrEqual = Integer.MIN_VALUE;
    public KalturaNullableBoolean forceStopEqual;
    public String systemNameEqual;
    public String systemNameIn;

    public KalturaCuePointBaseFilter() {
    }

    public KalturaCuePointBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("idEqual")) {
                this.idEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("idIn")) {
                this.idIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("cuePointTypeEqual")) {
                this.cuePointTypeEqual = KalturaCuePointType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("cuePointTypeIn")) {
                this.cuePointTypeIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("statusEqual")) {
                this.statusEqual = KalturaCuePointStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("entryIdEqual")) {
                this.entryIdEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("entryIdIn")) {
                this.entryIdIn = ParseUtils.parseString(txt);
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
            } else if (nodeName.equals("triggeredAtGreaterThanOrEqual")) {
                this.triggeredAtGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("triggeredAtLessThanOrEqual")) {
                this.triggeredAtLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("tagsLike")) {
                this.tagsLike = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tagsMultiLikeOr")) {
                this.tagsMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tagsMultiLikeAnd")) {
                this.tagsMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("startTimeGreaterThanOrEqual")) {
                this.startTimeGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("startTimeLessThanOrEqual")) {
                this.startTimeLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("userIdEqual")) {
                this.userIdEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("userIdIn")) {
                this.userIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerSortValueEqual")) {
                this.partnerSortValueEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerSortValueIn")) {
                this.partnerSortValueIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerSortValueGreaterThanOrEqual")) {
                this.partnerSortValueGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerSortValueLessThanOrEqual")) {
                this.partnerSortValueLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("forceStopEqual")) {
                this.forceStopEqual = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("systemNameEqual")) {
                this.systemNameEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("systemNameIn")) {
                this.systemNameIn = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaCuePointBaseFilter");
        kparams.add("idEqual", this.idEqual);
        kparams.add("idIn", this.idIn);
        kparams.add("cuePointTypeEqual", this.cuePointTypeEqual);
        kparams.add("cuePointTypeIn", this.cuePointTypeIn);
        kparams.add("statusEqual", this.statusEqual);
        kparams.add("statusIn", this.statusIn);
        kparams.add("entryIdEqual", this.entryIdEqual);
        kparams.add("entryIdIn", this.entryIdIn);
        kparams.add("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.add("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.add("updatedAtGreaterThanOrEqual", this.updatedAtGreaterThanOrEqual);
        kparams.add("updatedAtLessThanOrEqual", this.updatedAtLessThanOrEqual);
        kparams.add("triggeredAtGreaterThanOrEqual", this.triggeredAtGreaterThanOrEqual);
        kparams.add("triggeredAtLessThanOrEqual", this.triggeredAtLessThanOrEqual);
        kparams.add("tagsLike", this.tagsLike);
        kparams.add("tagsMultiLikeOr", this.tagsMultiLikeOr);
        kparams.add("tagsMultiLikeAnd", this.tagsMultiLikeAnd);
        kparams.add("startTimeGreaterThanOrEqual", this.startTimeGreaterThanOrEqual);
        kparams.add("startTimeLessThanOrEqual", this.startTimeLessThanOrEqual);
        kparams.add("userIdEqual", this.userIdEqual);
        kparams.add("userIdIn", this.userIdIn);
        kparams.add("partnerSortValueEqual", this.partnerSortValueEqual);
        kparams.add("partnerSortValueIn", this.partnerSortValueIn);
        kparams.add("partnerSortValueGreaterThanOrEqual", this.partnerSortValueGreaterThanOrEqual);
        kparams.add("partnerSortValueLessThanOrEqual", this.partnerSortValueLessThanOrEqual);
        kparams.add("forceStopEqual", this.forceStopEqual);
        kparams.add("systemNameEqual", this.systemNameEqual);
        kparams.add("systemNameIn", this.systemNameIn);
        return kparams;
    }

}

