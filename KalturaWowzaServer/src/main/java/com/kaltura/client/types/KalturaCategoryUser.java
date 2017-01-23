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
public class KalturaCategoryUser extends KalturaObjectBase {
    public int categoryId = Integer.MIN_VALUE;
	/**  User id  */
    public String userId;
	/**  Partner id  */
    public int partnerId = Integer.MIN_VALUE;
	/**  Permission level  */
    public KalturaCategoryUserPermissionLevel permissionLevel;
	/**  Status  */
    public KalturaCategoryUserStatus status;
	/**  CategoryUser creation date as Unix timestamp (In seconds)  */
    public int createdAt = Integer.MIN_VALUE;
	/**  CategoryUser update date as Unix timestamp (In seconds)  */
    public int updatedAt = Integer.MIN_VALUE;
	/**  Update method can be either manual or automatic to distinguish between manual
	  operations (for example in KMC) on automatic - using bulk upload  */
    public KalturaUpdateMethodType updateMethod;
	/**  The full ids of the Category  */
    public String categoryFullIds;
	/**  Set of category-related permissions for the current category user.  */
    public String permissionNames;

    public KalturaCategoryUser() {
    }

    public KalturaCategoryUser(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("categoryId")) {
                this.categoryId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("userId")) {
                this.userId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerId")) {
                this.partnerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("permissionLevel")) {
                this.permissionLevel = KalturaCategoryUserPermissionLevel.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaCategoryUserStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAt")) {
                this.updatedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updateMethod")) {
                this.updateMethod = KalturaUpdateMethodType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("categoryFullIds")) {
                this.categoryFullIds = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("permissionNames")) {
                this.permissionNames = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaCategoryUser");
        kparams.add("categoryId", this.categoryId);
        kparams.add("userId", this.userId);
        kparams.add("permissionLevel", this.permissionLevel);
        kparams.add("updateMethod", this.updateMethod);
        kparams.add("permissionNames", this.permissionNames);
        return kparams;
    }

}

