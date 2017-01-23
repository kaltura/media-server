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
public class KalturaModifyEntryObjectTask extends KalturaObjectTask {
	/**  The input metadata profile id  */
    public int inputMetadataProfileId = Integer.MIN_VALUE;
	/**  array of {input metadata xpath location,entry field} objects  */
    public ArrayList<KalturaKeyValue> inputMetadata;
	/**  The output metadata profile id  */
    public int outputMetadataProfileId = Integer.MIN_VALUE;
	/**  array of {output metadata xpath location,entry field} objects  */
    public ArrayList<KalturaKeyValue> outputMetadata;
	/**  The input user id to set on the entry  */
    public String inputUserId;
	/**  The input entitled users edit to set on the entry  */
    public String inputEntitledUsersEdit;
	/**  The input entitled users publish to set on the entry  */
    public String inputEntitledUsersPublish;

    public KalturaModifyEntryObjectTask() {
    }

    public KalturaModifyEntryObjectTask(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("inputMetadataProfileId")) {
                this.inputMetadataProfileId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("inputMetadata")) {
                this.inputMetadata = ParseUtils.parseArray(KalturaKeyValue.class, aNode);
                continue;
            } else if (nodeName.equals("outputMetadataProfileId")) {
                this.outputMetadataProfileId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("outputMetadata")) {
                this.outputMetadata = ParseUtils.parseArray(KalturaKeyValue.class, aNode);
                continue;
            } else if (nodeName.equals("inputUserId")) {
                this.inputUserId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("inputEntitledUsersEdit")) {
                this.inputEntitledUsersEdit = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("inputEntitledUsersPublish")) {
                this.inputEntitledUsersPublish = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaModifyEntryObjectTask");
        kparams.add("inputMetadataProfileId", this.inputMetadataProfileId);
        kparams.add("inputMetadata", this.inputMetadata);
        kparams.add("outputMetadataProfileId", this.outputMetadataProfileId);
        kparams.add("outputMetadata", this.outputMetadata);
        kparams.add("inputUserId", this.inputUserId);
        kparams.add("inputEntitledUsersEdit", this.inputEntitledUsersEdit);
        kparams.add("inputEntitledUsersPublish", this.inputEntitledUsersPublish);
        return kparams;
    }

}

