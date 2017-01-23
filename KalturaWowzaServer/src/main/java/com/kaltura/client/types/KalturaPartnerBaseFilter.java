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
import com.kaltura.client.enums.KalturaPartnerStatus;
import com.kaltura.client.enums.KalturaPartnerGroupType;
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
public abstract class KalturaPartnerBaseFilter extends KalturaFilter {
    public int idEqual = Integer.MIN_VALUE;
    public String idIn;
    public String idNotIn;
    public String nameLike;
    public String nameMultiLikeOr;
    public String nameMultiLikeAnd;
    public String nameEqual;
    public KalturaPartnerStatus statusEqual;
    public String statusIn;
    public int partnerPackageEqual = Integer.MIN_VALUE;
    public int partnerPackageGreaterThanOrEqual = Integer.MIN_VALUE;
    public int partnerPackageLessThanOrEqual = Integer.MIN_VALUE;
    public String partnerPackageIn;
    public KalturaPartnerGroupType partnerGroupTypeEqual;
    public String partnerNameDescriptionWebsiteAdminNameAdminEmailLike;

    public KalturaPartnerBaseFilter() {
    }

    public KalturaPartnerBaseFilter(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("nameLike")) {
                this.nameLike = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("nameMultiLikeOr")) {
                this.nameMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("nameMultiLikeAnd")) {
                this.nameMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("nameEqual")) {
                this.nameEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("statusEqual")) {
                this.statusEqual = KalturaPartnerStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerPackageEqual")) {
                this.partnerPackageEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerPackageGreaterThanOrEqual")) {
                this.partnerPackageGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerPackageLessThanOrEqual")) {
                this.partnerPackageLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerPackageIn")) {
                this.partnerPackageIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerGroupTypeEqual")) {
                this.partnerGroupTypeEqual = KalturaPartnerGroupType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("partnerNameDescriptionWebsiteAdminNameAdminEmailLike")) {
                this.partnerNameDescriptionWebsiteAdminNameAdminEmailLike = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaPartnerBaseFilter");
        kparams.add("idEqual", this.idEqual);
        kparams.add("idIn", this.idIn);
        kparams.add("idNotIn", this.idNotIn);
        kparams.add("nameLike", this.nameLike);
        kparams.add("nameMultiLikeOr", this.nameMultiLikeOr);
        kparams.add("nameMultiLikeAnd", this.nameMultiLikeAnd);
        kparams.add("nameEqual", this.nameEqual);
        kparams.add("statusEqual", this.statusEqual);
        kparams.add("statusIn", this.statusIn);
        kparams.add("partnerPackageEqual", this.partnerPackageEqual);
        kparams.add("partnerPackageGreaterThanOrEqual", this.partnerPackageGreaterThanOrEqual);
        kparams.add("partnerPackageLessThanOrEqual", this.partnerPackageLessThanOrEqual);
        kparams.add("partnerPackageIn", this.partnerPackageIn);
        kparams.add("partnerGroupTypeEqual", this.partnerGroupTypeEqual);
        kparams.add("partnerNameDescriptionWebsiteAdminNameAdminEmailLike", this.partnerNameDescriptionWebsiteAdminNameAdminEmailLike);
        return kparams;
    }

}

