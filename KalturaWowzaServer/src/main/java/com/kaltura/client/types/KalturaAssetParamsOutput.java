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
import com.kaltura.client.enums.KalturaContainerFormat;
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
public class KalturaAssetParamsOutput extends KalturaAssetParams {
    public int assetParamsId = Integer.MIN_VALUE;
    public String assetParamsVersion;
    public String assetId;
    public String assetVersion;
    public int readyBehavior = Integer.MIN_VALUE;
	/**  The container format of the Flavor Params  */
    public KalturaContainerFormat format;

    public KalturaAssetParamsOutput() {
    }

    public KalturaAssetParamsOutput(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("assetParamsId")) {
                this.assetParamsId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("assetParamsVersion")) {
                this.assetParamsVersion = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("assetId")) {
                this.assetId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("assetVersion")) {
                this.assetVersion = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("readyBehavior")) {
                this.readyBehavior = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("format")) {
                this.format = KalturaContainerFormat.get(ParseUtils.parseString(txt));
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaAssetParamsOutput");
        kparams.add("assetParamsId", this.assetParamsId);
        kparams.add("assetParamsVersion", this.assetParamsVersion);
        kparams.add("assetId", this.assetId);
        kparams.add("assetVersion", this.assetVersion);
        kparams.add("readyBehavior", this.readyBehavior);
        kparams.add("format", this.format);
        return kparams;
    }

}

