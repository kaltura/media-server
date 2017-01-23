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
public enum KalturaSourceType implements KalturaEnumAsString {
    LIMELIGHT_LIVE ("limeLight.LIVE_STREAM"),
    VELOCIX_LIVE ("velocix.VELOCIX_LIVE"),
    FILE ("1"),
    WEBCAM ("2"),
    URL ("5"),
    SEARCH_PROVIDER ("6"),
    AKAMAI_LIVE ("29"),
    MANUAL_LIVE_STREAM ("30"),
    AKAMAI_UNIVERSAL_LIVE ("31"),
    LIVE_STREAM ("32"),
    LIVE_CHANNEL ("33"),
    RECORDED_LIVE ("34"),
    CLIP ("35"),
    KALTURA_RECORDED_LIVE ("36"),
    LIVE_STREAM_ONTEXTDATA_CAPTIONS ("42");

    public String hashCode;

    KalturaSourceType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaSourceType get(String hashCode) {
        if (hashCode.equals("limeLight.LIVE_STREAM"))
        {
           return LIMELIGHT_LIVE;
        }
        else 
        if (hashCode.equals("velocix.VELOCIX_LIVE"))
        {
           return VELOCIX_LIVE;
        }
        else 
        if (hashCode.equals("1"))
        {
           return FILE;
        }
        else 
        if (hashCode.equals("2"))
        {
           return WEBCAM;
        }
        else 
        if (hashCode.equals("5"))
        {
           return URL;
        }
        else 
        if (hashCode.equals("6"))
        {
           return SEARCH_PROVIDER;
        }
        else 
        if (hashCode.equals("29"))
        {
           return AKAMAI_LIVE;
        }
        else 
        if (hashCode.equals("30"))
        {
           return MANUAL_LIVE_STREAM;
        }
        else 
        if (hashCode.equals("31"))
        {
           return AKAMAI_UNIVERSAL_LIVE;
        }
        else 
        if (hashCode.equals("32"))
        {
           return LIVE_STREAM;
        }
        else 
        if (hashCode.equals("33"))
        {
           return LIVE_CHANNEL;
        }
        else 
        if (hashCode.equals("34"))
        {
           return RECORDED_LIVE;
        }
        else 
        if (hashCode.equals("35"))
        {
           return CLIP;
        }
        else 
        if (hashCode.equals("36"))
        {
           return KALTURA_RECORDED_LIVE;
        }
        else 
        if (hashCode.equals("42"))
        {
           return LIVE_STREAM_ONTEXTDATA_CAPTIONS;
        }
        else 
        {
           return LIMELIGHT_LIVE;
        }
    }
}
