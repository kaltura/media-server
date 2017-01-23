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
package com.kaltura.client.enums;

/**
 * This class was generated using exec.php
 * against an XML schema provided by Kaltura.
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaMediaEntryCompareAttribute implements KalturaEnumAsString {
    ACCESS_CONTROL_ID ("accessControlId"),
    CREATED_AT ("createdAt"),
    END_DATE ("endDate"),
    LAST_PLAYED_AT ("lastPlayedAt"),
    MEDIA_DATE ("mediaDate"),
    MEDIA_TYPE ("mediaType"),
    MODERATION_COUNT ("moderationCount"),
    MODERATION_STATUS ("moderationStatus"),
    MS_DURATION ("msDuration"),
    PARTNER_ID ("partnerId"),
    PARTNER_SORT_VALUE ("partnerSortValue"),
    PLAYS ("plays"),
    RANK ("rank"),
    REPLACEMENT_STATUS ("replacementStatus"),
    START_DATE ("startDate"),
    STATUS ("status"),
    TOTAL_RANK ("totalRank"),
    TYPE ("type"),
    UPDATED_AT ("updatedAt"),
    VIEWS ("views");

    public String hashCode;

    KalturaMediaEntryCompareAttribute(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaMediaEntryCompareAttribute get(String hashCode) {
        if (hashCode.equals("accessControlId"))
        {
           return ACCESS_CONTROL_ID;
        }
        else 
        if (hashCode.equals("createdAt"))
        {
           return CREATED_AT;
        }
        else 
        if (hashCode.equals("endDate"))
        {
           return END_DATE;
        }
        else 
        if (hashCode.equals("lastPlayedAt"))
        {
           return LAST_PLAYED_AT;
        }
        else 
        if (hashCode.equals("mediaDate"))
        {
           return MEDIA_DATE;
        }
        else 
        if (hashCode.equals("mediaType"))
        {
           return MEDIA_TYPE;
        }
        else 
        if (hashCode.equals("moderationCount"))
        {
           return MODERATION_COUNT;
        }
        else 
        if (hashCode.equals("moderationStatus"))
        {
           return MODERATION_STATUS;
        }
        else 
        if (hashCode.equals("msDuration"))
        {
           return MS_DURATION;
        }
        else 
        if (hashCode.equals("partnerId"))
        {
           return PARTNER_ID;
        }
        else 
        if (hashCode.equals("partnerSortValue"))
        {
           return PARTNER_SORT_VALUE;
        }
        else 
        if (hashCode.equals("plays"))
        {
           return PLAYS;
        }
        else 
        if (hashCode.equals("rank"))
        {
           return RANK;
        }
        else 
        if (hashCode.equals("replacementStatus"))
        {
           return REPLACEMENT_STATUS;
        }
        else 
        if (hashCode.equals("startDate"))
        {
           return START_DATE;
        }
        else 
        if (hashCode.equals("status"))
        {
           return STATUS;
        }
        else 
        if (hashCode.equals("totalRank"))
        {
           return TOTAL_RANK;
        }
        else 
        if (hashCode.equals("type"))
        {
           return TYPE;
        }
        else 
        if (hashCode.equals("updatedAt"))
        {
           return UPDATED_AT;
        }
        else 
        if (hashCode.equals("views"))
        {
           return VIEWS;
        }
        else 
        {
           return ACCESS_CONTROL_ID;
        }
    }
}
