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
import com.kaltura.client.enums.KalturaResponseProfileType;
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
public class KalturaDetachedResponseProfile extends KalturaBaseResponseProfile {
	/**  Friendly name  */
    public String name;
    public KalturaResponseProfileType type;
	/**  Comma separated fields list to be included or excluded  */
    public String fields;
    public KalturaRelatedFilter filter;
    public KalturaFilterPager pager;
    public ArrayList<KalturaDetachedResponseProfile> relatedProfiles;
    public ArrayList<KalturaResponseProfileMapping> mappings;

    public KalturaDetachedResponseProfile() {
    }

    public KalturaDetachedResponseProfile(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("name")) {
                this.name = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("type")) {
                this.type = KalturaResponseProfileType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("fields")) {
                this.fields = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("filter")) {
                this.filter = ParseUtils.parseObject(KalturaRelatedFilter.class, aNode);
                continue;
            } else if (nodeName.equals("pager")) {
                this.pager = ParseUtils.parseObject(KalturaFilterPager.class, aNode);
                continue;
            } else if (nodeName.equals("relatedProfiles")) {
                this.relatedProfiles = ParseUtils.parseArray(KalturaDetachedResponseProfile.class, aNode);
                continue;
            } else if (nodeName.equals("mappings")) {
                this.mappings = ParseUtils.parseArray(KalturaResponseProfileMapping.class, aNode);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaDetachedResponseProfile");
        kparams.add("name", this.name);
        kparams.add("type", this.type);
        kparams.add("fields", this.fields);
        kparams.add("filter", this.filter);
        kparams.add("pager", this.pager);
        kparams.add("relatedProfiles", this.relatedProfiles);
        kparams.add("mappings", this.mappings);
        return kparams;
    }

}

