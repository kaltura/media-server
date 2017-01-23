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
public enum KalturaSyndicationFeedType implements KalturaEnumAsInt {
    GOOGLE_VIDEO (1),
    YAHOO (2),
    ITUNES (3),
    TUBE_MOGUL (4),
    KALTURA (5),
    KALTURA_XSLT (6);

    public int hashCode;

    KalturaSyndicationFeedType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaSyndicationFeedType get(int hashCode) {
        switch(hashCode) {
            case 1: return GOOGLE_VIDEO;
            case 2: return YAHOO;
            case 3: return ITUNES;
            case 4: return TUBE_MOGUL;
            case 5: return KALTURA;
            case 6: return KALTURA_XSLT;
            default: return GOOGLE_VIDEO;
        }
    }
}
