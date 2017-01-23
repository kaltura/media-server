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
import com.kaltura.client.enums.KalturaCategoryUserPermissionLevel;
import com.kaltura.client.enums.KalturaCategoryUserStatus;
import com.kaltura.client.enums.KalturaUpdateMethodType;
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
public abstract class KalturaCategoryUserBaseFilter extends KalturaRelatedFilter {
    public int categoryIdEqual = Integer.MIN_VALUE;
    public String categoryIdIn;
    public String userIdEqual;
    public String userIdIn;
    public KalturaCategoryUserPermissionLevel permissionLevelEqual;
    public String permissionLevelIn;
    public KalturaCategoryUserStatus statusEqual;
    public String statusIn;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtLessThanOrEqual = Integer.MIN_VALUE;
    public KalturaUpdateMethodType updateMethodEqual;
    public String updateMethodIn;
    public String categoryFullIdsStartsWith;
    public String categoryFullIdsEqual;
    public String permissionNamesMatchAnd;
    public String permissionNamesMatchOr;
    public String permissionNamesNotContains;

    public KalturaCategoryUserBaseFilter() {
    }

    public KalturaCategoryUserBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("categoryIdEqual")) {
                this.categoryIdEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("categoryIdIn")) {
                this.categoryIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("userIdEqual")) {
                this.userIdEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("userIdIn")) {
                this.userIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("permissionLevelEqual")) {
                this.permissionLevelEqual = KalturaCategoryUserPermissionLevel.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("permissionLevelIn")) {
                this.permissionLevelIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("statusEqual")) {
                this.statusEqual = KalturaCategoryUserStatus.get(ParseUtils.parseInt(txt));
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
            } else if (nodeName.equals("updatedAtGreaterThanOrEqual")) {
                this.updatedAtGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAtLessThanOrEqual")) {
                this.updatedAtLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updateMethodEqual")) {
                this.updateMethodEqual = KalturaUpdateMethodType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("updateMethodIn")) {
                this.updateMethodIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("categoryFullIdsStartsWith")) {
                this.categoryFullIdsStartsWith = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("categoryFullIdsEqual")) {
                this.categoryFullIdsEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("permissionNamesMatchAnd")) {
                this.permissionNamesMatchAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("permissionNamesMatchOr")) {
                this.permissionNamesMatchOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("permissionNamesNotContains")) {
                this.permissionNamesNotContains = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaCategoryUserBaseFilter");
        kparams.add("categoryIdEqual", this.categoryIdEqual);
        kparams.add("categoryIdIn", this.categoryIdIn);
        kparams.add("userIdEqual", this.userIdEqual);
        kparams.add("userIdIn", this.userIdIn);
        kparams.add("permissionLevelEqual", this.permissionLevelEqual);
        kparams.add("permissionLevelIn", this.permissionLevelIn);
        kparams.add("statusEqual", this.statusEqual);
        kparams.add("statusIn", this.statusIn);
        kparams.add("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.add("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.add("updatedAtGreaterThanOrEqual", this.updatedAtGreaterThanOrEqual);
        kparams.add("updatedAtLessThanOrEqual", this.updatedAtLessThanOrEqual);
        kparams.add("updateMethodEqual", this.updateMethodEqual);
        kparams.add("updateMethodIn", this.updateMethodIn);
        kparams.add("categoryFullIdsStartsWith", this.categoryFullIdsStartsWith);
        kparams.add("categoryFullIdsEqual", this.categoryFullIdsEqual);
        kparams.add("permissionNamesMatchAnd", this.permissionNamesMatchAnd);
        kparams.add("permissionNamesMatchOr", this.permissionNamesMatchOr);
        kparams.add("permissionNamesNotContains", this.permissionNamesNotContains);
        return kparams;
    }

}

