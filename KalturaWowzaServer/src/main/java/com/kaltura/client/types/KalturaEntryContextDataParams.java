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

/**  Object which contains contextual entry-related data.  */
@SuppressWarnings("serial")
public class KalturaEntryContextDataParams extends KalturaAccessControlScope {
	/**  Id of the current flavor.  */
    public String flavorAssetId;
	/**  The tags of the flavors that should be used for playback.  */
    public String flavorTags;
	/**  Playback streamer type: RTMP, HTTP, appleHttps, rtsp, sl.  */
    public String streamerType;
	/**  Protocol of the specific media object.  */
    public String mediaProtocol;

    public KalturaEntryContextDataParams() {
    }

    public KalturaEntryContextDataParams(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("flavorAssetId")) {
                this.flavorAssetId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("flavorTags")) {
                this.flavorTags = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("streamerType")) {
                this.streamerType = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("mediaProtocol")) {
                this.mediaProtocol = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaEntryContextDataParams");
        kparams.add("flavorAssetId", this.flavorAssetId);
        kparams.add("flavorTags", this.flavorTags);
        kparams.add("streamerType", this.streamerType);
        kparams.add("mediaProtocol", this.mediaProtocol);
        return kparams;
    }

}

