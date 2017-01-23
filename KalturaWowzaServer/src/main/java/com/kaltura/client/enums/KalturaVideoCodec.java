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
public enum KalturaVideoCodec implements KalturaEnumAsString {
    NONE (""),
    APCH ("apch"),
    APCN ("apcn"),
    APCO ("apco"),
    APCS ("apcs"),
    COPY ("copy"),
    DNXHD ("dnxhd"),
    DV ("dv"),
    FLV ("flv"),
    H263 ("h263"),
    H264 ("h264"),
    H264B ("h264b"),
    H264H ("h264h"),
    H264M ("h264m"),
    H265 ("h265"),
    MPEG2 ("mpeg2"),
    MPEG4 ("mpeg4"),
    THEORA ("theora"),
    VP6 ("vp6"),
    VP8 ("vp8"),
    VP9 ("vp9"),
    WMV2 ("wmv2"),
    WMV3 ("wmv3"),
    WVC1A ("wvc1a");

    public String hashCode;

    KalturaVideoCodec(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaVideoCodec get(String hashCode) {
        if (hashCode.equals(""))
        {
           return NONE;
        }
        else 
        if (hashCode.equals("apch"))
        {
           return APCH;
        }
        else 
        if (hashCode.equals("apcn"))
        {
           return APCN;
        }
        else 
        if (hashCode.equals("apco"))
        {
           return APCO;
        }
        else 
        if (hashCode.equals("apcs"))
        {
           return APCS;
        }
        else 
        if (hashCode.equals("copy"))
        {
           return COPY;
        }
        else 
        if (hashCode.equals("dnxhd"))
        {
           return DNXHD;
        }
        else 
        if (hashCode.equals("dv"))
        {
           return DV;
        }
        else 
        if (hashCode.equals("flv"))
        {
           return FLV;
        }
        else 
        if (hashCode.equals("h263"))
        {
           return H263;
        }
        else 
        if (hashCode.equals("h264"))
        {
           return H264;
        }
        else 
        if (hashCode.equals("h264b"))
        {
           return H264B;
        }
        else 
        if (hashCode.equals("h264h"))
        {
           return H264H;
        }
        else 
        if (hashCode.equals("h264m"))
        {
           return H264M;
        }
        else 
        if (hashCode.equals("h265"))
        {
           return H265;
        }
        else 
        if (hashCode.equals("mpeg2"))
        {
           return MPEG2;
        }
        else 
        if (hashCode.equals("mpeg4"))
        {
           return MPEG4;
        }
        else 
        if (hashCode.equals("theora"))
        {
           return THEORA;
        }
        else 
        if (hashCode.equals("vp6"))
        {
           return VP6;
        }
        else 
        if (hashCode.equals("vp8"))
        {
           return VP8;
        }
        else 
        if (hashCode.equals("vp9"))
        {
           return VP9;
        }
        else 
        if (hashCode.equals("wmv2"))
        {
           return WMV2;
        }
        else 
        if (hashCode.equals("wmv3"))
        {
           return WMV3;
        }
        else 
        if (hashCode.equals("wvc1a"))
        {
           return WVC1A;
        }
        else 
        {
           return NONE;
        }
    }
}
