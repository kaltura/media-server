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
public enum KalturaEntryStatus implements KalturaEnumAsString {
    ERROR_IMPORTING ("-2"),
    ERROR_CONVERTING ("-1"),
    SCAN_FAILURE ("virusScan.ScanFailure"),
    IMPORT ("0"),
    INFECTED ("virusScan.Infected"),
    PRECONVERT ("1"),
    READY ("2"),
    DELETED ("3"),
    PENDING ("4"),
    MODERATE ("5"),
    BLOCKED ("6"),
    NO_CONTENT ("7");

    public String hashCode;

    KalturaEntryStatus(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaEntryStatus get(String hashCode) {
        if (hashCode.equals("-2"))
        {
           return ERROR_IMPORTING;
        }
        else 
        if (hashCode.equals("-1"))
        {
           return ERROR_CONVERTING;
        }
        else 
        if (hashCode.equals("virusScan.ScanFailure"))
        {
           return SCAN_FAILURE;
        }
        else 
        if (hashCode.equals("0"))
        {
           return IMPORT;
        }
        else 
        if (hashCode.equals("virusScan.Infected"))
        {
           return INFECTED;
        }
        else 
        if (hashCode.equals("1"))
        {
           return PRECONVERT;
        }
        else 
        if (hashCode.equals("2"))
        {
           return READY;
        }
        else 
        if (hashCode.equals("3"))
        {
           return DELETED;
        }
        else 
        if (hashCode.equals("4"))
        {
           return PENDING;
        }
        else 
        if (hashCode.equals("5"))
        {
           return MODERATE;
        }
        else 
        if (hashCode.equals("6"))
        {
           return BLOCKED;
        }
        else 
        if (hashCode.equals("7"))
        {
           return NO_CONTENT;
        }
        else 
        {
           return ERROR_IMPORTING;
        }
    }
}
