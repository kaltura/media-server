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
import com.kaltura.client.enums.KalturaEventNotificationTemplateType;
import com.kaltura.client.enums.KalturaEventNotificationTemplateStatus;
import com.kaltura.client.enums.KalturaEventNotificationEventType;
import com.kaltura.client.enums.KalturaEventNotificationEventObjectType;
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
public class KalturaEventNotificationTemplate extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public String name;
    public String systemName;
    public String description;
    public KalturaEventNotificationTemplateType type;
    public KalturaEventNotificationTemplateStatus status;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;
	/**  Define that the template could be dispatched manually from the API  */
    public boolean manualDispatchEnabled;
	/**  Define that the template could be dispatched automatically by the system  */
    public boolean automaticDispatchEnabled;
	/**  Define the event that should trigger this notification  */
    public KalturaEventNotificationEventType eventType;
	/**  Define the object that raied the event that should trigger this notification  */
    public KalturaEventNotificationEventObjectType eventObjectType;
	/**  Define the conditions that cause this notification to be triggered  */
    public ArrayList<KalturaCondition> eventConditions;
	/**  Define the content dynamic parameters  */
    public ArrayList<KalturaEventNotificationParameter> contentParameters;
	/**  Define the content dynamic parameters  */
    public ArrayList<KalturaEventNotificationParameter> userParameters;

    public KalturaEventNotificationTemplate() {
    }

    public KalturaEventNotificationTemplate(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("systemName")) {
                this.systemName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("description")) {
                this.description = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("type")) {
                this.type = KalturaEventNotificationTemplateType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaEventNotificationTemplateStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAt")) {
                this.updatedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("manualDispatchEnabled")) {
                this.manualDispatchEnabled = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("automaticDispatchEnabled")) {
                this.automaticDispatchEnabled = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("eventType")) {
                this.eventType = KalturaEventNotificationEventType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("eventObjectType")) {
                this.eventObjectType = KalturaEventNotificationEventObjectType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("eventConditions")) {
                this.eventConditions = ParseUtils.parseArray(KalturaCondition.class, aNode);
                continue;
            } else if (nodeName.equals("contentParameters")) {
                this.contentParameters = ParseUtils.parseArray(KalturaEventNotificationParameter.class, aNode);
                continue;
            } else if (nodeName.equals("userParameters")) {
                this.userParameters = ParseUtils.parseArray(KalturaEventNotificationParameter.class, aNode);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaEventNotificationTemplate");
        kparams.add("name", this.name);
        kparams.add("systemName", this.systemName);
        kparams.add("description", this.description);
        kparams.add("type", this.type);
        kparams.add("manualDispatchEnabled", this.manualDispatchEnabled);
        kparams.add("automaticDispatchEnabled", this.automaticDispatchEnabled);
        kparams.add("eventType", this.eventType);
        kparams.add("eventObjectType", this.eventObjectType);
        kparams.add("eventConditions", this.eventConditions);
        kparams.add("contentParameters", this.contentParameters);
        kparams.add("userParameters", this.userParameters);
        return kparams;
    }

}

