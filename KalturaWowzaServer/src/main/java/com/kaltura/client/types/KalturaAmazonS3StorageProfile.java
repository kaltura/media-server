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
import com.kaltura.client.enums.KalturaAmazonS3StorageProfileFilesPermissionLevel;
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
public class KalturaAmazonS3StorageProfile extends KalturaStorageProfile {
    public KalturaAmazonS3StorageProfileFilesPermissionLevel filesPermissionInS3;
    public String s3Region;
    public String sseType;
    public String sseKmsKeyId;
    public String signatureType;
    public String endPoint;

    public KalturaAmazonS3StorageProfile() {
    }

    public KalturaAmazonS3StorageProfile(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("filesPermissionInS3")) {
                this.filesPermissionInS3 = KalturaAmazonS3StorageProfileFilesPermissionLevel.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("s3Region")) {
                this.s3Region = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sseType")) {
                this.sseType = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sseKmsKeyId")) {
                this.sseKmsKeyId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("signatureType")) {
                this.signatureType = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("endPoint")) {
                this.endPoint = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaAmazonS3StorageProfile");
        kparams.add("filesPermissionInS3", this.filesPermissionInS3);
        kparams.add("s3Region", this.s3Region);
        kparams.add("sseType", this.sseType);
        kparams.add("sseKmsKeyId", this.sseKmsKeyId);
        kparams.add("signatureType", this.signatureType);
        kparams.add("endPoint", this.endPoint);
        return kparams;
    }

}

