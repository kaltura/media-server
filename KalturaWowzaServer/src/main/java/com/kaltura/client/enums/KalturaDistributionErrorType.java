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
public enum KalturaDistributionErrorType implements KalturaEnumAsInt {
    MISSING_FLAVOR (1),
    MISSING_THUMBNAIL (2),
    MISSING_METADATA (3),
    INVALID_DATA (4),
    MISSING_ASSET (5),
    CONDITION_NOT_MET (6);

    public int hashCode;

    KalturaDistributionErrorType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaDistributionErrorType get(int hashCode) {
        switch(hashCode) {
            case 1: return MISSING_FLAVOR;
            case 2: return MISSING_THUMBNAIL;
            case 3: return MISSING_METADATA;
            case 4: return INVALID_DATA;
            case 5: return MISSING_ASSET;
            case 6: return CONDITION_NOT_MET;
            default: return MISSING_FLAVOR;
        }
    }
}
