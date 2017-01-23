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
import com.kaltura.client.enums.KalturaServerNodeStatus;
import com.kaltura.client.enums.KalturaServerNodeType;
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
public abstract class KalturaServerNodeBaseFilter extends KalturaFilter {
    public int idEqual = Integer.MIN_VALUE;
    public String idIn;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtLessThanOrEqual = Integer.MIN_VALUE;
    public int heartbeatTimeGreaterThanOrEqual = Integer.MIN_VALUE;
    public int heartbeatTimeLessThanOrEqual = Integer.MIN_VALUE;
    public String nameEqual;
    public String nameIn;
    public String systemNameEqual;
    public String systemNameIn;
    public String hostNameLike;
    public String hostNameMultiLikeOr;
    public String hostNameMultiLikeAnd;
    public KalturaServerNodeStatus statusEqual;
    public String statusIn;
    public KalturaServerNodeType typeEqual;
    public String typeIn;
    public String tagsLike;
    public String tagsMultiLikeOr;
    public String tagsMultiLikeAnd;
    public int dcEqual = Integer.MIN_VALUE;
    public String dcIn;
    public int parentIdEqual = Integer.MIN_VALUE;
    public String parentIdIn;

    public KalturaServerNodeBaseFilter() {
    }

    public KalturaServerNodeBaseFilter(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("createdAtGreaterThanOrEqual")) {
                this.createdAtGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("createdAtLessThanOrEqual")) {
                this.createdAtLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAtGreaterThanOrEqual")) {
                this.updatedAtGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAtLessThanOrEqual")) {
                this.updatedAtLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("heartbeatTimeGreaterThanOrEqual")) {
                this.heartbeatTimeGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("heartbeatTimeLessThanOrEqual")) {
                this.heartbeatTimeLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("nameEqual")) {
                this.nameEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("nameIn")) {
                this.nameIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("systemNameEqual")) {
                this.systemNameEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("systemNameIn")) {
                this.systemNameIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("hostNameLike")) {
                this.hostNameLike = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("hostNameMultiLikeOr")) {
                this.hostNameMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("hostNameMultiLikeAnd")) {
                this.hostNameMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("statusEqual")) {
                this.statusEqual = KalturaServerNodeStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("typeEqual")) {
                this.typeEqual = KalturaServerNodeType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("typeIn")) {
                this.typeIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tagsLike")) {
                this.tagsLike = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tagsMultiLikeOr")) {
                this.tagsMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tagsMultiLikeAnd")) {
                this.tagsMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("dcEqual")) {
                this.dcEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("dcIn")) {
                this.dcIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("parentIdEqual")) {
                this.parentIdEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("parentIdIn")) {
                this.parentIdIn = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaServerNodeBaseFilter");
        kparams.add("idEqual", this.idEqual);
        kparams.add("idIn", this.idIn);
        kparams.add("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.add("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.add("updatedAtGreaterThanOrEqual", this.updatedAtGreaterThanOrEqual);
        kparams.add("updatedAtLessThanOrEqual", this.updatedAtLessThanOrEqual);
        kparams.add("heartbeatTimeGreaterThanOrEqual", this.heartbeatTimeGreaterThanOrEqual);
        kparams.add("heartbeatTimeLessThanOrEqual", this.heartbeatTimeLessThanOrEqual);
        kparams.add("nameEqual", this.nameEqual);
        kparams.add("nameIn", this.nameIn);
        kparams.add("systemNameEqual", this.systemNameEqual);
        kparams.add("systemNameIn", this.systemNameIn);
        kparams.add("hostNameLike", this.hostNameLike);
        kparams.add("hostNameMultiLikeOr", this.hostNameMultiLikeOr);
        kparams.add("hostNameMultiLikeAnd", this.hostNameMultiLikeAnd);
        kparams.add("statusEqual", this.statusEqual);
        kparams.add("statusIn", this.statusIn);
        kparams.add("typeEqual", this.typeEqual);
        kparams.add("typeIn", this.typeIn);
        kparams.add("tagsLike", this.tagsLike);
        kparams.add("tagsMultiLikeOr", this.tagsMultiLikeOr);
        kparams.add("tagsMultiLikeAnd", this.tagsMultiLikeAnd);
        kparams.add("dcEqual", this.dcEqual);
        kparams.add("dcIn", this.dcIn);
        kparams.add("parentIdEqual", this.parentIdEqual);
        kparams.add("parentIdIn", this.parentIdIn);
        return kparams;
    }

}

