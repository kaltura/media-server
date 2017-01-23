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
import com.kaltura.client.enums.KalturaDropFolderType;
import com.kaltura.client.enums.KalturaDropFolderStatus;
import com.kaltura.client.enums.KalturaDropFolderFileDeletePolicy;
import com.kaltura.client.enums.KalturaDropFolderFileHandlerType;
import com.kaltura.client.enums.KalturaDropFolderErrorCode;
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
public class KalturaDropFolder extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public String name;
    public String description;
    public KalturaDropFolderType type;
    public KalturaDropFolderStatus status;
    public int conversionProfileId = Integer.MIN_VALUE;
    public int dc = Integer.MIN_VALUE;
    public String path;
	/**  The ammount of time, in seconds, that should pass so that a file with no change
	  in size we'll be treated as "finished uploading to folder"  */
    public int fileSizeCheckInterval = Integer.MIN_VALUE;
    public KalturaDropFolderFileDeletePolicy fileDeletePolicy;
    public int autoFileDeleteDays = Integer.MIN_VALUE;
    public KalturaDropFolderFileHandlerType fileHandlerType;
    public String fileNamePatterns;
    public KalturaDropFolderFileHandlerConfig fileHandlerConfig;
    public String tags;
    public KalturaDropFolderErrorCode errorCode;
    public String errorDescription;
    public String ignoreFileNamePatterns;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;
    public int lastAccessedAt = Integer.MIN_VALUE;
    public boolean incremental;
    public int lastFileTimestamp = Integer.MIN_VALUE;
    public int metadataProfileId = Integer.MIN_VALUE;
    public String categoriesMetadataFieldName;
    public boolean enforceEntitlement;
    public boolean shouldValidateKS;

    public KalturaDropFolder() {
    }

    public KalturaDropFolder(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerId")) {
                this.partnerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("name")) {
                this.name = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("description")) {
                this.description = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("type")) {
                this.type = KalturaDropFolderType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaDropFolderStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("conversionProfileId")) {
                this.conversionProfileId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("dc")) {
                this.dc = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("path")) {
                this.path = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("fileSizeCheckInterval")) {
                this.fileSizeCheckInterval = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("fileDeletePolicy")) {
                this.fileDeletePolicy = KalturaDropFolderFileDeletePolicy.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("autoFileDeleteDays")) {
                this.autoFileDeleteDays = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("fileHandlerType")) {
                this.fileHandlerType = KalturaDropFolderFileHandlerType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("fileNamePatterns")) {
                this.fileNamePatterns = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("fileHandlerConfig")) {
                this.fileHandlerConfig = ParseUtils.parseObject(KalturaDropFolderFileHandlerConfig.class, aNode);
                continue;
            } else if (nodeName.equals("tags")) {
                this.tags = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("errorCode")) {
                this.errorCode = KalturaDropFolderErrorCode.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("errorDescription")) {
                this.errorDescription = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("ignoreFileNamePatterns")) {
                this.ignoreFileNamePatterns = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAt")) {
                this.updatedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("lastAccessedAt")) {
                this.lastAccessedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("incremental")) {
                this.incremental = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("lastFileTimestamp")) {
                this.lastFileTimestamp = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("metadataProfileId")) {
                this.metadataProfileId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("categoriesMetadataFieldName")) {
                this.categoriesMetadataFieldName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("enforceEntitlement")) {
                this.enforceEntitlement = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("shouldValidateKS")) {
                this.shouldValidateKS = ParseUtils.parseBool(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaDropFolder");
        kparams.add("partnerId", this.partnerId);
        kparams.add("name", this.name);
        kparams.add("description", this.description);
        kparams.add("type", this.type);
        kparams.add("status", this.status);
        kparams.add("conversionProfileId", this.conversionProfileId);
        kparams.add("dc", this.dc);
        kparams.add("path", this.path);
        kparams.add("fileSizeCheckInterval", this.fileSizeCheckInterval);
        kparams.add("fileDeletePolicy", this.fileDeletePolicy);
        kparams.add("autoFileDeleteDays", this.autoFileDeleteDays);
        kparams.add("fileHandlerType", this.fileHandlerType);
        kparams.add("fileNamePatterns", this.fileNamePatterns);
        kparams.add("fileHandlerConfig", this.fileHandlerConfig);
        kparams.add("tags", this.tags);
        kparams.add("errorCode", this.errorCode);
        kparams.add("errorDescription", this.errorDescription);
        kparams.add("ignoreFileNamePatterns", this.ignoreFileNamePatterns);
        kparams.add("lastAccessedAt", this.lastAccessedAt);
        kparams.add("incremental", this.incremental);
        kparams.add("lastFileTimestamp", this.lastFileTimestamp);
        kparams.add("metadataProfileId", this.metadataProfileId);
        kparams.add("categoriesMetadataFieldName", this.categoriesMetadataFieldName);
        kparams.add("enforceEntitlement", this.enforceEntitlement);
        kparams.add("shouldValidateKS", this.shouldValidateKS);
        return kparams;
    }

}

