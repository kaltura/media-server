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
public class KalturaLiveToVodJobData extends KalturaJobData {
	/**  $vod Entry Id  */
    public String vodEntryId;
	/**  live Entry Id  */
    public String liveEntryId;
	/**  total VOD Duration  */
    public double totalVodDuration = Double.MIN_VALUE;
	/**  last Segment Duration  */
    public double lastSegmentDuration = Double.MIN_VALUE;
	/**  amf Array File Path  */
    public String amfArray;
	/**  last live to vod sync time  */
    public int lastCuePointSyncTime = Integer.MIN_VALUE;
	/**  last segment drift  */
    public int lastSegmentDrift = Integer.MIN_VALUE;

    public KalturaLiveToVodJobData() {
    }

    public KalturaLiveToVodJobData(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("vodEntryId")) {
                this.vodEntryId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("liveEntryId")) {
                this.liveEntryId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("totalVodDuration")) {
                this.totalVodDuration = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("lastSegmentDuration")) {
                this.lastSegmentDuration = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("amfArray")) {
                this.amfArray = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("lastCuePointSyncTime")) {
                this.lastCuePointSyncTime = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("lastSegmentDrift")) {
                this.lastSegmentDrift = ParseUtils.parseInt(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaLiveToVodJobData");
        kparams.add("vodEntryId", this.vodEntryId);
        kparams.add("liveEntryId", this.liveEntryId);
        kparams.add("totalVodDuration", this.totalVodDuration);
        kparams.add("lastSegmentDuration", this.lastSegmentDuration);
        kparams.add("amfArray", this.amfArray);
        kparams.add("lastCuePointSyncTime", this.lastCuePointSyncTime);
        kparams.add("lastSegmentDrift", this.lastSegmentDrift);
        return kparams;
    }

}

