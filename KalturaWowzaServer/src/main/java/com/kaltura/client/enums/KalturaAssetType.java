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
public enum KalturaAssetType implements KalturaEnumAsString {
    ATTACHMENT ("attachment.Attachment"),
    CAPTION ("caption.Caption"),
    DOCUMENT ("document.Document"),
    IMAGE ("document.Image"),
    PDF ("document.PDF"),
    SWF ("document.SWF"),
    TIMED_THUMB_ASSET ("thumbCuePoint.timedThumb"),
    TRANSCRIPT ("transcript.Transcript"),
    WIDEVINE_FLAVOR ("widevine.WidevineFlavor"),
    FLAVOR ("1"),
    THUMBNAIL ("2"),
    LIVE ("3");

    public String hashCode;

    KalturaAssetType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaAssetType get(String hashCode) {
        if (hashCode.equals("attachment.Attachment"))
        {
           return ATTACHMENT;
        }
        else 
        if (hashCode.equals("caption.Caption"))
        {
           return CAPTION;
        }
        else 
        if (hashCode.equals("document.Document"))
        {
           return DOCUMENT;
        }
        else 
        if (hashCode.equals("document.Image"))
        {
           return IMAGE;
        }
        else 
        if (hashCode.equals("document.PDF"))
        {
           return PDF;
        }
        else 
        if (hashCode.equals("document.SWF"))
        {
           return SWF;
        }
        else 
        if (hashCode.equals("thumbCuePoint.timedThumb"))
        {
           return TIMED_THUMB_ASSET;
        }
        else 
        if (hashCode.equals("transcript.Transcript"))
        {
           return TRANSCRIPT;
        }
        else 
        if (hashCode.equals("widevine.WidevineFlavor"))
        {
           return WIDEVINE_FLAVOR;
        }
        else 
        if (hashCode.equals("1"))
        {
           return FLAVOR;
        }
        else 
        if (hashCode.equals("2"))
        {
           return THUMBNAIL;
        }
        else 
        if (hashCode.equals("3"))
        {
           return LIVE;
        }
        else 
        {
           return ATTACHMENT;
        }
    }
}
