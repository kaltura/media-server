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
public class KalturaFeedItemInfo extends KalturaObjectBase {
    public String itemXPath;
    public String itemPublishDateXPath;
    public String itemUniqueIdentifierXPath;
    public String itemContentFileSizeXPath;
    public String itemContentUrlXPath;
    public String itemContentBitrateXPath;
    public String itemHashXPath;
    public String itemContentXpath;
    public String contentBitrateAttributeName;

    public KalturaFeedItemInfo() {
    }

    public KalturaFeedItemInfo(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("itemXPath")) {
                this.itemXPath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("itemPublishDateXPath")) {
                this.itemPublishDateXPath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("itemUniqueIdentifierXPath")) {
                this.itemUniqueIdentifierXPath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("itemContentFileSizeXPath")) {
                this.itemContentFileSizeXPath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("itemContentUrlXPath")) {
                this.itemContentUrlXPath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("itemContentBitrateXPath")) {
                this.itemContentBitrateXPath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("itemHashXPath")) {
                this.itemHashXPath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("itemContentXpath")) {
                this.itemContentXpath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("contentBitrateAttributeName")) {
                this.contentBitrateAttributeName = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaFeedItemInfo");
        kparams.add("itemXPath", this.itemXPath);
        kparams.add("itemPublishDateXPath", this.itemPublishDateXPath);
        kparams.add("itemUniqueIdentifierXPath", this.itemUniqueIdentifierXPath);
        kparams.add("itemContentFileSizeXPath", this.itemContentFileSizeXPath);
        kparams.add("itemContentUrlXPath", this.itemContentUrlXPath);
        kparams.add("itemContentBitrateXPath", this.itemContentBitrateXPath);
        kparams.add("itemHashXPath", this.itemHashXPath);
        kparams.add("itemContentXpath", this.itemContentXpath);
        kparams.add("contentBitrateAttributeName", this.contentBitrateAttributeName);
        return kparams;
    }

}

