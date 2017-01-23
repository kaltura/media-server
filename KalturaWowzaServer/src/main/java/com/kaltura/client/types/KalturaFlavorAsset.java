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
import com.kaltura.client.enums.KalturaFlavorAssetStatus;
import com.kaltura.client.enums.KalturaLanguage;
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
public class KalturaFlavorAsset extends KalturaAsset {
	/**  The Flavor Params used to create this Flavor Asset  */
    public int flavorParamsId = Integer.MIN_VALUE;
	/**  The width of the Flavor Asset  */
    public int width = Integer.MIN_VALUE;
	/**  The height of the Flavor Asset  */
    public int height = Integer.MIN_VALUE;
	/**  The overall bitrate (in KBits) of the Flavor Asset  */
    public int bitrate = Integer.MIN_VALUE;
	/**  The frame rate (in FPS) of the Flavor Asset  */
    public double frameRate = Double.MIN_VALUE;
	/**  True if this Flavor Asset is the original source  */
    public boolean isOriginal;
	/**  True if this Flavor Asset is playable in KDP  */
    public boolean isWeb;
	/**  The container format  */
    public String containerFormat;
	/**  The video codec  */
    public String videoCodecId;
	/**  The status of the Flavor Asset  */
    public KalturaFlavorAssetStatus status;
	/**  The language of the flavor asset  */
    public KalturaLanguage language;
	/**  The label of the flavor asset  */
    public String label;

    public KalturaFlavorAsset() {
    }

    public KalturaFlavorAsset(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("flavorParamsId")) {
                this.flavorParamsId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("width")) {
                this.width = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("height")) {
                this.height = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("bitrate")) {
                this.bitrate = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("frameRate")) {
                this.frameRate = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("isOriginal")) {
                this.isOriginal = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("isWeb")) {
                this.isWeb = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("containerFormat")) {
                this.containerFormat = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("videoCodecId")) {
                this.videoCodecId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaFlavorAssetStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("language")) {
                this.language = KalturaLanguage.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("label")) {
                this.label = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaFlavorAsset");
        kparams.add("flavorParamsId", this.flavorParamsId);
        kparams.add("language", this.language);
        return kparams;
    }

}

