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
public abstract class KalturaCodeCuePointBaseFilter extends KalturaCuePointFilter {
    public String codeLike;
    public String codeMultiLikeOr;
    public String codeMultiLikeAnd;
    public String codeEqual;
    public String codeIn;
    public String descriptionLike;
    public String descriptionMultiLikeOr;
    public String descriptionMultiLikeAnd;
    public int endTimeGreaterThanOrEqual = Integer.MIN_VALUE;
    public int endTimeLessThanOrEqual = Integer.MIN_VALUE;
    public int durationGreaterThanOrEqual = Integer.MIN_VALUE;
    public int durationLessThanOrEqual = Integer.MIN_VALUE;

    public KalturaCodeCuePointBaseFilter() {
    }

    public KalturaCodeCuePointBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("codeLike")) {
                this.codeLike = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("codeMultiLikeOr")) {
                this.codeMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("codeMultiLikeAnd")) {
                this.codeMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("codeEqual")) {
                this.codeEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("codeIn")) {
                this.codeIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("descriptionLike")) {
                this.descriptionLike = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("descriptionMultiLikeOr")) {
                this.descriptionMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("descriptionMultiLikeAnd")) {
                this.descriptionMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("endTimeGreaterThanOrEqual")) {
                this.endTimeGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("endTimeLessThanOrEqual")) {
                this.endTimeLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("durationGreaterThanOrEqual")) {
                this.durationGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("durationLessThanOrEqual")) {
                this.durationLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaCodeCuePointBaseFilter");
        kparams.add("codeLike", this.codeLike);
        kparams.add("codeMultiLikeOr", this.codeMultiLikeOr);
        kparams.add("codeMultiLikeAnd", this.codeMultiLikeAnd);
        kparams.add("codeEqual", this.codeEqual);
        kparams.add("codeIn", this.codeIn);
        kparams.add("descriptionLike", this.descriptionLike);
        kparams.add("descriptionMultiLikeOr", this.descriptionMultiLikeOr);
        kparams.add("descriptionMultiLikeAnd", this.descriptionMultiLikeAnd);
        kparams.add("endTimeGreaterThanOrEqual", this.endTimeGreaterThanOrEqual);
        kparams.add("endTimeLessThanOrEqual", this.endTimeLessThanOrEqual);
        kparams.add("durationGreaterThanOrEqual", this.durationGreaterThanOrEqual);
        kparams.add("durationLessThanOrEqual", this.durationLessThanOrEqual);
        return kparams;
    }

}

