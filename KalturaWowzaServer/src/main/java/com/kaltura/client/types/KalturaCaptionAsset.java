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
import com.kaltura.client.enums.KalturaLanguageCode;
import com.kaltura.client.enums.KalturaNullableBoolean;
import com.kaltura.client.enums.KalturaCaptionType;
import com.kaltura.client.enums.KalturaCaptionAssetStatus;
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
public class KalturaCaptionAsset extends KalturaAsset {
	/**  The Caption Params used to create this Caption Asset  */
    public int captionParamsId = Integer.MIN_VALUE;
	/**  The language of the caption asset content  */
    public KalturaLanguage language;
	/**  The language of the caption asset content  */
    public KalturaLanguageCode languageCode;
	/**  Is default caption asset of the entry  */
    public KalturaNullableBoolean isDefault;
	/**  Friendly label  */
    public String label;
	/**  The caption format  */
    public KalturaCaptionType format;
	/**  The status of the asset  */
    public KalturaCaptionAssetStatus status;
	/**  The parent id of the asset  */
    public String parentId;
	/**  The Accuracy of the caption content  */
    public int accuracy = Integer.MIN_VALUE;

    public KalturaCaptionAsset() {
    }

    public KalturaCaptionAsset(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("captionParamsId")) {
                this.captionParamsId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("language")) {
                this.language = KalturaLanguage.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("languageCode")) {
                this.languageCode = KalturaLanguageCode.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("isDefault")) {
                this.isDefault = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("label")) {
                this.label = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("format")) {
                this.format = KalturaCaptionType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaCaptionAssetStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("parentId")) {
                this.parentId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("accuracy")) {
                this.accuracy = ParseUtils.parseInt(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaCaptionAsset");
        kparams.add("captionParamsId", this.captionParamsId);
        kparams.add("language", this.language);
        kparams.add("isDefault", this.isDefault);
        kparams.add("label", this.label);
        kparams.add("format", this.format);
        kparams.add("parentId", this.parentId);
        kparams.add("accuracy", this.accuracy);
        return kparams;
    }

}

