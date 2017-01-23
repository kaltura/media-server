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
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.enums.KalturaConversionProfileStatus;
import com.kaltura.client.enums.KalturaConversionProfileType;
import com.kaltura.client.enums.KalturaNullableBoolean;
import com.kaltura.client.enums.KalturaMediaParserType;
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
public class KalturaConversionProfile extends KalturaObjectBase {
	/**  The id of the Conversion Profile  */
    public int id = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public KalturaConversionProfileStatus status;
    public KalturaConversionProfileType type;
	/**  The name of the Conversion Profile  */
    public String name;
	/**  System name of the Conversion Profile  */
    public String systemName;
	/**  Comma separated tags  */
    public String tags;
	/**  The description of the Conversion Profile  */
    public String description;
	/**  ID of the default entry to be used for template data  */
    public String defaultEntryId;
	/**  Creation date as Unix timestamp (In seconds)  */
    public int createdAt = Integer.MIN_VALUE;
	/**  List of included flavor ids (comma separated)  */
    public String flavorParamsIds;
	/**  Indicates that this conversion profile is system default  */
    public KalturaNullableBoolean isDefault;
	/**  Indicates that this conversion profile is partner default  */
    public boolean isPartnerDefault;
	/**  Cropping dimensions  */
    public KalturaCropDimensions cropDimensions;
	/**  Clipping start position (in miliseconds)  */
    public int clipStart = Integer.MIN_VALUE;
	/**  Clipping duration (in miliseconds)  */
    public int clipDuration = Integer.MIN_VALUE;
	/**  XSL to transform ingestion MRSS XML  */
    public String xslTransformation;
	/**  ID of default storage profile to be used for linked net-storage file syncs  */
    public int storageProfileId = Integer.MIN_VALUE;
	/**  Media parser type to be used for extract media  */
    public KalturaMediaParserType mediaParserType;
	/**  Should calculate file conversion complexity  */
    public KalturaNullableBoolean calculateComplexity;
	/**  Defines the tags that should be used to define 'collective'/group/multi-flavor
	  processing,   like 'mbr' or 'ism'  */
    public String collectionTags;

    public KalturaConversionProfile() {
    }

    public KalturaConversionProfile(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerId")) {
                this.partnerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaConversionProfileStatus.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("type")) {
                this.type = KalturaConversionProfileType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("name")) {
                this.name = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("systemName")) {
                this.systemName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tags")) {
                this.tags = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("description")) {
                this.description = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("defaultEntryId")) {
                this.defaultEntryId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("flavorParamsIds")) {
                this.flavorParamsIds = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("isDefault")) {
                this.isDefault = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("isPartnerDefault")) {
                this.isPartnerDefault = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("cropDimensions")) {
                this.cropDimensions = ParseUtils.parseObject(KalturaCropDimensions.class, aNode);
                continue;
            } else if (nodeName.equals("clipStart")) {
                this.clipStart = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("clipDuration")) {
                this.clipDuration = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("xslTransformation")) {
                this.xslTransformation = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("storageProfileId")) {
                this.storageProfileId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("mediaParserType")) {
                this.mediaParserType = KalturaMediaParserType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("calculateComplexity")) {
                this.calculateComplexity = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("collectionTags")) {
                this.collectionTags = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaConversionProfile");
        kparams.add("status", this.status);
        kparams.add("type", this.type);
        kparams.add("name", this.name);
        kparams.add("systemName", this.systemName);
        kparams.add("tags", this.tags);
        kparams.add("description", this.description);
        kparams.add("defaultEntryId", this.defaultEntryId);
        kparams.add("flavorParamsIds", this.flavorParamsIds);
        kparams.add("isDefault", this.isDefault);
        kparams.add("cropDimensions", this.cropDimensions);
        kparams.add("clipStart", this.clipStart);
        kparams.add("clipDuration", this.clipDuration);
        kparams.add("xslTransformation", this.xslTransformation);
        kparams.add("storageProfileId", this.storageProfileId);
        kparams.add("mediaParserType", this.mediaParserType);
        kparams.add("calculateComplexity", this.calculateComplexity);
        kparams.add("collectionTags", this.collectionTags);
        return kparams;
    }

}

