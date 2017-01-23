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
import com.kaltura.client.enums.KalturaNullableBoolean;
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
public class KalturaBaseEntryFilter extends KalturaBaseEntryBaseFilter {
    public String freeText;
    public KalturaNullableBoolean isRoot;
    public String categoriesFullNameIn;
	/**  All entries within this categoy or in child categories  */
    public String categoryAncestorIdIn;
	/**  The id of the original entry  */
    public String redirectFromEntryId;

    public KalturaBaseEntryFilter() {
    }

    public KalturaBaseEntryFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("freeText")) {
                this.freeText = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("isRoot")) {
                this.isRoot = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("categoriesFullNameIn")) {
                this.categoriesFullNameIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("categoryAncestorIdIn")) {
                this.categoryAncestorIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("redirectFromEntryId")) {
                this.redirectFromEntryId = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaBaseEntryFilter");
        kparams.add("freeText", this.freeText);
        kparams.add("isRoot", this.isRoot);
        kparams.add("categoriesFullNameIn", this.categoriesFullNameIn);
        kparams.add("categoryAncestorIdIn", this.categoryAncestorIdIn);
        kparams.add("redirectFromEntryId", this.redirectFromEntryId);
        return kparams;
    }

}

