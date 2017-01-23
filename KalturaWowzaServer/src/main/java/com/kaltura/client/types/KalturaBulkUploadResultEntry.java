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
public class KalturaBulkUploadResultEntry extends KalturaBulkUploadResult {
    public String entryId;
    public String title;
    public String description;
    public String tags;
    public String url;
    public String contentType;
    public int conversionProfileId = Integer.MIN_VALUE;
    public int accessControlProfileId = Integer.MIN_VALUE;
    public String category;
    public int scheduleStartDate = Integer.MIN_VALUE;
    public int scheduleEndDate = Integer.MIN_VALUE;
    public int entryStatus = Integer.MIN_VALUE;
    public String thumbnailUrl;
    public boolean thumbnailSaved;
    public String sshPrivateKey;
    public String sshPublicKey;
    public String sshKeyPassphrase;
    public String creatorId;
    public String entitledUsersEdit;
    public String entitledUsersPublish;
    public String ownerId;
    public String referenceId;
    public String templateEntryId;

    public KalturaBulkUploadResultEntry() {
    }

    public KalturaBulkUploadResultEntry(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("entryId")) {
                this.entryId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("title")) {
                this.title = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("description")) {
                this.description = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tags")) {
                this.tags = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("url")) {
                this.url = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("contentType")) {
                this.contentType = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("conversionProfileId")) {
                this.conversionProfileId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("accessControlProfileId")) {
                this.accessControlProfileId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("category")) {
                this.category = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("scheduleStartDate")) {
                this.scheduleStartDate = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("scheduleEndDate")) {
                this.scheduleEndDate = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("entryStatus")) {
                this.entryStatus = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("thumbnailUrl")) {
                this.thumbnailUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("thumbnailSaved")) {
                this.thumbnailSaved = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("sshPrivateKey")) {
                this.sshPrivateKey = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sshPublicKey")) {
                this.sshPublicKey = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sshKeyPassphrase")) {
                this.sshKeyPassphrase = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("creatorId")) {
                this.creatorId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("entitledUsersEdit")) {
                this.entitledUsersEdit = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("entitledUsersPublish")) {
                this.entitledUsersPublish = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("ownerId")) {
                this.ownerId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("referenceId")) {
                this.referenceId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("templateEntryId")) {
                this.templateEntryId = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaBulkUploadResultEntry");
        kparams.add("entryId", this.entryId);
        kparams.add("title", this.title);
        kparams.add("description", this.description);
        kparams.add("tags", this.tags);
        kparams.add("url", this.url);
        kparams.add("contentType", this.contentType);
        kparams.add("conversionProfileId", this.conversionProfileId);
        kparams.add("accessControlProfileId", this.accessControlProfileId);
        kparams.add("category", this.category);
        kparams.add("scheduleStartDate", this.scheduleStartDate);
        kparams.add("scheduleEndDate", this.scheduleEndDate);
        kparams.add("entryStatus", this.entryStatus);
        kparams.add("thumbnailUrl", this.thumbnailUrl);
        kparams.add("thumbnailSaved", this.thumbnailSaved);
        kparams.add("sshPrivateKey", this.sshPrivateKey);
        kparams.add("sshPublicKey", this.sshPublicKey);
        kparams.add("sshKeyPassphrase", this.sshKeyPassphrase);
        kparams.add("creatorId", this.creatorId);
        kparams.add("entitledUsersEdit", this.entitledUsersEdit);
        kparams.add("entitledUsersPublish", this.entitledUsersPublish);
        kparams.add("ownerId", this.ownerId);
        kparams.add("referenceId", this.referenceId);
        kparams.add("templateEntryId", this.templateEntryId);
        return kparams;
    }

}

