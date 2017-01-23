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
public class KalturaCaptionAssetItemFilter extends KalturaCaptionAssetFilter {
    public String contentLike;
    public String contentMultiLikeOr;
    public String contentMultiLikeAnd;
    public String partnerDescriptionLike;
    public String partnerDescriptionMultiLikeOr;
    public String partnerDescriptionMultiLikeAnd;
    public KalturaLanguage languageEqual;
    public String languageIn;
    public String labelEqual;
    public String labelIn;
    public int startTimeGreaterThanOrEqual = Integer.MIN_VALUE;
    public int startTimeLessThanOrEqual = Integer.MIN_VALUE;
    public int endTimeGreaterThanOrEqual = Integer.MIN_VALUE;
    public int endTimeLessThanOrEqual = Integer.MIN_VALUE;

    public KalturaCaptionAssetItemFilter() {
    }

    public KalturaCaptionAssetItemFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("contentLike")) {
                this.contentLike = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("contentMultiLikeOr")) {
                this.contentMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("contentMultiLikeAnd")) {
                this.contentMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerDescriptionLike")) {
                this.partnerDescriptionLike = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerDescriptionMultiLikeOr")) {
                this.partnerDescriptionMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerDescriptionMultiLikeAnd")) {
                this.partnerDescriptionMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("languageEqual")) {
                this.languageEqual = KalturaLanguage.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("languageIn")) {
                this.languageIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("labelEqual")) {
                this.labelEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("labelIn")) {
                this.labelIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("startTimeGreaterThanOrEqual")) {
                this.startTimeGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("startTimeLessThanOrEqual")) {
                this.startTimeLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("endTimeGreaterThanOrEqual")) {
                this.endTimeGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("endTimeLessThanOrEqual")) {
                this.endTimeLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaCaptionAssetItemFilter");
        kparams.add("contentLike", this.contentLike);
        kparams.add("contentMultiLikeOr", this.contentMultiLikeOr);
        kparams.add("contentMultiLikeAnd", this.contentMultiLikeAnd);
        kparams.add("partnerDescriptionLike", this.partnerDescriptionLike);
        kparams.add("partnerDescriptionMultiLikeOr", this.partnerDescriptionMultiLikeOr);
        kparams.add("partnerDescriptionMultiLikeAnd", this.partnerDescriptionMultiLikeAnd);
        kparams.add("languageEqual", this.languageEqual);
        kparams.add("languageIn", this.languageIn);
        kparams.add("labelEqual", this.labelEqual);
        kparams.add("labelIn", this.labelIn);
        kparams.add("startTimeGreaterThanOrEqual", this.startTimeGreaterThanOrEqual);
        kparams.add("startTimeLessThanOrEqual", this.startTimeLessThanOrEqual);
        kparams.add("endTimeGreaterThanOrEqual", this.endTimeGreaterThanOrEqual);
        kparams.add("endTimeLessThanOrEqual", this.endTimeLessThanOrEqual);
        return kparams;
    }

}

