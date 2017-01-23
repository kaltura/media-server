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
import com.kaltura.client.enums.KalturaMediaType;
import com.kaltura.client.enums.KalturaSourceType;
import com.kaltura.client.enums.KalturaSearchProviderType;
import com.kaltura.client.enums.KalturaNullableBoolean;
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
public class KalturaMediaEntry extends KalturaPlayableEntry {
	/**  The media type of the entry  */
    public KalturaMediaType mediaType;
	/**  Override the default conversion quality  */
    public String conversionQuality;
	/**  The source type of the entry  */
    public KalturaSourceType sourceType;
	/**  The search provider type used to import this entry  */
    public KalturaSearchProviderType searchProviderType;
	/**  The ID of the media in the importing site  */
    public String searchProviderId;
	/**  The user name used for credits  */
    public String creditUserName;
	/**  The URL for credits  */
    public String creditUrl;
	/**  The media date extracted from EXIF data (For images) as Unix timestamp (In
	  seconds)  */
    public int mediaDate = Integer.MIN_VALUE;
	/**  The URL used for playback. This is not the download URL.  */
    public String dataUrl;
	/**  Comma separated flavor params ids that exists for this media entry  */
    public String flavorParamsIds;
	/**  True if trim action is disabled for this entry  */
    public KalturaNullableBoolean isTrimDisabled;
	/**  Array of streams that exists on the entry  */
    public ArrayList<KalturaStreamContainer> streams;

    public KalturaMediaEntry() {
    }

    public KalturaMediaEntry(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("mediaType")) {
                this.mediaType = KalturaMediaType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("conversionQuality")) {
                this.conversionQuality = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sourceType")) {
                this.sourceType = KalturaSourceType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("searchProviderType")) {
                this.searchProviderType = KalturaSearchProviderType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("searchProviderId")) {
                this.searchProviderId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("creditUserName")) {
                this.creditUserName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("creditUrl")) {
                this.creditUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("mediaDate")) {
                this.mediaDate = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("dataUrl")) {
                this.dataUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("flavorParamsIds")) {
                this.flavorParamsIds = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("isTrimDisabled")) {
                this.isTrimDisabled = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("streams")) {
                this.streams = ParseUtils.parseArray(KalturaStreamContainer.class, aNode);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaMediaEntry");
        kparams.add("mediaType", this.mediaType);
        kparams.add("conversionQuality", this.conversionQuality);
        kparams.add("sourceType", this.sourceType);
        kparams.add("searchProviderType", this.searchProviderType);
        kparams.add("searchProviderId", this.searchProviderId);
        kparams.add("creditUserName", this.creditUserName);
        kparams.add("creditUrl", this.creditUrl);
        kparams.add("streams", this.streams);
        return kparams;
    }

}

