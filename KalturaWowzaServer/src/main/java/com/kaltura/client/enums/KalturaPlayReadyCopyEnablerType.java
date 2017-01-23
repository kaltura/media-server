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
public enum KalturaPlayReadyCopyEnablerType implements KalturaEnumAsString {
    CSS ("3CAF2814-A7AB-467C-B4DF-54ACC56C66DC"),
    PRINTER ("3CF2E054-F4D5-46cd-85A6-FCD152AD5FBE"),
    DEVICE ("6848955D-516B-4EB0-90E8-8F6D5A77B85F"),
    CLIPBOARD ("6E76C588-C3A9-47ea-A875-546D5209FF38"),
    SDC ("79F78A0D-0B69-401e-8A90-8BEF30BCE192"),
    SDC_PREVIEW ("81BD9AD4-A720-4ea1-B510-5D4E6FFB6A4D"),
    AACS ("C3CF56E0-7FF2-4491-809F-53E21D3ABF07"),
    HELIX ("CCB0B4E3-8B46-409e-A998-82556E3F5AF4"),
    CPRM ("CDD801AD-A577-48DB-950E-46D5F1592FAE"),
    PC ("CE480EDE-516B-40B3-90E1-D6CFC47630C5"),
    SDC_LIMITED ("E6785609-64CC-4bfa-B82D-6B619733B746"),
    ORANGE_BOOK_CD ("EC930B7D-1F2D-4682-A38B-8AB977721D0D");

    public String hashCode;

    KalturaPlayReadyCopyEnablerType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaPlayReadyCopyEnablerType get(String hashCode) {
        if (hashCode.equals("3CAF2814-A7AB-467C-B4DF-54ACC56C66DC"))
        {
           return CSS;
        }
        else 
        if (hashCode.equals("3CF2E054-F4D5-46cd-85A6-FCD152AD5FBE"))
        {
           return PRINTER;
        }
        else 
        if (hashCode.equals("6848955D-516B-4EB0-90E8-8F6D5A77B85F"))
        {
           return DEVICE;
        }
        else 
        if (hashCode.equals("6E76C588-C3A9-47ea-A875-546D5209FF38"))
        {
           return CLIPBOARD;
        }
        else 
        if (hashCode.equals("79F78A0D-0B69-401e-8A90-8BEF30BCE192"))
        {
           return SDC;
        }
        else 
        if (hashCode.equals("81BD9AD4-A720-4ea1-B510-5D4E6FFB6A4D"))
        {
           return SDC_PREVIEW;
        }
        else 
        if (hashCode.equals("C3CF56E0-7FF2-4491-809F-53E21D3ABF07"))
        {
           return AACS;
        }
        else 
        if (hashCode.equals("CCB0B4E3-8B46-409e-A998-82556E3F5AF4"))
        {
           return HELIX;
        }
        else 
        if (hashCode.equals("CDD801AD-A577-48DB-950E-46D5F1592FAE"))
        {
           return CPRM;
        }
        else 
        if (hashCode.equals("CE480EDE-516B-40B3-90E1-D6CFC47630C5"))
        {
           return PC;
        }
        else 
        if (hashCode.equals("E6785609-64CC-4bfa-B82D-6B619733B746"))
        {
           return SDC_LIMITED;
        }
        else 
        if (hashCode.equals("EC930B7D-1F2D-4682-A38B-8AB977721D0D"))
        {
           return ORANGE_BOOK_CD;
        }
        else 
        {
           return CSS;
        }
    }
}
