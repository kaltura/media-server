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
import com.kaltura.client.enums.KalturaWidgetSecurityType;
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
public class KalturaWidget extends KalturaObjectBase {
    public String id;
    public String sourceWidgetId;
    public String rootWidgetId;
    public int partnerId = Integer.MIN_VALUE;
    public String entryId;
    public int uiConfId = Integer.MIN_VALUE;
    public KalturaWidgetSecurityType securityType;
    public int securityPolicy = Integer.MIN_VALUE;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;
	/**  Can be used to store various partner related data as a string  */
    public String partnerData;
    public String widgetHTML;
	/**  Should enforce entitlement on feed entries  */
    public boolean enforceEntitlement;
	/**  Set privacy context for search entries that assiged to private and public
	  categories within a category privacy context.  */
    public String privacyContext;
	/**  Addes the HTML5 script line to the widget's embed code  */
    public boolean addEmbedHtml5Support;
    public String roles;

    public KalturaWidget() {
    }

    public KalturaWidget(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sourceWidgetId")) {
                this.sourceWidgetId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("rootWidgetId")) {
                this.rootWidgetId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerId")) {
                this.partnerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("entryId")) {
                this.entryId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("uiConfId")) {
                this.uiConfId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("securityType")) {
                this.securityType = KalturaWidgetSecurityType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("securityPolicy")) {
                this.securityPolicy = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAt")) {
                this.updatedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerData")) {
                this.partnerData = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("widgetHTML")) {
                this.widgetHTML = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("enforceEntitlement")) {
                this.enforceEntitlement = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("privacyContext")) {
                this.privacyContext = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("addEmbedHtml5Support")) {
                this.addEmbedHtml5Support = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("roles")) {
                this.roles = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaWidget");
        kparams.add("sourceWidgetId", this.sourceWidgetId);
        kparams.add("entryId", this.entryId);
        kparams.add("uiConfId", this.uiConfId);
        kparams.add("securityType", this.securityType);
        kparams.add("securityPolicy", this.securityPolicy);
        kparams.add("partnerData", this.partnerData);
        kparams.add("enforceEntitlement", this.enforceEntitlement);
        kparams.add("privacyContext", this.privacyContext);
        kparams.add("addEmbedHtml5Support", this.addEmbedHtml5Support);
        kparams.add("roles", this.roles);
        return kparams;
    }

}

