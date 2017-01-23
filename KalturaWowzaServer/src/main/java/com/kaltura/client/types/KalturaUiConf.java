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
import com.kaltura.client.enums.KalturaUiConfObjType;
import com.kaltura.client.enums.KalturaUiConfCreationMode;
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
public class KalturaUiConf extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
	/**  Name of the uiConf, this is not a primary key  */
    public String name;
    public String description;
    public int partnerId = Integer.MIN_VALUE;
    public KalturaUiConfObjType objType;
    public String objTypeAsString;
    public int width = Integer.MIN_VALUE;
    public int height = Integer.MIN_VALUE;
    public String htmlParams;
    public String swfUrl;
    public String confFilePath;
    public String confFile;
    public String confFileFeatures;
    public String config;
    public String confVars;
    public boolean useCdn;
    public String tags;
    public String swfUrlVersion;
	/**  Entry creation date as Unix timestamp (In seconds)  */
    public int createdAt = Integer.MIN_VALUE;
	/**  Entry creation date as Unix timestamp (In seconds)  */
    public int updatedAt = Integer.MIN_VALUE;
    public KalturaUiConfCreationMode creationMode;
    public String html5Url;
	/**  UiConf version  */
    public String version;
    public String partnerTags;

    public KalturaUiConf() {
    }

    public KalturaUiConf(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("partnerId")) {
                this.partnerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("objType")) {
                this.objType = KalturaUiConfObjType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("objTypeAsString")) {
                this.objTypeAsString = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("width")) {
                this.width = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("height")) {
                this.height = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("htmlParams")) {
                this.htmlParams = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("swfUrl")) {
                this.swfUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("confFilePath")) {
                this.confFilePath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("confFile")) {
                this.confFile = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("confFileFeatures")) {
                this.confFileFeatures = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("config")) {
                this.config = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("confVars")) {
                this.confVars = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("useCdn")) {
                this.useCdn = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("tags")) {
                this.tags = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("swfUrlVersion")) {
                this.swfUrlVersion = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAt")) {
                this.updatedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("creationMode")) {
                this.creationMode = KalturaUiConfCreationMode.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("html5Url")) {
                this.html5Url = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("version")) {
                this.version = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerTags")) {
                this.partnerTags = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaUiConf");
        kparams.add("name", this.name);
        kparams.add("description", this.description);
        kparams.add("objType", this.objType);
        kparams.add("width", this.width);
        kparams.add("height", this.height);
        kparams.add("htmlParams", this.htmlParams);
        kparams.add("swfUrl", this.swfUrl);
        kparams.add("confFile", this.confFile);
        kparams.add("confFileFeatures", this.confFileFeatures);
        kparams.add("config", this.config);
        kparams.add("confVars", this.confVars);
        kparams.add("useCdn", this.useCdn);
        kparams.add("tags", this.tags);
        kparams.add("swfUrlVersion", this.swfUrlVersion);
        kparams.add("creationMode", this.creationMode);
        kparams.add("html5Url", this.html5Url);
        kparams.add("partnerTags", this.partnerTags);
        return kparams;
    }

}

