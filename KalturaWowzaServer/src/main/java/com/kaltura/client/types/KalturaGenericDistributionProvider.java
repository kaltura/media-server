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
import com.kaltura.client.enums.KalturaGenericDistributionProviderStatus;
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
public class KalturaGenericDistributionProvider extends KalturaDistributionProvider {
	/**  Auto generated  */
    public int id = Integer.MIN_VALUE;
	/**  Generic distribution provider creation date as Unix timestamp (In seconds)  */
    public int createdAt = Integer.MIN_VALUE;
	/**  Generic distribution provider last update date as Unix timestamp (In seconds)  */
    public int updatedAt = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public boolean isDefault;
    public KalturaGenericDistributionProviderStatus status;
    public String optionalFlavorParamsIds;
    public String requiredFlavorParamsIds;
    public ArrayList<KalturaDistributionThumbDimensions> optionalThumbDimensions;
    public ArrayList<KalturaDistributionThumbDimensions> requiredThumbDimensions;
    public String editableFields;
    public String mandatoryFields;

    public KalturaGenericDistributionProvider() {
    }

    public KalturaGenericDistributionProvider(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAt")) {
                this.updatedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerId")) {
                this.partnerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("isDefault")) {
                this.isDefault = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaGenericDistributionProviderStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("optionalFlavorParamsIds")) {
                this.optionalFlavorParamsIds = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("requiredFlavorParamsIds")) {
                this.requiredFlavorParamsIds = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("optionalThumbDimensions")) {
                this.optionalThumbDimensions = ParseUtils.parseArray(KalturaDistributionThumbDimensions.class, aNode);
                continue;
            } else if (nodeName.equals("requiredThumbDimensions")) {
                this.requiredThumbDimensions = ParseUtils.parseArray(KalturaDistributionThumbDimensions.class, aNode);
                continue;
            } else if (nodeName.equals("editableFields")) {
                this.editableFields = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("mandatoryFields")) {
                this.mandatoryFields = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaGenericDistributionProvider");
        kparams.add("isDefault", this.isDefault);
        kparams.add("optionalFlavorParamsIds", this.optionalFlavorParamsIds);
        kparams.add("requiredFlavorParamsIds", this.requiredFlavorParamsIds);
        kparams.add("optionalThumbDimensions", this.optionalThumbDimensions);
        kparams.add("requiredThumbDimensions", this.requiredThumbDimensions);
        kparams.add("editableFields", this.editableFields);
        kparams.add("mandatoryFields", this.mandatoryFields);
        return kparams;
    }

}

