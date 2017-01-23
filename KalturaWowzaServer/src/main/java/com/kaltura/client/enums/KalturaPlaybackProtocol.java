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
public enum KalturaPlaybackProtocol implements KalturaEnumAsString {
    APPLE_HTTP ("applehttp"),
    APPLE_HTTP_TO_MC ("applehttp_to_mc"),
    AUTO ("auto"),
    AKAMAI_HD ("hdnetwork"),
    AKAMAI_HDS ("hdnetworkmanifest"),
    HDS ("hds"),
    HLS ("hls"),
    HTTP ("http"),
    MPEG_DASH ("mpegdash"),
    MULTICAST_SL ("multicast_silverlight"),
    RTMP ("rtmp"),
    RTSP ("rtsp"),
    SILVER_LIGHT ("sl"),
    URL ("url");

    public String hashCode;

    KalturaPlaybackProtocol(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaPlaybackProtocol get(String hashCode) {
        if (hashCode.equals("applehttp"))
        {
           return APPLE_HTTP;
        }
        else 
        if (hashCode.equals("applehttp_to_mc"))
        {
           return APPLE_HTTP_TO_MC;
        }
        else 
        if (hashCode.equals("auto"))
        {
           return AUTO;
        }
        else 
        if (hashCode.equals("hdnetwork"))
        {
           return AKAMAI_HD;
        }
        else 
        if (hashCode.equals("hdnetworkmanifest"))
        {
           return AKAMAI_HDS;
        }
        else 
        if (hashCode.equals("hds"))
        {
           return HDS;
        }
        else 
        if (hashCode.equals("hls"))
        {
           return HLS;
        }
        else 
        if (hashCode.equals("http"))
        {
           return HTTP;
        }
        else 
        if (hashCode.equals("mpegdash"))
        {
           return MPEG_DASH;
        }
        else 
        if (hashCode.equals("multicast_silverlight"))
        {
           return MULTICAST_SL;
        }
        else 
        if (hashCode.equals("rtmp"))
        {
           return RTMP;
        }
        else 
        if (hashCode.equals("rtsp"))
        {
           return RTSP;
        }
        else 
        if (hashCode.equals("sl"))
        {
           return SILVER_LIGHT;
        }
        else 
        if (hashCode.equals("url"))
        {
           return URL;
        }
        else 
        {
           return APPLE_HTTP;
        }
    }
}
