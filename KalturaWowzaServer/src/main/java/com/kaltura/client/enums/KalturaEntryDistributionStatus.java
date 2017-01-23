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
public enum KalturaEntryDistributionStatus implements KalturaEnumAsInt {
    PENDING (0),
    QUEUED (1),
    READY (2),
    DELETED (3),
    SUBMITTING (4),
    UPDATING (5),
    DELETING (6),
    ERROR_SUBMITTING (7),
    ERROR_UPDATING (8),
    ERROR_DELETING (9),
    REMOVED (10),
    IMPORT_SUBMITTING (11),
    IMPORT_UPDATING (12);

    public int hashCode;

    KalturaEntryDistributionStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaEntryDistributionStatus get(int hashCode) {
        switch(hashCode) {
            case 0: return PENDING;
            case 1: return QUEUED;
            case 2: return READY;
            case 3: return DELETED;
            case 4: return SUBMITTING;
            case 5: return UPDATING;
            case 6: return DELETING;
            case 7: return ERROR_SUBMITTING;
            case 8: return ERROR_UPDATING;
            case 9: return ERROR_DELETING;
            case 10: return REMOVED;
            case 11: return IMPORT_SUBMITTING;
            case 12: return IMPORT_UPDATING;
            default: return PENDING;
        }
    }
}
