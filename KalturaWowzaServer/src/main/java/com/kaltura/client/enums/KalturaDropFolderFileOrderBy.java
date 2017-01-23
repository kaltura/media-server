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
public enum KalturaDropFolderFileOrderBy implements KalturaEnumAsString {
    CREATED_AT_ASC ("+createdAt"),
    FILE_NAME_ASC ("+fileName"),
    FILE_SIZE_ASC ("+fileSize"),
    FILE_SIZE_LAST_SET_AT_ASC ("+fileSizeLastSetAt"),
    ID_ASC ("+id"),
    PARSED_FLAVOR_ASC ("+parsedFlavor"),
    PARSED_SLUG_ASC ("+parsedSlug"),
    UPDATED_AT_ASC ("+updatedAt"),
    CREATED_AT_DESC ("-createdAt"),
    FILE_NAME_DESC ("-fileName"),
    FILE_SIZE_DESC ("-fileSize"),
    FILE_SIZE_LAST_SET_AT_DESC ("-fileSizeLastSetAt"),
    ID_DESC ("-id"),
    PARSED_FLAVOR_DESC ("-parsedFlavor"),
    PARSED_SLUG_DESC ("-parsedSlug"),
    UPDATED_AT_DESC ("-updatedAt");

    public String hashCode;

    KalturaDropFolderFileOrderBy(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaDropFolderFileOrderBy get(String hashCode) {
        if (hashCode.equals("+createdAt"))
        {
           return CREATED_AT_ASC;
        }
        else 
        if (hashCode.equals("+fileName"))
        {
           return FILE_NAME_ASC;
        }
        else 
        if (hashCode.equals("+fileSize"))
        {
           return FILE_SIZE_ASC;
        }
        else 
        if (hashCode.equals("+fileSizeLastSetAt"))
        {
           return FILE_SIZE_LAST_SET_AT_ASC;
        }
        else 
        if (hashCode.equals("+id"))
        {
           return ID_ASC;
        }
        else 
        if (hashCode.equals("+parsedFlavor"))
        {
           return PARSED_FLAVOR_ASC;
        }
        else 
        if (hashCode.equals("+parsedSlug"))
        {
           return PARSED_SLUG_ASC;
        }
        else 
        if (hashCode.equals("+updatedAt"))
        {
           return UPDATED_AT_ASC;
        }
        else 
        if (hashCode.equals("-createdAt"))
        {
           return CREATED_AT_DESC;
        }
        else 
        if (hashCode.equals("-fileName"))
        {
           return FILE_NAME_DESC;
        }
        else 
        if (hashCode.equals("-fileSize"))
        {
           return FILE_SIZE_DESC;
        }
        else 
        if (hashCode.equals("-fileSizeLastSetAt"))
        {
           return FILE_SIZE_LAST_SET_AT_DESC;
        }
        else 
        if (hashCode.equals("-id"))
        {
           return ID_DESC;
        }
        else 
        if (hashCode.equals("-parsedFlavor"))
        {
           return PARSED_FLAVOR_DESC;
        }
        else 
        if (hashCode.equals("-parsedSlug"))
        {
           return PARSED_SLUG_DESC;
        }
        else 
        if (hashCode.equals("-updatedAt"))
        {
           return UPDATED_AT_DESC;
        }
        else 
        {
           return CREATED_AT_ASC;
        }
    }
}
