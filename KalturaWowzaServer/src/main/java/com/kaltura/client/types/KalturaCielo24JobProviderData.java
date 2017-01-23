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
import com.kaltura.client.enums.KalturaCielo24Priority;
import com.kaltura.client.enums.KalturaCielo24Fidelity;
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
public class KalturaCielo24JobProviderData extends KalturaIntegrationJobProviderData {
	/**  Entry ID  */
    public String entryId;
	/**  Flavor ID  */
    public String flavorAssetId;
	/**  Caption formats  */
    public String captionAssetFormats;
    public KalturaCielo24Priority priority;
    public KalturaCielo24Fidelity fidelity;
	/**  Api key for service provider  */
    public String username;
	/**  Api key for service provider  */
    public String password;
	/**  Base url for service provider  */
    public String baseUrl;
	/**  Transcript content language  */
    public KalturaLanguage spokenLanguage;
	/**  should replace remote media content  */
    public boolean replaceMediaContent;

    public KalturaCielo24JobProviderData() {
    }

    public KalturaCielo24JobProviderData(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("entryId")) {
                this.entryId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("flavorAssetId")) {
                this.flavorAssetId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("captionAssetFormats")) {
                this.captionAssetFormats = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("priority")) {
                this.priority = KalturaCielo24Priority.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("fidelity")) {
                this.fidelity = KalturaCielo24Fidelity.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("username")) {
                this.username = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("password")) {
                this.password = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("baseUrl")) {
                this.baseUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("spokenLanguage")) {
                this.spokenLanguage = KalturaLanguage.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("replaceMediaContent")) {
                this.replaceMediaContent = ParseUtils.parseBool(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaCielo24JobProviderData");
        kparams.add("entryId", this.entryId);
        kparams.add("flavorAssetId", this.flavorAssetId);
        kparams.add("captionAssetFormats", this.captionAssetFormats);
        kparams.add("priority", this.priority);
        kparams.add("fidelity", this.fidelity);
        kparams.add("spokenLanguage", this.spokenLanguage);
        kparams.add("replaceMediaContent", this.replaceMediaContent);
        return kparams;
    }

}

