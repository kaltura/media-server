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
import com.kaltura.client.enums.KalturaUiConfObjType;
import com.kaltura.client.enums.KalturaUiConfCreationMode;
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
public abstract class KalturaUiConfBaseFilter extends KalturaFilter {
    public int idEqual = Integer.MIN_VALUE;
    public String idIn;
    public String nameLike;
    public int partnerIdEqual = Integer.MIN_VALUE;
    public String partnerIdIn;
    public KalturaUiConfObjType objTypeEqual;
    public String objTypeIn;
    public String tagsMultiLikeOr;
    public String tagsMultiLikeAnd;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtLessThanOrEqual = Integer.MIN_VALUE;
    public KalturaUiConfCreationMode creationModeEqual;
    public String creationModeIn;
    public String versionEqual;
    public String versionMultiLikeOr;
    public String versionMultiLikeAnd;
    public String partnerTagsMultiLikeOr;
    public String partnerTagsMultiLikeAnd;

    public KalturaUiConfBaseFilter() {
    }

    public KalturaUiConfBaseFilter(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("nameLike")) {
                this.nameLike = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerIdEqual")) {
                this.partnerIdEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerIdIn")) {
                this.partnerIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("objTypeEqual")) {
                this.objTypeEqual = KalturaUiConfObjType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("objTypeIn")) {
                this.objTypeIn = ParseUtils.parseString(txt);
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
            } else if (nodeName.equals("creationModeEqual")) {
                this.creationModeEqual = KalturaUiConfCreationMode.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("creationModeIn")) {
                this.creationModeIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("versionEqual")) {
                this.versionEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("versionMultiLikeOr")) {
                this.versionMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("versionMultiLikeAnd")) {
                this.versionMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerTagsMultiLikeOr")) {
                this.partnerTagsMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerTagsMultiLikeAnd")) {
                this.partnerTagsMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaUiConfBaseFilter");
        kparams.add("idEqual", this.idEqual);
        kparams.add("idIn", this.idIn);
        kparams.add("nameLike", this.nameLike);
        kparams.add("partnerIdEqual", this.partnerIdEqual);
        kparams.add("partnerIdIn", this.partnerIdIn);
        kparams.add("objTypeEqual", this.objTypeEqual);
        kparams.add("objTypeIn", this.objTypeIn);
        kparams.add("tagsMultiLikeOr", this.tagsMultiLikeOr);
        kparams.add("tagsMultiLikeAnd", this.tagsMultiLikeAnd);
        kparams.add("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.add("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.add("updatedAtGreaterThanOrEqual", this.updatedAtGreaterThanOrEqual);
        kparams.add("updatedAtLessThanOrEqual", this.updatedAtLessThanOrEqual);
        kparams.add("creationModeEqual", this.creationModeEqual);
        kparams.add("creationModeIn", this.creationModeIn);
        kparams.add("versionEqual", this.versionEqual);
        kparams.add("versionMultiLikeOr", this.versionMultiLikeOr);
        kparams.add("versionMultiLikeAnd", this.versionMultiLikeAnd);
        kparams.add("partnerTagsMultiLikeOr", this.partnerTagsMultiLikeOr);
        kparams.add("partnerTagsMultiLikeAnd", this.partnerTagsMultiLikeAnd);
        return kparams;
    }

}

