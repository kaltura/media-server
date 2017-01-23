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
import com.kaltura.client.enums.KalturaFileSyncObjectType;
import com.kaltura.client.enums.KalturaFileSyncStatus;
import com.kaltura.client.enums.KalturaFileSyncType;
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
public class KalturaFileSync extends KalturaObjectBase {
    public long id = Long.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public KalturaFileSyncObjectType fileObjectType;
    public String objectId;
    public String version;
    public int objectSubType = Integer.MIN_VALUE;
    public String dc;
    public int original = Integer.MIN_VALUE;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;
    public int readyAt = Integer.MIN_VALUE;
    public int syncTime = Integer.MIN_VALUE;
    public KalturaFileSyncStatus status;
    public KalturaFileSyncType fileType;
    public int linkedId = Integer.MIN_VALUE;
    public int linkCount = Integer.MIN_VALUE;
    public String fileRoot;
    public String filePath;
    public double fileSize = Double.MIN_VALUE;
    public String fileUrl;
    public String fileContent;
    public double fileDiscSize = Double.MIN_VALUE;
    public boolean isCurrentDc;
    public boolean isDir;
    public int originalId = Integer.MIN_VALUE;

    public KalturaFileSync() {
    }

    public KalturaFileSync(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseBigint(txt);
                continue;
            } else if (nodeName.equals("partnerId")) {
                this.partnerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("fileObjectType")) {
                this.fileObjectType = KalturaFileSyncObjectType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("objectId")) {
                this.objectId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("version")) {
                this.version = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("objectSubType")) {
                this.objectSubType = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("dc")) {
                this.dc = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("original")) {
                this.original = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAt")) {
                this.updatedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("readyAt")) {
                this.readyAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("syncTime")) {
                this.syncTime = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaFileSyncStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("fileType")) {
                this.fileType = KalturaFileSyncType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("linkedId")) {
                this.linkedId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("linkCount")) {
                this.linkCount = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("fileRoot")) {
                this.fileRoot = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("filePath")) {
                this.filePath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("fileSize")) {
                this.fileSize = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("fileUrl")) {
                this.fileUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("fileContent")) {
                this.fileContent = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("fileDiscSize")) {
                this.fileDiscSize = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("isCurrentDc")) {
                this.isCurrentDc = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("isDir")) {
                this.isDir = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("originalId")) {
                this.originalId = ParseUtils.parseInt(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaFileSync");
        kparams.add("status", this.status);
        kparams.add("fileRoot", this.fileRoot);
        kparams.add("filePath", this.filePath);
        return kparams;
    }

}

