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
public enum KalturaAmazonS3StorageProfileFilesPermissionLevel implements KalturaEnumAsString {
    ACL_AUTHENTICATED_READ ("authenticated-read"),
    ACL_PRIVATE ("private"),
    ACL_PUBLIC_READ ("public-read"),
    ACL_PUBLIC_READ_WRITE ("public-read-write");

    public String hashCode;

    KalturaAmazonS3StorageProfileFilesPermissionLevel(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaAmazonS3StorageProfileFilesPermissionLevel get(String hashCode) {
        if (hashCode.equals("authenticated-read"))
        {
           return ACL_AUTHENTICATED_READ;
        }
        else 
        if (hashCode.equals("private"))
        {
           return ACL_PRIVATE;
        }
        else 
        if (hashCode.equals("public-read"))
        {
           return ACL_PUBLIC_READ;
        }
        else 
        if (hashCode.equals("public-read-write"))
        {
           return ACL_PUBLIC_READ_WRITE;
        }
        else 
        {
           return ACL_AUTHENTICATED_READ;
        }
    }
}
