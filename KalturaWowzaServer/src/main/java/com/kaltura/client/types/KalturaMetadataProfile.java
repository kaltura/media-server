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
import com.kaltura.client.enums.KalturaMetadataObjectType;
import com.kaltura.client.enums.KalturaMetadataProfileStatus;
import com.kaltura.client.enums.KalturaMetadataProfileCreateMode;
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
public class KalturaMetadataProfile extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public KalturaMetadataObjectType metadataObjectType;
    public int version = Integer.MIN_VALUE;
    public String name;
    public String systemName;
    public String description;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;
    public KalturaMetadataProfileStatus status;
    public String xsd;
    public String views;
    public String xslt;
    public KalturaMetadataProfileCreateMode createMode;
    public boolean disableReIndexing;

    public KalturaMetadataProfile() {
    }

    public KalturaMetadataProfile(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("metadataObjectType")) {
                this.metadataObjectType = KalturaMetadataObjectType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("version")) {
                this.version = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("name")) {
                this.name = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("systemName")) {
                this.systemName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("description")) {
                this.description = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAt")) {
                this.updatedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaMetadataProfileStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("xsd")) {
                this.xsd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("views")) {
                this.views = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("xslt")) {
                this.xslt = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("createMode")) {
                this.createMode = KalturaMetadataProfileCreateMode.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("disableReIndexing")) {
                this.disableReIndexing = ParseUtils.parseBool(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaMetadataProfile");
        kparams.add("metadataObjectType", this.metadataObjectType);
        kparams.add("name", this.name);
        kparams.add("systemName", this.systemName);
        kparams.add("description", this.description);
        kparams.add("createMode", this.createMode);
        kparams.add("disableReIndexing", this.disableReIndexing);
        return kparams;
    }

}

