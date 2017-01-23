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
import com.kaltura.client.enums.KalturaScheduleEventStatus;
import com.kaltura.client.enums.KalturaScheduleEventRecurrenceType;
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
public abstract class KalturaScheduleEventBaseFilter extends KalturaRelatedFilter {
    public int idEqual = Integer.MIN_VALUE;
    public String idIn;
    public String idNotIn;
    public int parentIdEqual = Integer.MIN_VALUE;
    public String parentIdIn;
    public String parentIdNotIn;
    public KalturaScheduleEventStatus statusEqual;
    public String statusIn;
    public int startDateGreaterThanOrEqual = Integer.MIN_VALUE;
    public int startDateLessThanOrEqual = Integer.MIN_VALUE;
    public int endDateGreaterThanOrEqual = Integer.MIN_VALUE;
    public int endDateLessThanOrEqual = Integer.MIN_VALUE;
    public String referenceIdEqual;
    public String referenceIdIn;
    public String ownerIdEqual;
    public String ownerIdIn;
    public int priorityEqual = Integer.MIN_VALUE;
    public String priorityIn;
    public int priorityGreaterThanOrEqual = Integer.MIN_VALUE;
    public int priorityLessThanOrEqual = Integer.MIN_VALUE;
    public KalturaScheduleEventRecurrenceType recurrenceTypeEqual;
    public String recurrenceTypeIn;
    public String tagsLike;
    public String tagsMultiLikeOr;
    public String tagsMultiLikeAnd;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtLessThanOrEqual = Integer.MIN_VALUE;

    public KalturaScheduleEventBaseFilter() {
    }

    public KalturaScheduleEventBaseFilter(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("idNotIn")) {
                this.idNotIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("parentIdEqual")) {
                this.parentIdEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("parentIdIn")) {
                this.parentIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("parentIdNotIn")) {
                this.parentIdNotIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("statusEqual")) {
                this.statusEqual = KalturaScheduleEventStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("startDateGreaterThanOrEqual")) {
                this.startDateGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("startDateLessThanOrEqual")) {
                this.startDateLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("endDateGreaterThanOrEqual")) {
                this.endDateGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("endDateLessThanOrEqual")) {
                this.endDateLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("referenceIdEqual")) {
                this.referenceIdEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("referenceIdIn")) {
                this.referenceIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("ownerIdEqual")) {
                this.ownerIdEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("ownerIdIn")) {
                this.ownerIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("priorityEqual")) {
                this.priorityEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("priorityIn")) {
                this.priorityIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("priorityGreaterThanOrEqual")) {
                this.priorityGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("priorityLessThanOrEqual")) {
                this.priorityLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("recurrenceTypeEqual")) {
                this.recurrenceTypeEqual = KalturaScheduleEventRecurrenceType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("recurrenceTypeIn")) {
                this.recurrenceTypeIn = ParseUtils.parseString(txt);
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
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaScheduleEventBaseFilter");
        kparams.add("idEqual", this.idEqual);
        kparams.add("idIn", this.idIn);
        kparams.add("idNotIn", this.idNotIn);
        kparams.add("parentIdEqual", this.parentIdEqual);
        kparams.add("parentIdIn", this.parentIdIn);
        kparams.add("parentIdNotIn", this.parentIdNotIn);
        kparams.add("statusEqual", this.statusEqual);
        kparams.add("statusIn", this.statusIn);
        kparams.add("startDateGreaterThanOrEqual", this.startDateGreaterThanOrEqual);
        kparams.add("startDateLessThanOrEqual", this.startDateLessThanOrEqual);
        kparams.add("endDateGreaterThanOrEqual", this.endDateGreaterThanOrEqual);
        kparams.add("endDateLessThanOrEqual", this.endDateLessThanOrEqual);
        kparams.add("referenceIdEqual", this.referenceIdEqual);
        kparams.add("referenceIdIn", this.referenceIdIn);
        kparams.add("ownerIdEqual", this.ownerIdEqual);
        kparams.add("ownerIdIn", this.ownerIdIn);
        kparams.add("priorityEqual", this.priorityEqual);
        kparams.add("priorityIn", this.priorityIn);
        kparams.add("priorityGreaterThanOrEqual", this.priorityGreaterThanOrEqual);
        kparams.add("priorityLessThanOrEqual", this.priorityLessThanOrEqual);
        kparams.add("recurrenceTypeEqual", this.recurrenceTypeEqual);
        kparams.add("recurrenceTypeIn", this.recurrenceTypeIn);
        kparams.add("tagsLike", this.tagsLike);
        kparams.add("tagsMultiLikeOr", this.tagsMultiLikeOr);
        kparams.add("tagsMultiLikeAnd", this.tagsMultiLikeAnd);
        kparams.add("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.add("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.add("updatedAtGreaterThanOrEqual", this.updatedAtGreaterThanOrEqual);
        kparams.add("updatedAtLessThanOrEqual", this.updatedAtLessThanOrEqual);
        return kparams;
    }

}

