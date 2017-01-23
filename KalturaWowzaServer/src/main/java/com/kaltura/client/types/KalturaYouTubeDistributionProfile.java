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
import com.kaltura.client.enums.KalturaYouTubeDistributionFeedSpecVersion;
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
public class KalturaYouTubeDistributionProfile extends KalturaConfigurableDistributionProfile {
    public KalturaYouTubeDistributionFeedSpecVersion feedSpecVersion;
    public String username;
    public String notificationEmail;
    public String sftpHost;
    public int sftpPort = Integer.MIN_VALUE;
    public String sftpLogin;
    public String sftpPublicKey;
    public String sftpPrivateKey;
    public String sftpBaseDir;
    public String ownerName;
    public String defaultCategory;
    public String allowComments;
    public String allowEmbedding;
    public String allowRatings;
    public String allowResponses;
    public String commercialPolicy;
    public String ugcPolicy;
    public String target;
    public String adServerPartnerId;
    public boolean enableAdServer;
    public boolean allowPreRollAds;
    public boolean allowPostRollAds;
    public String strict;
    public String overrideManualEdits;
    public String urgentReference;
    public String allowSyndication;
    public String hideViewCount;
    public String allowAdsenseForVideo;
    public String allowInvideo;
    public boolean allowMidRollAds;
    public String instreamStandard;
    public String instreamTrueview;
    public String claimType;
    public String blockOutsideOwnership;
    public String captionAutosync;
    public boolean deleteReference;
    public boolean releaseClaims;
    public String apiAuthorizeUrl;

    public KalturaYouTubeDistributionProfile() {
    }

    public KalturaYouTubeDistributionProfile(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("feedSpecVersion")) {
                this.feedSpecVersion = KalturaYouTubeDistributionFeedSpecVersion.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("username")) {
                this.username = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("notificationEmail")) {
                this.notificationEmail = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sftpHost")) {
                this.sftpHost = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sftpPort")) {
                this.sftpPort = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("sftpLogin")) {
                this.sftpLogin = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sftpPublicKey")) {
                this.sftpPublicKey = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sftpPrivateKey")) {
                this.sftpPrivateKey = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sftpBaseDir")) {
                this.sftpBaseDir = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("ownerName")) {
                this.ownerName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("defaultCategory")) {
                this.defaultCategory = ParseUtils.parseString(txt);
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
            } else if (nodeName.equals("commercialPolicy")) {
                this.commercialPolicy = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("ugcPolicy")) {
                this.ugcPolicy = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("target")) {
                this.target = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("adServerPartnerId")) {
                this.adServerPartnerId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("enableAdServer")) {
                this.enableAdServer = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("allowPreRollAds")) {
                this.allowPreRollAds = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("allowPostRollAds")) {
                this.allowPostRollAds = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("strict")) {
                this.strict = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("overrideManualEdits")) {
                this.overrideManualEdits = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("urgentReference")) {
                this.urgentReference = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("allowSyndication")) {
                this.allowSyndication = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("hideViewCount")) {
                this.hideViewCount = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("allowAdsenseForVideo")) {
                this.allowAdsenseForVideo = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("allowInvideo")) {
                this.allowInvideo = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("allowMidRollAds")) {
                this.allowMidRollAds = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("instreamStandard")) {
                this.instreamStandard = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("instreamTrueview")) {
                this.instreamTrueview = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("claimType")) {
                this.claimType = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("blockOutsideOwnership")) {
                this.blockOutsideOwnership = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("captionAutosync")) {
                this.captionAutosync = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("deleteReference")) {
                this.deleteReference = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("releaseClaims")) {
                this.releaseClaims = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("apiAuthorizeUrl")) {
                this.apiAuthorizeUrl = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaYouTubeDistributionProfile");
        kparams.add("feedSpecVersion", this.feedSpecVersion);
        kparams.add("username", this.username);
        kparams.add("notificationEmail", this.notificationEmail);
        kparams.add("sftpHost", this.sftpHost);
        kparams.add("sftpPort", this.sftpPort);
        kparams.add("sftpLogin", this.sftpLogin);
        kparams.add("sftpPublicKey", this.sftpPublicKey);
        kparams.add("sftpPrivateKey", this.sftpPrivateKey);
        kparams.add("sftpBaseDir", this.sftpBaseDir);
        kparams.add("ownerName", this.ownerName);
        kparams.add("defaultCategory", this.defaultCategory);
        kparams.add("allowComments", this.allowComments);
        kparams.add("allowEmbedding", this.allowEmbedding);
        kparams.add("allowRatings", this.allowRatings);
        kparams.add("allowResponses", this.allowResponses);
        kparams.add("commercialPolicy", this.commercialPolicy);
        kparams.add("ugcPolicy", this.ugcPolicy);
        kparams.add("target", this.target);
        kparams.add("adServerPartnerId", this.adServerPartnerId);
        kparams.add("enableAdServer", this.enableAdServer);
        kparams.add("allowPreRollAds", this.allowPreRollAds);
        kparams.add("allowPostRollAds", this.allowPostRollAds);
        kparams.add("strict", this.strict);
        kparams.add("overrideManualEdits", this.overrideManualEdits);
        kparams.add("urgentReference", this.urgentReference);
        kparams.add("allowSyndication", this.allowSyndication);
        kparams.add("hideViewCount", this.hideViewCount);
        kparams.add("allowAdsenseForVideo", this.allowAdsenseForVideo);
        kparams.add("allowInvideo", this.allowInvideo);
        kparams.add("allowMidRollAds", this.allowMidRollAds);
        kparams.add("instreamStandard", this.instreamStandard);
        kparams.add("instreamTrueview", this.instreamTrueview);
        kparams.add("claimType", this.claimType);
        kparams.add("blockOutsideOwnership", this.blockOutsideOwnership);
        kparams.add("captionAutosync", this.captionAutosync);
        kparams.add("deleteReference", this.deleteReference);
        kparams.add("releaseClaims", this.releaseClaims);
        kparams.add("apiAuthorizeUrl", this.apiAuthorizeUrl);
        return kparams;
    }

}

