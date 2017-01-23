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
public enum KalturaEventNotificationEventType implements KalturaEnumAsString {
    INTEGRATION_JOB_CLOSED ("integrationEventNotifications.INTEGRATION_JOB_CLOSED"),
    BATCH_JOB_STATUS ("1"),
    OBJECT_ADDED ("2"),
    OBJECT_CHANGED ("3"),
    OBJECT_COPIED ("4"),
    OBJECT_CREATED ("5"),
    OBJECT_DATA_CHANGED ("6"),
    OBJECT_DELETED ("7"),
    OBJECT_ERASED ("8"),
    OBJECT_READY_FOR_REPLACMENT ("9"),
    OBJECT_SAVED ("10"),
    OBJECT_UPDATED ("11"),
    OBJECT_REPLACED ("12"),
    OBJECT_READY_FOR_INDEX ("13");

    public String hashCode;

    KalturaEventNotificationEventType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaEventNotificationEventType get(String hashCode) {
        if (hashCode.equals("integrationEventNotifications.INTEGRATION_JOB_CLOSED"))
        {
           return INTEGRATION_JOB_CLOSED;
        }
        else 
        if (hashCode.equals("1"))
        {
           return BATCH_JOB_STATUS;
        }
        else 
        if (hashCode.equals("2"))
        {
           return OBJECT_ADDED;
        }
        else 
        if (hashCode.equals("3"))
        {
           return OBJECT_CHANGED;
        }
        else 
        if (hashCode.equals("4"))
        {
           return OBJECT_COPIED;
        }
        else 
        if (hashCode.equals("5"))
        {
           return OBJECT_CREATED;
        }
        else 
        if (hashCode.equals("6"))
        {
           return OBJECT_DATA_CHANGED;
        }
        else 
        if (hashCode.equals("7"))
        {
           return OBJECT_DELETED;
        }
        else 
        if (hashCode.equals("8"))
        {
           return OBJECT_ERASED;
        }
        else 
        if (hashCode.equals("9"))
        {
           return OBJECT_READY_FOR_REPLACMENT;
        }
        else 
        if (hashCode.equals("10"))
        {
           return OBJECT_SAVED;
        }
        else 
        if (hashCode.equals("11"))
        {
           return OBJECT_UPDATED;
        }
        else 
        if (hashCode.equals("12"))
        {
           return OBJECT_REPLACED;
        }
        else 
        if (hashCode.equals("13"))
        {
           return OBJECT_READY_FOR_INDEX;
        }
        else 
        {
           return INTEGRATION_JOB_CLOSED;
        }
    }
}
