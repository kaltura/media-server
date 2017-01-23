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
public enum KalturaSearchProviderType implements KalturaEnumAsInt {
    FLICKR (3),
    YOUTUBE (4),
    MYSPACE (7),
    PHOTOBUCKET (8),
    JAMENDO (9),
    CCMIXTER (10),
    NYPL (11),
    CURRENT (12),
    MEDIA_COMMONS (13),
    KALTURA (20),
    KALTURA_USER_CLIPS (21),
    ARCHIVE_ORG (22),
    KALTURA_PARTNER (23),
    METACAFE (24),
    SEARCH_PROXY (28),
    PARTNER_SPECIFIC (100);

    public int hashCode;

    KalturaSearchProviderType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaSearchProviderType get(int hashCode) {
        switch(hashCode) {
            case 3: return FLICKR;
            case 4: return YOUTUBE;
            case 7: return MYSPACE;
            case 8: return PHOTOBUCKET;
            case 9: return JAMENDO;
            case 10: return CCMIXTER;
            case 11: return NYPL;
            case 12: return CURRENT;
            case 13: return MEDIA_COMMONS;
            case 20: return KALTURA;
            case 21: return KALTURA_USER_CLIPS;
            case 22: return ARCHIVE_ORG;
            case 23: return KALTURA_PARTNER;
            case 24: return METACAFE;
            case 28: return SEARCH_PROXY;
            case 100: return PARTNER_SPECIFIC;
            default: return FLICKR;
        }
    }
}
