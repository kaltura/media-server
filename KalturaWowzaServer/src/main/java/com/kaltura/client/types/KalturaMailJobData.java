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
import com.kaltura.client.enums.KalturaMailType;
import com.kaltura.client.enums.KalturaMailJobStatus;
import com.kaltura.client.enums.KalturaLanguageCode;
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
public class KalturaMailJobData extends KalturaJobData {
    public KalturaMailType mailType;
    public int mailPriority = Integer.MIN_VALUE;
    public KalturaMailJobStatus status;
    public String recipientName;
    public String recipientEmail;
	/**  kuserId  */
    public int recipientId = Integer.MIN_VALUE;
    public String fromName;
    public String fromEmail;
    public String bodyParams;
    public String subjectParams;
    public String templatePath;
    public KalturaLanguageCode language;
    public int campaignId = Integer.MIN_VALUE;
    public int minSendDate = Integer.MIN_VALUE;
    public boolean isHtml;
    public String separator;

    public KalturaMailJobData() {
    }

    public KalturaMailJobData(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("mailType")) {
                this.mailType = KalturaMailType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("mailPriority")) {
                this.mailPriority = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaMailJobStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("recipientName")) {
                this.recipientName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("recipientEmail")) {
                this.recipientEmail = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("recipientId")) {
                this.recipientId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("fromName")) {
                this.fromName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("fromEmail")) {
                this.fromEmail = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("bodyParams")) {
                this.bodyParams = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("subjectParams")) {
                this.subjectParams = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("templatePath")) {
                this.templatePath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("language")) {
                this.language = KalturaLanguageCode.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("campaignId")) {
                this.campaignId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("minSendDate")) {
                this.minSendDate = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("isHtml")) {
                this.isHtml = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("separator")) {
                this.separator = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaMailJobData");
        kparams.add("mailType", this.mailType);
        kparams.add("mailPriority", this.mailPriority);
        kparams.add("status", this.status);
        kparams.add("recipientName", this.recipientName);
        kparams.add("recipientEmail", this.recipientEmail);
        kparams.add("recipientId", this.recipientId);
        kparams.add("fromName", this.fromName);
        kparams.add("fromEmail", this.fromEmail);
        kparams.add("bodyParams", this.bodyParams);
        kparams.add("subjectParams", this.subjectParams);
        kparams.add("templatePath", this.templatePath);
        kparams.add("language", this.language);
        kparams.add("campaignId", this.campaignId);
        kparams.add("minSendDate", this.minSendDate);
        kparams.add("isHtml", this.isHtml);
        kparams.add("separator", this.separator);
        return kparams;
    }

}

