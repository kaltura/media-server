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

/**  A resource that perform operation (transcoding, clipping, cropping) before the
  flavor is ready.  */
@SuppressWarnings("serial")
public class KalturaOperationResource extends KalturaContentResource {
	/**  Only KalturaEntryResource and KalturaAssetResource are supported  */
    public KalturaContentResource resource;
    public ArrayList<KalturaOperationAttributes> operationAttributes;
	/**  ID of alternative asset params to be used instead of the system default flavor
	  params  */
    public int assetParamsId = Integer.MIN_VALUE;

    public KalturaOperationResource() {
    }

    public KalturaOperationResource(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("resource")) {
                this.resource = ParseUtils.parseObject(KalturaContentResource.class, aNode);
                continue;
            } else if (nodeName.equals("operationAttributes")) {
                this.operationAttributes = ParseUtils.parseArray(KalturaOperationAttributes.class, aNode);
                continue;
            } else if (nodeName.equals("assetParamsId")) {
                this.assetParamsId = ParseUtils.parseInt(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaOperationResource");
        kparams.add("resource", this.resource);
        kparams.add("operationAttributes", this.operationAttributes);
        kparams.add("assetParamsId", this.assetParamsId);
        return kparams;
    }

}

