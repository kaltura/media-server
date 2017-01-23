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
import com.kaltura.client.enums.KalturaBulkUploadObjectType;
import com.kaltura.client.enums.KalturaBulkUploadType;
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
public class KalturaBulkUploadJobData extends KalturaJobData {
    public String userId;
	/**  The screen name of the user  */
    public String uploadedBy;
	/**  Selected profile id for all bulk entries  */
    public int conversionProfileId = Integer.MIN_VALUE;
	/**  Created by the API  */
    public String resultsFileLocalPath;
	/**  Created by the API  */
    public String resultsFileUrl;
	/**  Number of created entries  */
    public int numOfEntries = Integer.MIN_VALUE;
	/**  Number of created objects  */
    public int numOfObjects = Integer.MIN_VALUE;
	/**  The bulk upload file path  */
    public String filePath;
	/**  Type of object for bulk upload  */
    public KalturaBulkUploadObjectType bulkUploadObjectType;
	/**  Friendly name of the file, used to be recognized later in the logs.  */
    public String fileName;
	/**  Data pertaining to the objects being uploaded  */
    public KalturaBulkUploadObjectData objectData;
	/**  Type of bulk upload  */
    public KalturaBulkUploadType type;
	/**  Recipients of the email for bulk upload success/failure  */
    public String emailRecipients;
	/**  Number of objects that finished on error status  */
    public int numOfErrorObjects = Integer.MIN_VALUE;

    public KalturaBulkUploadJobData() {
    }

    public KalturaBulkUploadJobData(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("userId")) {
                this.userId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("uploadedBy")) {
                this.uploadedBy = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("conversionProfileId")) {
                this.conversionProfileId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("resultsFileLocalPath")) {
                this.resultsFileLocalPath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("resultsFileUrl")) {
                this.resultsFileUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("numOfEntries")) {
                this.numOfEntries = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("numOfObjects")) {
                this.numOfObjects = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("filePath")) {
                this.filePath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("bulkUploadObjectType")) {
                this.bulkUploadObjectType = KalturaBulkUploadObjectType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("fileName")) {
                this.fileName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("objectData")) {
                this.objectData = ParseUtils.parseObject(KalturaBulkUploadObjectData.class, aNode);
                continue;
            } else if (nodeName.equals("type")) {
                this.type = KalturaBulkUploadType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("emailRecipients")) {
                this.emailRecipients = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("numOfErrorObjects")) {
                this.numOfErrorObjects = ParseUtils.parseInt(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaBulkUploadJobData");
        kparams.add("fileName", this.fileName);
        kparams.add("emailRecipients", this.emailRecipients);
        kparams.add("numOfErrorObjects", this.numOfErrorObjects);
        return kparams;
    }

}

