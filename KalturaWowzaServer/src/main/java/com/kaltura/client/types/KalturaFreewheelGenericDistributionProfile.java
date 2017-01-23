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
public class KalturaFreewheelGenericDistributionProfile extends KalturaConfigurableDistributionProfile {
    public String apikey;
    public String email;
    public String sftpPass;
    public String sftpLogin;
    public String contentOwner;
    public String upstreamVideoId;
    public String upstreamNetworkName;
    public String upstreamNetworkId;
    public String categoryId;
    public boolean replaceGroup;
    public boolean replaceAirDates;

    public KalturaFreewheelGenericDistributionProfile() {
    }

    public KalturaFreewheelGenericDistributionProfile(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("apikey")) {
                this.apikey = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("email")) {
                this.email = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sftpPass")) {
                this.sftpPass = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sftpLogin")) {
                this.sftpLogin = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("contentOwner")) {
                this.contentOwner = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("upstreamVideoId")) {
                this.upstreamVideoId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("upstreamNetworkName")) {
                this.upstreamNetworkName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("upstreamNetworkId")) {
                this.upstreamNetworkId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("categoryId")) {
                this.categoryId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("replaceGroup")) {
                this.replaceGroup = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("replaceAirDates")) {
                this.replaceAirDates = ParseUtils.parseBool(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaFreewheelGenericDistributionProfile");
        kparams.add("apikey", this.apikey);
        kparams.add("email", this.email);
        kparams.add("sftpPass", this.sftpPass);
        kparams.add("sftpLogin", this.sftpLogin);
        kparams.add("contentOwner", this.contentOwner);
        kparams.add("upstreamVideoId", this.upstreamVideoId);
        kparams.add("upstreamNetworkName", this.upstreamNetworkName);
        kparams.add("upstreamNetworkId", this.upstreamNetworkId);
        kparams.add("categoryId", this.categoryId);
        kparams.add("replaceGroup", this.replaceGroup);
        kparams.add("replaceAirDates", this.replaceAirDates);
        return kparams;
    }

}

