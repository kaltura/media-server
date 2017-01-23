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
public class KalturaCrossKalturaDistributionProfile extends KalturaConfigurableDistributionProfile {
    public String targetServiceUrl;
    public int targetAccountId = Integer.MIN_VALUE;
    public String targetLoginId;
    public String targetLoginPassword;
    public String metadataXslt;
    public ArrayList<KalturaStringValue> metadataXpathsTriggerUpdate;
    public boolean distributeCaptions;
    public boolean distributeCuePoints;
    public boolean distributeRemoteFlavorAssetContent;
    public boolean distributeRemoteThumbAssetContent;
    public boolean distributeRemoteCaptionAssetContent;
    public ArrayList<KalturaKeyValue> mapAccessControlProfileIds;
    public ArrayList<KalturaKeyValue> mapConversionProfileIds;
    public ArrayList<KalturaKeyValue> mapMetadataProfileIds;
    public ArrayList<KalturaKeyValue> mapStorageProfileIds;
    public ArrayList<KalturaKeyValue> mapFlavorParamsIds;
    public ArrayList<KalturaKeyValue> mapThumbParamsIds;
    public ArrayList<KalturaKeyValue> mapCaptionParamsIds;

    public KalturaCrossKalturaDistributionProfile() {
    }

    public KalturaCrossKalturaDistributionProfile(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("targetServiceUrl")) {
                this.targetServiceUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("targetAccountId")) {
                this.targetAccountId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("targetLoginId")) {
                this.targetLoginId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("targetLoginPassword")) {
                this.targetLoginPassword = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("metadataXslt")) {
                this.metadataXslt = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("metadataXpathsTriggerUpdate")) {
                this.metadataXpathsTriggerUpdate = ParseUtils.parseArray(KalturaStringValue.class, aNode);
                continue;
            } else if (nodeName.equals("distributeCaptions")) {
                this.distributeCaptions = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("distributeCuePoints")) {
                this.distributeCuePoints = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("distributeRemoteFlavorAssetContent")) {
                this.distributeRemoteFlavorAssetContent = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("distributeRemoteThumbAssetContent")) {
                this.distributeRemoteThumbAssetContent = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("distributeRemoteCaptionAssetContent")) {
                this.distributeRemoteCaptionAssetContent = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("mapAccessControlProfileIds")) {
                this.mapAccessControlProfileIds = ParseUtils.parseArray(KalturaKeyValue.class, aNode);
                continue;
            } else if (nodeName.equals("mapConversionProfileIds")) {
                this.mapConversionProfileIds = ParseUtils.parseArray(KalturaKeyValue.class, aNode);
                continue;
            } else if (nodeName.equals("mapMetadataProfileIds")) {
                this.mapMetadataProfileIds = ParseUtils.parseArray(KalturaKeyValue.class, aNode);
                continue;
            } else if (nodeName.equals("mapStorageProfileIds")) {
                this.mapStorageProfileIds = ParseUtils.parseArray(KalturaKeyValue.class, aNode);
                continue;
            } else if (nodeName.equals("mapFlavorParamsIds")) {
                this.mapFlavorParamsIds = ParseUtils.parseArray(KalturaKeyValue.class, aNode);
                continue;
            } else if (nodeName.equals("mapThumbParamsIds")) {
                this.mapThumbParamsIds = ParseUtils.parseArray(KalturaKeyValue.class, aNode);
                continue;
            } else if (nodeName.equals("mapCaptionParamsIds")) {
                this.mapCaptionParamsIds = ParseUtils.parseArray(KalturaKeyValue.class, aNode);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaCrossKalturaDistributionProfile");
        kparams.add("targetServiceUrl", this.targetServiceUrl);
        kparams.add("targetAccountId", this.targetAccountId);
        kparams.add("targetLoginId", this.targetLoginId);
        kparams.add("targetLoginPassword", this.targetLoginPassword);
        kparams.add("metadataXslt", this.metadataXslt);
        kparams.add("metadataXpathsTriggerUpdate", this.metadataXpathsTriggerUpdate);
        kparams.add("distributeCaptions", this.distributeCaptions);
        kparams.add("distributeCuePoints", this.distributeCuePoints);
        kparams.add("distributeRemoteFlavorAssetContent", this.distributeRemoteFlavorAssetContent);
        kparams.add("distributeRemoteThumbAssetContent", this.distributeRemoteThumbAssetContent);
        kparams.add("distributeRemoteCaptionAssetContent", this.distributeRemoteCaptionAssetContent);
        kparams.add("mapAccessControlProfileIds", this.mapAccessControlProfileIds);
        kparams.add("mapConversionProfileIds", this.mapConversionProfileIds);
        kparams.add("mapMetadataProfileIds", this.mapMetadataProfileIds);
        kparams.add("mapStorageProfileIds", this.mapStorageProfileIds);
        kparams.add("mapFlavorParamsIds", this.mapFlavorParamsIds);
        kparams.add("mapThumbParamsIds", this.mapThumbParamsIds);
        kparams.add("mapCaptionParamsIds", this.mapCaptionParamsIds);
        return kparams;
    }

}

