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
public class KalturaCEError extends KalturaObjectBase {
    public String id;
    public int partnerId = Integer.MIN_VALUE;
    public String browser;
    public String serverIp;
    public String serverOs;
    public String phpVersion;
    public String ceAdminEmail;
    public String type;
    public String description;
    public String data;

    public KalturaCEError() {
    }

    public KalturaCEError(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("browser")) {
                this.browser = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("serverIp")) {
                this.serverIp = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("serverOs")) {
                this.serverOs = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("phpVersion")) {
                this.phpVersion = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("ceAdminEmail")) {
                this.ceAdminEmail = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("type")) {
                this.type = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("description")) {
                this.description = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("data")) {
                this.data = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaCEError");
        kparams.add("partnerId", this.partnerId);
        kparams.add("browser", this.browser);
        kparams.add("serverIp", this.serverIp);
        kparams.add("serverOs", this.serverOs);
        kparams.add("phpVersion", this.phpVersion);
        kparams.add("ceAdminEmail", this.ceAdminEmail);
        kparams.add("type", this.type);
        kparams.add("description", this.description);
        kparams.add("data", this.data);
        return kparams;
    }

}

