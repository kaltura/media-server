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
public enum KalturaEntryType implements KalturaEnumAsString {
    AUTOMATIC ("-1"),
    EXTERNAL_MEDIA ("externalMedia.externalMedia"),
    MEDIA_CLIP ("1"),
    MIX ("2"),
    PLAYLIST ("5"),
    DATA ("6"),
    LIVE_STREAM ("7"),
    LIVE_CHANNEL ("8"),
    DOCUMENT ("10");

    public String hashCode;

    KalturaEntryType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaEntryType get(String hashCode) {
        if (hashCode.equals("-1"))
        {
           return AUTOMATIC;
        }
        else 
        if (hashCode.equals("externalMedia.externalMedia"))
        {
           return EXTERNAL_MEDIA;
        }
        else 
        if (hashCode.equals("1"))
        {
           return MEDIA_CLIP;
        }
        else 
        if (hashCode.equals("2"))
        {
           return MIX;
        }
        else 
        if (hashCode.equals("5"))
        {
           return PLAYLIST;
        }
        else 
        if (hashCode.equals("6"))
        {
           return DATA;
        }
        else 
        if (hashCode.equals("7"))
        {
           return LIVE_STREAM;
        }
        else 
        if (hashCode.equals("8"))
        {
           return LIVE_CHANNEL;
        }
        else 
        if (hashCode.equals("10"))
        {
           return DOCUMENT;
        }
        else 
        {
           return AUTOMATIC;
        }
    }
}
