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
public class KalturaPartnerUsage extends KalturaObjectBase {
	/**  Partner total hosting in GB on the disk  */
    public double hostingGB = Double.MIN_VALUE;
	/**  percent of usage out of partner's package. if usageGB is 5 and package is 10GB,
	  this value will be 50  */
    public double Percent = Double.MIN_VALUE;
	/**  package total BW - actually this is usage, which represents BW+storage  */
    public int packageBW = Integer.MIN_VALUE;
	/**  total usage in GB - including bandwidth and storage  */
    public double usageGB = Double.MIN_VALUE;
	/**  date when partner reached the limit of his package (timestamp)  */
    public int reachedLimitDate = Integer.MIN_VALUE;
	/**  a semi-colon separated list of comma-separated key-values to represent a usage
	  graph.   keys could be 1-12 for a year view (1,1.2;2,1.1;3,0.9;...;12,1.4;)  
	  keys could be 1-[28,29,30,31] depending on the requested month, for a daily view
	  in a given month (1,0.4;2,0.2;...;31,0.1;)  */
    public String usageGraph;

    public KalturaPartnerUsage() {
    }

    public KalturaPartnerUsage(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("hostingGB")) {
                this.hostingGB = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("Percent")) {
                this.Percent = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("packageBW")) {
                this.packageBW = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("usageGB")) {
                this.usageGB = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("reachedLimitDate")) {
                this.reachedLimitDate = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("usageGraph")) {
                this.usageGraph = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaPartnerUsage");
        return kparams;
    }

}

