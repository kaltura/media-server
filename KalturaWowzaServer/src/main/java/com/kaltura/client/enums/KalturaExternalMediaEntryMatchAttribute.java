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
public enum KalturaExternalMediaEntryMatchAttribute implements KalturaEnumAsString {
    ADMIN_TAGS ("adminTags"),
    CATEGORIES_IDS ("categoriesIds"),
    CREATOR_ID ("creatorId"),
    DESCRIPTION ("description"),
    DURATION_TYPE ("durationType"),
    FLAVOR_PARAMS_IDS ("flavorParamsIds"),
    GROUP_ID ("groupId"),
    ID ("id"),
    NAME ("name"),
    REFERENCE_ID ("referenceId"),
    REPLACED_ENTRY_ID ("replacedEntryId"),
    REPLACING_ENTRY_ID ("replacingEntryId"),
    SEARCH_TEXT ("searchText"),
    TAGS ("tags"),
    USER_ID ("userId");

    public String hashCode;

    KalturaExternalMediaEntryMatchAttribute(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaExternalMediaEntryMatchAttribute get(String hashCode) {
        if (hashCode.equals("adminTags"))
        {
           return ADMIN_TAGS;
        }
        else 
        if (hashCode.equals("categoriesIds"))
        {
           return CATEGORIES_IDS;
        }
        else 
        if (hashCode.equals("creatorId"))
        {
           return CREATOR_ID;
        }
        else 
        if (hashCode.equals("description"))
        {
           return DESCRIPTION;
        }
        else 
        if (hashCode.equals("durationType"))
        {
           return DURATION_TYPE;
        }
        else 
        if (hashCode.equals("flavorParamsIds"))
        {
           return FLAVOR_PARAMS_IDS;
        }
        else 
        if (hashCode.equals("groupId"))
        {
           return GROUP_ID;
        }
        else 
        if (hashCode.equals("id"))
        {
           return ID;
        }
        else 
        if (hashCode.equals("name"))
        {
           return NAME;
        }
        else 
        if (hashCode.equals("referenceId"))
        {
           return REFERENCE_ID;
        }
        else 
        if (hashCode.equals("replacedEntryId"))
        {
           return REPLACED_ENTRY_ID;
        }
        else 
        if (hashCode.equals("replacingEntryId"))
        {
           return REPLACING_ENTRY_ID;
        }
        else 
        if (hashCode.equals("searchText"))
        {
           return SEARCH_TEXT;
        }
        else 
        if (hashCode.equals("tags"))
        {
           return TAGS;
        }
        else 
        if (hashCode.equals("userId"))
        {
           return USER_ID;
        }
        else 
        {
           return ADMIN_TAGS;
        }
    }
}
