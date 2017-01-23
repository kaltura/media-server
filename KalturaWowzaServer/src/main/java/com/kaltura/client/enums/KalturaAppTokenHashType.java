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
public enum KalturaAppTokenHashType implements KalturaEnumAsString {
    MD5 ("MD5"),
    SHA1 ("SHA1"),
    SHA256 ("SHA256"),
    SHA512 ("SHA512");

    public String hashCode;

    KalturaAppTokenHashType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaAppTokenHashType get(String hashCode) {
        if (hashCode.equals("MD5"))
        {
           return MD5;
        }
        else 
        if (hashCode.equals("SHA1"))
        {
           return SHA1;
        }
        else 
        if (hashCode.equals("SHA256"))
        {
           return SHA256;
        }
        else 
        if (hashCode.equals("SHA512"))
        {
           return SHA512;
        }
        else 
        {
           return MD5;
        }
    }
}
