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
public class KalturaPostConvertJobData extends KalturaConvartableJobData {
    public String flavorAssetId;
	/**  Indicates if a thumbnail should be created  */
    public boolean createThumb;
	/**  The path of the created thumbnail  */
    public String thumbPath;
	/**  The position of the thumbnail in the media file  */
    public int thumbOffset = Integer.MIN_VALUE;
	/**  The height of the movie, will be used to comapare if this thumbnail is the best
	  we can have  */
    public int thumbHeight = Integer.MIN_VALUE;
	/**  The bit rate of the movie, will be used to comapare if this thumbnail is the
	  best we can have  */
    public int thumbBitrate = Integer.MIN_VALUE;
    public String customData;

    public KalturaPostConvertJobData() {
    }

    public KalturaPostConvertJobData(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("flavorAssetId")) {
                this.flavorAssetId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("createThumb")) {
                this.createThumb = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("thumbPath")) {
                this.thumbPath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("thumbOffset")) {
                this.thumbOffset = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("thumbHeight")) {
                this.thumbHeight = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("thumbBitrate")) {
                this.thumbBitrate = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("customData")) {
                this.customData = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaPostConvertJobData");
        kparams.add("flavorAssetId", this.flavorAssetId);
        kparams.add("createThumb", this.createThumb);
        kparams.add("thumbPath", this.thumbPath);
        kparams.add("thumbOffset", this.thumbOffset);
        kparams.add("thumbHeight", this.thumbHeight);
        kparams.add("thumbBitrate", this.thumbBitrate);
        kparams.add("customData", this.customData);
        return kparams;
    }

}

