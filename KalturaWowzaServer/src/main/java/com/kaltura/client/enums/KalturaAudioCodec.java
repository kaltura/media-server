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
public enum KalturaAudioCodec implements KalturaEnumAsString {
    NONE (""),
    AAC ("aac"),
    AACHE ("aache"),
    AC3 ("ac3"),
    AMRNB ("amrnb"),
    COPY ("copy"),
    MP3 ("mp3"),
    MPEG2 ("mpeg2"),
    PCM ("pcm"),
    VORBIS ("vorbis"),
    WMA ("wma"),
    WMAPRO ("wmapro");

    public String hashCode;

    KalturaAudioCodec(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaAudioCodec get(String hashCode) {
        if (hashCode.equals(""))
        {
           return NONE;
        }
        else 
        if (hashCode.equals("aac"))
        {
           return AAC;
        }
        else 
        if (hashCode.equals("aache"))
        {
           return AACHE;
        }
        else 
        if (hashCode.equals("ac3"))
        {
           return AC3;
        }
        else 
        if (hashCode.equals("amrnb"))
        {
           return AMRNB;
        }
        else 
        if (hashCode.equals("copy"))
        {
           return COPY;
        }
        else 
        if (hashCode.equals("mp3"))
        {
           return MP3;
        }
        else 
        if (hashCode.equals("mpeg2"))
        {
           return MPEG2;
        }
        else 
        if (hashCode.equals("pcm"))
        {
           return PCM;
        }
        else 
        if (hashCode.equals("vorbis"))
        {
           return VORBIS;
        }
        else 
        if (hashCode.equals("wma"))
        {
           return WMA;
        }
        else 
        if (hashCode.equals("wmapro"))
        {
           return WMAPRO;
        }
        else 
        {
           return NONE;
        }
    }
}
