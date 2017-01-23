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
public enum KalturaBatchJobType implements KalturaEnumAsString {
    PARSE_MULTI_LANGUAGE_CAPTION_ASSET ("caption.parsemultilanguagecaptionasset"),
    PARSE_CAPTION_ASSET ("captionSearch.parseCaptionAsset"),
    DISTRIBUTION_DELETE ("contentDistribution.DistributionDelete"),
    CONVERT ("0"),
    DISTRIBUTION_DISABLE ("contentDistribution.DistributionDisable"),
    DISTRIBUTION_ENABLE ("contentDistribution.DistributionEnable"),
    DISTRIBUTION_FETCH_REPORT ("contentDistribution.DistributionFetchReport"),
    DISTRIBUTION_SUBMIT ("contentDistribution.DistributionSubmit"),
    DISTRIBUTION_SYNC ("contentDistribution.DistributionSync"),
    DISTRIBUTION_UPDATE ("contentDistribution.DistributionUpdate"),
    DROP_FOLDER_CONTENT_PROCESSOR ("dropFolder.DropFolderContentProcessor"),
    DROP_FOLDER_WATCHER ("dropFolder.DropFolderWatcher"),
    EVENT_NOTIFICATION_HANDLER ("eventNotification.EventNotificationHandler"),
    INTEGRATION ("integration.Integration"),
    SCHEDULED_TASK ("scheduledTask.ScheduledTask"),
    INDEX_TAGS ("tagSearch.IndexTagsByPrivacyContext"),
    TAG_RESOLVE ("tagSearch.TagResolve"),
    VIRUS_SCAN ("virusScan.VirusScan"),
    WIDEVINE_REPOSITORY_SYNC ("widevine.WidevineRepositorySync"),
    IMPORT ("1"),
    DELETE ("2"),
    FLATTEN ("3"),
    BULKUPLOAD ("4"),
    DVDCREATOR ("5"),
    DOWNLOAD ("6"),
    OOCONVERT ("7"),
    CONVERT_PROFILE ("10"),
    POSTCONVERT ("11"),
    EXTRACT_MEDIA ("14"),
    MAIL ("15"),
    NOTIFICATION ("16"),
    CLEANUP ("17"),
    SCHEDULER_HELPER ("18"),
    BULKDOWNLOAD ("19"),
    DB_CLEANUP ("20"),
    PROVISION_PROVIDE ("21"),
    CONVERT_COLLECTION ("22"),
    STORAGE_EXPORT ("23"),
    PROVISION_DELETE ("24"),
    STORAGE_DELETE ("25"),
    EMAIL_INGESTION ("26"),
    METADATA_IMPORT ("27"),
    METADATA_TRANSFORM ("28"),
    FILESYNC_IMPORT ("29"),
    CAPTURE_THUMB ("30"),
    DELETE_FILE ("31"),
    INDEX ("32"),
    MOVE_CATEGORY_ENTRIES ("33"),
    COPY ("34"),
    CONCAT ("35"),
    CONVERT_LIVE_SEGMENT ("36"),
    COPY_PARTNER ("37"),
    VALIDATE_LIVE_MEDIA_SERVERS ("38"),
    SYNC_CATEGORY_PRIVACY_CONTEXT ("39"),
    LIVE_REPORT_EXPORT ("40"),
    RECALCULATE_CACHE ("41"),
    LIVE_TO_VOD ("42");

    public String hashCode;

    KalturaBatchJobType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaBatchJobType get(String hashCode) {
        if (hashCode.equals("caption.parsemultilanguagecaptionasset"))
        {
           return PARSE_MULTI_LANGUAGE_CAPTION_ASSET;
        }
        else 
        if (hashCode.equals("captionSearch.parseCaptionAsset"))
        {
           return PARSE_CAPTION_ASSET;
        }
        else 
        if (hashCode.equals("contentDistribution.DistributionDelete"))
        {
           return DISTRIBUTION_DELETE;
        }
        else 
        if (hashCode.equals("0"))
        {
           return CONVERT;
        }
        else 
        if (hashCode.equals("contentDistribution.DistributionDisable"))
        {
           return DISTRIBUTION_DISABLE;
        }
        else 
        if (hashCode.equals("contentDistribution.DistributionEnable"))
        {
           return DISTRIBUTION_ENABLE;
        }
        else 
        if (hashCode.equals("contentDistribution.DistributionFetchReport"))
        {
           return DISTRIBUTION_FETCH_REPORT;
        }
        else 
        if (hashCode.equals("contentDistribution.DistributionSubmit"))
        {
           return DISTRIBUTION_SUBMIT;
        }
        else 
        if (hashCode.equals("contentDistribution.DistributionSync"))
        {
           return DISTRIBUTION_SYNC;
        }
        else 
        if (hashCode.equals("contentDistribution.DistributionUpdate"))
        {
           return DISTRIBUTION_UPDATE;
        }
        else 
        if (hashCode.equals("dropFolder.DropFolderContentProcessor"))
        {
           return DROP_FOLDER_CONTENT_PROCESSOR;
        }
        else 
        if (hashCode.equals("dropFolder.DropFolderWatcher"))
        {
           return DROP_FOLDER_WATCHER;
        }
        else 
        if (hashCode.equals("eventNotification.EventNotificationHandler"))
        {
           return EVENT_NOTIFICATION_HANDLER;
        }
        else 
        if (hashCode.equals("integration.Integration"))
        {
           return INTEGRATION;
        }
        else 
        if (hashCode.equals("scheduledTask.ScheduledTask"))
        {
           return SCHEDULED_TASK;
        }
        else 
        if (hashCode.equals("tagSearch.IndexTagsByPrivacyContext"))
        {
           return INDEX_TAGS;
        }
        else 
        if (hashCode.equals("tagSearch.TagResolve"))
        {
           return TAG_RESOLVE;
        }
        else 
        if (hashCode.equals("virusScan.VirusScan"))
        {
           return VIRUS_SCAN;
        }
        else 
        if (hashCode.equals("widevine.WidevineRepositorySync"))
        {
           return WIDEVINE_REPOSITORY_SYNC;
        }
        else 
        if (hashCode.equals("1"))
        {
           return IMPORT;
        }
        else 
        if (hashCode.equals("2"))
        {
           return DELETE;
        }
        else 
        if (hashCode.equals("3"))
        {
           return FLATTEN;
        }
        else 
        if (hashCode.equals("4"))
        {
           return BULKUPLOAD;
        }
        else 
        if (hashCode.equals("5"))
        {
           return DVDCREATOR;
        }
        else 
        if (hashCode.equals("6"))
        {
           return DOWNLOAD;
        }
        else 
        if (hashCode.equals("7"))
        {
           return OOCONVERT;
        }
        else 
        if (hashCode.equals("10"))
        {
           return CONVERT_PROFILE;
        }
        else 
        if (hashCode.equals("11"))
        {
           return POSTCONVERT;
        }
        else 
        if (hashCode.equals("14"))
        {
           return EXTRACT_MEDIA;
        }
        else 
        if (hashCode.equals("15"))
        {
           return MAIL;
        }
        else 
        if (hashCode.equals("16"))
        {
           return NOTIFICATION;
        }
        else 
        if (hashCode.equals("17"))
        {
           return CLEANUP;
        }
        else 
        if (hashCode.equals("18"))
        {
           return SCHEDULER_HELPER;
        }
        else 
        if (hashCode.equals("19"))
        {
           return BULKDOWNLOAD;
        }
        else 
        if (hashCode.equals("20"))
        {
           return DB_CLEANUP;
        }
        else 
        if (hashCode.equals("21"))
        {
           return PROVISION_PROVIDE;
        }
        else 
        if (hashCode.equals("22"))
        {
           return CONVERT_COLLECTION;
        }
        else 
        if (hashCode.equals("23"))
        {
           return STORAGE_EXPORT;
        }
        else 
        if (hashCode.equals("24"))
        {
           return PROVISION_DELETE;
        }
        else 
        if (hashCode.equals("25"))
        {
           return STORAGE_DELETE;
        }
        else 
        if (hashCode.equals("26"))
        {
           return EMAIL_INGESTION;
        }
        else 
        if (hashCode.equals("27"))
        {
           return METADATA_IMPORT;
        }
        else 
        if (hashCode.equals("28"))
        {
           return METADATA_TRANSFORM;
        }
        else 
        if (hashCode.equals("29"))
        {
           return FILESYNC_IMPORT;
        }
        else 
        if (hashCode.equals("30"))
        {
           return CAPTURE_THUMB;
        }
        else 
        if (hashCode.equals("31"))
        {
           return DELETE_FILE;
        }
        else 
        if (hashCode.equals("32"))
        {
           return INDEX;
        }
        else 
        if (hashCode.equals("33"))
        {
           return MOVE_CATEGORY_ENTRIES;
        }
        else 
        if (hashCode.equals("34"))
        {
           return COPY;
        }
        else 
        if (hashCode.equals("35"))
        {
           return CONCAT;
        }
        else 
        if (hashCode.equals("36"))
        {
           return CONVERT_LIVE_SEGMENT;
        }
        else 
        if (hashCode.equals("37"))
        {
           return COPY_PARTNER;
        }
        else 
        if (hashCode.equals("38"))
        {
           return VALIDATE_LIVE_MEDIA_SERVERS;
        }
        else 
        if (hashCode.equals("39"))
        {
           return SYNC_CATEGORY_PRIVACY_CONTEXT;
        }
        else 
        if (hashCode.equals("40"))
        {
           return LIVE_REPORT_EXPORT;
        }
        else 
        if (hashCode.equals("41"))
        {
           return RECALCULATE_CACHE;
        }
        else 
        if (hashCode.equals("42"))
        {
           return LIVE_TO_VOD;
        }
        else 
        {
           return PARSE_MULTI_LANGUAGE_CAPTION_ASSET;
        }
    }
}
