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
public enum KalturaConditionType implements KalturaEnumAsString {
    ABC_WATERMARK ("abcScreenersWatermarkAccessControl.abcWatermark"),
    EVENT_NOTIFICATION_FIELD ("eventNotification.BooleanField"),
    EVENT_NOTIFICATION_OBJECT_CHANGED ("eventNotification.ObjectChanged"),
    METADATA_FIELD_CHANGED ("metadata.FieldChanged"),
    METADATA_FIELD_COMPARE ("metadata.FieldCompare"),
    METADATA_FIELD_MATCH ("metadata.FieldMatch"),
    AUTHENTICATED ("1"),
    COUNTRY ("2"),
    IP_ADDRESS ("3"),
    SITE ("4"),
    USER_AGENT ("5"),
    FIELD_MATCH ("6"),
    FIELD_COMPARE ("7"),
    ASSET_PROPERTIES_COMPARE ("8"),
    USER_ROLE ("9"),
    GEO_DISTANCE ("10"),
    OR_OPERATOR ("11"),
    HASH ("12"),
    DELIVERY_PROFILE ("13"),
    ACTIVE_EDGE_VALIDATE ("14");

    public String hashCode;

    KalturaConditionType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaConditionType get(String hashCode) {
        if (hashCode.equals("abcScreenersWatermarkAccessControl.abcWatermark"))
        {
           return ABC_WATERMARK;
        }
        else 
        if (hashCode.equals("eventNotification.BooleanField"))
        {
           return EVENT_NOTIFICATION_FIELD;
        }
        else 
        if (hashCode.equals("eventNotification.ObjectChanged"))
        {
           return EVENT_NOTIFICATION_OBJECT_CHANGED;
        }
        else 
        if (hashCode.equals("metadata.FieldChanged"))
        {
           return METADATA_FIELD_CHANGED;
        }
        else 
        if (hashCode.equals("metadata.FieldCompare"))
        {
           return METADATA_FIELD_COMPARE;
        }
        else 
        if (hashCode.equals("metadata.FieldMatch"))
        {
           return METADATA_FIELD_MATCH;
        }
        else 
        if (hashCode.equals("1"))
        {
           return AUTHENTICATED;
        }
        else 
        if (hashCode.equals("2"))
        {
           return COUNTRY;
        }
        else 
        if (hashCode.equals("3"))
        {
           return IP_ADDRESS;
        }
        else 
        if (hashCode.equals("4"))
        {
           return SITE;
        }
        else 
        if (hashCode.equals("5"))
        {
           return USER_AGENT;
        }
        else 
        if (hashCode.equals("6"))
        {
           return FIELD_MATCH;
        }
        else 
        if (hashCode.equals("7"))
        {
           return FIELD_COMPARE;
        }
        else 
        if (hashCode.equals("8"))
        {
           return ASSET_PROPERTIES_COMPARE;
        }
        else 
        if (hashCode.equals("9"))
        {
           return USER_ROLE;
        }
        else 
        if (hashCode.equals("10"))
        {
           return GEO_DISTANCE;
        }
        else 
        if (hashCode.equals("11"))
        {
           return OR_OPERATOR;
        }
        else 
        if (hashCode.equals("12"))
        {
           return HASH;
        }
        else 
        if (hashCode.equals("13"))
        {
           return DELIVERY_PROFILE;
        }
        else 
        if (hashCode.equals("14"))
        {
           return ACTIVE_EDGE_VALIDATE;
        }
        else 
        {
           return ABC_WATERMARK;
        }
    }
}
