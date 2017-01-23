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
public enum KalturaNotificationType implements KalturaEnumAsInt {
    ENTRY_ADD (1),
    ENTR_UPDATE_PERMISSIONS (2),
    ENTRY_DELETE (3),
    ENTRY_BLOCK (4),
    ENTRY_UPDATE (5),
    ENTRY_UPDATE_THUMBNAIL (6),
    ENTRY_UPDATE_MODERATION (7),
    USER_ADD (21),
    USER_BANNED (26);

    public int hashCode;

    KalturaNotificationType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaNotificationType get(int hashCode) {
        switch(hashCode) {
            case 1: return ENTRY_ADD;
            case 2: return ENTR_UPDATE_PERMISSIONS;
            case 3: return ENTRY_DELETE;
            case 4: return ENTRY_BLOCK;
            case 5: return ENTRY_UPDATE;
            case 6: return ENTRY_UPDATE_THUMBNAIL;
            case 7: return ENTRY_UPDATE_MODERATION;
            case 21: return USER_ADD;
            case 26: return USER_BANNED;
            default: return ENTRY_ADD;
        }
    }
}
