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
public class KalturaCrossKalturaDistributionJobProviderData extends KalturaConfigurableDistributionJobProviderData {
	/**  Key-value array where the keys are IDs of distributed flavor assets in the
	  source account and the values are the matching IDs in the target account  */
    public String distributedFlavorAssets;
	/**  Key-value array where the keys are IDs of distributed thumb assets in the source
	  account and the values are the matching IDs in the target account  */
    public String distributedThumbAssets;
	/**  Key-value array where the keys are IDs of distributed metadata objects in the
	  source account and the values are the matching IDs in the target account  */
    public String distributedMetadata;
	/**  Key-value array where the keys are IDs of distributed caption assets in the
	  source account and the values are the matching IDs in the target account  */
    public String distributedCaptionAssets;
	/**  Key-value array where the keys are IDs of distributed cue points in the source
	  account and the values are the matching IDs in the target account  */
    public String distributedCuePoints;

    public KalturaCrossKalturaDistributionJobProviderData() {
    }

    public KalturaCrossKalturaDistributionJobProviderData(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("distributedFlavorAssets")) {
                this.distributedFlavorAssets = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("distributedThumbAssets")) {
                this.distributedThumbAssets = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("distributedMetadata")) {
                this.distributedMetadata = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("distributedCaptionAssets")) {
                this.distributedCaptionAssets = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("distributedCuePoints")) {
                this.distributedCuePoints = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaCrossKalturaDistributionJobProviderData");
        kparams.add("distributedFlavorAssets", this.distributedFlavorAssets);
        kparams.add("distributedThumbAssets", this.distributedThumbAssets);
        kparams.add("distributedMetadata", this.distributedMetadata);
        kparams.add("distributedCaptionAssets", this.distributedCaptionAssets);
        kparams.add("distributedCuePoints", this.distributedCuePoints);
        return kparams;
    }

}

