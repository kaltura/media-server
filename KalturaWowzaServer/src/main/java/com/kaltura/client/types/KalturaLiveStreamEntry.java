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
public class KalturaLiveStreamEntry extends KalturaLiveEntry {
	/**  The stream id as provided by the provider  */
    public String streamRemoteId;
	/**  The backup stream id as provided by the provider  */
    public String streamRemoteBackupId;
	/**  Array of supported bitrates  */
    public ArrayList<KalturaLiveStreamBitrate> bitrates;
    public String primaryBroadcastingUrl;
    public String secondaryBroadcastingUrl;
    public String primaryRtspBroadcastingUrl;
    public String secondaryRtspBroadcastingUrl;
    public String streamName;
	/**  The stream url  */
    public String streamUrl;
	/**  HLS URL - URL for live stream playback on mobile device  */
    public String hlsStreamUrl;
	/**  URL Manager to handle the live stream URL (for instance, add token)  */
    public String urlManager;
	/**  The broadcast primary ip  */
    public String encodingIP1;
	/**  The broadcast secondary ip  */
    public String encodingIP2;
	/**  The broadcast password  */
    public String streamPassword;
	/**  The broadcast username  */
    public String streamUsername;
	/**  The Streams primary server node id  */
    public int primaryServerNodeId = Integer.MIN_VALUE;

    public KalturaLiveStreamEntry() {
    }

    public KalturaLiveStreamEntry(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("streamRemoteId")) {
                this.streamRemoteId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("streamRemoteBackupId")) {
                this.streamRemoteBackupId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("bitrates")) {
                this.bitrates = ParseUtils.parseArray(KalturaLiveStreamBitrate.class, aNode);
                continue;
            } else if (nodeName.equals("primaryBroadcastingUrl")) {
                this.primaryBroadcastingUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("secondaryBroadcastingUrl")) {
                this.secondaryBroadcastingUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("primaryRtspBroadcastingUrl")) {
                this.primaryRtspBroadcastingUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("secondaryRtspBroadcastingUrl")) {
                this.secondaryRtspBroadcastingUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("streamName")) {
                this.streamName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("streamUrl")) {
                this.streamUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("hlsStreamUrl")) {
                this.hlsStreamUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("urlManager")) {
                this.urlManager = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("encodingIP1")) {
                this.encodingIP1 = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("encodingIP2")) {
                this.encodingIP2 = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("streamPassword")) {
                this.streamPassword = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("streamUsername")) {
                this.streamUsername = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("primaryServerNodeId")) {
                this.primaryServerNodeId = ParseUtils.parseInt(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaLiveStreamEntry");
        kparams.add("bitrates", this.bitrates);
        kparams.add("primaryBroadcastingUrl", this.primaryBroadcastingUrl);
        kparams.add("secondaryBroadcastingUrl", this.secondaryBroadcastingUrl);
        kparams.add("primaryRtspBroadcastingUrl", this.primaryRtspBroadcastingUrl);
        kparams.add("secondaryRtspBroadcastingUrl", this.secondaryRtspBroadcastingUrl);
        kparams.add("streamName", this.streamName);
        kparams.add("streamUrl", this.streamUrl);
        kparams.add("hlsStreamUrl", this.hlsStreamUrl);
        kparams.add("urlManager", this.urlManager);
        kparams.add("encodingIP1", this.encodingIP1);
        kparams.add("encodingIP2", this.encodingIP2);
        kparams.add("streamPassword", this.streamPassword);
        return kparams;
    }

}

