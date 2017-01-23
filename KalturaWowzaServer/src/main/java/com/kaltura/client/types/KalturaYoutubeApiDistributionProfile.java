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
public class KalturaYoutubeApiDistributionProfile extends KalturaConfigurableDistributionProfile {
    public String username;
    public int defaultCategory = Integer.MIN_VALUE;
    public String allowComments;
    public String allowEmbedding;
    public String allowRatings;
    public String allowResponses;
    public String apiAuthorizeUrl;
    public String googleClientId;
    public String googleClientSecret;
    public String googleTokenData;
    public boolean assumeSuccess;
    public String privacyStatus;

    public KalturaYoutubeApiDistributionProfile() {
    }

    public KalturaYoutubeApiDistributionProfile(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("username")) {
                this.username = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("defaultCategory")) {
                this.defaultCategory = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("allowComments")) {
                this.allowComments = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("allowEmbedding")) {
                this.allowEmbedding = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("allowRatings")) {
                this.allowRatings = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("allowResponses")) {
                this.allowResponses = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("apiAuthorizeUrl")) {
                this.apiAuthorizeUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("googleClientId")) {
                this.googleClientId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("googleClientSecret")) {
                this.googleClientSecret = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("googleTokenData")) {
                this.googleTokenData = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("assumeSuccess")) {
                this.assumeSuccess = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("privacyStatus")) {
                this.privacyStatus = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaYoutubeApiDistributionProfile");
        kparams.add("username", this.username);
        kparams.add("defaultCategory", this.defaultCategory);
        kparams.add("allowComments", this.allowComments);
        kparams.add("allowEmbedding", this.allowEmbedding);
        kparams.add("allowRatings", this.allowRatings);
        kparams.add("allowResponses", this.allowResponses);
        kparams.add("apiAuthorizeUrl", this.apiAuthorizeUrl);
        kparams.add("googleClientId", this.googleClientId);
        kparams.add("googleClientSecret", this.googleClientSecret);
        kparams.add("googleTokenData", this.googleTokenData);
        kparams.add("assumeSuccess", this.assumeSuccess);
        kparams.add("privacyStatus", this.privacyStatus);
        return kparams;
    }

}

