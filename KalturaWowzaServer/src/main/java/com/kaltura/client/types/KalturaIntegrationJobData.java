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
import com.kaltura.client.enums.KalturaIntegrationProviderType;
import com.kaltura.client.enums.KalturaIntegrationTriggerType;
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
public class KalturaIntegrationJobData extends KalturaJobData {
    public String callbackNotificationUrl;
    public KalturaIntegrationProviderType providerType;
	/**  Additional data that relevant for the provider only  */
    public KalturaIntegrationJobProviderData providerData;
    public KalturaIntegrationTriggerType triggerType;
	/**  Additional data that relevant for the trigger only  */
    public KalturaIntegrationJobTriggerData triggerData;

    public KalturaIntegrationJobData() {
    }

    public KalturaIntegrationJobData(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("callbackNotificationUrl")) {
                this.callbackNotificationUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("providerType")) {
                this.providerType = KalturaIntegrationProviderType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("providerData")) {
                this.providerData = ParseUtils.parseObject(KalturaIntegrationJobProviderData.class, aNode);
                continue;
            } else if (nodeName.equals("triggerType")) {
                this.triggerType = KalturaIntegrationTriggerType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("triggerData")) {
                this.triggerData = ParseUtils.parseObject(KalturaIntegrationJobTriggerData.class, aNode);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaIntegrationJobData");
        kparams.add("providerType", this.providerType);
        kparams.add("providerData", this.providerData);
        kparams.add("triggerType", this.triggerType);
        kparams.add("triggerData", this.triggerData);
        return kparams;
    }

}

