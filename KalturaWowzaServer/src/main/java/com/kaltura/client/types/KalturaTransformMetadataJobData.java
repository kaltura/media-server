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
public class KalturaTransformMetadataJobData extends KalturaJobData {
    public String srcXslPath;
    public int srcVersion = Integer.MIN_VALUE;
    public int destVersion = Integer.MIN_VALUE;
    public String destXsdPath;
    public int metadataProfileId = Integer.MIN_VALUE;

    public KalturaTransformMetadataJobData() {
    }

    public KalturaTransformMetadataJobData(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("srcXslPath")) {
                this.srcXslPath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("srcVersion")) {
                this.srcVersion = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("destVersion")) {
                this.destVersion = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("destXsdPath")) {
                this.destXsdPath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("metadataProfileId")) {
                this.metadataProfileId = ParseUtils.parseInt(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaTransformMetadataJobData");
        kparams.add("srcXslPath", this.srcXslPath);
        kparams.add("srcVersion", this.srcVersion);
        kparams.add("destVersion", this.destVersion);
        kparams.add("destXsdPath", this.destXsdPath);
        kparams.add("metadataProfileId", this.metadataProfileId);
        return kparams;
    }

}

