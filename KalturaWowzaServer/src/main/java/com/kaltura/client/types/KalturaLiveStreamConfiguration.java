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
import com.kaltura.client.enums.KalturaPlaybackProtocol;
import com.kaltura.client.utils.ParseUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * This class was generated using exec.php
 * against an XML schema provided by Kaltura.
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

/**  A representation of a live stream configuration  */
@SuppressWarnings("serial")
public class KalturaLiveStreamConfiguration extends KalturaObjectBase {
    public KalturaPlaybackProtocol protocol;
    public String url;
    public String publishUrl;
    public String backupUrl;
    public String streamName;

    public KalturaLiveStreamConfiguration() {
    }

    public KalturaLiveStreamConfiguration(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("protocol")) {
                this.protocol = KalturaPlaybackProtocol.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("url")) {
                this.url = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("publishUrl")) {
                this.publishUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("backupUrl")) {
                this.backupUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("streamName")) {
                this.streamName = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaLiveStreamConfiguration");
        kparams.add("protocol", this.protocol);
        kparams.add("url", this.url);
        kparams.add("publishUrl", this.publishUrl);
        kparams.add("backupUrl", this.backupUrl);
        kparams.add("streamName", this.streamName);
        return kparams;
    }

}

