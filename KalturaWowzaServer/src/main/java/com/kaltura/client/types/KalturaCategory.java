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
import com.kaltura.client.enums.KalturaAppearInListType;
import com.kaltura.client.enums.KalturaPrivacyType;
import com.kaltura.client.enums.KalturaInheritanceType;
import com.kaltura.client.enums.KalturaUserJoinPolicyType;
import com.kaltura.client.enums.KalturaCategoryUserPermissionLevel;
import com.kaltura.client.enums.KalturaContributionPolicyType;
import com.kaltura.client.enums.KalturaCategoryStatus;
import com.kaltura.client.enums.KalturaCategoryOrderBy;
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
public class KalturaCategory extends KalturaObjectBase {
	/**  The id of the Category  */
    public int id = Integer.MIN_VALUE;
    public int parentId = Integer.MIN_VALUE;
    public int depth = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
	/**  The name of the Category.    The following characters are not allowed: '&lt;',
	  '&gt;', ','  */
    public String name;
	/**  The full name of the Category  */
    public String fullName;
	/**  The full ids of the Category  */
    public String fullIds;
	/**  Number of entries in this Category (including child categories)  */
    public int entriesCount = Integer.MIN_VALUE;
	/**  Creation date as Unix timestamp (In seconds)  */
    public int createdAt = Integer.MIN_VALUE;
	/**  Update date as Unix timestamp (In seconds)  */
    public int updatedAt = Integer.MIN_VALUE;
	/**  Category description  */
    public String description;
	/**  Category tags  */
    public String tags;
	/**  If category will be returned for list action.  */
    public KalturaAppearInListType appearInList;
	/**  defines the privacy of the entries that assigned to this category  */
    public KalturaPrivacyType privacy;
	/**  If Category members are inherited from parent category or set manualy.  */
    public KalturaInheritanceType inheritanceType;
	/**  Who can ask to join this category  */
    public KalturaUserJoinPolicyType userJoinPolicy;
	/**  Default permissionLevel for new users  */
    public KalturaCategoryUserPermissionLevel defaultPermissionLevel;
	/**  Category Owner (User id)  */
    public String owner;
	/**  Number of entries that belong to this category directly  */
    public int directEntriesCount = Integer.MIN_VALUE;
	/**  Category external id, controlled and managed by the partner.  */
    public String referenceId;
	/**  who can assign entries to this category  */
    public KalturaContributionPolicyType contributionPolicy;
	/**  Number of active members for this category  */
    public int membersCount = Integer.MIN_VALUE;
	/**  Number of pending members for this category  */
    public int pendingMembersCount = Integer.MIN_VALUE;
	/**  Set privacy context for search entries that assiged to private and public
	  categories. the entries will be private if the search context is set with those
	  categories.  */
    public String privacyContext;
	/**  comma separated parents that defines a privacyContext for search  */
    public String privacyContexts;
	/**  Status  */
    public KalturaCategoryStatus status;
	/**  The category id that this category inherit its members and members permission
	  (for contribution and join)  */
    public int inheritedParentId = Integer.MIN_VALUE;
	/**  Can be used to store various partner related data as a numeric value  */
    public int partnerSortValue = Integer.MIN_VALUE;
	/**  Can be used to store various partner related data as a string  */
    public String partnerData;
	/**  Enable client side applications to define how to sort the category child
	  categories  */
    public KalturaCategoryOrderBy defaultOrderBy;
	/**  Number of direct children categories  */
    public int directSubCategoriesCount = Integer.MIN_VALUE;
	/**  Moderation to add entries to this category by users that are not of permission
	  level Manager or Moderator.  */
    public KalturaNullableBoolean moderation;
	/**  Nunber of pending moderation entries  */
    public int pendingEntriesCount = Integer.MIN_VALUE;
	/**  Flag indicating that the category is an aggregation category  */
    public KalturaNullableBoolean isAggregationCategory;
	/**  List of aggregation channels the category belongs to  */
    public String aggregationCategories;

    public KalturaCategory() {
    }

    public KalturaCategory(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("parentId")) {
                this.parentId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("depth")) {
                this.depth = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerId")) {
                this.partnerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("name")) {
                this.name = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("fullName")) {
                this.fullName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("fullIds")) {
                this.fullIds = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("entriesCount")) {
                this.entriesCount = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAt")) {
                this.updatedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("description")) {
                this.description = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tags")) {
                this.tags = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("appearInList")) {
                this.appearInList = KalturaAppearInListType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("privacy")) {
                this.privacy = KalturaPrivacyType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("inheritanceType")) {
                this.inheritanceType = KalturaInheritanceType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("userJoinPolicy")) {
                this.userJoinPolicy = KalturaUserJoinPolicyType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("defaultPermissionLevel")) {
                this.defaultPermissionLevel = KalturaCategoryUserPermissionLevel.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("owner")) {
                this.owner = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("directEntriesCount")) {
                this.directEntriesCount = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("referenceId")) {
                this.referenceId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("contributionPolicy")) {
                this.contributionPolicy = KalturaContributionPolicyType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("membersCount")) {
                this.membersCount = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("pendingMembersCount")) {
                this.pendingMembersCount = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("privacyContext")) {
                this.privacyContext = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("privacyContexts")) {
                this.privacyContexts = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaCategoryStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("inheritedParentId")) {
                this.inheritedParentId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerSortValue")) {
                this.partnerSortValue = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerData")) {
                this.partnerData = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("defaultOrderBy")) {
                this.defaultOrderBy = KalturaCategoryOrderBy.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("directSubCategoriesCount")) {
                this.directSubCategoriesCount = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("moderation")) {
                this.moderation = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("pendingEntriesCount")) {
                this.pendingEntriesCount = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("isAggregationCategory")) {
                this.isAggregationCategory = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("aggregationCategories")) {
                this.aggregationCategories = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaCategory");
        kparams.add("parentId", this.parentId);
        kparams.add("name", this.name);
        kparams.add("description", this.description);
        kparams.add("tags", this.tags);
        kparams.add("appearInList", this.appearInList);
        kparams.add("privacy", this.privacy);
        kparams.add("inheritanceType", this.inheritanceType);
        kparams.add("defaultPermissionLevel", this.defaultPermissionLevel);
        kparams.add("owner", this.owner);
        kparams.add("referenceId", this.referenceId);
        kparams.add("contributionPolicy", this.contributionPolicy);
        kparams.add("privacyContext", this.privacyContext);
        kparams.add("partnerSortValue", this.partnerSortValue);
        kparams.add("partnerData", this.partnerData);
        kparams.add("defaultOrderBy", this.defaultOrderBy);
        kparams.add("moderation", this.moderation);
        kparams.add("isAggregationCategory", this.isAggregationCategory);
        kparams.add("aggregationCategories", this.aggregationCategories);
        return kparams;
    }

}

