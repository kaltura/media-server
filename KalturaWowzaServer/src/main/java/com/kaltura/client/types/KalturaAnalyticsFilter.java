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
public class KalturaAnalyticsFilter extends KalturaObjectBase {
	/**  Query start time (in local time) MM/dd/yyyy HH:mi  */
    public String from_time;
	/**  Query end time (in local time) MM/dd/yyyy HH:mi  */
    public String to_time;
	/**  Comma separated metrics list  */
    public String metrics;
	/**  Timezone offset from UTC (in minutes)  */
    public double utcOffset = Double.MIN_VALUE;
	/**  Comma separated dimensions list  */
    public String dimensions;
	/**  Array of filters  */
    public ArrayList<KalturaReportFilter> filters;
	/**  Query order by metric/dimension  */
    public String orderBy;

    public KalturaAnalyticsFilter() {
    }

    public KalturaAnalyticsFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("from_time")) {
                this.from_time = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("to_time")) {
                this.to_time = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("metrics")) {
                this.metrics = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("utcOffset")) {
                this.utcOffset = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("dimensions")) {
                this.dimensions = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("filters")) {
                this.filters = ParseUtils.parseArray(KalturaReportFilter.class, aNode);
                continue;
            } else if (nodeName.equals("orderBy")) {
                this.orderBy = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaAnalyticsFilter");
        kparams.add("from_time", this.from_time);
        kparams.add("to_time", this.to_time);
        kparams.add("metrics", this.metrics);
        kparams.add("utcOffset", this.utcOffset);
        kparams.add("dimensions", this.dimensions);
        kparams.add("filters", this.filters);
        kparams.add("orderBy", this.orderBy);
        return kparams;
    }

}

