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
import com.kaltura.client.enums.KalturaEntryStatus;
import com.kaltura.client.enums.KalturaEntryModerationStatus;
import com.kaltura.client.enums.KalturaEntryType;
import com.kaltura.client.enums.KalturaEntryReplacementStatus;
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
public abstract class KalturaBaseEntryBaseFilter extends KalturaRelatedFilter {
	/**  This filter should be in use for retrieving only a specific entry (identified by
	  its entryId).  */
    public String idEqual;
	/**  This filter should be in use for retrieving few specific entries (string should
	  include comma separated list of entryId strings).  */
    public String idIn;
    public String idNotIn;
	/**  This filter should be in use for retrieving specific entries. It should include
	  only one string to search for in entry names (no wildcards, spaces are treated
	  as part of the string).  */
    public String nameLike;
	/**  This filter should be in use for retrieving specific entries. It could include
	  few (comma separated) strings for searching in entry names, while applying an OR
	  logic to retrieve entries that contain at least one input string (no wildcards,
	  spaces are treated as part of the string).  */
    public String nameMultiLikeOr;
	/**  This filter should be in use for retrieving specific entries. It could include
	  few (comma separated) strings for searching in entry names, while applying an
	  AND logic to retrieve entries that contain all input strings (no wildcards,
	  spaces are treated as part of the string).  */
    public String nameMultiLikeAnd;
	/**  This filter should be in use for retrieving entries with a specific name.  */
    public String nameEqual;
	/**  This filter should be in use for retrieving only entries which were uploaded
	  by/assigned to users of a specific Kaltura Partner (identified by Partner ID).  */
    public int partnerIdEqual = Integer.MIN_VALUE;
	/**  This filter should be in use for retrieving only entries within Kaltura network
	  which were uploaded by/assigned to users of few Kaltura Partners  (string should
	  include comma separated list of PartnerIDs)  */
    public String partnerIdIn;
	/**  This filter parameter should be in use for retrieving only entries, uploaded
	  by/assigned to a specific user (identified by user Id).  */
    public String userIdEqual;
    public String userIdIn;
    public String userIdNotIn;
    public String creatorIdEqual;
	/**  This filter should be in use for retrieving specific entries. It should include
	  only one string to search for in entry tags (no wildcards, spaces are treated as
	  part of the string).  */
    public String tagsLike;
	/**  This filter should be in use for retrieving specific entries. It could include
	  few (comma separated) strings for searching in entry tags, while applying an OR
	  logic to retrieve entries that contain at least one input string (no wildcards,
	  spaces are treated as part of the string).  */
    public String tagsMultiLikeOr;
	/**  This filter should be in use for retrieving specific entries. It could include
	  few (comma separated) strings for searching in entry tags, while applying an AND
	  logic to retrieve entries that contain all input strings (no wildcards, spaces
	  are treated as part of the string).  */
    public String tagsMultiLikeAnd;
	/**  This filter should be in use for retrieving specific entries. It should include
	  only one string to search for in entry tags set by an ADMIN user (no wildcards,
	  spaces are treated as part of the string).  */
    public String adminTagsLike;
	/**  This filter should be in use for retrieving specific entries. It could include
	  few (comma separated) strings for searching in entry tags, set by an ADMIN user,
	  while applying an OR logic to retrieve entries that contain at least one input
	  string (no wildcards, spaces are treated as part of the string).  */
    public String adminTagsMultiLikeOr;
	/**  This filter should be in use for retrieving specific entries. It could include
	  few (comma separated) strings for searching in entry tags, set by an ADMIN user,
	  while applying an AND logic to retrieve entries that contain all input strings
	  (no wildcards, spaces are treated as part of the string).  */
    public String adminTagsMultiLikeAnd;
    public String categoriesMatchAnd;
	/**  All entries within these categories or their child categories.  */
    public String categoriesMatchOr;
    public String categoriesNotContains;
    public String categoriesIdsMatchAnd;
	/**  All entries of the categories, excluding their child categories.   To include
	  entries of the child categories, use categoryAncestorIdIn, or categoriesMatchOr.  */
    public String categoriesIdsMatchOr;
    public String categoriesIdsNotContains;
    public KalturaNullableBoolean categoriesIdsEmpty;
	/**  This filter should be in use for retrieving only entries, at a specific {  */
    public KalturaEntryStatus statusEqual;
	/**  This filter should be in use for retrieving only entries, not at a specific {  */
    public KalturaEntryStatus statusNotEqual;
	/**  This filter should be in use for retrieving only entries, at few specific {  */
    public String statusIn;
	/**  This filter should be in use for retrieving only entries, not at few specific {  */
    public String statusNotIn;
    public KalturaEntryModerationStatus moderationStatusEqual;
    public KalturaEntryModerationStatus moderationStatusNotEqual;
    public String moderationStatusIn;
    public String moderationStatusNotIn;
    public KalturaEntryType typeEqual;
	/**  This filter should be in use for retrieving entries of few {  */
    public String typeIn;
	/**  This filter parameter should be in use for retrieving only entries which were
	  created at Kaltura system after a specific time/date (standard timestamp
	  format).  */
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
	/**  This filter parameter should be in use for retrieving only entries which were
	  created at Kaltura system before a specific time/date (standard timestamp
	  format).  */
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtLessThanOrEqual = Integer.MIN_VALUE;
    public int totalRankLessThanOrEqual = Integer.MIN_VALUE;
    public int totalRankGreaterThanOrEqual = Integer.MIN_VALUE;
    public int groupIdEqual = Integer.MIN_VALUE;
	/**  This filter should be in use for retrieving specific entries while search match
	  the input string within all of the following metadata attributes: name,
	  description, tags, adminTags.  */
    public String searchTextMatchAnd;
	/**  This filter should be in use for retrieving specific entries while search match
	  the input string within at least one of the following metadata attributes: name,
	  description, tags, adminTags.  */
    public String searchTextMatchOr;
    public int accessControlIdEqual = Integer.MIN_VALUE;
    public String accessControlIdIn;
    public int startDateGreaterThanOrEqual = Integer.MIN_VALUE;
    public int startDateLessThanOrEqual = Integer.MIN_VALUE;
    public int startDateGreaterThanOrEqualOrNull = Integer.MIN_VALUE;
    public int startDateLessThanOrEqualOrNull = Integer.MIN_VALUE;
    public int endDateGreaterThanOrEqual = Integer.MIN_VALUE;
    public int endDateLessThanOrEqual = Integer.MIN_VALUE;
    public int endDateGreaterThanOrEqualOrNull = Integer.MIN_VALUE;
    public int endDateLessThanOrEqualOrNull = Integer.MIN_VALUE;
    public String referenceIdEqual;
    public String referenceIdIn;
    public String replacingEntryIdEqual;
    public String replacingEntryIdIn;
    public String replacedEntryIdEqual;
    public String replacedEntryIdIn;
    public KalturaEntryReplacementStatus replacementStatusEqual;
    public String replacementStatusIn;
    public int partnerSortValueGreaterThanOrEqual = Integer.MIN_VALUE;
    public int partnerSortValueLessThanOrEqual = Integer.MIN_VALUE;
    public String rootEntryIdEqual;
    public String rootEntryIdIn;
    public String parentEntryIdEqual;
    public String entitledUsersEditMatchAnd;
    public String entitledUsersEditMatchOr;
    public String entitledUsersPublishMatchAnd;
    public String entitledUsersPublishMatchOr;
    public String tagsNameMultiLikeOr;
    public String tagsAdminTagsMultiLikeOr;
    public String tagsAdminTagsNameMultiLikeOr;
    public String tagsNameMultiLikeAnd;
    public String tagsAdminTagsMultiLikeAnd;
    public String tagsAdminTagsNameMultiLikeAnd;

    public KalturaBaseEntryBaseFilter() {
    }

    public KalturaBaseEntryBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("idEqual")) {
                this.idEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("idIn")) {
                this.idIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("idNotIn")) {
                this.idNotIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("nameLike")) {
                this.nameLike = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("nameMultiLikeOr")) {
                this.nameMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("nameMultiLikeAnd")) {
                this.nameMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("nameEqual")) {
                this.nameEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerIdEqual")) {
                this.partnerIdEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerIdIn")) {
                this.partnerIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("userIdEqual")) {
                this.userIdEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("userIdIn")) {
                this.userIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("userIdNotIn")) {
                this.userIdNotIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("creatorIdEqual")) {
                this.creatorIdEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tagsLike")) {
                this.tagsLike = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tagsMultiLikeOr")) {
                this.tagsMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tagsMultiLikeAnd")) {
                this.tagsMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("adminTagsLike")) {
                this.adminTagsLike = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("adminTagsMultiLikeOr")) {
                this.adminTagsMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("adminTagsMultiLikeAnd")) {
                this.adminTagsMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("categoriesMatchAnd")) {
                this.categoriesMatchAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("categoriesMatchOr")) {
                this.categoriesMatchOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("categoriesNotContains")) {
                this.categoriesNotContains = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("categoriesIdsMatchAnd")) {
                this.categoriesIdsMatchAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("categoriesIdsMatchOr")) {
                this.categoriesIdsMatchOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("categoriesIdsNotContains")) {
                this.categoriesIdsNotContains = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("categoriesIdsEmpty")) {
                this.categoriesIdsEmpty = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("statusEqual")) {
                this.statusEqual = KalturaEntryStatus.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("statusNotEqual")) {
                this.statusNotEqual = KalturaEntryStatus.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("statusNotIn")) {
                this.statusNotIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("moderationStatusEqual")) {
                this.moderationStatusEqual = KalturaEntryModerationStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("moderationStatusNotEqual")) {
                this.moderationStatusNotEqual = KalturaEntryModerationStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("moderationStatusIn")) {
                this.moderationStatusIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("moderationStatusNotIn")) {
                this.moderationStatusNotIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("typeEqual")) {
                this.typeEqual = KalturaEntryType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("typeIn")) {
                this.typeIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("createdAtGreaterThanOrEqual")) {
                this.createdAtGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("createdAtLessThanOrEqual")) {
                this.createdAtLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAtGreaterThanOrEqual")) {
                this.updatedAtGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAtLessThanOrEqual")) {
                this.updatedAtLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("totalRankLessThanOrEqual")) {
                this.totalRankLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("totalRankGreaterThanOrEqual")) {
                this.totalRankGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("groupIdEqual")) {
                this.groupIdEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("searchTextMatchAnd")) {
                this.searchTextMatchAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("searchTextMatchOr")) {
                this.searchTextMatchOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("accessControlIdEqual")) {
                this.accessControlIdEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("accessControlIdIn")) {
                this.accessControlIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("startDateGreaterThanOrEqual")) {
                this.startDateGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("startDateLessThanOrEqual")) {
                this.startDateLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("startDateGreaterThanOrEqualOrNull")) {
                this.startDateGreaterThanOrEqualOrNull = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("startDateLessThanOrEqualOrNull")) {
                this.startDateLessThanOrEqualOrNull = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("endDateGreaterThanOrEqual")) {
                this.endDateGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("endDateLessThanOrEqual")) {
                this.endDateLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("endDateGreaterThanOrEqualOrNull")) {
                this.endDateGreaterThanOrEqualOrNull = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("endDateLessThanOrEqualOrNull")) {
                this.endDateLessThanOrEqualOrNull = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("referenceIdEqual")) {
                this.referenceIdEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("referenceIdIn")) {
                this.referenceIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("replacingEntryIdEqual")) {
                this.replacingEntryIdEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("replacingEntryIdIn")) {
                this.replacingEntryIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("replacedEntryIdEqual")) {
                this.replacedEntryIdEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("replacedEntryIdIn")) {
                this.replacedEntryIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("replacementStatusEqual")) {
                this.replacementStatusEqual = KalturaEntryReplacementStatus.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("replacementStatusIn")) {
                this.replacementStatusIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerSortValueGreaterThanOrEqual")) {
                this.partnerSortValueGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerSortValueLessThanOrEqual")) {
                this.partnerSortValueLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("rootEntryIdEqual")) {
                this.rootEntryIdEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("rootEntryIdIn")) {
                this.rootEntryIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("parentEntryIdEqual")) {
                this.parentEntryIdEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("entitledUsersEditMatchAnd")) {
                this.entitledUsersEditMatchAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("entitledUsersEditMatchOr")) {
                this.entitledUsersEditMatchOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("entitledUsersPublishMatchAnd")) {
                this.entitledUsersPublishMatchAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("entitledUsersPublishMatchOr")) {
                this.entitledUsersPublishMatchOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tagsNameMultiLikeOr")) {
                this.tagsNameMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tagsAdminTagsMultiLikeOr")) {
                this.tagsAdminTagsMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tagsAdminTagsNameMultiLikeOr")) {
                this.tagsAdminTagsNameMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tagsNameMultiLikeAnd")) {
                this.tagsNameMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tagsAdminTagsMultiLikeAnd")) {
                this.tagsAdminTagsMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tagsAdminTagsNameMultiLikeAnd")) {
                this.tagsAdminTagsNameMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaBaseEntryBaseFilter");
        kparams.add("idEqual", this.idEqual);
        kparams.add("idIn", this.idIn);
        kparams.add("idNotIn", this.idNotIn);
        kparams.add("nameLike", this.nameLike);
        kparams.add("nameMultiLikeOr", this.nameMultiLikeOr);
        kparams.add("nameMultiLikeAnd", this.nameMultiLikeAnd);
        kparams.add("nameEqual", this.nameEqual);
        kparams.add("partnerIdEqual", this.partnerIdEqual);
        kparams.add("partnerIdIn", this.partnerIdIn);
        kparams.add("userIdEqual", this.userIdEqual);
        kparams.add("userIdIn", this.userIdIn);
        kparams.add("userIdNotIn", this.userIdNotIn);
        kparams.add("creatorIdEqual", this.creatorIdEqual);
        kparams.add("tagsLike", this.tagsLike);
        kparams.add("tagsMultiLikeOr", this.tagsMultiLikeOr);
        kparams.add("tagsMultiLikeAnd", this.tagsMultiLikeAnd);
        kparams.add("adminTagsLike", this.adminTagsLike);
        kparams.add("adminTagsMultiLikeOr", this.adminTagsMultiLikeOr);
        kparams.add("adminTagsMultiLikeAnd", this.adminTagsMultiLikeAnd);
        kparams.add("categoriesMatchAnd", this.categoriesMatchAnd);
        kparams.add("categoriesMatchOr", this.categoriesMatchOr);
        kparams.add("categoriesNotContains", this.categoriesNotContains);
        kparams.add("categoriesIdsMatchAnd", this.categoriesIdsMatchAnd);
        kparams.add("categoriesIdsMatchOr", this.categoriesIdsMatchOr);
        kparams.add("categoriesIdsNotContains", this.categoriesIdsNotContains);
        kparams.add("categoriesIdsEmpty", this.categoriesIdsEmpty);
        kparams.add("statusEqual", this.statusEqual);
        kparams.add("statusNotEqual", this.statusNotEqual);
        kparams.add("statusIn", this.statusIn);
        kparams.add("statusNotIn", this.statusNotIn);
        kparams.add("moderationStatusEqual", this.moderationStatusEqual);
        kparams.add("moderationStatusNotEqual", this.moderationStatusNotEqual);
        kparams.add("moderationStatusIn", this.moderationStatusIn);
        kparams.add("moderationStatusNotIn", this.moderationStatusNotIn);
        kparams.add("typeEqual", this.typeEqual);
        kparams.add("typeIn", this.typeIn);
        kparams.add("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.add("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.add("updatedAtGreaterThanOrEqual", this.updatedAtGreaterThanOrEqual);
        kparams.add("updatedAtLessThanOrEqual", this.updatedAtLessThanOrEqual);
        kparams.add("totalRankLessThanOrEqual", this.totalRankLessThanOrEqual);
        kparams.add("totalRankGreaterThanOrEqual", this.totalRankGreaterThanOrEqual);
        kparams.add("groupIdEqual", this.groupIdEqual);
        kparams.add("searchTextMatchAnd", this.searchTextMatchAnd);
        kparams.add("searchTextMatchOr", this.searchTextMatchOr);
        kparams.add("accessControlIdEqual", this.accessControlIdEqual);
        kparams.add("accessControlIdIn", this.accessControlIdIn);
        kparams.add("startDateGreaterThanOrEqual", this.startDateGreaterThanOrEqual);
        kparams.add("startDateLessThanOrEqual", this.startDateLessThanOrEqual);
        kparams.add("startDateGreaterThanOrEqualOrNull", this.startDateGreaterThanOrEqualOrNull);
        kparams.add("startDateLessThanOrEqualOrNull", this.startDateLessThanOrEqualOrNull);
        kparams.add("endDateGreaterThanOrEqual", this.endDateGreaterThanOrEqual);
        kparams.add("endDateLessThanOrEqual", this.endDateLessThanOrEqual);
        kparams.add("endDateGreaterThanOrEqualOrNull", this.endDateGreaterThanOrEqualOrNull);
        kparams.add("endDateLessThanOrEqualOrNull", this.endDateLessThanOrEqualOrNull);
        kparams.add("referenceIdEqual", this.referenceIdEqual);
        kparams.add("referenceIdIn", this.referenceIdIn);
        kparams.add("replacingEntryIdEqual", this.replacingEntryIdEqual);
        kparams.add("replacingEntryIdIn", this.replacingEntryIdIn);
        kparams.add("replacedEntryIdEqual", this.replacedEntryIdEqual);
        kparams.add("replacedEntryIdIn", this.replacedEntryIdIn);
        kparams.add("replacementStatusEqual", this.replacementStatusEqual);
        kparams.add("replacementStatusIn", this.replacementStatusIn);
        kparams.add("partnerSortValueGreaterThanOrEqual", this.partnerSortValueGreaterThanOrEqual);
        kparams.add("partnerSortValueLessThanOrEqual", this.partnerSortValueLessThanOrEqual);
        kparams.add("rootEntryIdEqual", this.rootEntryIdEqual);
        kparams.add("rootEntryIdIn", this.rootEntryIdIn);
        kparams.add("parentEntryIdEqual", this.parentEntryIdEqual);
        kparams.add("entitledUsersEditMatchAnd", this.entitledUsersEditMatchAnd);
        kparams.add("entitledUsersEditMatchOr", this.entitledUsersEditMatchOr);
        kparams.add("entitledUsersPublishMatchAnd", this.entitledUsersPublishMatchAnd);
        kparams.add("entitledUsersPublishMatchOr", this.entitledUsersPublishMatchOr);
        kparams.add("tagsNameMultiLikeOr", this.tagsNameMultiLikeOr);
        kparams.add("tagsAdminTagsMultiLikeOr", this.tagsAdminTagsMultiLikeOr);
        kparams.add("tagsAdminTagsNameMultiLikeOr", this.tagsAdminTagsNameMultiLikeOr);
        kparams.add("tagsNameMultiLikeAnd", this.tagsNameMultiLikeAnd);
        kparams.add("tagsAdminTagsMultiLikeAnd", this.tagsAdminTagsMultiLikeAnd);
        kparams.add("tagsAdminTagsNameMultiLikeAnd", this.tagsAdminTagsNameMultiLikeAnd);
        return kparams;
    }

}

