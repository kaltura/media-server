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

@SuppressWarnings("serial")
public class KalturaDeliveryProfileForensicWatermark extends KalturaDeliveryProfile {
	/**  The URL used to pull manifest from the server, keyed by dc id, asterisk means
	  all dcs  */
    public ArrayList<KalturaKeyValue> internalUrl;
	/**  The key used to encrypt the URI (256 bits)  */
    public String encryptionKey;
	/**  The iv used to encrypt the URI (128 bits)  */
    public String encryptionIv;
	/**  The regex used to match the encrypted part of the URI (according to the
	  'encrypt' named group)  */
    public String encryptionRegex;

    public KalturaDeliveryProfileForensicWatermark() {
    }

    public KalturaDeliveryProfileForensicWatermark(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("internalUrl")) {
                this.internalUrl = ParseUtils.parseArray(KalturaKeyValue.class, aNode);
                continue;
            } else if (nodeName.equals("encryptionKey")) {
                this.encryptionKey = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("encryptionIv")) {
                this.encryptionIv = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("encryptionRegex")) {
                this.encryptionRegex = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaDeliveryProfileForensicWatermark");
        kparams.add("internalUrl", this.internalUrl);
        kparams.add("encryptionKey", this.encryptionKey);
        kparams.add("encryptionIv", this.encryptionIv);
        kparams.add("encryptionRegex", this.encryptionRegex);
        return kparams;
    }

}

