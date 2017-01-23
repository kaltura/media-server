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
public enum KalturaLiveChannelOrderBy implements KalturaEnumAsString {
    CREATED_AT_ASC ("+createdAt"),
    DURATION_ASC ("+duration"),
    END_DATE_ASC ("+endDate"),
    FIRST_BROADCAST_ASC ("+firstBroadcast"),
    LAST_BROADCAST_ASC ("+lastBroadcast"),
    LAST_PLAYED_AT_ASC ("+lastPlayedAt"),
    MEDIA_TYPE_ASC ("+mediaType"),
    MODERATION_COUNT_ASC ("+moderationCount"),
    NAME_ASC ("+name"),
    PARTNER_SORT_VALUE_ASC ("+partnerSortValue"),
    PLAYS_ASC ("+plays"),
    RANK_ASC ("+rank"),
    RECENT_ASC ("+recent"),
    START_DATE_ASC ("+startDate"),
    TOTAL_RANK_ASC ("+totalRank"),
    UPDATED_AT_ASC ("+updatedAt"),
    VIEWS_ASC ("+views"),
    WEIGHT_ASC ("+weight"),
    CREATED_AT_DESC ("-createdAt"),
    DURATION_DESC ("-duration"),
    END_DATE_DESC ("-endDate"),
    FIRST_BROADCAST_DESC ("-firstBroadcast"),
    LAST_BROADCAST_DESC ("-lastBroadcast"),
    LAST_PLAYED_AT_DESC ("-lastPlayedAt"),
    MEDIA_TYPE_DESC ("-mediaType"),
    MODERATION_COUNT_DESC ("-moderationCount"),
    NAME_DESC ("-name"),
    PARTNER_SORT_VALUE_DESC ("-partnerSortValue"),
    PLAYS_DESC ("-plays"),
    RANK_DESC ("-rank"),
    RECENT_DESC ("-recent"),
    START_DATE_DESC ("-startDate"),
    TOTAL_RANK_DESC ("-totalRank"),
    UPDATED_AT_DESC ("-updatedAt"),
    VIEWS_DESC ("-views"),
    WEIGHT_DESC ("-weight");

    public String hashCode;

    KalturaLiveChannelOrderBy(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaLiveChannelOrderBy get(String hashCode) {
        if (hashCode.equals("+createdAt"))
        {
           return CREATED_AT_ASC;
        }
        else 
        if (hashCode.equals("+duration"))
        {
           return DURATION_ASC;
        }
        else 
        if (hashCode.equals("+endDate"))
        {
           return END_DATE_ASC;
        }
        else 
        if (hashCode.equals("+firstBroadcast"))
        {
           return FIRST_BROADCAST_ASC;
        }
        else 
        if (hashCode.equals("+lastBroadcast"))
        {
           return LAST_BROADCAST_ASC;
        }
        else 
        if (hashCode.equals("+lastPlayedAt"))
        {
           return LAST_PLAYED_AT_ASC;
        }
        else 
        if (hashCode.equals("+mediaType"))
        {
           return MEDIA_TYPE_ASC;
        }
        else 
        if (hashCode.equals("+moderationCount"))
        {
           return MODERATION_COUNT_ASC;
        }
        else 
        if (hashCode.equals("+name"))
        {
           return NAME_ASC;
        }
        else 
        if (hashCode.equals("+partnerSortValue"))
        {
           return PARTNER_SORT_VALUE_ASC;
        }
        else 
        if (hashCode.equals("+plays"))
        {
           return PLAYS_ASC;
        }
        else 
        if (hashCode.equals("+rank"))
        {
           return RANK_ASC;
        }
        else 
        if (hashCode.equals("+recent"))
        {
           return RECENT_ASC;
        }
        else 
        if (hashCode.equals("+startDate"))
        {
           return START_DATE_ASC;
        }
        else 
        if (hashCode.equals("+totalRank"))
        {
           return TOTAL_RANK_ASC;
        }
        else 
        if (hashCode.equals("+updatedAt"))
        {
           return UPDATED_AT_ASC;
        }
        else 
        if (hashCode.equals("+views"))
        {
           return VIEWS_ASC;
        }
        else 
        if (hashCode.equals("+weight"))
        {
           return WEIGHT_ASC;
        }
        else 
        if (hashCode.equals("-createdAt"))
        {
           return CREATED_AT_DESC;
        }
        else 
        if (hashCode.equals("-duration"))
        {
           return DURATION_DESC;
        }
        else 
        if (hashCode.equals("-endDate"))
        {
           return END_DATE_DESC;
        }
        else 
        if (hashCode.equals("-firstBroadcast"))
        {
           return FIRST_BROADCAST_DESC;
        }
        else 
        if (hashCode.equals("-lastBroadcast"))
        {
           return LAST_BROADCAST_DESC;
        }
        else 
        if (hashCode.equals("-lastPlayedAt"))
        {
           return LAST_PLAYED_AT_DESC;
        }
        else 
        if (hashCode.equals("-mediaType"))
        {
           return MEDIA_TYPE_DESC;
        }
        else 
        if (hashCode.equals("-moderationCount"))
        {
           return MODERATION_COUNT_DESC;
        }
        else 
        if (hashCode.equals("-name"))
        {
           return NAME_DESC;
        }
        else 
        if (hashCode.equals("-partnerSortValue"))
        {
           return PARTNER_SORT_VALUE_DESC;
        }
        else 
        if (hashCode.equals("-plays"))
        {
           return PLAYS_DESC;
        }
        else 
        if (hashCode.equals("-rank"))
        {
           return RANK_DESC;
        }
        else 
        if (hashCode.equals("-recent"))
        {
           return RECENT_DESC;
        }
        else 
        if (hashCode.equals("-startDate"))
        {
           return START_DATE_DESC;
        }
        else 
        if (hashCode.equals("-totalRank"))
        {
           return TOTAL_RANK_DESC;
        }
        else 
        if (hashCode.equals("-updatedAt"))
        {
           return UPDATED_AT_DESC;
        }
        else 
        if (hashCode.equals("-views"))
        {
           return VIEWS_DESC;
        }
        else 
        if (hashCode.equals("-weight"))
        {
           return WEIGHT_DESC;
        }
        else 
        {
           return CREATED_AT_ASC;
        }
    }
}
