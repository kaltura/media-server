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
import com.kaltura.client.enums.KalturaDVRStatus;
import com.kaltura.client.enums.KalturaAkamaiUniversalStreamType;
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
public class KalturaAkamaiUniversalProvisionJobData extends KalturaProvisionJobData {
    public int streamId = Integer.MIN_VALUE;
    public String systemUserName;
    public String systemPassword;
    public String domainName;
    public KalturaDVRStatus dvrEnabled;
    public int dvrWindow = Integer.MIN_VALUE;
    public String primaryContact;
    public String secondaryContact;
    public KalturaAkamaiUniversalStreamType streamType;
    public String notificationEmail;

    public KalturaAkamaiUniversalProvisionJobData() {
    }

    public KalturaAkamaiUniversalProvisionJobData(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("streamId")) {
                this.streamId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("systemUserName")) {
                this.systemUserName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("systemPassword")) {
                this.systemPassword = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("domainName")) {
                this.domainName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("dvrEnabled")) {
                this.dvrEnabled = KalturaDVRStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("dvrWindow")) {
                this.dvrWindow = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("primaryContact")) {
                this.primaryContact = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("secondaryContact")) {
                this.secondaryContact = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("streamType")) {
                this.streamType = KalturaAkamaiUniversalStreamType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("notificationEmail")) {
                this.notificationEmail = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaAkamaiUniversalProvisionJobData");
        kparams.add("streamId", this.streamId);
        kparams.add("systemUserName", this.systemUserName);
        kparams.add("systemPassword", this.systemPassword);
        kparams.add("domainName", this.domainName);
        kparams.add("dvrEnabled", this.dvrEnabled);
        kparams.add("dvrWindow", this.dvrWindow);
        kparams.add("primaryContact", this.primaryContact);
        kparams.add("secondaryContact", this.secondaryContact);
        kparams.add("streamType", this.streamType);
        kparams.add("notificationEmail", this.notificationEmail);
        return kparams;
    }

}

