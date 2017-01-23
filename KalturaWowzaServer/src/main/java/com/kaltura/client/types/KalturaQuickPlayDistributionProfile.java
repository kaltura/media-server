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
public class KalturaQuickPlayDistributionProfile extends KalturaConfigurableDistributionProfile {
    public String sftpHost;
    public String sftpLogin;
    public String sftpPass;
    public String sftpBasePath;
    public String channelTitle;
    public String channelLink;
    public String channelDescription;
    public String channelManagingEditor;
    public String channelLanguage;
    public String channelImageTitle;
    public String channelImageWidth;
    public String channelImageHeight;
    public String channelImageLink;
    public String channelImageUrl;
    public String channelCopyright;
    public String channelGenerator;
    public String channelRating;

    public KalturaQuickPlayDistributionProfile() {
    }

    public KalturaQuickPlayDistributionProfile(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("sftpHost")) {
                this.sftpHost = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sftpLogin")) {
                this.sftpLogin = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sftpPass")) {
                this.sftpPass = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sftpBasePath")) {
                this.sftpBasePath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("channelTitle")) {
                this.channelTitle = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("channelLink")) {
                this.channelLink = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("channelDescription")) {
                this.channelDescription = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("channelManagingEditor")) {
                this.channelManagingEditor = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("channelLanguage")) {
                this.channelLanguage = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("channelImageTitle")) {
                this.channelImageTitle = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("channelImageWidth")) {
                this.channelImageWidth = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("channelImageHeight")) {
                this.channelImageHeight = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("channelImageLink")) {
                this.channelImageLink = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("channelImageUrl")) {
                this.channelImageUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("channelCopyright")) {
                this.channelCopyright = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("channelGenerator")) {
                this.channelGenerator = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("channelRating")) {
                this.channelRating = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaQuickPlayDistributionProfile");
        kparams.add("sftpHost", this.sftpHost);
        kparams.add("sftpLogin", this.sftpLogin);
        kparams.add("sftpPass", this.sftpPass);
        kparams.add("sftpBasePath", this.sftpBasePath);
        kparams.add("channelTitle", this.channelTitle);
        kparams.add("channelLink", this.channelLink);
        kparams.add("channelDescription", this.channelDescription);
        kparams.add("channelManagingEditor", this.channelManagingEditor);
        kparams.add("channelLanguage", this.channelLanguage);
        kparams.add("channelImageTitle", this.channelImageTitle);
        kparams.add("channelImageWidth", this.channelImageWidth);
        kparams.add("channelImageHeight", this.channelImageHeight);
        kparams.add("channelImageLink", this.channelImageLink);
        kparams.add("channelImageUrl", this.channelImageUrl);
        kparams.add("channelCopyright", this.channelCopyright);
        kparams.add("channelGenerator", this.channelGenerator);
        kparams.add("channelRating", this.channelRating);
        return kparams;
    }

}

