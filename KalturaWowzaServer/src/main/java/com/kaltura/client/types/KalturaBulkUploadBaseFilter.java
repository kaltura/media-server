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
import com.kaltura.client.enums.KalturaBatchJobStatus;
import com.kaltura.client.enums.KalturaBulkUploadObjectType;
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
public abstract class KalturaBulkUploadBaseFilter extends KalturaFilter {
    public int uploadedOnGreaterThanOrEqual = Integer.MIN_VALUE;
    public int uploadedOnLessThanOrEqual = Integer.MIN_VALUE;
    public int uploadedOnEqual = Integer.MIN_VALUE;
    public String statusIn;
    public KalturaBatchJobStatus statusEqual;
    public KalturaBulkUploadObjectType bulkUploadObjectTypeEqual;
    public String bulkUploadObjectTypeIn;

    public KalturaBulkUploadBaseFilter() {
    }

    public KalturaBulkUploadBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("uploadedOnGreaterThanOrEqual")) {
                this.uploadedOnGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("uploadedOnLessThanOrEqual")) {
                this.uploadedOnLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("uploadedOnEqual")) {
                this.uploadedOnEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("statusEqual")) {
                this.statusEqual = KalturaBatchJobStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("bulkUploadObjectTypeEqual")) {
                this.bulkUploadObjectTypeEqual = KalturaBulkUploadObjectType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("bulkUploadObjectTypeIn")) {
                this.bulkUploadObjectTypeIn = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaBulkUploadBaseFilter");
        kparams.add("uploadedOnGreaterThanOrEqual", this.uploadedOnGreaterThanOrEqual);
        kparams.add("uploadedOnLessThanOrEqual", this.uploadedOnLessThanOrEqual);
        kparams.add("uploadedOnEqual", this.uploadedOnEqual);
        kparams.add("statusIn", this.statusIn);
        kparams.add("statusEqual", this.statusEqual);
        kparams.add("bulkUploadObjectTypeEqual", this.bulkUploadObjectTypeEqual);
        kparams.add("bulkUploadObjectTypeIn", this.bulkUploadObjectTypeIn);
        return kparams;
    }

}

