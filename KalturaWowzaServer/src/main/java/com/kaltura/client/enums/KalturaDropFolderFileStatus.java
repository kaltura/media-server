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
public enum KalturaDropFolderFileStatus implements KalturaEnumAsInt {
    UPLOADING (1),
    PENDING (2),
    WAITING (3),
    HANDLED (4),
    IGNORE (5),
    DELETED (6),
    PURGED (7),
    NO_MATCH (8),
    ERROR_HANDLING (9),
    ERROR_DELETING (10),
    DOWNLOADING (11),
    ERROR_DOWNLOADING (12),
    PROCESSING (13),
    PARSED (14),
    DETECTED (15);

    public int hashCode;

    KalturaDropFolderFileStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaDropFolderFileStatus get(int hashCode) {
        switch(hashCode) {
            case 1: return UPLOADING;
            case 2: return PENDING;
            case 3: return WAITING;
            case 4: return HANDLED;
            case 5: return IGNORE;
            case 6: return DELETED;
            case 7: return PURGED;
            case 8: return NO_MATCH;
            case 9: return ERROR_HANDLING;
            case 10: return ERROR_DELETING;
            case 11: return DOWNLOADING;
            case 12: return ERROR_DOWNLOADING;
            case 13: return PROCESSING;
            case 14: return PARSED;
            case 15: return DETECTED;
            default: return UPLOADING;
        }
    }
}
