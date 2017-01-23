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
import com.kaltura.client.enums.KalturaEntryModerationStatus;
import com.kaltura.client.enums.KalturaEmailIngestionProfileStatus;
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
public class KalturaEmailIngestionProfile extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public String name;
    public String description;
    public String emailAddress;
    public String mailboxId;
    public int partnerId = Integer.MIN_VALUE;
    public int conversionProfile2Id = Integer.MIN_VALUE;
    public KalturaEntryModerationStatus moderationStatus;
    public KalturaEmailIngestionProfileStatus status;
    public String createdAt;
    public String defaultCategory;
    public String defaultUserId;
    public String defaultTags;
    public String defaultAdminTags;
    public int maxAttachmentSizeKbytes = Integer.MIN_VALUE;
    public int maxAttachmentsPerMail = Integer.MIN_VALUE;

    public KalturaEmailIngestionProfile() {
    }

    public KalturaEmailIngestionProfile(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("name")) {
                this.name = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("description")) {
                this.description = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("emailAddress")) {
                this.emailAddress = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("mailboxId")) {
                this.mailboxId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerId")) {
                this.partnerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("conversionProfile2Id")) {
                this.conversionProfile2Id = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("moderationStatus")) {
                this.moderationStatus = KalturaEntryModerationStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaEmailIngestionProfileStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("defaultCategory")) {
                this.defaultCategory = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("defaultUserId")) {
                this.defaultUserId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("defaultTags")) {
                this.defaultTags = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("defaultAdminTags")) {
                this.defaultAdminTags = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("maxAttachmentSizeKbytes")) {
                this.maxAttachmentSizeKbytes = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("maxAttachmentsPerMail")) {
                this.maxAttachmentsPerMail = ParseUtils.parseInt(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaEmailIngestionProfile");
        kparams.add("name", this.name);
        kparams.add("description", this.description);
        kparams.add("emailAddress", this.emailAddress);
        kparams.add("mailboxId", this.mailboxId);
        kparams.add("conversionProfile2Id", this.conversionProfile2Id);
        kparams.add("moderationStatus", this.moderationStatus);
        kparams.add("defaultCategory", this.defaultCategory);
        kparams.add("defaultUserId", this.defaultUserId);
        kparams.add("defaultTags", this.defaultTags);
        kparams.add("defaultAdminTags", this.defaultAdminTags);
        kparams.add("maxAttachmentSizeKbytes", this.maxAttachmentSizeKbytes);
        kparams.add("maxAttachmentsPerMail", this.maxAttachmentsPerMail);
        return kparams;
    }

}

