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
import com.kaltura.client.enums.KalturaNullableBoolean;
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
public class KalturaAccessControl extends KalturaObjectBase {
	/**  The id of the Access Control Profile  */
    public int id = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
	/**  The name of the Access Control Profile  */
    public String name;
	/**  System name of the Access Control Profile  */
    public String systemName;
	/**  The description of the Access Control Profile  */
    public String description;
	/**  Creation date as Unix timestamp (In seconds)  */
    public int createdAt = Integer.MIN_VALUE;
	/**  True if this Conversion Profile is the default  */
    public KalturaNullableBoolean isDefault;
	/**  Array of Access Control Restrictions  */
    public ArrayList<KalturaBaseRestriction> restrictions;
	/**  Indicates that the access control profile is new and should be handled using
	  KalturaAccessControlProfile object and accessControlProfile service  */
    public boolean containsUnsuportedRestrictions;

    public KalturaAccessControl() {
    }

    public KalturaAccessControl(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerId")) {
                this.partnerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("name")) {
                this.name = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("systemName")) {
                this.systemName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("description")) {
                this.description = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("isDefault")) {
                this.isDefault = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("restrictions")) {
                this.restrictions = ParseUtils.parseArray(KalturaBaseRestriction.class, aNode);
                continue;
            } else if (nodeName.equals("containsUnsuportedRestrictions")) {
                this.containsUnsuportedRestrictions = ParseUtils.parseBool(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaAccessControl");
        kparams.add("name", this.name);
        kparams.add("systemName", this.systemName);
        kparams.add("description", this.description);
        kparams.add("isDefault", this.isDefault);
        kparams.add("restrictions", this.restrictions);
        return kparams;
    }

}

