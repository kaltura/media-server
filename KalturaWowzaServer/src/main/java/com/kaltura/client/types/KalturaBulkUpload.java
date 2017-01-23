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
import com.kaltura.client.enums.KalturaBatchJobStatus;
import com.kaltura.client.enums.KalturaBulkUploadType;
import com.kaltura.client.enums.KalturaBatchJobErrorTypes;
import com.kaltura.client.enums.KalturaBulkUploadObjectType;
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
public class KalturaBulkUpload extends KalturaObjectBase {
    public long id = Long.MIN_VALUE;
    public String uploadedBy;
    public String uploadedByUserId;
    public int uploadedOn = Integer.MIN_VALUE;
    public int numOfEntries = Integer.MIN_VALUE;
    public KalturaBatchJobStatus status;
    public String logFileUrl;
    public String csvFileUrl;
    public String bulkFileUrl;
    public KalturaBulkUploadType bulkUploadType;
    public ArrayList<KalturaBulkUploadResult> results;
    public String error;
    public KalturaBatchJobErrorTypes errorType;
    public int errorNumber = Integer.MIN_VALUE;
    public String fileName;
    public String description;
    public int numOfObjects = Integer.MIN_VALUE;
    public KalturaBulkUploadObjectType bulkUploadObjectType;

    public KalturaBulkUpload() {
    }

    public KalturaBulkUpload(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseBigint(txt);
                continue;
            } else if (nodeName.equals("uploadedBy")) {
                this.uploadedBy = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("uploadedByUserId")) {
                this.uploadedByUserId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("uploadedOn")) {
                this.uploadedOn = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("numOfEntries")) {
                this.numOfEntries = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaBatchJobStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("logFileUrl")) {
                this.logFileUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("csvFileUrl")) {
                this.csvFileUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("bulkFileUrl")) {
                this.bulkFileUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("bulkUploadType")) {
                this.bulkUploadType = KalturaBulkUploadType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("results")) {
                this.results = ParseUtils.parseArray(KalturaBulkUploadResult.class, aNode);
                continue;
            } else if (nodeName.equals("error")) {
                this.error = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("errorType")) {
                this.errorType = KalturaBatchJobErrorTypes.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("errorNumber")) {
                this.errorNumber = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("fileName")) {
                this.fileName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("description")) {
                this.description = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("numOfObjects")) {
                this.numOfObjects = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("bulkUploadObjectType")) {
                this.bulkUploadObjectType = KalturaBulkUploadObjectType.get(ParseUtils.parseString(txt));
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaBulkUpload");
        kparams.add("id", this.id);
        kparams.add("uploadedBy", this.uploadedBy);
        kparams.add("uploadedByUserId", this.uploadedByUserId);
        kparams.add("uploadedOn", this.uploadedOn);
        kparams.add("numOfEntries", this.numOfEntries);
        kparams.add("status", this.status);
        kparams.add("logFileUrl", this.logFileUrl);
        kparams.add("csvFileUrl", this.csvFileUrl);
        kparams.add("bulkFileUrl", this.bulkFileUrl);
        kparams.add("bulkUploadType", this.bulkUploadType);
        kparams.add("results", this.results);
        kparams.add("error", this.error);
        kparams.add("errorType", this.errorType);
        kparams.add("errorNumber", this.errorNumber);
        kparams.add("fileName", this.fileName);
        kparams.add("description", this.description);
        kparams.add("numOfObjects", this.numOfObjects);
        kparams.add("bulkUploadObjectType", this.bulkUploadObjectType);
        return kparams;
    }

}

