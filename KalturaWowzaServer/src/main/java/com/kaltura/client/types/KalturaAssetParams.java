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
import com.kaltura.client.enums.KalturaNullableBoolean;
import com.kaltura.client.enums.KalturaMediaParserType;
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
public class KalturaAssetParams extends KalturaObjectBase {
	/**  The id of the Flavor Params  */
    public int id = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
	/**  The name of the Flavor Params  */
    public String name;
	/**  System name of the Flavor Params  */
    public String systemName;
	/**  The description of the Flavor Params  */
    public String description;
	/**  Creation date as Unix timestamp (In seconds)  */
    public int createdAt = Integer.MIN_VALUE;
	/**  True if those Flavor Params are part of system defaults  */
    public KalturaNullableBoolean isSystemDefault;
	/**  The Flavor Params tags are used to identify the flavor for different usage (e.g.
	  web, hd, mobile)  */
    public String tags;
	/**  Array of partner permisison names that required for using this asset params  */
    public ArrayList<KalturaString> requiredPermissions;
	/**  Id of remote storage profile that used to get the source, zero indicates Kaltura
	  data center  */
    public int sourceRemoteStorageProfileId = Integer.MIN_VALUE;
	/**  Comma seperated ids of remote storage profiles that the flavor distributed to,
	  the distribution done by the conversion engine  */
    public int remoteStorageProfileIds = Integer.MIN_VALUE;
	/**  Media parser type to be used for post-conversion validation  */
    public KalturaMediaParserType mediaParserType;
	/**  Comma seperated ids of source flavor params this flavor is created from  */
    public String sourceAssetParamsIds;

    public KalturaAssetParams() {
    }

    public KalturaAssetParams(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("name")) {
                this.name = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("systemName")) {
                this.systemName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("description")) {
                this.description = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("isSystemDefault")) {
                this.isSystemDefault = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("tags")) {
                this.tags = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("requiredPermissions")) {
                this.requiredPermissions = ParseUtils.parseArray(KalturaString.class, aNode);
                continue;
            } else if (nodeName.equals("sourceRemoteStorageProfileId")) {
                this.sourceRemoteStorageProfileId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("remoteStorageProfileIds")) {
                this.remoteStorageProfileIds = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("mediaParserType")) {
                this.mediaParserType = KalturaMediaParserType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("sourceAssetParamsIds")) {
                this.sourceAssetParamsIds = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaAssetParams");
        kparams.add("partnerId", this.partnerId);
        kparams.add("name", this.name);
        kparams.add("systemName", this.systemName);
        kparams.add("description", this.description);
        kparams.add("tags", this.tags);
        kparams.add("requiredPermissions", this.requiredPermissions);
        kparams.add("sourceRemoteStorageProfileId", this.sourceRemoteStorageProfileId);
        kparams.add("remoteStorageProfileIds", this.remoteStorageProfileIds);
        kparams.add("mediaParserType", this.mediaParserType);
        kparams.add("sourceAssetParamsIds", this.sourceAssetParamsIds);
        return kparams;
    }

}

