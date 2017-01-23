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
import com.kaltura.client.enums.KalturaSessionType;
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
public class KalturaRecalculateResponseProfileCacheJobData extends KalturaRecalculateCacheJobData {
	/**  http / https  */
    public String protocol;
    public KalturaSessionType ksType;
    public ArrayList<KalturaIntegerValue> userRoles;
	/**  Class name  */
    public String cachedObjectType;
    public String objectId;
    public String startObjectKey;
    public String endObjectKey;

    public KalturaRecalculateResponseProfileCacheJobData() {
    }

    public KalturaRecalculateResponseProfileCacheJobData(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("protocol")) {
                this.protocol = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("ksType")) {
                this.ksType = KalturaSessionType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("userRoles")) {
                this.userRoles = ParseUtils.parseArray(KalturaIntegerValue.class, aNode);
                continue;
            } else if (nodeName.equals("cachedObjectType")) {
                this.cachedObjectType = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("objectId")) {
                this.objectId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("startObjectKey")) {
                this.startObjectKey = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("endObjectKey")) {
                this.endObjectKey = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaRecalculateResponseProfileCacheJobData");
        kparams.add("protocol", this.protocol);
        kparams.add("ksType", this.ksType);
        kparams.add("userRoles", this.userRoles);
        kparams.add("cachedObjectType", this.cachedObjectType);
        kparams.add("objectId", this.objectId);
        kparams.add("startObjectKey", this.startObjectKey);
        kparams.add("endObjectKey", this.endObjectKey);
        return kparams;
    }

}

