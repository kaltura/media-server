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
public enum KalturaLiveReportExportType implements KalturaEnumAsInt {
    PARTNER_TOTAL_ALL (1),
    PARTNER_TOTAL_LIVE (2),
    ENTRY_TIME_LINE_ALL (11),
    ENTRY_TIME_LINE_LIVE (12),
    LOCATION_ALL (21),
    LOCATION_LIVE (22),
    SYNDICATION_ALL (31),
    SYNDICATION_LIVE (32);

    public int hashCode;

    KalturaLiveReportExportType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaLiveReportExportType get(int hashCode) {
        switch(hashCode) {
            case 1: return PARTNER_TOTAL_ALL;
            case 2: return PARTNER_TOTAL_LIVE;
            case 11: return ENTRY_TIME_LINE_ALL;
            case 12: return ENTRY_TIME_LINE_LIVE;
            case 21: return LOCATION_ALL;
            case 22: return LOCATION_LIVE;
            case 31: return SYNDICATION_ALL;
            case 32: return SYNDICATION_LIVE;
            default: return PARTNER_TOTAL_ALL;
        }
    }
}
