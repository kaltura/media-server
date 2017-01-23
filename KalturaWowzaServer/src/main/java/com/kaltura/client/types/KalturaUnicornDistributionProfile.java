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
public class KalturaUnicornDistributionProfile extends KalturaConfigurableDistributionProfile {
	/**  The email address associated with the Upload User, used to authorize the
	  incoming request.  */
    public String username;
	/**  The password used in association with the email to determine if the Upload User
	  is authorized the incoming request.  */
    public String password;
	/**  The name of the Domain that the Upload User should have access to, Used for
	  authentication.  */
    public String domainName;
	/**  The Channel GUID assigned to this Publication Rule. Must be a valid Channel in
	  the Domain that was used in authentication.  */
    public String channelGuid;
	/**  The API host URL that the Upload User should have access to, Used for HTTP
	  content submission.  */
    public String apiHostUrl;
	/**  The GUID of the Customer Domain in the Unicorn system obtained by contacting
	  your Unicorn representative.  */
    public String domainGuid;
	/**  The GUID for the application in which to record metrics and enforce business
	  rules obtained through your Unicorn representative.  */
    public String adFreeApplicationGuid;
	/**  The flavor-params that will be used for the remote asset.  */
    public int remoteAssetParamsId = Integer.MIN_VALUE;
	/**  The remote storage that should be used for the remote asset.  */
    public String storageProfileId;

    public KalturaUnicornDistributionProfile() {
    }

    public KalturaUnicornDistributionProfile(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("username")) {
                this.username = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("password")) {
                this.password = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("domainName")) {
                this.domainName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("channelGuid")) {
                this.channelGuid = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("apiHostUrl")) {
                this.apiHostUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("domainGuid")) {
                this.domainGuid = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("adFreeApplicationGuid")) {
                this.adFreeApplicationGuid = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("remoteAssetParamsId")) {
                this.remoteAssetParamsId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("storageProfileId")) {
                this.storageProfileId = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaUnicornDistributionProfile");
        kparams.add("username", this.username);
        kparams.add("password", this.password);
        kparams.add("domainName", this.domainName);
        kparams.add("channelGuid", this.channelGuid);
        kparams.add("apiHostUrl", this.apiHostUrl);
        kparams.add("domainGuid", this.domainGuid);
        kparams.add("adFreeApplicationGuid", this.adFreeApplicationGuid);
        kparams.add("remoteAssetParamsId", this.remoteAssetParamsId);
        kparams.add("storageProfileId", this.storageProfileId);
        return kparams;
    }

}

