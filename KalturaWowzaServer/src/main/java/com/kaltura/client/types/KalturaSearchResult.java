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
import com.kaltura.client.enums.KalturaLicenseType;
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
public class KalturaSearchResult extends KalturaSearch {
    public String id;
    public String title;
    public String thumbUrl;
    public String description;
    public String tags;
    public String url;
    public String sourceLink;
    public String credit;
    public KalturaLicenseType licenseType;
    public String flashPlaybackType;
    public String fileExt;

    public KalturaSearchResult() {
    }

    public KalturaSearchResult(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("title")) {
                this.title = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("thumbUrl")) {
                this.thumbUrl = ParseUtils.parseString(txt);
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
            } else if (nodeName.equals("sourceLink")) {
                this.sourceLink = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("credit")) {
                this.credit = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("licenseType")) {
                this.licenseType = KalturaLicenseType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("flashPlaybackType")) {
                this.flashPlaybackType = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("fileExt")) {
                this.fileExt = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaSearchResult");
        kparams.add("id", this.id);
        kparams.add("title", this.title);
        kparams.add("thumbUrl", this.thumbUrl);
        kparams.add("description", this.description);
        kparams.add("tags", this.tags);
        kparams.add("url", this.url);
        kparams.add("sourceLink", this.sourceLink);
        kparams.add("credit", this.credit);
        kparams.add("licenseType", this.licenseType);
        kparams.add("flashPlaybackType", this.flashPlaybackType);
        kparams.add("fileExt", this.fileExt);
        return kparams;
    }

}

