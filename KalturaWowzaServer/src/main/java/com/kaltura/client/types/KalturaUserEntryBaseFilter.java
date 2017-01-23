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
import com.kaltura.client.enums.KalturaUserEntryStatus;
import com.kaltura.client.enums.KalturaUserEntryType;
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
public abstract class KalturaUserEntryBaseFilter extends KalturaRelatedFilter {
    public int idEqual = Integer.MIN_VALUE;
    public String idIn;
    public String idNotIn;
    public String entryIdEqual;
    public String entryIdIn;
    public String entryIdNotIn;
    public String userIdEqual;
    public String userIdIn;
    public String userIdNotIn;
    public KalturaUserEntryStatus statusEqual;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtLessThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public KalturaUserEntryType typeEqual;

    public KalturaUserEntryBaseFilter() {
    }

    public KalturaUserEntryBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("idEqual")) {
                this.idEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("idIn")) {
                this.idIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("idNotIn")) {
                this.idNotIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("entryIdEqual")) {
                this.entryIdEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("entryIdIn")) {
                this.entryIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("entryIdNotIn")) {
                this.entryIdNotIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("userIdEqual")) {
                this.userIdEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("userIdIn")) {
                this.userIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("userIdNotIn")) {
                this.userIdNotIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("statusEqual")) {
                this.statusEqual = KalturaUserEntryStatus.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("createdAtLessThanOrEqual")) {
                this.createdAtLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("createdAtGreaterThanOrEqual")) {
                this.createdAtGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAtLessThanOrEqual")) {
                this.updatedAtLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAtGreaterThanOrEqual")) {
                this.updatedAtGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("typeEqual")) {
                this.typeEqual = KalturaUserEntryType.get(ParseUtils.parseString(txt));
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaUserEntryBaseFilter");
        kparams.add("idEqual", this.idEqual);
        kparams.add("idIn", this.idIn);
        kparams.add("idNotIn", this.idNotIn);
        kparams.add("entryIdEqual", this.entryIdEqual);
        kparams.add("entryIdIn", this.entryIdIn);
        kparams.add("entryIdNotIn", this.entryIdNotIn);
        kparams.add("userIdEqual", this.userIdEqual);
        kparams.add("userIdIn", this.userIdIn);
        kparams.add("userIdNotIn", this.userIdNotIn);
        kparams.add("statusEqual", this.statusEqual);
        kparams.add("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.add("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.add("updatedAtLessThanOrEqual", this.updatedAtLessThanOrEqual);
        kparams.add("updatedAtGreaterThanOrEqual", this.updatedAtGreaterThanOrEqual);
        kparams.add("typeEqual", this.typeEqual);
        return kparams;
    }

}

