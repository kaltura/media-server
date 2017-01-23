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
import com.kaltura.client.enums.KalturaLiveChannelSegmentType;
import com.kaltura.client.enums.KalturaLiveChannelSegmentStatus;
import com.kaltura.client.enums.KalturaLiveChannelSegmentTriggerType;
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
public class KalturaLiveChannelSegment extends KalturaObjectBase {
	/**  Unique identifier  */
    public String id;
    public int partnerId = Integer.MIN_VALUE;
	/**  Segment creation date as Unix timestamp (In seconds)  */
    public int createdAt = Integer.MIN_VALUE;
	/**  Segment update date as Unix timestamp (In seconds)  */
    public int updatedAt = Integer.MIN_VALUE;
	/**  Segment name  */
    public String name;
	/**  Segment description  */
    public String description;
	/**  Segment tags  */
    public String tags;
	/**  Segment could be associated with the main stream, as additional stream or as
	  overlay  */
    public KalturaLiveChannelSegmentType type;
    public KalturaLiveChannelSegmentStatus status;
	/**  Live channel id  */
    public String channelId;
	/**  Entry id to be played  */
    public String entryId;
	/**  Segment start time trigger type  */
    public KalturaLiveChannelSegmentTriggerType triggerType;
	/**  Live channel segment that the trigger relates to  */
    public String triggerSegmentId;
	/**  Segment play start time, in mili-seconds, according to trigger type  */
    public double startTime = Double.MIN_VALUE;
	/**  Segment play duration time, in mili-seconds  */
    public double duration = Double.MIN_VALUE;

    public KalturaLiveChannelSegment() {
    }

    public KalturaLiveChannelSegment(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAt")) {
                this.updatedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("name")) {
                this.name = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("description")) {
                this.description = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tags")) {
                this.tags = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("type")) {
                this.type = KalturaLiveChannelSegmentType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaLiveChannelSegmentStatus.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("channelId")) {
                this.channelId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("entryId")) {
                this.entryId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("triggerType")) {
                this.triggerType = KalturaLiveChannelSegmentTriggerType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("triggerSegmentId")) {
                this.triggerSegmentId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("startTime")) {
                this.startTime = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("duration")) {
                this.duration = ParseUtils.parseDouble(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaLiveChannelSegment");
        kparams.add("name", this.name);
        kparams.add("description", this.description);
        kparams.add("tags", this.tags);
        kparams.add("type", this.type);
        kparams.add("channelId", this.channelId);
        kparams.add("entryId", this.entryId);
        kparams.add("triggerType", this.triggerType);
        kparams.add("triggerSegmentId", this.triggerSegmentId);
        kparams.add("startTime", this.startTime);
        kparams.add("duration", this.duration);
        return kparams;
    }

}

