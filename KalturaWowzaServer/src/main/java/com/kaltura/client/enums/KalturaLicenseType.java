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
public enum KalturaLicenseType implements KalturaEnumAsInt {
    UNKNOWN (-1),
    NONE (0),
    COPYRIGHTED (1),
    PUBLIC_DOMAIN (2),
    CREATIVECOMMONS_ATTRIBUTION (3),
    CREATIVECOMMONS_ATTRIBUTION_SHARE_ALIKE (4),
    CREATIVECOMMONS_ATTRIBUTION_NO_DERIVATIVES (5),
    CREATIVECOMMONS_ATTRIBUTION_NON_COMMERCIAL (6),
    CREATIVECOMMONS_ATTRIBUTION_NON_COMMERCIAL_SHARE_ALIKE (7),
    CREATIVECOMMONS_ATTRIBUTION_NON_COMMERCIAL_NO_DERIVATIVES (8),
    GFDL (9),
    GPL (10),
    AFFERO_GPL (11),
    LGPL (12),
    BSD (13),
    APACHE (14),
    MOZILLA (15);

    public int hashCode;

    KalturaLicenseType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaLicenseType get(int hashCode) {
        switch(hashCode) {
            case -1: return UNKNOWN;
            case 0: return NONE;
            case 1: return COPYRIGHTED;
            case 2: return PUBLIC_DOMAIN;
            case 3: return CREATIVECOMMONS_ATTRIBUTION;
            case 4: return CREATIVECOMMONS_ATTRIBUTION_SHARE_ALIKE;
            case 5: return CREATIVECOMMONS_ATTRIBUTION_NO_DERIVATIVES;
            case 6: return CREATIVECOMMONS_ATTRIBUTION_NON_COMMERCIAL;
            case 7: return CREATIVECOMMONS_ATTRIBUTION_NON_COMMERCIAL_SHARE_ALIKE;
            case 8: return CREATIVECOMMONS_ATTRIBUTION_NON_COMMERCIAL_NO_DERIVATIVES;
            case 9: return GFDL;
            case 10: return GPL;
            case 11: return AFFERO_GPL;
            case 12: return LGPL;
            case 13: return BSD;
            case 14: return APACHE;
            case 15: return MOZILLA;
            default: return UNKNOWN;
        }
    }
}
