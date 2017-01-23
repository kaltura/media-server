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
import com.kaltura.client.enums.KalturaDeliveryProfileType;
import com.kaltura.client.enums.KalturaPlaybackProtocol;
import com.kaltura.client.enums.KalturaDeliveryStatus;
import com.kaltura.client.enums.KalturaNullableBoolean;
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
public class KalturaDeliveryProfile extends KalturaObjectBase {
	/**  The id of the Delivery  */
    public int id = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
	/**  The name of the Delivery  */
    public String name;
	/**  Delivery type  */
    public KalturaDeliveryProfileType type;
	/**  System name of the delivery  */
    public String systemName;
	/**  The description of the Delivery  */
    public String description;
	/**  Creation time as Unix timestamp (In seconds)  */
    public int createdAt = Integer.MIN_VALUE;
	/**  Update time as Unix timestamp (In seconds)  */
    public int updatedAt = Integer.MIN_VALUE;
    public KalturaPlaybackProtocol streamerType;
    public String url;
	/**  the host part of the url  */
    public String hostName;
    public KalturaDeliveryStatus status;
    public KalturaUrlRecognizer recognizer;
    public KalturaUrlTokenizer tokenizer;
	/**  True if this is the systemwide default for the protocol  */
    public KalturaNullableBoolean isDefault;
	/**  the object from which this object was cloned (or 0)  */
    public int parentId = Integer.MIN_VALUE;
	/**  Comma separated list of supported media protocols. f.i. rtmpe  */
    public String mediaProtocols;
	/**  priority used for ordering similar delivery profiles  */
    public int priority = Integer.MIN_VALUE;
	/**  Extra query string parameters that should be added to the url  */
    public String extraParams;
	/**  A filter that can be used to include additional assets in the URL (e.g.
	  captions)  */
    public KalturaAssetFilter supplementaryAssetsFilter;

    public KalturaDeliveryProfile() {
    }

    public KalturaDeliveryProfile(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerId")) {
                this.partnerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("name")) {
                this.name = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("type")) {
                this.type = KalturaDeliveryProfileType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("systemName")) {
                this.systemName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("description")) {
                this.description = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAt")) {
                this.updatedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("streamerType")) {
                this.streamerType = KalturaPlaybackProtocol.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("url")) {
                this.url = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("hostName")) {
                this.hostName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaDeliveryStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("recognizer")) {
                this.recognizer = ParseUtils.parseObject(KalturaUrlRecognizer.class, aNode);
                continue;
            } else if (nodeName.equals("tokenizer")) {
                this.tokenizer = ParseUtils.parseObject(KalturaUrlTokenizer.class, aNode);
                continue;
            } else if (nodeName.equals("isDefault")) {
                this.isDefault = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("parentId")) {
                this.parentId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("mediaProtocols")) {
                this.mediaProtocols = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("priority")) {
                this.priority = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("extraParams")) {
                this.extraParams = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("supplementaryAssetsFilter")) {
                this.supplementaryAssetsFilter = ParseUtils.parseObject(KalturaAssetFilter.class, aNode);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaDeliveryProfile");
        kparams.add("name", this.name);
        kparams.add("type", this.type);
        kparams.add("systemName", this.systemName);
        kparams.add("description", this.description);
        kparams.add("streamerType", this.streamerType);
        kparams.add("url", this.url);
        kparams.add("status", this.status);
        kparams.add("recognizer", this.recognizer);
        kparams.add("tokenizer", this.tokenizer);
        kparams.add("mediaProtocols", this.mediaProtocols);
        kparams.add("priority", this.priority);
        kparams.add("extraParams", this.extraParams);
        kparams.add("supplementaryAssetsFilter", this.supplementaryAssetsFilter);
        return kparams;
    }

}

