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
public enum KalturaFlavorAssetStatus implements KalturaEnumAsInt {
    ERROR (-1),
    QUEUED (0),
    CONVERTING (1),
    READY (2),
    DELETED (3),
    NOT_APPLICABLE (4),
    TEMP (5),
    WAIT_FOR_CONVERT (6),
    IMPORTING (7),
    VALIDATING (8),
    EXPORTING (9);

    public int hashCode;

    KalturaFlavorAssetStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaFlavorAssetStatus get(int hashCode) {
        switch(hashCode) {
            case -1: return ERROR;
            case 0: return QUEUED;
            case 1: return CONVERTING;
            case 2: return READY;
            case 3: return DELETED;
            case 4: return NOT_APPLICABLE;
            case 5: return TEMP;
            case 6: return WAIT_FOR_CONVERT;
            case 7: return IMPORTING;
            case 8: return VALIDATING;
            case 9: return EXPORTING;
            default: return ERROR;
        }
    }
}
