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
public class KalturaScheduleEventFilter extends KalturaScheduleEventBaseFilter {
    public String resourceIdsLike;
    public String resourceIdsMultiLikeOr;
    public String resourceIdsMultiLikeAnd;
    public String parentResourceIdsLike;
    public String parentResourceIdsMultiLikeOr;
    public String parentResourceIdsMultiLikeAnd;
    public String templateEntryCategoriesIdsMultiLikeAnd;
    public String templateEntryCategoriesIdsMultiLikeOr;
    public String resourceSystemNamesMultiLikeOr;
    public String templateEntryCategoriesIdsLike;
    public String resourceSystemNamesMultiLikeAnd;
    public String resourceSystemNamesLike;

    public KalturaScheduleEventFilter() {
    }

    public KalturaScheduleEventFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("resourceIdsLike")) {
                this.resourceIdsLike = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("resourceIdsMultiLikeOr")) {
                this.resourceIdsMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("resourceIdsMultiLikeAnd")) {
                this.resourceIdsMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("parentResourceIdsLike")) {
                this.parentResourceIdsLike = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("parentResourceIdsMultiLikeOr")) {
                this.parentResourceIdsMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("parentResourceIdsMultiLikeAnd")) {
                this.parentResourceIdsMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("templateEntryCategoriesIdsMultiLikeAnd")) {
                this.templateEntryCategoriesIdsMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("templateEntryCategoriesIdsMultiLikeOr")) {
                this.templateEntryCategoriesIdsMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("resourceSystemNamesMultiLikeOr")) {
                this.resourceSystemNamesMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("templateEntryCategoriesIdsLike")) {
                this.templateEntryCategoriesIdsLike = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("resourceSystemNamesMultiLikeAnd")) {
                this.resourceSystemNamesMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("resourceSystemNamesLike")) {
                this.resourceSystemNamesLike = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaScheduleEventFilter");
        kparams.add("resourceIdsLike", this.resourceIdsLike);
        kparams.add("resourceIdsMultiLikeOr", this.resourceIdsMultiLikeOr);
        kparams.add("resourceIdsMultiLikeAnd", this.resourceIdsMultiLikeAnd);
        kparams.add("parentResourceIdsLike", this.parentResourceIdsLike);
        kparams.add("parentResourceIdsMultiLikeOr", this.parentResourceIdsMultiLikeOr);
        kparams.add("parentResourceIdsMultiLikeAnd", this.parentResourceIdsMultiLikeAnd);
        kparams.add("templateEntryCategoriesIdsMultiLikeAnd", this.templateEntryCategoriesIdsMultiLikeAnd);
        kparams.add("templateEntryCategoriesIdsMultiLikeOr", this.templateEntryCategoriesIdsMultiLikeOr);
        kparams.add("resourceSystemNamesMultiLikeOr", this.resourceSystemNamesMultiLikeOr);
        kparams.add("templateEntryCategoriesIdsLike", this.templateEntryCategoriesIdsLike);
        kparams.add("resourceSystemNamesMultiLikeAnd", this.resourceSystemNamesMultiLikeAnd);
        kparams.add("resourceSystemNamesLike", this.resourceSystemNamesLike);
        return kparams;
    }

}

