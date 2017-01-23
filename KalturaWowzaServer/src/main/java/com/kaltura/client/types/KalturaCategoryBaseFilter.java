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
import com.kaltura.client.enums.KalturaAppearInListType;
import com.kaltura.client.enums.KalturaPrivacyType;
import com.kaltura.client.enums.KalturaInheritanceType;
import com.kaltura.client.enums.KalturaNullableBoolean;
import com.kaltura.client.enums.KalturaContributionPolicyType;
import com.kaltura.client.enums.KalturaCategoryStatus;
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
public abstract class KalturaCategoryBaseFilter extends KalturaRelatedFilter {
    public int idEqual = Integer.MIN_VALUE;
    public String idIn;
    public String idNotIn;
    public int parentIdEqual = Integer.MIN_VALUE;
    public String parentIdIn;
    public int depthEqual = Integer.MIN_VALUE;
    public String fullNameEqual;
    public String fullNameStartsWith;
    public String fullNameIn;
    public String fullIdsEqual;
    public String fullIdsStartsWith;
    public String fullIdsMatchOr;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtLessThanOrEqual = Integer.MIN_VALUE;
    public String tagsLike;
    public String tagsMultiLikeOr;
    public String tagsMultiLikeAnd;
    public KalturaAppearInListType appearInListEqual;
    public KalturaPrivacyType privacyEqual;
    public String privacyIn;
    public KalturaInheritanceType inheritanceTypeEqual;
    public String inheritanceTypeIn;
    public String referenceIdEqual;
    public KalturaNullableBoolean referenceIdEmpty;
    public KalturaContributionPolicyType contributionPolicyEqual;
    public int membersCountGreaterThanOrEqual = Integer.MIN_VALUE;
    public int membersCountLessThanOrEqual = Integer.MIN_VALUE;
    public int pendingMembersCountGreaterThanOrEqual = Integer.MIN_VALUE;
    public int pendingMembersCountLessThanOrEqual = Integer.MIN_VALUE;
    public String privacyContextEqual;
    public KalturaCategoryStatus statusEqual;
    public String statusIn;
    public int inheritedParentIdEqual = Integer.MIN_VALUE;
    public String inheritedParentIdIn;
    public int partnerSortValueGreaterThanOrEqual = Integer.MIN_VALUE;
    public int partnerSortValueLessThanOrEqual = Integer.MIN_VALUE;
    public String aggregationCategoriesMultiLikeOr;
    public String aggregationCategoriesMultiLikeAnd;

    public KalturaCategoryBaseFilter() {
    }

    public KalturaCategoryBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("idEqual")) {
                this.idEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("idIn")) {
                this.idIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("idNotIn")) {
                this.idNotIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("parentIdEqual")) {
                this.parentIdEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("parentIdIn")) {
                this.parentIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("depthEqual")) {
                this.depthEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("fullNameEqual")) {
                this.fullNameEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("fullNameStartsWith")) {
                this.fullNameStartsWith = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("fullNameIn")) {
                this.fullNameIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("fullIdsEqual")) {
                this.fullIdsEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("fullIdsStartsWith")) {
                this.fullIdsStartsWith = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("fullIdsMatchOr")) {
                this.fullIdsMatchOr = ParseUtils.parseString(txt);
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
            } else if (nodeName.equals("tagsLike")) {
                this.tagsLike = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tagsMultiLikeOr")) {
                this.tagsMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tagsMultiLikeAnd")) {
                this.tagsMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("appearInListEqual")) {
                this.appearInListEqual = KalturaAppearInListType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("privacyEqual")) {
                this.privacyEqual = KalturaPrivacyType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("privacyIn")) {
                this.privacyIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("inheritanceTypeEqual")) {
                this.inheritanceTypeEqual = KalturaInheritanceType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("inheritanceTypeIn")) {
                this.inheritanceTypeIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("referenceIdEqual")) {
                this.referenceIdEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("referenceIdEmpty")) {
                this.referenceIdEmpty = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("contributionPolicyEqual")) {
                this.contributionPolicyEqual = KalturaContributionPolicyType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("membersCountGreaterThanOrEqual")) {
                this.membersCountGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("membersCountLessThanOrEqual")) {
                this.membersCountLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("pendingMembersCountGreaterThanOrEqual")) {
                this.pendingMembersCountGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("pendingMembersCountLessThanOrEqual")) {
                this.pendingMembersCountLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("privacyContextEqual")) {
                this.privacyContextEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("statusEqual")) {
                this.statusEqual = KalturaCategoryStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("inheritedParentIdEqual")) {
                this.inheritedParentIdEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("inheritedParentIdIn")) {
                this.inheritedParentIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerSortValueGreaterThanOrEqual")) {
                this.partnerSortValueGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerSortValueLessThanOrEqual")) {
                this.partnerSortValueLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("aggregationCategoriesMultiLikeOr")) {
                this.aggregationCategoriesMultiLikeOr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("aggregationCategoriesMultiLikeAnd")) {
                this.aggregationCategoriesMultiLikeAnd = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaCategoryBaseFilter");
        kparams.add("idEqual", this.idEqual);
        kparams.add("idIn", this.idIn);
        kparams.add("idNotIn", this.idNotIn);
        kparams.add("parentIdEqual", this.parentIdEqual);
        kparams.add("parentIdIn", this.parentIdIn);
        kparams.add("depthEqual", this.depthEqual);
        kparams.add("fullNameEqual", this.fullNameEqual);
        kparams.add("fullNameStartsWith", this.fullNameStartsWith);
        kparams.add("fullNameIn", this.fullNameIn);
        kparams.add("fullIdsEqual", this.fullIdsEqual);
        kparams.add("fullIdsStartsWith", this.fullIdsStartsWith);
        kparams.add("fullIdsMatchOr", this.fullIdsMatchOr);
        kparams.add("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.add("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.add("updatedAtGreaterThanOrEqual", this.updatedAtGreaterThanOrEqual);
        kparams.add("updatedAtLessThanOrEqual", this.updatedAtLessThanOrEqual);
        kparams.add("tagsLike", this.tagsLike);
        kparams.add("tagsMultiLikeOr", this.tagsMultiLikeOr);
        kparams.add("tagsMultiLikeAnd", this.tagsMultiLikeAnd);
        kparams.add("appearInListEqual", this.appearInListEqual);
        kparams.add("privacyEqual", this.privacyEqual);
        kparams.add("privacyIn", this.privacyIn);
        kparams.add("inheritanceTypeEqual", this.inheritanceTypeEqual);
        kparams.add("inheritanceTypeIn", this.inheritanceTypeIn);
        kparams.add("referenceIdEqual", this.referenceIdEqual);
        kparams.add("referenceIdEmpty", this.referenceIdEmpty);
        kparams.add("contributionPolicyEqual", this.contributionPolicyEqual);
        kparams.add("membersCountGreaterThanOrEqual", this.membersCountGreaterThanOrEqual);
        kparams.add("membersCountLessThanOrEqual", this.membersCountLessThanOrEqual);
        kparams.add("pendingMembersCountGreaterThanOrEqual", this.pendingMembersCountGreaterThanOrEqual);
        kparams.add("pendingMembersCountLessThanOrEqual", this.pendingMembersCountLessThanOrEqual);
        kparams.add("privacyContextEqual", this.privacyContextEqual);
        kparams.add("statusEqual", this.statusEqual);
        kparams.add("statusIn", this.statusIn);
        kparams.add("inheritedParentIdEqual", this.inheritedParentIdEqual);
        kparams.add("inheritedParentIdIn", this.inheritedParentIdIn);
        kparams.add("partnerSortValueGreaterThanOrEqual", this.partnerSortValueGreaterThanOrEqual);
        kparams.add("partnerSortValueLessThanOrEqual", this.partnerSortValueLessThanOrEqual);
        kparams.add("aggregationCategoriesMultiLikeOr", this.aggregationCategoriesMultiLikeOr);
        kparams.add("aggregationCategoriesMultiLikeAnd", this.aggregationCategoriesMultiLikeAnd);
        return kparams;
    }

}

