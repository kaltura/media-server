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
public class KalturaDexterIntegrationJobProviderData extends KalturaIntegrationJobProviderData {
	/**  ID of the metadata profile for the extracted term metadata  */
    public int metadataProfileId = Integer.MIN_VALUE;
	/**  ID of the transcript asset  */
    public String transcriptAssetId;
	/**  ID of the entry  */
    public String entryId;
	/**  Voicebase API key to fetch transcript tokens  */
    public String voicebaseApiKey;
	/**  Voicebase API password to fetch transcript tokens  */
    public String voicebaseApiPassword;

    public KalturaDexterIntegrationJobProviderData() {
    }

    public KalturaDexterIntegrationJobProviderData(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("metadataProfileId")) {
                this.metadataProfileId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("transcriptAssetId")) {
                this.transcriptAssetId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("entryId")) {
                this.entryId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("voicebaseApiKey")) {
                this.voicebaseApiKey = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("voicebaseApiPassword")) {
                this.voicebaseApiPassword = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaDexterIntegrationJobProviderData");
        kparams.add("metadataProfileId", this.metadataProfileId);
        kparams.add("transcriptAssetId", this.transcriptAssetId);
        kparams.add("entryId", this.entryId);
        kparams.add("voicebaseApiKey", this.voicebaseApiKey);
        kparams.add("voicebaseApiPassword", this.voicebaseApiPassword);
        return kparams;
    }

}

