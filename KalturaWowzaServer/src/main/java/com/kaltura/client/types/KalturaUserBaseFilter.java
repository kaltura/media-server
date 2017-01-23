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
import com.kaltura.client.enums.KalturaUserType;
import com.kaltura.client.enums.KalturaUserStatus;
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
public abstract class KalturaUserBaseFilter extends KalturaRelatedFilter {
    public int partnerIdEqual = Integer.MIN_VALUE;
    public KalturaUserType typeEqual;
    public String typeIn;
    public String screenNameLike;
    public String screenNameStartsWith;
    public String emailLike;
    public String emailStartsWith;
    public String tagsMultiLikeOr;
    public String tagsMultiLikeAnd;
    public KalturaUserStatus statusEqual;
    public String statusIn;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public String firstNameStartsWith;
    public String lastNameStartsWith;
    public KalturaNullableBoolean isAdminEqual;

    public KalturaUserBaseFilter() {
    }

    public KalturaUserBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("partnerIdEqual")) {
                this.partnerIdEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("typeEqual")) {
                this.typeEqual = KalturaUserType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("typeIn")) {
                this.typeIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("screenNameLike")) {
                this.screenNameLike = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("screenNameStartsWith")) {
                this.screenNameStartsWith = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("emailLike")) {
                this.emailLike = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("emailStartsWith")) {
                this.emailStartsWith = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tagsMultiLikeOr")) {
                this.tagsMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tagsMultiLikeAnd")) {
                this.tagsMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("statusEqual")) {
                this.statusEqual = KalturaUserStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("createdAtGreaterThanOrEqual")) {
                this.createdAtGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("createdAtLessThanOrEqual")) {
                this.createdAtLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("firstNameStartsWith")) {
                this.firstNameStartsWith = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("lastNameStartsWith")) {
                this.lastNameStartsWith = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("isAdminEqual")) {
                this.isAdminEqual = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaUserBaseFilter");
        kparams.add("partnerIdEqual", this.partnerIdEqual);
        kparams.add("typeEqual", this.typeEqual);
        kparams.add("typeIn", this.typeIn);
        kparams.add("screenNameLike", this.screenNameLike);
        kparams.add("screenNameStartsWith", this.screenNameStartsWith);
        kparams.add("emailLike", this.emailLike);
        kparams.add("emailStartsWith", this.emailStartsWith);
        kparams.add("tagsMultiLikeOr", this.tagsMultiLikeOr);
        kparams.add("tagsMultiLikeAnd", this.tagsMultiLikeAnd);
        kparams.add("statusEqual", this.statusEqual);
        kparams.add("statusIn", this.statusIn);
        kparams.add("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.add("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.add("firstNameStartsWith", this.firstNameStartsWith);
        kparams.add("lastNameStartsWith", this.lastNameStartsWith);
        kparams.add("isAdminEqual", this.isAdminEqual);
        return kparams;
    }

}

