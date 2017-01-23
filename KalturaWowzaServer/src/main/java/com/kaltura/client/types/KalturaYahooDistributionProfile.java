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
import com.kaltura.client.enums.KalturaYahooDistributionProcessFeedActionStatus;
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
public class KalturaYahooDistributionProfile extends KalturaConfigurableDistributionProfile {
    public String ftpPath;
    public String ftpUsername;
    public String ftpPassword;
    public String ftpHost;
    public String contactTelephone;
    public String contactEmail;
    public KalturaYahooDistributionProcessFeedActionStatus processFeed;

    public KalturaYahooDistributionProfile() {
    }

    public KalturaYahooDistributionProfile(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("ftpPath")) {
                this.ftpPath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("ftpUsername")) {
                this.ftpUsername = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("ftpPassword")) {
                this.ftpPassword = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("ftpHost")) {
                this.ftpHost = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("contactTelephone")) {
                this.contactTelephone = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("contactEmail")) {
                this.contactEmail = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("processFeed")) {
                this.processFeed = KalturaYahooDistributionProcessFeedActionStatus.get(ParseUtils.parseInt(txt));
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaYahooDistributionProfile");
        kparams.add("ftpPath", this.ftpPath);
        kparams.add("ftpUsername", this.ftpUsername);
        kparams.add("ftpPassword", this.ftpPassword);
        kparams.add("ftpHost", this.ftpHost);
        kparams.add("contactTelephone", this.contactTelephone);
        kparams.add("contactEmail", this.contactEmail);
        kparams.add("processFeed", this.processFeed);
        return kparams;
    }

}

