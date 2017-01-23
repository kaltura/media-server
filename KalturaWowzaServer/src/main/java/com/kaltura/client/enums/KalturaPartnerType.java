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
public enum KalturaPartnerType implements KalturaEnumAsInt {
    KMC (1),
    WIKI (100),
    WORDPRESS (101),
    DRUPAL (102),
    DEKIWIKI (103),
    MOODLE (104),
    COMMUNITY_EDITION (105),
    JOOMLA (106),
    BLACKBOARD (107),
    SAKAI (108),
    ADMIN_CONSOLE (109);

    public int hashCode;

    KalturaPartnerType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaPartnerType get(int hashCode) {
        switch(hashCode) {
            case 1: return KMC;
            case 100: return WIKI;
            case 101: return WORDPRESS;
            case 102: return DRUPAL;
            case 103: return DEKIWIKI;
            case 104: return MOODLE;
            case 105: return COMMUNITY_EDITION;
            case 106: return JOOMLA;
            case 107: return BLACKBOARD;
            case 108: return SAKAI;
            case 109: return ADMIN_CONSOLE;
            default: return KMC;
        }
    }
}
