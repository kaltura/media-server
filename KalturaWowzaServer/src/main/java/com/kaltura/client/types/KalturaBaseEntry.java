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
import com.kaltura.client.enums.KalturaEntryStatus;
import com.kaltura.client.enums.KalturaEntryModerationStatus;
import com.kaltura.client.enums.KalturaEntryType;
import com.kaltura.client.enums.KalturaLicenseType;
import com.kaltura.client.enums.KalturaEntryReplacementStatus;
import com.kaltura.client.enums.KalturaEntryDisplayInSearchType;
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
public class KalturaBaseEntry extends KalturaObjectBase {
	/**  Auto generated 10 characters alphanumeric string  */
    public String id;
	/**  Entry name (Min 1 chars)  */
    public String name;
	/**  Entry description  */
    public String description;
    public int partnerId = Integer.MIN_VALUE;
	/**  The ID of the user who is the owner of this entry  */
    public String userId;
	/**  The ID of the user who created this entry  */
    public String creatorId;
	/**  Entry tags  */
    public String tags;
	/**  Entry admin tags can be updated only by administrators  */
    public String adminTags;
	/**  Comma separated list of full names of categories to which this entry belongs.
	  Only categories that don't have entitlement (privacy context) are listed, to
	  retrieve the full list of categories, use the categoryEntry.list action.  */
    public String categories;
	/**  Comma separated list of ids of categories to which this entry belongs. Only
	  categories that don't have entitlement (privacy context) are listed, to retrieve
	  the full list of categories, use the categoryEntry.list action.  */
    public String categoriesIds;
    public KalturaEntryStatus status;
	/**  Entry moderation status  */
    public KalturaEntryModerationStatus moderationStatus;
	/**  Number of moderation requests waiting for this entry  */
    public int moderationCount = Integer.MIN_VALUE;
	/**  The type of the entry, this is auto filled by the derived entry object  */
    public KalturaEntryType type;
	/**  Entry creation date as Unix timestamp (In seconds)  */
    public int createdAt = Integer.MIN_VALUE;
	/**  Entry update date as Unix timestamp (In seconds)  */
    public int updatedAt = Integer.MIN_VALUE;
	/**  The calculated average rank. rank = totalRank / votes  */
    public double rank = Double.MIN_VALUE;
	/**  The sum of all rank values submitted to the baseEntry.anonymousRank action  */
    public int totalRank = Integer.MIN_VALUE;
	/**  A count of all requests made to the baseEntry.anonymousRank action  */
    public int votes = Integer.MIN_VALUE;
    public int groupId = Integer.MIN_VALUE;
	/**  Can be used to store various partner related data as a string  */
    public String partnerData;
	/**  Download URL for the entry  */
    public String downloadUrl;
	/**  Indexed search text for full text search  */
    public String searchText;
	/**  License type used for this entry  */
    public KalturaLicenseType licenseType;
	/**  Version of the entry data  */
    public int version = Integer.MIN_VALUE;
	/**  Thumbnail URL  */
    public String thumbnailUrl;
	/**  The Access Control ID assigned to this entry (null when not set, send -1 to
	  remove)  */
    public int accessControlId = Integer.MIN_VALUE;
	/**  Entry scheduling start date (null when not set, send -1 to remove)  */
    public int startDate = Integer.MIN_VALUE;
	/**  Entry scheduling end date (null when not set, send -1 to remove)  */
    public int endDate = Integer.MIN_VALUE;
	/**  Entry external reference id  */
    public String referenceId;
	/**  ID of temporary entry that will replace this entry when it's approved and ready
	  for replacement  */
    public String replacingEntryId;
	/**  ID of the entry that will be replaced when the replacement approved and this
	  entry is ready  */
    public String replacedEntryId;
	/**  Status of the replacement readiness and approval  */
    public KalturaEntryReplacementStatus replacementStatus;
	/**  Can be used to store various partner related data as a numeric value  */
    public int partnerSortValue = Integer.MIN_VALUE;
	/**  Override the default ingestion profile  */
    public int conversionProfileId = Integer.MIN_VALUE;
	/**  IF not empty, points to an entry ID the should replace this current entry's id.  */
    public String redirectEntryId;
	/**  ID of source root entry, used for clipped, skipped and cropped entries that
	  created from another entry  */
    public String rootEntryId;
	/**  ID of source root entry, used for defining entires association  */
    public String parentEntryId;
	/**  clipping, skipping and cropping attributes that used to create this entry  */
    public ArrayList<KalturaOperationAttributes> operationAttributes;
	/**  list of user ids that are entitled to edit the entry (no server enforcement) The
	  difference between entitledUsersEdit and entitledUsersPublish is applicative
	  only  */
    public String entitledUsersEdit;
	/**  list of user ids that are entitled to publish the entry (no server enforcement)
	  The difference between entitledUsersEdit and entitledUsersPublish is applicative
	  only  */
    public String entitledUsersPublish;
	/**  Comma seperated string of the capabilities of the entry. Any capability needed
	  can be added to this list.  */
    public String capabilities;
	/**  Template entry id  */
    public String templateEntryId;
	/**  should we display this entry in search  */
    public KalturaEntryDisplayInSearchType displayInSearch;

    public KalturaBaseEntry() {
    }

    public KalturaBaseEntry(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("name")) {
                this.name = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("description")) {
                this.description = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerId")) {
                this.partnerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("userId")) {
                this.userId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("creatorId")) {
                this.creatorId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tags")) {
                this.tags = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("adminTags")) {
                this.adminTags = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("categories")) {
                this.categories = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("categoriesIds")) {
                this.categoriesIds = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaEntryStatus.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("moderationStatus")) {
                this.moderationStatus = KalturaEntryModerationStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("moderationCount")) {
                this.moderationCount = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("type")) {
                this.type = KalturaEntryType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAt")) {
                this.updatedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("rank")) {
                this.rank = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("totalRank")) {
                this.totalRank = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("votes")) {
                this.votes = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("groupId")) {
                this.groupId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerData")) {
                this.partnerData = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("downloadUrl")) {
                this.downloadUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("searchText")) {
                this.searchText = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("licenseType")) {
                this.licenseType = KalturaLicenseType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("version")) {
                this.version = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("thumbnailUrl")) {
                this.thumbnailUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("accessControlId")) {
                this.accessControlId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("startDate")) {
                this.startDate = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("endDate")) {
                this.endDate = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("referenceId")) {
                this.referenceId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("replacingEntryId")) {
                this.replacingEntryId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("replacedEntryId")) {
                this.replacedEntryId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("replacementStatus")) {
                this.replacementStatus = KalturaEntryReplacementStatus.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("partnerSortValue")) {
                this.partnerSortValue = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("conversionProfileId")) {
                this.conversionProfileId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("redirectEntryId")) {
                this.redirectEntryId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("rootEntryId")) {
                this.rootEntryId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("parentEntryId")) {
                this.parentEntryId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("operationAttributes")) {
                this.operationAttributes = ParseUtils.parseArray(KalturaOperationAttributes.class, aNode);
                continue;
            } else if (nodeName.equals("entitledUsersEdit")) {
                this.entitledUsersEdit = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("entitledUsersPublish")) {
                this.entitledUsersPublish = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("capabilities")) {
                this.capabilities = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("templateEntryId")) {
                this.templateEntryId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("displayInSearch")) {
                this.displayInSearch = KalturaEntryDisplayInSearchType.get(ParseUtils.parseInt(txt));
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaBaseEntry");
        kparams.add("name", this.name);
        kparams.add("description", this.description);
        kparams.add("userId", this.userId);
        kparams.add("creatorId", this.creatorId);
        kparams.add("tags", this.tags);
        kparams.add("adminTags", this.adminTags);
        kparams.add("categories", this.categories);
        kparams.add("categoriesIds", this.categoriesIds);
        kparams.add("type", this.type);
        kparams.add("groupId", this.groupId);
        kparams.add("partnerData", this.partnerData);
        kparams.add("licenseType", this.licenseType);
        kparams.add("accessControlId", this.accessControlId);
        kparams.add("startDate", this.startDate);
        kparams.add("endDate", this.endDate);
        kparams.add("referenceId", this.referenceId);
        kparams.add("partnerSortValue", this.partnerSortValue);
        kparams.add("conversionProfileId", this.conversionProfileId);
        kparams.add("redirectEntryId", this.redirectEntryId);
        kparams.add("parentEntryId", this.parentEntryId);
        kparams.add("operationAttributes", this.operationAttributes);
        kparams.add("entitledUsersEdit", this.entitledUsersEdit);
        kparams.add("entitledUsersPublish", this.entitledUsersPublish);
        kparams.add("templateEntryId", this.templateEntryId);
        kparams.add("displayInSearch", this.displayInSearch);
        return kparams;
    }

}

