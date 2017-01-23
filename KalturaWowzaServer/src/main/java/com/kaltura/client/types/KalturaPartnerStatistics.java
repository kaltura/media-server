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
public class KalturaPartnerStatistics extends KalturaObjectBase {
	/**  Package total allowed bandwidth and storage  */
    public int packageBandwidthAndStorage = Integer.MIN_VALUE;
	/**  Partner total hosting in GB on the disk  */
    public double hosting = Double.MIN_VALUE;
	/**  Partner total bandwidth in GB  */
    public double bandwidth = Double.MIN_VALUE;
	/**  total usage in GB - including bandwidth and storage  */
    public int usage = Integer.MIN_VALUE;
	/**  Percent of usage out of partner's package. if usage is 5GB and package is 10GB,
	  this value will be 50  */
    public double usagePercent = Double.MIN_VALUE;
	/**  date when partner reached the limit of his package (timestamp)  */
    public int reachedLimitDate = Integer.MIN_VALUE;

    public KalturaPartnerStatistics() {
    }

    public KalturaPartnerStatistics(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("packageBandwidthAndStorage")) {
                this.packageBandwidthAndStorage = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("hosting")) {
                this.hosting = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("bandwidth")) {
                this.bandwidth = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("usage")) {
                this.usage = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("usagePercent")) {
                this.usagePercent = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("reachedLimitDate")) {
                this.reachedLimitDate = ParseUtils.parseInt(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaPartnerStatistics");
        return kparams;
    }

}

