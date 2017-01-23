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
import com.kaltura.client.enums.KalturaDistributionProviderType;
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
public class KalturaDistributionJobData extends KalturaJobData {
    public int distributionProfileId = Integer.MIN_VALUE;
    public KalturaDistributionProfile distributionProfile;
    public int entryDistributionId = Integer.MIN_VALUE;
    public KalturaEntryDistribution entryDistribution;
	/**  Id of the media in the remote system  */
    public String remoteId;
    public KalturaDistributionProviderType providerType;
	/**  Additional data that relevant for the provider only  */
    public KalturaDistributionJobProviderData providerData;
	/**  The results as returned from the remote destination  */
    public String results;
	/**  The data as sent to the remote destination  */
    public String sentData;
	/**  Stores array of media files that submitted to the destination site   Could be
	  used later for media update  */
    public ArrayList<KalturaDistributionRemoteMediaFile> mediaFiles;

    public KalturaDistributionJobData() {
    }

    public KalturaDistributionJobData(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("distributionProfileId")) {
                this.distributionProfileId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("distributionProfile")) {
                this.distributionProfile = ParseUtils.parseObject(KalturaDistributionProfile.class, aNode);
                continue;
            } else if (nodeName.equals("entryDistributionId")) {
                this.entryDistributionId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("entryDistribution")) {
                this.entryDistribution = ParseUtils.parseObject(KalturaEntryDistribution.class, aNode);
                continue;
            } else if (nodeName.equals("remoteId")) {
                this.remoteId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("providerType")) {
                this.providerType = KalturaDistributionProviderType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("providerData")) {
                this.providerData = ParseUtils.parseObject(KalturaDistributionJobProviderData.class, aNode);
                continue;
            } else if (nodeName.equals("results")) {
                this.results = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sentData")) {
                this.sentData = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("mediaFiles")) {
                this.mediaFiles = ParseUtils.parseArray(KalturaDistributionRemoteMediaFile.class, aNode);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaDistributionJobData");
        kparams.add("distributionProfileId", this.distributionProfileId);
        kparams.add("distributionProfile", this.distributionProfile);
        kparams.add("entryDistributionId", this.entryDistributionId);
        kparams.add("entryDistribution", this.entryDistribution);
        kparams.add("remoteId", this.remoteId);
        kparams.add("providerType", this.providerType);
        kparams.add("providerData", this.providerData);
        kparams.add("results", this.results);
        kparams.add("sentData", this.sentData);
        kparams.add("mediaFiles", this.mediaFiles);
        return kparams;
    }

}

