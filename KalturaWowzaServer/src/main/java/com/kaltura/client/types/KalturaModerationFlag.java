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
import com.kaltura.client.enums.KalturaModerationObjectType;
import com.kaltura.client.enums.KalturaModerationFlagStatus;
import com.kaltura.client.enums.KalturaModerationFlagType;
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
public class KalturaModerationFlag extends KalturaObjectBase {
	/**  Moderation flag id  */
    public int id = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
	/**  The user id that added the moderation flag  */
    public String userId;
	/**  The type of the moderation flag (entry or user)  */
    public KalturaModerationObjectType moderationObjectType;
	/**  If moderation flag is set for entry, this is the flagged entry id  */
    public String flaggedEntryId;
	/**  If moderation flag is set for user, this is the flagged user id  */
    public String flaggedUserId;
	/**  The moderation flag status  */
    public KalturaModerationFlagStatus status;
	/**  The comment that was added to the flag  */
    public String comments;
    public KalturaModerationFlagType flagType;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;

    public KalturaModerationFlag() {
    }

    public KalturaModerationFlag(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("userId")) {
                this.userId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("moderationObjectType")) {
                this.moderationObjectType = KalturaModerationObjectType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("flaggedEntryId")) {
                this.flaggedEntryId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("flaggedUserId")) {
                this.flaggedUserId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaModerationFlagStatus.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("comments")) {
                this.comments = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("flagType")) {
                this.flagType = KalturaModerationFlagType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAt")) {
                this.updatedAt = ParseUtils.parseInt(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaModerationFlag");
        kparams.add("flaggedEntryId", this.flaggedEntryId);
        kparams.add("flaggedUserId", this.flaggedUserId);
        kparams.add("comments", this.comments);
        kparams.add("flagType", this.flagType);
        return kparams;
    }

}

