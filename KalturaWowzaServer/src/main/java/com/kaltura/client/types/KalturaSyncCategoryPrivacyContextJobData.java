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
public class KalturaSyncCategoryPrivacyContextJobData extends KalturaJobData {
	/**  category id  */
    public int categoryId = Integer.MIN_VALUE;
	/**  Saves the last category entry creation date that was updated      In case of
	  crash the batch will restart from that point  */
    public int lastUpdatedCategoryEntryCreatedAt = Integer.MIN_VALUE;
	/**  Saves the last sub category creation date that was updated      In case of crash
	  the batch will restart from that point  */
    public int lastUpdatedCategoryCreatedAt = Integer.MIN_VALUE;

    public KalturaSyncCategoryPrivacyContextJobData() {
    }

    public KalturaSyncCategoryPrivacyContextJobData(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("categoryId")) {
                this.categoryId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("lastUpdatedCategoryEntryCreatedAt")) {
                this.lastUpdatedCategoryEntryCreatedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("lastUpdatedCategoryCreatedAt")) {
                this.lastUpdatedCategoryCreatedAt = ParseUtils.parseInt(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaSyncCategoryPrivacyContextJobData");
        kparams.add("categoryId", this.categoryId);
        kparams.add("lastUpdatedCategoryEntryCreatedAt", this.lastUpdatedCategoryEntryCreatedAt);
        kparams.add("lastUpdatedCategoryCreatedAt", this.lastUpdatedCategoryCreatedAt);
        return kparams;
    }

}

