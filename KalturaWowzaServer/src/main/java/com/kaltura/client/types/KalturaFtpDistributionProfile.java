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
public class KalturaFtpDistributionProfile extends KalturaConfigurableDistributionProfile {
    public KalturaDistributionProtocol protocol;
    public String host;
    public int port = Integer.MIN_VALUE;
    public String basePath;
    public String username;
    public String password;
    public String passphrase;
    public String sftpPublicKey;
    public String sftpPrivateKey;
    public boolean disableMetadata;
    public String metadataXslt;
    public String metadataFilenameXslt;
    public String flavorAssetFilenameXslt;
    public String thumbnailAssetFilenameXslt;
    public String assetFilenameXslt;
    public String asperaPublicKey;
    public String asperaPrivateKey;
    public boolean sendMetadataAfterAssets;

    public KalturaFtpDistributionProfile() {
    }

    public KalturaFtpDistributionProfile(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("protocol")) {
                this.protocol = KalturaDistributionProtocol.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("host")) {
                this.host = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("port")) {
                this.port = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("basePath")) {
                this.basePath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("username")) {
                this.username = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("password")) {
                this.password = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("passphrase")) {
                this.passphrase = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sftpPublicKey")) {
                this.sftpPublicKey = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sftpPrivateKey")) {
                this.sftpPrivateKey = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("disableMetadata")) {
                this.disableMetadata = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("metadataXslt")) {
                this.metadataXslt = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("metadataFilenameXslt")) {
                this.metadataFilenameXslt = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("flavorAssetFilenameXslt")) {
                this.flavorAssetFilenameXslt = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("thumbnailAssetFilenameXslt")) {
                this.thumbnailAssetFilenameXslt = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("assetFilenameXslt")) {
                this.assetFilenameXslt = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("asperaPublicKey")) {
                this.asperaPublicKey = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("asperaPrivateKey")) {
                this.asperaPrivateKey = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sendMetadataAfterAssets")) {
                this.sendMetadataAfterAssets = ParseUtils.parseBool(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaFtpDistributionProfile");
        kparams.add("protocol", this.protocol);
        kparams.add("host", this.host);
        kparams.add("port", this.port);
        kparams.add("basePath", this.basePath);
        kparams.add("username", this.username);
        kparams.add("password", this.password);
        kparams.add("passphrase", this.passphrase);
        kparams.add("sftpPublicKey", this.sftpPublicKey);
        kparams.add("sftpPrivateKey", this.sftpPrivateKey);
        kparams.add("disableMetadata", this.disableMetadata);
        kparams.add("metadataXslt", this.metadataXslt);
        kparams.add("metadataFilenameXslt", this.metadataFilenameXslt);
        kparams.add("flavorAssetFilenameXslt", this.flavorAssetFilenameXslt);
        kparams.add("thumbnailAssetFilenameXslt", this.thumbnailAssetFilenameXslt);
        kparams.add("assetFilenameXslt", this.assetFilenameXslt);
        kparams.add("asperaPublicKey", this.asperaPublicKey);
        kparams.add("asperaPrivateKey", this.asperaPrivateKey);
        kparams.add("sendMetadataAfterAssets", this.sendMetadataAfterAssets);
        return kparams;
    }

}

