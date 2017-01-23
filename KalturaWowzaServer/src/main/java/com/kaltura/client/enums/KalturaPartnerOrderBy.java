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
public enum KalturaPartnerOrderBy implements KalturaEnumAsString {
    ADMIN_EMAIL_ASC ("+adminEmail"),
    ADMIN_NAME_ASC ("+adminName"),
    CREATED_AT_ASC ("+createdAt"),
    ID_ASC ("+id"),
    NAME_ASC ("+name"),
    STATUS_ASC ("+status"),
    WEBSITE_ASC ("+website"),
    ADMIN_EMAIL_DESC ("-adminEmail"),
    ADMIN_NAME_DESC ("-adminName"),
    CREATED_AT_DESC ("-createdAt"),
    ID_DESC ("-id"),
    NAME_DESC ("-name"),
    STATUS_DESC ("-status"),
    WEBSITE_DESC ("-website");

    public String hashCode;

    KalturaPartnerOrderBy(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaPartnerOrderBy get(String hashCode) {
        if (hashCode.equals("+adminEmail"))
        {
           return ADMIN_EMAIL_ASC;
        }
        else 
        if (hashCode.equals("+adminName"))
        {
           return ADMIN_NAME_ASC;
        }
        else 
        if (hashCode.equals("+createdAt"))
        {
           return CREATED_AT_ASC;
        }
        else 
        if (hashCode.equals("+id"))
        {
           return ID_ASC;
        }
        else 
        if (hashCode.equals("+name"))
        {
           return NAME_ASC;
        }
        else 
        if (hashCode.equals("+status"))
        {
           return STATUS_ASC;
        }
        else 
        if (hashCode.equals("+website"))
        {
           return WEBSITE_ASC;
        }
        else 
        if (hashCode.equals("-adminEmail"))
        {
           return ADMIN_EMAIL_DESC;
        }
        else 
        if (hashCode.equals("-adminName"))
        {
           return ADMIN_NAME_DESC;
        }
        else 
        if (hashCode.equals("-createdAt"))
        {
           return CREATED_AT_DESC;
        }
        else 
        if (hashCode.equals("-id"))
        {
           return ID_DESC;
        }
        else 
        if (hashCode.equals("-name"))
        {
           return NAME_DESC;
        }
        else 
        if (hashCode.equals("-status"))
        {
           return STATUS_DESC;
        }
        else 
        if (hashCode.equals("-website"))
        {
           return WEBSITE_DESC;
        }
        else 
        {
           return ADMIN_EMAIL_ASC;
        }
    }
}
