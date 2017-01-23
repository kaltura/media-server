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
public enum KalturaMailType implements KalturaEnumAsString {
    MAIL_TYPE_KALTURA_NEWSLETTER ("10"),
    MAIL_TYPE_ADDED_TO_FAVORITES ("11"),
    MAIL_TYPE_ADDED_TO_CLIP_FAVORITES ("12"),
    MAIL_TYPE_NEW_COMMENT_IN_PROFILE ("13"),
    MAIL_TYPE_CLIP_ADDED_YOUR_KALTURA ("20"),
    MAIL_TYPE_VIDEO_ADDED ("21"),
    MAIL_TYPE_ROUGHCUT_CREATED ("22"),
    MAIL_TYPE_ADDED_KALTURA_TO_YOUR_FAVORITES ("23"),
    MAIL_TYPE_NEW_COMMENT_IN_KALTURA ("24"),
    MAIL_TYPE_CLIP_ADDED ("30"),
    MAIL_TYPE_VIDEO_CREATED ("31"),
    MAIL_TYPE_ADDED_KALTURA_TO_HIS_FAVORITES ("32"),
    MAIL_TYPE_NEW_COMMENT_IN_KALTURA_YOU_CONTRIBUTED ("33"),
    MAIL_TYPE_CLIP_CONTRIBUTED ("40"),
    MAIL_TYPE_ROUGHCUT_CREATED_SUBSCRIBED ("41"),
    MAIL_TYPE_ADDED_KALTURA_TO_HIS_FAVORITES_SUBSCRIBED ("42"),
    MAIL_TYPE_NEW_COMMENT_IN_KALTURA_YOU_SUBSCRIBED ("43"),
    MAIL_TYPE_REGISTER_CONFIRM ("50"),
    MAIL_TYPE_PASSWORD_RESET ("51"),
    MAIL_TYPE_LOGIN_MAIL_RESET ("52"),
    MAIL_TYPE_REGISTER_CONFIRM_VIDEO_SERVICE ("54"),
    MAIL_TYPE_VIDEO_READY ("60"),
    MAIL_TYPE_VIDEO_IS_READY ("62"),
    MAIL_TYPE_BULK_DOWNLOAD_READY ("63"),
    MAIL_TYPE_BULKUPLOAD_FINISHED ("64"),
    MAIL_TYPE_BULKUPLOAD_FAILED ("65"),
    MAIL_TYPE_BULKUPLOAD_ABORTED ("66"),
    MAIL_TYPE_NOTIFY_ERR ("70"),
    MAIL_TYPE_ACCOUNT_UPGRADE_CONFIRM ("80"),
    MAIL_TYPE_VIDEO_SERVICE_NOTICE ("81"),
    MAIL_TYPE_VIDEO_SERVICE_NOTICE_LIMIT_REACHED ("82"),
    MAIL_TYPE_VIDEO_SERVICE_NOTICE_ACCOUNT_LOCKED ("83"),
    MAIL_TYPE_VIDEO_SERVICE_NOTICE_ACCOUNT_DELETED ("84"),
    MAIL_TYPE_VIDEO_SERVICE_NOTICE_UPGRADE_OFFER ("85"),
    MAIL_TYPE_ACCOUNT_REACTIVE_CONFIRM ("86"),
    MAIL_TYPE_SYSTEM_USER_RESET_PASSWORD ("110"),
    MAIL_TYPE_SYSTEM_USER_RESET_PASSWORD_SUCCESS ("111"),
    MAIL_TYPE_SYSTEM_USER_NEW_PASSWORD ("112"),
    MAIL_TYPE_SYSTEM_USER_CREDENTIALS_SAVED ("113"),
    MAIL_TYPE_LIVE_REPORT_EXPORT_SUCCESS ("130"),
    MAIL_TYPE_LIVE_REPORT_EXPORT_FAILURE ("131"),
    MAIL_TYPE_LIVE_REPORT_EXPORT_ABORT ("132");

    public String hashCode;

    KalturaMailType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaMailType get(String hashCode) {
        if (hashCode.equals("10"))
        {
           return MAIL_TYPE_KALTURA_NEWSLETTER;
        }
        else 
        if (hashCode.equals("11"))
        {
           return MAIL_TYPE_ADDED_TO_FAVORITES;
        }
        else 
        if (hashCode.equals("12"))
        {
           return MAIL_TYPE_ADDED_TO_CLIP_FAVORITES;
        }
        else 
        if (hashCode.equals("13"))
        {
           return MAIL_TYPE_NEW_COMMENT_IN_PROFILE;
        }
        else 
        if (hashCode.equals("20"))
        {
           return MAIL_TYPE_CLIP_ADDED_YOUR_KALTURA;
        }
        else 
        if (hashCode.equals("21"))
        {
           return MAIL_TYPE_VIDEO_ADDED;
        }
        else 
        if (hashCode.equals("22"))
        {
           return MAIL_TYPE_ROUGHCUT_CREATED;
        }
        else 
        if (hashCode.equals("23"))
        {
           return MAIL_TYPE_ADDED_KALTURA_TO_YOUR_FAVORITES;
        }
        else 
        if (hashCode.equals("24"))
        {
           return MAIL_TYPE_NEW_COMMENT_IN_KALTURA;
        }
        else 
        if (hashCode.equals("30"))
        {
           return MAIL_TYPE_CLIP_ADDED;
        }
        else 
        if (hashCode.equals("31"))
        {
           return MAIL_TYPE_VIDEO_CREATED;
        }
        else 
        if (hashCode.equals("32"))
        {
           return MAIL_TYPE_ADDED_KALTURA_TO_HIS_FAVORITES;
        }
        else 
        if (hashCode.equals("33"))
        {
           return MAIL_TYPE_NEW_COMMENT_IN_KALTURA_YOU_CONTRIBUTED;
        }
        else 
        if (hashCode.equals("40"))
        {
           return MAIL_TYPE_CLIP_CONTRIBUTED;
        }
        else 
        if (hashCode.equals("41"))
        {
           return MAIL_TYPE_ROUGHCUT_CREATED_SUBSCRIBED;
        }
        else 
        if (hashCode.equals("42"))
        {
           return MAIL_TYPE_ADDED_KALTURA_TO_HIS_FAVORITES_SUBSCRIBED;
        }
        else 
        if (hashCode.equals("43"))
        {
           return MAIL_TYPE_NEW_COMMENT_IN_KALTURA_YOU_SUBSCRIBED;
        }
        else 
        if (hashCode.equals("50"))
        {
           return MAIL_TYPE_REGISTER_CONFIRM;
        }
        else 
        if (hashCode.equals("51"))
        {
           return MAIL_TYPE_PASSWORD_RESET;
        }
        else 
        if (hashCode.equals("52"))
        {
           return MAIL_TYPE_LOGIN_MAIL_RESET;
        }
        else 
        if (hashCode.equals("54"))
        {
           return MAIL_TYPE_REGISTER_CONFIRM_VIDEO_SERVICE;
        }
        else 
        if (hashCode.equals("60"))
        {
           return MAIL_TYPE_VIDEO_READY;
        }
        else 
        if (hashCode.equals("62"))
        {
           return MAIL_TYPE_VIDEO_IS_READY;
        }
        else 
        if (hashCode.equals("63"))
        {
           return MAIL_TYPE_BULK_DOWNLOAD_READY;
        }
        else 
        if (hashCode.equals("64"))
        {
           return MAIL_TYPE_BULKUPLOAD_FINISHED;
        }
        else 
        if (hashCode.equals("65"))
        {
           return MAIL_TYPE_BULKUPLOAD_FAILED;
        }
        else 
        if (hashCode.equals("66"))
        {
           return MAIL_TYPE_BULKUPLOAD_ABORTED;
        }
        else 
        if (hashCode.equals("70"))
        {
           return MAIL_TYPE_NOTIFY_ERR;
        }
        else 
        if (hashCode.equals("80"))
        {
           return MAIL_TYPE_ACCOUNT_UPGRADE_CONFIRM;
        }
        else 
        if (hashCode.equals("81"))
        {
           return MAIL_TYPE_VIDEO_SERVICE_NOTICE;
        }
        else 
        if (hashCode.equals("82"))
        {
           return MAIL_TYPE_VIDEO_SERVICE_NOTICE_LIMIT_REACHED;
        }
        else 
        if (hashCode.equals("83"))
        {
           return MAIL_TYPE_VIDEO_SERVICE_NOTICE_ACCOUNT_LOCKED;
        }
        else 
        if (hashCode.equals("84"))
        {
           return MAIL_TYPE_VIDEO_SERVICE_NOTICE_ACCOUNT_DELETED;
        }
        else 
        if (hashCode.equals("85"))
        {
           return MAIL_TYPE_VIDEO_SERVICE_NOTICE_UPGRADE_OFFER;
        }
        else 
        if (hashCode.equals("86"))
        {
           return MAIL_TYPE_ACCOUNT_REACTIVE_CONFIRM;
        }
        else 
        if (hashCode.equals("110"))
        {
           return MAIL_TYPE_SYSTEM_USER_RESET_PASSWORD;
        }
        else 
        if (hashCode.equals("111"))
        {
           return MAIL_TYPE_SYSTEM_USER_RESET_PASSWORD_SUCCESS;
        }
        else 
        if (hashCode.equals("112"))
        {
           return MAIL_TYPE_SYSTEM_USER_NEW_PASSWORD;
        }
        else 
        if (hashCode.equals("113"))
        {
           return MAIL_TYPE_SYSTEM_USER_CREDENTIALS_SAVED;
        }
        else 
        if (hashCode.equals("130"))
        {
           return MAIL_TYPE_LIVE_REPORT_EXPORT_SUCCESS;
        }
        else 
        if (hashCode.equals("131"))
        {
           return MAIL_TYPE_LIVE_REPORT_EXPORT_FAILURE;
        }
        else 
        if (hashCode.equals("132"))
        {
           return MAIL_TYPE_LIVE_REPORT_EXPORT_ABORT;
        }
        else 
        {
           return MAIL_TYPE_KALTURA_NEWSLETTER;
        }
    }
}
