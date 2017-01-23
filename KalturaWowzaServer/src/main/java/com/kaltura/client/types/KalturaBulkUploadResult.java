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
import com.kaltura.client.enums.KalturaBulkUploadResultStatus;
import com.kaltura.client.enums.KalturaBulkUploadAction;
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
public class KalturaBulkUploadResult extends KalturaObjectBase {
	/**  The id of the result  */
    public int id = Integer.MIN_VALUE;
	/**  The id of the parent job  */
    public long bulkUploadJobId = Long.MIN_VALUE;
	/**  The index of the line in the CSV  */
    public int lineIndex = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public KalturaBulkUploadResultStatus status;
    public KalturaBulkUploadAction action;
    public String objectId;
    public int objectStatus = Integer.MIN_VALUE;
    public KalturaBulkUploadObjectType bulkUploadResultObjectType;
	/**  The data as recieved in the csv  */
    public String rowData;
    public String partnerData;
    public String objectErrorDescription;
    public ArrayList<KalturaBulkUploadPluginData> pluginsData;
    public String errorDescription;
    public String errorCode;
    public int errorType = Integer.MIN_VALUE;

    public KalturaBulkUploadResult() {
    }

    public KalturaBulkUploadResult(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("bulkUploadJobId")) {
                this.bulkUploadJobId = ParseUtils.parseBigint(txt);
                continue;
            } else if (nodeName.equals("lineIndex")) {
                this.lineIndex = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerId")) {
                this.partnerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaBulkUploadResultStatus.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("action")) {
                this.action = KalturaBulkUploadAction.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("objectId")) {
                this.objectId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("objectStatus")) {
                this.objectStatus = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("bulkUploadResultObjectType")) {
                this.bulkUploadResultObjectType = KalturaBulkUploadObjectType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("rowData")) {
                this.rowData = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerData")) {
                this.partnerData = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("objectErrorDescription")) {
                this.objectErrorDescription = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("pluginsData")) {
                this.pluginsData = ParseUtils.parseArray(KalturaBulkUploadPluginData.class, aNode);
                continue;
            } else if (nodeName.equals("errorDescription")) {
                this.errorDescription = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("errorCode")) {
                this.errorCode = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("errorType")) {
                this.errorType = ParseUtils.parseInt(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaBulkUploadResult");
        kparams.add("bulkUploadJobId", this.bulkUploadJobId);
        kparams.add("lineIndex", this.lineIndex);
        kparams.add("partnerId", this.partnerId);
        kparams.add("status", this.status);
        kparams.add("action", this.action);
        kparams.add("objectId", this.objectId);
        kparams.add("objectStatus", this.objectStatus);
        kparams.add("bulkUploadResultObjectType", this.bulkUploadResultObjectType);
        kparams.add("rowData", this.rowData);
        kparams.add("partnerData", this.partnerData);
        kparams.add("objectErrorDescription", this.objectErrorDescription);
        kparams.add("pluginsData", this.pluginsData);
        kparams.add("errorDescription", this.errorDescription);
        kparams.add("errorCode", this.errorCode);
        kparams.add("errorType", this.errorType);
        return kparams;
    }

}

