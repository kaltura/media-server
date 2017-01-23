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
import com.kaltura.client.enums.KalturaDistributionProviderType;
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
public abstract class KalturaDistributionProvider extends KalturaObjectBase {
    public KalturaDistributionProviderType type;
    public String name;
    public boolean scheduleUpdateEnabled;
    public boolean availabilityUpdateEnabled;
    public boolean deleteInsteadUpdate;
    public int intervalBeforeSunrise = Integer.MIN_VALUE;
    public int intervalBeforeSunset = Integer.MIN_VALUE;
    public String updateRequiredEntryFields;
    public String updateRequiredMetadataXPaths;

    public KalturaDistributionProvider() {
    }

    public KalturaDistributionProvider(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("type")) {
                this.type = KalturaDistributionProviderType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("name")) {
                this.name = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("scheduleUpdateEnabled")) {
                this.scheduleUpdateEnabled = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("availabilityUpdateEnabled")) {
                this.availabilityUpdateEnabled = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("deleteInsteadUpdate")) {
                this.deleteInsteadUpdate = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("intervalBeforeSunrise")) {
                this.intervalBeforeSunrise = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("intervalBeforeSunset")) {
                this.intervalBeforeSunset = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updateRequiredEntryFields")) {
                this.updateRequiredEntryFields = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("updateRequiredMetadataXPaths")) {
                this.updateRequiredMetadataXPaths = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaDistributionProvider");
        kparams.add("name", this.name);
        kparams.add("scheduleUpdateEnabled", this.scheduleUpdateEnabled);
        kparams.add("availabilityUpdateEnabled", this.availabilityUpdateEnabled);
        kparams.add("deleteInsteadUpdate", this.deleteInsteadUpdate);
        kparams.add("intervalBeforeSunrise", this.intervalBeforeSunrise);
        kparams.add("intervalBeforeSunset", this.intervalBeforeSunset);
        kparams.add("updateRequiredEntryFields", this.updateRequiredEntryFields);
        kparams.add("updateRequiredMetadataXPaths", this.updateRequiredMetadataXPaths);
        return kparams;
    }

}

