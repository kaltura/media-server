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
import com.kaltura.client.enums.KalturaDistributionProtocol;
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
public class KalturaHuluDistributionProfile extends KalturaConfigurableDistributionProfile {
    public String sftpHost;
    public String sftpLogin;
    public String sftpPass;
    public String seriesChannel;
    public String seriesPrimaryCategory;
    public ArrayList<KalturaString> seriesAdditionalCategories;
    public String seasonNumber;
    public String seasonSynopsis;
    public String seasonTuneInInformation;
    public String videoMediaType;
    public boolean disableEpisodeNumberCustomValidation;
    public KalturaDistributionProtocol protocol;
    public String asperaHost;
    public String asperaLogin;
    public String asperaPass;
    public int port = Integer.MIN_VALUE;
    public String passphrase;
    public String asperaPublicKey;
    public String asperaPrivateKey;

    public KalturaHuluDistributionProfile() {
    }

    public KalturaHuluDistributionProfile(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("seriesChannel")) {
                this.seriesChannel = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("seriesPrimaryCategory")) {
                this.seriesPrimaryCategory = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("seriesAdditionalCategories")) {
                this.seriesAdditionalCategories = ParseUtils.parseArray(KalturaString.class, aNode);
                continue;
            } else if (nodeName.equals("seasonNumber")) {
                this.seasonNumber = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("seasonSynopsis")) {
                this.seasonSynopsis = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("seasonTuneInInformation")) {
                this.seasonTuneInInformation = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("videoMediaType")) {
                this.videoMediaType = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("disableEpisodeNumberCustomValidation")) {
                this.disableEpisodeNumberCustomValidation = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("protocol")) {
                this.protocol = KalturaDistributionProtocol.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("asperaHost")) {
                this.asperaHost = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("asperaLogin")) {
                this.asperaLogin = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("asperaPass")) {
                this.asperaPass = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("port")) {
                this.port = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("passphrase")) {
                this.passphrase = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("asperaPublicKey")) {
                this.asperaPublicKey = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("asperaPrivateKey")) {
                this.asperaPrivateKey = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaHuluDistributionProfile");
        kparams.add("sftpHost", this.sftpHost);
        kparams.add("sftpLogin", this.sftpLogin);
        kparams.add("sftpPass", this.sftpPass);
        kparams.add("seriesChannel", this.seriesChannel);
        kparams.add("seriesPrimaryCategory", this.seriesPrimaryCategory);
        kparams.add("seriesAdditionalCategories", this.seriesAdditionalCategories);
        kparams.add("seasonNumber", this.seasonNumber);
        kparams.add("seasonSynopsis", this.seasonSynopsis);
        kparams.add("seasonTuneInInformation", this.seasonTuneInInformation);
        kparams.add("videoMediaType", this.videoMediaType);
        kparams.add("disableEpisodeNumberCustomValidation", this.disableEpisodeNumberCustomValidation);
        kparams.add("protocol", this.protocol);
        kparams.add("asperaHost", this.asperaHost);
        kparams.add("asperaLogin", this.asperaLogin);
        kparams.add("asperaPass", this.asperaPass);
        kparams.add("port", this.port);
        kparams.add("passphrase", this.passphrase);
        kparams.add("asperaPublicKey", this.asperaPublicKey);
        kparams.add("asperaPrivateKey", this.asperaPrivateKey);
        return kparams;
    }

}

