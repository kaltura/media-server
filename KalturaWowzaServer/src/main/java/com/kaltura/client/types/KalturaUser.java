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
import com.kaltura.client.enums.KalturaUserType;
import com.kaltura.client.enums.KalturaGender;
import com.kaltura.client.enums.KalturaUserStatus;
import com.kaltura.client.enums.KalturaLanguageCode;
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
public class KalturaUser extends KalturaObjectBase {
    public String id;
    public int partnerId = Integer.MIN_VALUE;
    public KalturaUserType type;
    public String screenName;
    public String fullName;
    public String email;
    public int dateOfBirth = Integer.MIN_VALUE;
    public String country;
    public String state;
    public String city;
    public String zip;
    public String thumbnailUrl;
    public String description;
    public String tags;
	/**  Admin tags can be updated only by using an admin session  */
    public String adminTags;
    public KalturaGender gender;
    public KalturaUserStatus status;
	/**  Creation date as Unix timestamp (In seconds)  */
    public int createdAt = Integer.MIN_VALUE;
	/**  Last update date as Unix timestamp (In seconds)  */
    public int updatedAt = Integer.MIN_VALUE;
	/**  Can be used to store various partner related data as a string  */
    public String partnerData;
    public int indexedPartnerDataInt = Integer.MIN_VALUE;
    public String indexedPartnerDataString;
    public int storageSize = Integer.MIN_VALUE;
    public String password;
    public String firstName;
    public String lastName;
    public boolean isAdmin;
    public KalturaLanguageCode language;
    public int lastLoginTime = Integer.MIN_VALUE;
    public int statusUpdatedAt = Integer.MIN_VALUE;
    public int deletedAt = Integer.MIN_VALUE;
    public boolean loginEnabled;
    public String roleIds;
    public String roleNames;
    public boolean isAccountOwner;
    public String allowedPartnerIds;
    public String allowedPartnerPackages;

    public KalturaUser() {
    }

    public KalturaUser(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerId")) {
                this.partnerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("type")) {
                this.type = KalturaUserType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("screenName")) {
                this.screenName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("fullName")) {
                this.fullName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("email")) {
                this.email = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("dateOfBirth")) {
                this.dateOfBirth = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("country")) {
                this.country = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("state")) {
                this.state = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("city")) {
                this.city = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("zip")) {
                this.zip = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("thumbnailUrl")) {
                this.thumbnailUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("description")) {
                this.description = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tags")) {
                this.tags = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("adminTags")) {
                this.adminTags = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("gender")) {
                this.gender = KalturaGender.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaUserStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAt")) {
                this.updatedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerData")) {
                this.partnerData = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("indexedPartnerDataInt")) {
                this.indexedPartnerDataInt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("indexedPartnerDataString")) {
                this.indexedPartnerDataString = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("storageSize")) {
                this.storageSize = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("password")) {
                this.password = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("firstName")) {
                this.firstName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("lastName")) {
                this.lastName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("isAdmin")) {
                this.isAdmin = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("language")) {
                this.language = KalturaLanguageCode.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("lastLoginTime")) {
                this.lastLoginTime = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("statusUpdatedAt")) {
                this.statusUpdatedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("deletedAt")) {
                this.deletedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("loginEnabled")) {
                this.loginEnabled = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("roleIds")) {
                this.roleIds = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("roleNames")) {
                this.roleNames = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("isAccountOwner")) {
                this.isAccountOwner = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("allowedPartnerIds")) {
                this.allowedPartnerIds = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("allowedPartnerPackages")) {
                this.allowedPartnerPackages = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaUser");
        kparams.add("id", this.id);
        kparams.add("type", this.type);
        kparams.add("screenName", this.screenName);
        kparams.add("fullName", this.fullName);
        kparams.add("email", this.email);
        kparams.add("dateOfBirth", this.dateOfBirth);
        kparams.add("country", this.country);
        kparams.add("state", this.state);
        kparams.add("city", this.city);
        kparams.add("zip", this.zip);
        kparams.add("thumbnailUrl", this.thumbnailUrl);
        kparams.add("description", this.description);
        kparams.add("tags", this.tags);
        kparams.add("adminTags", this.adminTags);
        kparams.add("gender", this.gender);
        kparams.add("status", this.status);
        kparams.add("partnerData", this.partnerData);
        kparams.add("indexedPartnerDataInt", this.indexedPartnerDataInt);
        kparams.add("indexedPartnerDataString", this.indexedPartnerDataString);
        kparams.add("password", this.password);
        kparams.add("firstName", this.firstName);
        kparams.add("lastName", this.lastName);
        kparams.add("isAdmin", this.isAdmin);
        kparams.add("language", this.language);
        kparams.add("roleIds", this.roleIds);
        kparams.add("allowedPartnerIds", this.allowedPartnerIds);
        kparams.add("allowedPartnerPackages", this.allowedPartnerPackages);
        return kparams;
    }

}

