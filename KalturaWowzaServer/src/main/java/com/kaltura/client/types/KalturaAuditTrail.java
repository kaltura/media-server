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
import com.kaltura.client.enums.KalturaAuditTrailStatus;
import com.kaltura.client.enums.KalturaAuditTrailObjectType;
import com.kaltura.client.enums.KalturaAuditTrailAction;
import com.kaltura.client.enums.KalturaAuditTrailContext;
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
public class KalturaAuditTrail extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public int createdAt = Integer.MIN_VALUE;
	/**  Indicates when the data was parsed  */
    public int parsedAt = Integer.MIN_VALUE;
    public KalturaAuditTrailStatus status;
    public KalturaAuditTrailObjectType auditObjectType;
    public String objectId;
    public String relatedObjectId;
    public KalturaAuditTrailObjectType relatedObjectType;
    public String entryId;
    public int masterPartnerId = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public String requestId;
    public String userId;
    public KalturaAuditTrailAction action;
    public KalturaAuditTrailInfo data;
    public String ks;
    public KalturaAuditTrailContext context;
	/**  The API service and action that called and caused this audit  */
    public String entryPoint;
    public String serverName;
    public String ipAddress;
    public String userAgent;
    public String clientTag;
    public String description;
    public String errorDescription;

    public KalturaAuditTrail() {
    }

    public KalturaAuditTrail(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("parsedAt")) {
                this.parsedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaAuditTrailStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("auditObjectType")) {
                this.auditObjectType = KalturaAuditTrailObjectType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("objectId")) {
                this.objectId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("relatedObjectId")) {
                this.relatedObjectId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("relatedObjectType")) {
                this.relatedObjectType = KalturaAuditTrailObjectType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("entryId")) {
                this.entryId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("masterPartnerId")) {
                this.masterPartnerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerId")) {
                this.partnerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("requestId")) {
                this.requestId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("userId")) {
                this.userId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("action")) {
                this.action = KalturaAuditTrailAction.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("data")) {
                this.data = ParseUtils.parseObject(KalturaAuditTrailInfo.class, aNode);
                continue;
            } else if (nodeName.equals("ks")) {
                this.ks = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("context")) {
                this.context = KalturaAuditTrailContext.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("entryPoint")) {
                this.entryPoint = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("serverName")) {
                this.serverName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("ipAddress")) {
                this.ipAddress = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("userAgent")) {
                this.userAgent = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("clientTag")) {
                this.clientTag = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("description")) {
                this.description = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("errorDescription")) {
                this.errorDescription = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaAuditTrail");
        kparams.add("auditObjectType", this.auditObjectType);
        kparams.add("objectId", this.objectId);
        kparams.add("relatedObjectId", this.relatedObjectId);
        kparams.add("relatedObjectType", this.relatedObjectType);
        kparams.add("entryId", this.entryId);
        kparams.add("userId", this.userId);
        kparams.add("action", this.action);
        kparams.add("data", this.data);
        kparams.add("clientTag", this.clientTag);
        kparams.add("description", this.description);
        return kparams;
    }

}

