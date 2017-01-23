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
public enum KalturaContainerFormat implements KalturaEnumAsString {
    _3GP ("3gp"),
    APPLEHTTP ("applehttp"),
    AVI ("avi"),
    BMP ("bmp"),
    COPY ("copy"),
    FLV ("flv"),
    HLS ("hls"),
    ISMA ("isma"),
    ISMV ("ismv"),
    JPG ("jpg"),
    M2TS ("m2ts"),
    M4V ("m4v"),
    MKV ("mkv"),
    MOV ("mov"),
    MP3 ("mp3"),
    MP4 ("mp4"),
    MPEG ("mpeg"),
    MPEGTS ("mpegts"),
    MXF ("mxf"),
    OGG ("ogg"),
    OGV ("ogv"),
    PDF ("pdf"),
    PNG ("png"),
    SWF ("swf"),
    WAV ("wav"),
    WEBM ("webm"),
    WMA ("wma"),
    WMV ("wmv"),
    WVM ("wvm");

    public String hashCode;

    KalturaContainerFormat(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaContainerFormat get(String hashCode) {
        if (hashCode.equals("3gp"))
        {
           return _3GP;
        }
        else 
        if (hashCode.equals("applehttp"))
        {
           return APPLEHTTP;
        }
        else 
        if (hashCode.equals("avi"))
        {
           return AVI;
        }
        else 
        if (hashCode.equals("bmp"))
        {
           return BMP;
        }
        else 
        if (hashCode.equals("copy"))
        {
           return COPY;
        }
        else 
        if (hashCode.equals("flv"))
        {
           return FLV;
        }
        else 
        if (hashCode.equals("hls"))
        {
           return HLS;
        }
        else 
        if (hashCode.equals("isma"))
        {
           return ISMA;
        }
        else 
        if (hashCode.equals("ismv"))
        {
           return ISMV;
        }
        else 
        if (hashCode.equals("jpg"))
        {
           return JPG;
        }
        else 
        if (hashCode.equals("m2ts"))
        {
           return M2TS;
        }
        else 
        if (hashCode.equals("m4v"))
        {
           return M4V;
        }
        else 
        if (hashCode.equals("mkv"))
        {
           return MKV;
        }
        else 
        if (hashCode.equals("mov"))
        {
           return MOV;
        }
        else 
        if (hashCode.equals("mp3"))
        {
           return MP3;
        }
        else 
        if (hashCode.equals("mp4"))
        {
           return MP4;
        }
        else 
        if (hashCode.equals("mpeg"))
        {
           return MPEG;
        }
        else 
        if (hashCode.equals("mpegts"))
        {
           return MPEGTS;
        }
        else 
        if (hashCode.equals("mxf"))
        {
           return MXF;
        }
        else 
        if (hashCode.equals("ogg"))
        {
           return OGG;
        }
        else 
        if (hashCode.equals("ogv"))
        {
           return OGV;
        }
        else 
        if (hashCode.equals("pdf"))
        {
           return PDF;
        }
        else 
        if (hashCode.equals("png"))
        {
           return PNG;
        }
        else 
        if (hashCode.equals("swf"))
        {
           return SWF;
        }
        else 
        if (hashCode.equals("wav"))
        {
           return WAV;
        }
        else 
        if (hashCode.equals("webm"))
        {
           return WEBM;
        }
        else 
        if (hashCode.equals("wma"))
        {
           return WMA;
        }
        else 
        if (hashCode.equals("wmv"))
        {
           return WMV;
        }
        else 
        if (hashCode.equals("wvm"))
        {
           return WVM;
        }
        else 
        {
           return _3GP;
        }
    }
}
