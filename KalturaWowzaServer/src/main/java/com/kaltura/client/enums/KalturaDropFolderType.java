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
public enum KalturaDropFolderType implements KalturaEnumAsString {
    FEED ("FeedDropFolder.FEED"),
    WEBEX ("WebexDropFolder.WEBEX"),
    LOCAL ("1"),
    FTP ("2"),
    SCP ("3"),
    SFTP ("4"),
    S3 ("6");

    public String hashCode;

    KalturaDropFolderType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaDropFolderType get(String hashCode) {
        if (hashCode.equals("FeedDropFolder.FEED"))
        {
           return FEED;
        }
        else 
        if (hashCode.equals("WebexDropFolder.WEBEX"))
        {
           return WEBEX;
        }
        else 
        if (hashCode.equals("1"))
        {
           return LOCAL;
        }
        else 
        if (hashCode.equals("2"))
        {
           return FTP;
        }
        else 
        if (hashCode.equals("3"))
        {
           return SCP;
        }
        else 
        if (hashCode.equals("4"))
        {
           return SFTP;
        }
        else 
        if (hashCode.equals("6"))
        {
           return S3;
        }
        else 
        {
           return FEED;
        }
    }
}
