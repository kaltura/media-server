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
import com.kaltura.client.enums.KalturaPlaylistType;
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
public class KalturaPlaylist extends KalturaBaseEntry {
	/**  Content of the playlist -    XML if the playlistType is dynamic    text if the
	  playlistType is static    url if the playlistType is mRss  */
    public String playlistContent;
    public ArrayList<KalturaMediaEntryFilterForPlaylist> filters;
	/**  Maximum count of results to be returned in playlist execution  */
    public int totalResults = Integer.MIN_VALUE;
	/**  Type of playlist  */
    public KalturaPlaylistType playlistType;
	/**  Number of plays  */
    public int plays = Integer.MIN_VALUE;
	/**  Number of views  */
    public int views = Integer.MIN_VALUE;
	/**  The duration in seconds  */
    public int duration = Integer.MIN_VALUE;
	/**  The url for this playlist  */
    public String executeUrl;

    public KalturaPlaylist() {
    }

    public KalturaPlaylist(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("playlistContent")) {
                this.playlistContent = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("filters")) {
                this.filters = ParseUtils.parseArray(KalturaMediaEntryFilterForPlaylist.class, aNode);
                continue;
            } else if (nodeName.equals("totalResults")) {
                this.totalResults = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("playlistType")) {
                this.playlistType = KalturaPlaylistType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("plays")) {
                this.plays = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("views")) {
                this.views = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("duration")) {
                this.duration = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("executeUrl")) {
                this.executeUrl = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaPlaylist");
        kparams.add("playlistContent", this.playlistContent);
        kparams.add("filters", this.filters);
        kparams.add("totalResults", this.totalResults);
        kparams.add("playlistType", this.playlistType);
        return kparams;
    }

}

