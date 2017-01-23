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
import com.kaltura.client.enums.KalturaFlavorReadyBehaviorType;
import com.kaltura.client.enums.KalturaAssetParamsOrigin;
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
public abstract class KalturaConversionProfileAssetParamsBaseFilter extends KalturaRelatedFilter {
    public int conversionProfileIdEqual = Integer.MIN_VALUE;
    public String conversionProfileIdIn;
    public int assetParamsIdEqual = Integer.MIN_VALUE;
    public String assetParamsIdIn;
    public KalturaFlavorReadyBehaviorType readyBehaviorEqual;
    public String readyBehaviorIn;
    public KalturaAssetParamsOrigin originEqual;
    public String originIn;
    public String systemNameEqual;
    public String systemNameIn;

    public KalturaConversionProfileAssetParamsBaseFilter() {
    }

    public KalturaConversionProfileAssetParamsBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("conversionProfileIdEqual")) {
                this.conversionProfileIdEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("conversionProfileIdIn")) {
                this.conversionProfileIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("assetParamsIdEqual")) {
                this.assetParamsIdEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("assetParamsIdIn")) {
                this.assetParamsIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("readyBehaviorEqual")) {
                this.readyBehaviorEqual = KalturaFlavorReadyBehaviorType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("readyBehaviorIn")) {
                this.readyBehaviorIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("originEqual")) {
                this.originEqual = KalturaAssetParamsOrigin.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("originIn")) {
                this.originIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("systemNameEqual")) {
                this.systemNameEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("systemNameIn")) {
                this.systemNameIn = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaConversionProfileAssetParamsBaseFilter");
        kparams.add("conversionProfileIdEqual", this.conversionProfileIdEqual);
        kparams.add("conversionProfileIdIn", this.conversionProfileIdIn);
        kparams.add("assetParamsIdEqual", this.assetParamsIdEqual);
        kparams.add("assetParamsIdIn", this.assetParamsIdIn);
        kparams.add("readyBehaviorEqual", this.readyBehaviorEqual);
        kparams.add("readyBehaviorIn", this.readyBehaviorIn);
        kparams.add("originEqual", this.originEqual);
        kparams.add("originIn", this.originIn);
        kparams.add("systemNameEqual", this.systemNameEqual);
        kparams.add("systemNameIn", this.systemNameIn);
        return kparams;
    }

}

