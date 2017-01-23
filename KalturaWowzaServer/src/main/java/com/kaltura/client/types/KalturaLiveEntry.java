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
import com.kaltura.client.enums.KalturaRecordStatus;
import com.kaltura.client.enums.KalturaDVRStatus;
import com.kaltura.client.enums.KalturaLivePublishStatus;
import com.kaltura.client.enums.KalturaEntryServerNodeStatus;
import java.util.ArrayList;
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
public abstract class KalturaLiveEntry extends KalturaMediaEntry {
	/**  The message to be presented when the stream is offline  */
    public String offlineMessage;
	/**  Recording Status Enabled/Disabled  */
    public KalturaRecordStatus recordStatus;
	/**  DVR Status Enabled/Disabled  */
    public KalturaDVRStatus dvrStatus;
	/**  Window of time which the DVR allows for backwards scrubbing (in minutes)  */
    public int dvrWindow = Integer.MIN_VALUE;
	/**  Elapsed recording time (in msec) up to the point where the live stream was last
	  stopped (unpublished).  */
    public int lastElapsedRecordingTime = Integer.MIN_VALUE;
	/**  Array of key value protocol-&gt;live stream url objects  */
    public ArrayList<KalturaLiveStreamConfiguration> liveStreamConfigurations;
	/**  Recorded entry id  */
    public String recordedEntryId;
	/**  Flag denoting whether entry should be published by the media server  */
    public KalturaLivePublishStatus pushPublishEnabled;
	/**  Array of publish configurations  */
    public ArrayList<KalturaLiveStreamPushPublishConfiguration> publishConfigurations;
	/**  The first time in which the entry was broadcast  */
    public int firstBroadcast = Integer.MIN_VALUE;
	/**  The Last time in which the entry was broadcast  */
    public int lastBroadcast = Integer.MIN_VALUE;
	/**  The time (unix timestamp in milliseconds) in which the entry broadcast started
	  or 0 when the entry is off the air  */
    public double currentBroadcastStartTime = Double.MIN_VALUE;
    public KalturaLiveEntryRecordingOptions recordingOptions;
	/**  the status of the entry of type EntryServerNodeStatus  */
    public KalturaEntryServerNodeStatus liveStatus;

    public KalturaLiveEntry() {
    }

    public KalturaLiveEntry(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("offlineMessage")) {
                this.offlineMessage = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("recordStatus")) {
                this.recordStatus = KalturaRecordStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("dvrStatus")) {
                this.dvrStatus = KalturaDVRStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("dvrWindow")) {
                this.dvrWindow = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("lastElapsedRecordingTime")) {
                this.lastElapsedRecordingTime = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("liveStreamConfigurations")) {
                this.liveStreamConfigurations = ParseUtils.parseArray(KalturaLiveStreamConfiguration.class, aNode);
                continue;
            } else if (nodeName.equals("recordedEntryId")) {
                this.recordedEntryId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("pushPublishEnabled")) {
                this.pushPublishEnabled = KalturaLivePublishStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("publishConfigurations")) {
                this.publishConfigurations = ParseUtils.parseArray(KalturaLiveStreamPushPublishConfiguration.class, aNode);
                continue;
            } else if (nodeName.equals("firstBroadcast")) {
                this.firstBroadcast = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("lastBroadcast")) {
                this.lastBroadcast = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("currentBroadcastStartTime")) {
                this.currentBroadcastStartTime = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("recordingOptions")) {
                this.recordingOptions = ParseUtils.parseObject(KalturaLiveEntryRecordingOptions.class, aNode);
                continue;
            } else if (nodeName.equals("liveStatus")) {
                this.liveStatus = KalturaEntryServerNodeStatus.get(ParseUtils.parseInt(txt));
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaLiveEntry");
        kparams.add("offlineMessage", this.offlineMessage);
        kparams.add("recordStatus", this.recordStatus);
        kparams.add("dvrStatus", this.dvrStatus);
        kparams.add("dvrWindow", this.dvrWindow);
        kparams.add("lastElapsedRecordingTime", this.lastElapsedRecordingTime);
        kparams.add("liveStreamConfigurations", this.liveStreamConfigurations);
        kparams.add("recordedEntryId", this.recordedEntryId);
        kparams.add("pushPublishEnabled", this.pushPublishEnabled);
        kparams.add("publishConfigurations", this.publishConfigurations);
        kparams.add("currentBroadcastStartTime", this.currentBroadcastStartTime);
        kparams.add("recordingOptions", this.recordingOptions);
        return kparams;
    }

}

