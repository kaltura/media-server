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
public class KalturaComcastMrssDistributionProfile extends KalturaConfigurableDistributionProfile {
    public int metadataProfileId = Integer.MIN_VALUE;
    public String feedUrl;
    public String feedTitle;
    public String feedLink;
    public String feedDescription;
    public String feedLastBuildDate;
    public String itemLink;
    public ArrayList<KalturaKeyValue> cPlatformTvSeries;
    public String cPlatformTvSeriesField;
    public boolean shouldIncludeCuePoints;
    public boolean shouldIncludeCaptions;
    public boolean shouldAddThumbExtension;

    public KalturaComcastMrssDistributionProfile() {
    }

    public KalturaComcastMrssDistributionProfile(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("metadataProfileId")) {
                this.metadataProfileId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("feedUrl")) {
                this.feedUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("feedTitle")) {
                this.feedTitle = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("feedLink")) {
                this.feedLink = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("feedDescription")) {
                this.feedDescription = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("feedLastBuildDate")) {
                this.feedLastBuildDate = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("itemLink")) {
                this.itemLink = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("cPlatformTvSeries")) {
                this.cPlatformTvSeries = ParseUtils.parseArray(KalturaKeyValue.class, aNode);
                continue;
            } else if (nodeName.equals("cPlatformTvSeriesField")) {
                this.cPlatformTvSeriesField = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("shouldIncludeCuePoints")) {
                this.shouldIncludeCuePoints = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("shouldIncludeCaptions")) {
                this.shouldIncludeCaptions = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("shouldAddThumbExtension")) {
                this.shouldAddThumbExtension = ParseUtils.parseBool(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaComcastMrssDistributionProfile");
        kparams.add("metadataProfileId", this.metadataProfileId);
        kparams.add("feedTitle", this.feedTitle);
        kparams.add("feedLink", this.feedLink);
        kparams.add("feedDescription", this.feedDescription);
        kparams.add("feedLastBuildDate", this.feedLastBuildDate);
        kparams.add("itemLink", this.itemLink);
        kparams.add("cPlatformTvSeries", this.cPlatformTvSeries);
        kparams.add("cPlatformTvSeriesField", this.cPlatformTvSeriesField);
        kparams.add("shouldIncludeCuePoints", this.shouldIncludeCuePoints);
        kparams.add("shouldIncludeCaptions", this.shouldIncludeCaptions);
        kparams.add("shouldAddThumbExtension", this.shouldAddThumbExtension);
        return kparams;
    }

}

