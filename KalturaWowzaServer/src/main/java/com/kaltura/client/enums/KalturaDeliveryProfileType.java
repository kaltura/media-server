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
public enum KalturaDeliveryProfileType implements KalturaEnumAsString {
    EDGE_CAST_HTTP ("edgeCast.EDGE_CAST_HTTP"),
    EDGE_CAST_RTMP ("edgeCast.EDGE_CAST_RTMP"),
    FORENSIC_WATERMARK_APPLE_HTTP ("forensicWatermark.FORENSIC_WATERMARK_APPLE_HTTP"),
    FORENSIC_WATERMARK_DASH ("forensicWatermark.FORENSIC_WATERMARK_DASH"),
    KONTIKI_HTTP ("kontiki.KONTIKI_HTTP"),
    UPLYNK_HTTP ("uplynk.UPLYNK_HTTP"),
    UPLYNK_RTMP ("uplynk.UPLYNK_RTMP"),
    VELOCIX_HDS ("velocix.VELOCIX_HDS"),
    VELOCIX_HLS ("velocix.VELOCIX_HLS"),
    APPLE_HTTP ("1"),
    HDS ("3"),
    HTTP ("4"),
    RTMP ("5"),
    RTSP ("6"),
    SILVER_LIGHT ("7"),
    AKAMAI_HLS_DIRECT ("10"),
    AKAMAI_HLS_MANIFEST ("11"),
    AKAMAI_HD ("12"),
    AKAMAI_HDS ("13"),
    AKAMAI_HTTP ("14"),
    AKAMAI_RTMP ("15"),
    AKAMAI_RTSP ("16"),
    AKAMAI_SS ("17"),
    GENERIC_HLS ("21"),
    GENERIC_HDS ("23"),
    GENERIC_HTTP ("24"),
    GENERIC_HLS_MANIFEST ("25"),
    GENERIC_HDS_MANIFEST ("26"),
    GENERIC_SS ("27"),
    GENERIC_RTMP ("28"),
    LEVEL3_HLS ("31"),
    LEVEL3_HTTP ("34"),
    LEVEL3_RTMP ("35"),
    LIMELIGHT_HTTP ("44"),
    LIMELIGHT_RTMP ("45"),
    LOCAL_PATH_APPLE_HTTP ("51"),
    LOCAL_PATH_HDS ("53"),
    LOCAL_PATH_HTTP ("54"),
    LOCAL_PATH_RTMP ("55"),
    VOD_PACKAGER_HLS ("61"),
    VOD_PACKAGER_HDS ("63"),
    VOD_PACKAGER_MSS ("67"),
    VOD_PACKAGER_DASH ("68"),
    LIVE_HLS ("1001"),
    LIVE_HDS ("1002"),
    LIVE_DASH ("1003"),
    LIVE_RTMP ("1005"),
    LIVE_HLS_TO_MULTICAST ("1006"),
    LIVE_PACKAGER_HLS ("1007"),
    LIVE_PACKAGER_HDS ("1008"),
    LIVE_PACKAGER_DASH ("1009"),
    LIVE_PACKAGER_MSS ("1010"),
    LIVE_AKAMAI_HDS ("1013");

    public String hashCode;

    KalturaDeliveryProfileType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaDeliveryProfileType get(String hashCode) {
        if (hashCode.equals("edgeCast.EDGE_CAST_HTTP"))
        {
           return EDGE_CAST_HTTP;
        }
        else 
        if (hashCode.equals("edgeCast.EDGE_CAST_RTMP"))
        {
           return EDGE_CAST_RTMP;
        }
        else 
        if (hashCode.equals("forensicWatermark.FORENSIC_WATERMARK_APPLE_HTTP"))
        {
           return FORENSIC_WATERMARK_APPLE_HTTP;
        }
        else 
        if (hashCode.equals("forensicWatermark.FORENSIC_WATERMARK_DASH"))
        {
           return FORENSIC_WATERMARK_DASH;
        }
        else 
        if (hashCode.equals("kontiki.KONTIKI_HTTP"))
        {
           return KONTIKI_HTTP;
        }
        else 
        if (hashCode.equals("uplynk.UPLYNK_HTTP"))
        {
           return UPLYNK_HTTP;
        }
        else 
        if (hashCode.equals("uplynk.UPLYNK_RTMP"))
        {
           return UPLYNK_RTMP;
        }
        else 
        if (hashCode.equals("velocix.VELOCIX_HDS"))
        {
           return VELOCIX_HDS;
        }
        else 
        if (hashCode.equals("velocix.VELOCIX_HLS"))
        {
           return VELOCIX_HLS;
        }
        else 
        if (hashCode.equals("1"))
        {
           return APPLE_HTTP;
        }
        else 
        if (hashCode.equals("3"))
        {
           return HDS;
        }
        else 
        if (hashCode.equals("4"))
        {
           return HTTP;
        }
        else 
        if (hashCode.equals("5"))
        {
           return RTMP;
        }
        else 
        if (hashCode.equals("6"))
        {
           return RTSP;
        }
        else 
        if (hashCode.equals("7"))
        {
           return SILVER_LIGHT;
        }
        else 
        if (hashCode.equals("10"))
        {
           return AKAMAI_HLS_DIRECT;
        }
        else 
        if (hashCode.equals("11"))
        {
           return AKAMAI_HLS_MANIFEST;
        }
        else 
        if (hashCode.equals("12"))
        {
           return AKAMAI_HD;
        }
        else 
        if (hashCode.equals("13"))
        {
           return AKAMAI_HDS;
        }
        else 
        if (hashCode.equals("14"))
        {
           return AKAMAI_HTTP;
        }
        else 
        if (hashCode.equals("15"))
        {
           return AKAMAI_RTMP;
        }
        else 
        if (hashCode.equals("16"))
        {
           return AKAMAI_RTSP;
        }
        else 
        if (hashCode.equals("17"))
        {
           return AKAMAI_SS;
        }
        else 
        if (hashCode.equals("21"))
        {
           return GENERIC_HLS;
        }
        else 
        if (hashCode.equals("23"))
        {
           return GENERIC_HDS;
        }
        else 
        if (hashCode.equals("24"))
        {
           return GENERIC_HTTP;
        }
        else 
        if (hashCode.equals("25"))
        {
           return GENERIC_HLS_MANIFEST;
        }
        else 
        if (hashCode.equals("26"))
        {
           return GENERIC_HDS_MANIFEST;
        }
        else 
        if (hashCode.equals("27"))
        {
           return GENERIC_SS;
        }
        else 
        if (hashCode.equals("28"))
        {
           return GENERIC_RTMP;
        }
        else 
        if (hashCode.equals("31"))
        {
           return LEVEL3_HLS;
        }
        else 
        if (hashCode.equals("34"))
        {
           return LEVEL3_HTTP;
        }
        else 
        if (hashCode.equals("35"))
        {
           return LEVEL3_RTMP;
        }
        else 
        if (hashCode.equals("44"))
        {
           return LIMELIGHT_HTTP;
        }
        else 
        if (hashCode.equals("45"))
        {
           return LIMELIGHT_RTMP;
        }
        else 
        if (hashCode.equals("51"))
        {
           return LOCAL_PATH_APPLE_HTTP;
        }
        else 
        if (hashCode.equals("53"))
        {
           return LOCAL_PATH_HDS;
        }
        else 
        if (hashCode.equals("54"))
        {
           return LOCAL_PATH_HTTP;
        }
        else 
        if (hashCode.equals("55"))
        {
           return LOCAL_PATH_RTMP;
        }
        else 
        if (hashCode.equals("61"))
        {
           return VOD_PACKAGER_HLS;
        }
        else 
        if (hashCode.equals("63"))
        {
           return VOD_PACKAGER_HDS;
        }
        else 
        if (hashCode.equals("67"))
        {
           return VOD_PACKAGER_MSS;
        }
        else 
        if (hashCode.equals("68"))
        {
           return VOD_PACKAGER_DASH;
        }
        else 
        if (hashCode.equals("1001"))
        {
           return LIVE_HLS;
        }
        else 
        if (hashCode.equals("1002"))
        {
           return LIVE_HDS;
        }
        else 
        if (hashCode.equals("1003"))
        {
           return LIVE_DASH;
        }
        else 
        if (hashCode.equals("1005"))
        {
           return LIVE_RTMP;
        }
        else 
        if (hashCode.equals("1006"))
        {
           return LIVE_HLS_TO_MULTICAST;
        }
        else 
        if (hashCode.equals("1007"))
        {
           return LIVE_PACKAGER_HLS;
        }
        else 
        if (hashCode.equals("1008"))
        {
           return LIVE_PACKAGER_HDS;
        }
        else 
        if (hashCode.equals("1009"))
        {
           return LIVE_PACKAGER_DASH;
        }
        else 
        if (hashCode.equals("1010"))
        {
           return LIVE_PACKAGER_MSS;
        }
        else 
        if (hashCode.equals("1013"))
        {
           return LIVE_AKAMAI_HDS;
        }
        else 
        {
           return EDGE_CAST_HTTP;
        }
    }
}
