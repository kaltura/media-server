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
public class KalturaStreamContainer extends KalturaObjectBase {
    public String type;
    public int trackIndex = Integer.MIN_VALUE;
    public String language;
    public int channelIndex = Integer.MIN_VALUE;
    public String label;
    public String channelLayout;

    public KalturaStreamContainer() {
    }

    public KalturaStreamContainer(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("type")) {
                this.type = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("trackIndex")) {
                this.trackIndex = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("language")) {
                this.language = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("channelIndex")) {
                this.channelIndex = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("label")) {
                this.label = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("channelLayout")) {
                this.channelLayout = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaStreamContainer");
        kparams.add("type", this.type);
        kparams.add("trackIndex", this.trackIndex);
        kparams.add("language", this.language);
        kparams.add("channelIndex", this.channelIndex);
        kparams.add("label", this.label);
        kparams.add("channelLayout", this.channelLayout);
        return kparams;
    }

}

