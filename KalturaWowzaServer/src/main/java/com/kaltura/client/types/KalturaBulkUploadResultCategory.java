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
public class KalturaBulkUploadResultCategory extends KalturaBulkUploadResult {
    public String relativePath;
    public String name;
    public String referenceId;
    public String description;
    public String tags;
    public int appearInList = Integer.MIN_VALUE;
    public int privacy = Integer.MIN_VALUE;
    public int inheritanceType = Integer.MIN_VALUE;
    public int userJoinPolicy = Integer.MIN_VALUE;
    public int defaultPermissionLevel = Integer.MIN_VALUE;
    public String owner;
    public int contributionPolicy = Integer.MIN_VALUE;
    public int partnerSortValue = Integer.MIN_VALUE;
    public boolean moderation;

    public KalturaBulkUploadResultCategory() {
    }

    public KalturaBulkUploadResultCategory(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("relativePath")) {
                this.relativePath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("name")) {
                this.name = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("referenceId")) {
                this.referenceId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("description")) {
                this.description = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tags")) {
                this.tags = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("appearInList")) {
                this.appearInList = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("privacy")) {
                this.privacy = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("inheritanceType")) {
                this.inheritanceType = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("userJoinPolicy")) {
                this.userJoinPolicy = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("defaultPermissionLevel")) {
                this.defaultPermissionLevel = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("owner")) {
                this.owner = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("contributionPolicy")) {
                this.contributionPolicy = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerSortValue")) {
                this.partnerSortValue = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("moderation")) {
                this.moderation = ParseUtils.parseBool(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaBulkUploadResultCategory");
        kparams.add("relativePath", this.relativePath);
        kparams.add("name", this.name);
        kparams.add("referenceId", this.referenceId);
        kparams.add("description", this.description);
        kparams.add("tags", this.tags);
        kparams.add("appearInList", this.appearInList);
        kparams.add("privacy", this.privacy);
        kparams.add("inheritanceType", this.inheritanceType);
        kparams.add("userJoinPolicy", this.userJoinPolicy);
        kparams.add("defaultPermissionLevel", this.defaultPermissionLevel);
        kparams.add("owner", this.owner);
        kparams.add("contributionPolicy", this.contributionPolicy);
        kparams.add("partnerSortValue", this.partnerSortValue);
        kparams.add("moderation", this.moderation);
        return kparams;
    }

}

