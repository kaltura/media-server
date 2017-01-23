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
public enum KalturaObjectTaskType implements KalturaEnumAsString {
    DISTRIBUTE ("scheduledTaskContentDistribution.Distribute"),
    DISPATCH_EVENT_NOTIFICATION ("scheduledTaskEventNotification.DispatchEventNotification"),
    EXECUTE_METADATA_XSLT ("scheduledTaskMetadata.ExecuteMetadataXslt"),
    DELETE_ENTRY ("1"),
    MODIFY_CATEGORIES ("2"),
    DELETE_ENTRY_FLAVORS ("3"),
    CONVERT_ENTRY_FLAVORS ("4"),
    DELETE_LOCAL_CONTENT ("5"),
    STORAGE_EXPORT ("6"),
    MODIFY_ENTRY ("7");

    public String hashCode;

    KalturaObjectTaskType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaObjectTaskType get(String hashCode) {
        if (hashCode.equals("scheduledTaskContentDistribution.Distribute"))
        {
           return DISTRIBUTE;
        }
        else 
        if (hashCode.equals("scheduledTaskEventNotification.DispatchEventNotification"))
        {
           return DISPATCH_EVENT_NOTIFICATION;
        }
        else 
        if (hashCode.equals("scheduledTaskMetadata.ExecuteMetadataXslt"))
        {
           return EXECUTE_METADATA_XSLT;
        }
        else 
        if (hashCode.equals("1"))
        {
           return DELETE_ENTRY;
        }
        else 
        if (hashCode.equals("2"))
        {
           return MODIFY_CATEGORIES;
        }
        else 
        if (hashCode.equals("3"))
        {
           return DELETE_ENTRY_FLAVORS;
        }
        else 
        if (hashCode.equals("4"))
        {
           return CONVERT_ENTRY_FLAVORS;
        }
        else 
        if (hashCode.equals("5"))
        {
           return DELETE_LOCAL_CONTENT;
        }
        else 
        if (hashCode.equals("6"))
        {
           return STORAGE_EXPORT;
        }
        else 
        if (hashCode.equals("7"))
        {
           return MODIFY_ENTRY;
        }
        else 
        {
           return DISTRIBUTE;
        }
    }
}
