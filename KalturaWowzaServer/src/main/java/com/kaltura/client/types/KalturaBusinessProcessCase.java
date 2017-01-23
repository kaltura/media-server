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
public class KalturaBusinessProcessCase extends KalturaObjectBase {
    public String id;
    public String businessProcessId;
    public int businessProcessStartNotificationTemplateId = Integer.MIN_VALUE;
    public boolean suspended;
    public String activityId;

    public KalturaBusinessProcessCase() {
    }

    public KalturaBusinessProcessCase(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("businessProcessId")) {
                this.businessProcessId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("businessProcessStartNotificationTemplateId")) {
                this.businessProcessStartNotificationTemplateId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("suspended")) {
                this.suspended = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("activityId")) {
                this.activityId = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaBusinessProcessCase");
        kparams.add("id", this.id);
        kparams.add("businessProcessId", this.businessProcessId);
        kparams.add("businessProcessStartNotificationTemplateId", this.businessProcessStartNotificationTemplateId);
        kparams.add("suspended", this.suspended);
        kparams.add("activityId", this.activityId);
        return kparams;
    }

}

