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
public class KalturaCategoryFilter extends KalturaCategoryBaseFilter {
    public String freeText;
    public String membersIn;
    public String nameOrReferenceIdStartsWith;
    public String managerEqual;
    public String memberEqual;
    public String fullNameStartsWithIn;
	/**  not includes the category itself (only sub categories)  */
    public String ancestorIdIn;
    public String idOrInheritedParentIdIn;

    public KalturaCategoryFilter() {
    }

    public KalturaCategoryFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("freeText")) {
                this.freeText = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("membersIn")) {
                this.membersIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("nameOrReferenceIdStartsWith")) {
                this.nameOrReferenceIdStartsWith = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("managerEqual")) {
                this.managerEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("memberEqual")) {
                this.memberEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("fullNameStartsWithIn")) {
                this.fullNameStartsWithIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("ancestorIdIn")) {
                this.ancestorIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("idOrInheritedParentIdIn")) {
                this.idOrInheritedParentIdIn = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaCategoryFilter");
        kparams.add("freeText", this.freeText);
        kparams.add("membersIn", this.membersIn);
        kparams.add("nameOrReferenceIdStartsWith", this.nameOrReferenceIdStartsWith);
        kparams.add("managerEqual", this.managerEqual);
        kparams.add("memberEqual", this.memberEqual);
        kparams.add("fullNameStartsWithIn", this.fullNameStartsWithIn);
        kparams.add("ancestorIdIn", this.ancestorIdIn);
        kparams.add("idOrInheritedParentIdIn", this.idOrInheritedParentIdIn);
        return kparams;
    }

}

