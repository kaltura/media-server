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
public class KalturaWebexDropFolder extends KalturaDropFolder {
    public String webexUserId;
    public String webexPassword;
    public int webexSiteId = Integer.MIN_VALUE;
    public String webexPartnerId;
    public String webexServiceUrl;
    public String webexHostIdMetadataFieldName;

    public KalturaWebexDropFolder() {
    }

    public KalturaWebexDropFolder(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("webexUserId")) {
                this.webexUserId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("webexPassword")) {
                this.webexPassword = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("webexSiteId")) {
                this.webexSiteId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("webexPartnerId")) {
                this.webexPartnerId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("webexServiceUrl")) {
                this.webexServiceUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("webexHostIdMetadataFieldName")) {
                this.webexHostIdMetadataFieldName = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaWebexDropFolder");
        kparams.add("webexUserId", this.webexUserId);
        kparams.add("webexPassword", this.webexPassword);
        kparams.add("webexSiteId", this.webexSiteId);
        kparams.add("webexPartnerId", this.webexPartnerId);
        kparams.add("webexServiceUrl", this.webexServiceUrl);
        kparams.add("webexHostIdMetadataFieldName", this.webexHostIdMetadataFieldName);
        return kparams;
    }

}

