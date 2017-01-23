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
public class KalturaUnicornDistributionJobProviderData extends KalturaConfigurableDistributionJobProviderData {
	/**  The Catalog GUID the video is in or will be ingested into.  */
    public String catalogGuid;
	/**  The Title assigned to the video. The Foreign Key will be used if no title is
	  provided.  */
    public String title;
	/**  Indicates that the media content changed and therefore the job should wait for
	  HTTP callback notification to be closed.  */
    public boolean mediaChanged;
	/**  Flavor asset version.  */
    public String flavorAssetVersion;
	/**  The schema and host name to the Kaltura server, e.g. http://www.kaltura.com  */
    public String notificationBaseUrl;

    public KalturaUnicornDistributionJobProviderData() {
    }

    public KalturaUnicornDistributionJobProviderData(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("catalogGuid")) {
                this.catalogGuid = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("title")) {
                this.title = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("mediaChanged")) {
                this.mediaChanged = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("flavorAssetVersion")) {
                this.flavorAssetVersion = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("notificationBaseUrl")) {
                this.notificationBaseUrl = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaUnicornDistributionJobProviderData");
        kparams.add("catalogGuid", this.catalogGuid);
        kparams.add("title", this.title);
        kparams.add("mediaChanged", this.mediaChanged);
        kparams.add("flavorAssetVersion", this.flavorAssetVersion);
        kparams.add("notificationBaseUrl", this.notificationBaseUrl);
        return kparams;
    }

}

