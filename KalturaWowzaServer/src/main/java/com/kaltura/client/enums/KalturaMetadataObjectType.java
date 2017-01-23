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
public enum KalturaMetadataObjectType implements KalturaEnumAsString {
    AD_CUE_POINT ("adCuePointMetadata.AdCuePoint"),
    ANNOTATION ("annotationMetadata.Annotation"),
    CODE_CUE_POINT ("codeCuePointMetadata.CodeCuePoint"),
    THUMB_CUE_POINT ("thumbCuePointMetadata.thumbCuePoint"),
    ENTRY ("1"),
    CATEGORY ("2"),
    USER ("3"),
    PARTNER ("4"),
    DYNAMIC_OBJECT ("5");

    public String hashCode;

    KalturaMetadataObjectType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaMetadataObjectType get(String hashCode) {
        if (hashCode.equals("adCuePointMetadata.AdCuePoint"))
        {
           return AD_CUE_POINT;
        }
        else 
        if (hashCode.equals("annotationMetadata.Annotation"))
        {
           return ANNOTATION;
        }
        else 
        if (hashCode.equals("codeCuePointMetadata.CodeCuePoint"))
        {
           return CODE_CUE_POINT;
        }
        else 
        if (hashCode.equals("thumbCuePointMetadata.thumbCuePoint"))
        {
           return THUMB_CUE_POINT;
        }
        else 
        if (hashCode.equals("1"))
        {
           return ENTRY;
        }
        else 
        if (hashCode.equals("2"))
        {
           return CATEGORY;
        }
        else 
        if (hashCode.equals("3"))
        {
           return USER;
        }
        else 
        if (hashCode.equals("4"))
        {
           return PARTNER;
        }
        else 
        if (hashCode.equals("5"))
        {
           return DYNAMIC_OBJECT;
        }
        else 
        {
           return AD_CUE_POINT;
        }
    }
}
