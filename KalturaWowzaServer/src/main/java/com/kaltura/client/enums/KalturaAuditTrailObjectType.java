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
public enum KalturaAuditTrailObjectType implements KalturaEnumAsString {
    BATCH_JOB ("BatchJob"),
    EMAIL_INGESTION_PROFILE ("EmailIngestionProfile"),
    FILE_SYNC ("FileSync"),
    KSHOW_KUSER ("KshowKuser"),
    METADATA ("Metadata"),
    METADATA_PROFILE ("MetadataProfile"),
    PARTNER ("Partner"),
    PERMISSION ("Permission"),
    UPLOAD_TOKEN ("UploadToken"),
    USER_LOGIN_DATA ("UserLoginData"),
    USER_ROLE ("UserRole"),
    ACCESS_CONTROL ("accessControl"),
    CATEGORY ("category"),
    CONVERSION_PROFILE_2 ("conversionProfile2"),
    ENTRY ("entry"),
    FLAVOR_ASSET ("flavorAsset"),
    FLAVOR_PARAMS ("flavorParams"),
    FLAVOR_PARAMS_CONVERSION_PROFILE ("flavorParamsConversionProfile"),
    FLAVOR_PARAMS_OUTPUT ("flavorParamsOutput"),
    KSHOW ("kshow"),
    KUSER ("kuser"),
    MEDIA_INFO ("mediaInfo"),
    MODERATION ("moderation"),
    ROUGHCUT ("roughcutEntry"),
    SYNDICATION ("syndicationFeed"),
    THUMBNAIL_ASSET ("thumbAsset"),
    THUMBNAIL_PARAMS ("thumbParams"),
    THUMBNAIL_PARAMS_OUTPUT ("thumbParamsOutput"),
    UI_CONF ("uiConf"),
    WIDGET ("widget");

    public String hashCode;

    KalturaAuditTrailObjectType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaAuditTrailObjectType get(String hashCode) {
        if (hashCode.equals("BatchJob"))
        {
           return BATCH_JOB;
        }
        else 
        if (hashCode.equals("EmailIngestionProfile"))
        {
           return EMAIL_INGESTION_PROFILE;
        }
        else 
        if (hashCode.equals("FileSync"))
        {
           return FILE_SYNC;
        }
        else 
        if (hashCode.equals("KshowKuser"))
        {
           return KSHOW_KUSER;
        }
        else 
        if (hashCode.equals("Metadata"))
        {
           return METADATA;
        }
        else 
        if (hashCode.equals("MetadataProfile"))
        {
           return METADATA_PROFILE;
        }
        else 
        if (hashCode.equals("Partner"))
        {
           return PARTNER;
        }
        else 
        if (hashCode.equals("Permission"))
        {
           return PERMISSION;
        }
        else 
        if (hashCode.equals("UploadToken"))
        {
           return UPLOAD_TOKEN;
        }
        else 
        if (hashCode.equals("UserLoginData"))
        {
           return USER_LOGIN_DATA;
        }
        else 
        if (hashCode.equals("UserRole"))
        {
           return USER_ROLE;
        }
        else 
        if (hashCode.equals("accessControl"))
        {
           return ACCESS_CONTROL;
        }
        else 
        if (hashCode.equals("category"))
        {
           return CATEGORY;
        }
        else 
        if (hashCode.equals("conversionProfile2"))
        {
           return CONVERSION_PROFILE_2;
        }
        else 
        if (hashCode.equals("entry"))
        {
           return ENTRY;
        }
        else 
        if (hashCode.equals("flavorAsset"))
        {
           return FLAVOR_ASSET;
        }
        else 
        if (hashCode.equals("flavorParams"))
        {
           return FLAVOR_PARAMS;
        }
        else 
        if (hashCode.equals("flavorParamsConversionProfile"))
        {
           return FLAVOR_PARAMS_CONVERSION_PROFILE;
        }
        else 
        if (hashCode.equals("flavorParamsOutput"))
        {
           return FLAVOR_PARAMS_OUTPUT;
        }
        else 
        if (hashCode.equals("kshow"))
        {
           return KSHOW;
        }
        else 
        if (hashCode.equals("kuser"))
        {
           return KUSER;
        }
        else 
        if (hashCode.equals("mediaInfo"))
        {
           return MEDIA_INFO;
        }
        else 
        if (hashCode.equals("moderation"))
        {
           return MODERATION;
        }
        else 
        if (hashCode.equals("roughcutEntry"))
        {
           return ROUGHCUT;
        }
        else 
        if (hashCode.equals("syndicationFeed"))
        {
           return SYNDICATION;
        }
        else 
        if (hashCode.equals("thumbAsset"))
        {
           return THUMBNAIL_ASSET;
        }
        else 
        if (hashCode.equals("thumbParams"))
        {
           return THUMBNAIL_PARAMS;
        }
        else 
        if (hashCode.equals("thumbParamsOutput"))
        {
           return THUMBNAIL_PARAMS_OUTPUT;
        }
        else 
        if (hashCode.equals("uiConf"))
        {
           return UI_CONF;
        }
        else 
        if (hashCode.equals("widget"))
        {
           return WIDGET;
        }
        else 
        {
           return BATCH_JOB;
        }
    }
}
