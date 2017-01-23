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
public class KalturaLiveStats extends KalturaObjectBase {
    public int audience = Integer.MIN_VALUE;
    public int dvrAudience = Integer.MIN_VALUE;
    public double avgBitrate = Double.MIN_VALUE;
    public int bufferTime = Integer.MIN_VALUE;
    public int plays = Integer.MIN_VALUE;
    public int secondsViewed = Integer.MIN_VALUE;
    public long startEvent = Long.MIN_VALUE;
    public int timestamp = Integer.MIN_VALUE;

    public KalturaLiveStats() {
    }

    public KalturaLiveStats(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("audience")) {
                this.audience = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("dvrAudience")) {
                this.dvrAudience = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("avgBitrate")) {
                this.avgBitrate = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("bufferTime")) {
                this.bufferTime = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("plays")) {
                this.plays = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("secondsViewed")) {
                this.secondsViewed = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("startEvent")) {
                this.startEvent = ParseUtils.parseBigint(txt);
                continue;
            } else if (nodeName.equals("timestamp")) {
                this.timestamp = ParseUtils.parseInt(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaLiveStats");
        kparams.add("audience", this.audience);
        kparams.add("dvrAudience", this.dvrAudience);
        kparams.add("avgBitrate", this.avgBitrate);
        kparams.add("bufferTime", this.bufferTime);
        kparams.add("plays", this.plays);
        kparams.add("secondsViewed", this.secondsViewed);
        kparams.add("startEvent", this.startEvent);
        kparams.add("timestamp", this.timestamp);
        return kparams;
    }

}

