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
public enum KalturaModerationFlagType implements KalturaEnumAsInt {
    SEXUAL_CONTENT (1),
    VIOLENT_REPULSIVE (2),
    HARMFUL_DANGEROUS (3),
    SPAM_COMMERCIALS (4),
    COPYRIGHT (5),
    TERMS_OF_USE_VIOLATION (6);

    public int hashCode;

    KalturaModerationFlagType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaModerationFlagType get(int hashCode) {
        switch(hashCode) {
            case 1: return SEXUAL_CONTENT;
            case 2: return VIOLENT_REPULSIVE;
            case 3: return HARMFUL_DANGEROUS;
            case 4: return SPAM_COMMERCIALS;
            case 5: return COPYRIGHT;
            case 6: return TERMS_OF_USE_VIOLATION;
            default: return SEXUAL_CONTENT;
        }
    }
}
