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
public enum KalturaStatsKmcEventType implements KalturaEnumAsInt {
    CONTENT_PAGE_VIEW (1001),
    CONTENT_ADD_PLAYLIST (1010),
    CONTENT_EDIT_PLAYLIST (1011),
    CONTENT_DELETE_PLAYLIST (1012),
    CONTENT_EDIT_ENTRY (1013),
    CONTENT_CHANGE_THUMBNAIL (1014),
    CONTENT_ADD_TAGS (1015),
    CONTENT_REMOVE_TAGS (1016),
    CONTENT_ADD_ADMIN_TAGS (1017),
    CONTENT_REMOVE_ADMIN_TAGS (1018),
    CONTENT_DOWNLOAD (1019),
    CONTENT_APPROVE_MODERATION (1020),
    CONTENT_REJECT_MODERATION (1021),
    CONTENT_BULK_UPLOAD (1022),
    CONTENT_ADMIN_KCW_UPLOAD (1023),
    ACCOUNT_CHANGE_PARTNER_INFO (1030),
    ACCOUNT_CHANGE_LOGIN_INFO (1031),
    ACCOUNT_CONTACT_US_USAGE (1032),
    ACCOUNT_UPDATE_SERVER_SETTINGS (1033),
    ACCOUNT_ACCOUNT_OVERVIEW (1034),
    ACCOUNT_ACCESS_CONTROL (1035),
    ACCOUNT_TRANSCODING_SETTINGS (1036),
    ACCOUNT_ACCOUNT_UPGRADE (1037),
    ACCOUNT_SAVE_SERVER_SETTINGS (1038),
    ACCOUNT_ACCESS_CONTROL_DELETE (1039),
    ACCOUNT_SAVE_TRANSCODING_SETTINGS (1040),
    LOGIN (1041),
    DASHBOARD_IMPORT_CONTENT (1042),
    DASHBOARD_UPDATE_CONTENT (1043),
    DASHBOARD_ACCOUNT_CONTACT_US (1044),
    DASHBOARD_VIEW_REPORTS (1045),
    DASHBOARD_EMBED_PLAYER (1046),
    DASHBOARD_EMBED_PLAYLIST (1047),
    DASHBOARD_CUSTOMIZE_PLAYERS (1048),
    APP_STUDIO_NEW_PLAYER_SINGLE_VIDEO (1050),
    APP_STUDIO_NEW_PLAYER_PLAYLIST (1051),
    APP_STUDIO_NEW_PLAYER_MULTI_TAB_PLAYLIST (1052),
    APP_STUDIO_EDIT_PLAYER_SINGLE_VIDEO (1053),
    APP_STUDIO_EDIT_PLAYER_PLAYLIST (1054),
    APP_STUDIO_EDIT_PLAYER_MULTI_TAB_PLAYLIST (1055),
    APP_STUDIO_DUPLICATE_PLAYER (1056),
    CONTENT_CONTENT_GO_TO_PAGE (1057),
    CONTENT_DELETE_ITEM (1058),
    CONTENT_DELETE_MIX (1059),
    REPORTS_AND_ANALYTICS_BANDWIDTH_USAGE_TAB (1070),
    REPORTS_AND_ANALYTICS_CONTENT_REPORTS_TAB (1071),
    REPORTS_AND_ANALYTICS_USERS_AND_COMMUNITY_REPORTS_TAB (1072),
    REPORTS_AND_ANALYTICS_TOP_CONTRIBUTORS (1073),
    REPORTS_AND_ANALYTICS_MAP_OVERLAYS (1074),
    REPORTS_AND_ANALYTICS_TOP_SYNDICATIONS (1075),
    REPORTS_AND_ANALYTICS_TOP_CONTENT (1076),
    REPORTS_AND_ANALYTICS_CONTENT_DROPOFF (1077),
    REPORTS_AND_ANALYTICS_CONTENT_INTERACTIONS (1078),
    REPORTS_AND_ANALYTICS_CONTENT_CONTRIBUTIONS (1079),
    REPORTS_AND_ANALYTICS_VIDEO_DRILL_DOWN (1080),
    REPORTS_AND_ANALYTICS_CONTENT_DRILL_DOWN_INTERACTION (1081),
    REPORTS_AND_ANALYTICS_CONTENT_CONTRIBUTIONS_DRILLDOWN (1082),
    REPORTS_AND_ANALYTICS_VIDEO_DRILL_DOWN_DROPOFF (1083),
    REPORTS_AND_ANALYTICS_MAP_OVERLAYS_DRILLDOWN (1084),
    REPORTS_AND_ANALYTICS_TOP_SYNDICATIONS_DRILL_DOWN (1085),
    REPORTS_AND_ANALYTICS_BANDWIDTH_USAGE_VIEW_MONTHLY (1086),
    REPORTS_AND_ANALYTICS_BANDWIDTH_USAGE_VIEW_YEARLY (1087),
    CONTENT_ENTRY_DRILLDOWN (1088),
    CONTENT_OPEN_PREVIEW_AND_EMBED (1089);

    public int hashCode;

    KalturaStatsKmcEventType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaStatsKmcEventType get(int hashCode) {
        switch(hashCode) {
            case 1001: return CONTENT_PAGE_VIEW;
            case 1010: return CONTENT_ADD_PLAYLIST;
            case 1011: return CONTENT_EDIT_PLAYLIST;
            case 1012: return CONTENT_DELETE_PLAYLIST;
            case 1013: return CONTENT_EDIT_ENTRY;
            case 1014: return CONTENT_CHANGE_THUMBNAIL;
            case 1015: return CONTENT_ADD_TAGS;
            case 1016: return CONTENT_REMOVE_TAGS;
            case 1017: return CONTENT_ADD_ADMIN_TAGS;
            case 1018: return CONTENT_REMOVE_ADMIN_TAGS;
            case 1019: return CONTENT_DOWNLOAD;
            case 1020: return CONTENT_APPROVE_MODERATION;
            case 1021: return CONTENT_REJECT_MODERATION;
            case 1022: return CONTENT_BULK_UPLOAD;
            case 1023: return CONTENT_ADMIN_KCW_UPLOAD;
            case 1030: return ACCOUNT_CHANGE_PARTNER_INFO;
            case 1031: return ACCOUNT_CHANGE_LOGIN_INFO;
            case 1032: return ACCOUNT_CONTACT_US_USAGE;
            case 1033: return ACCOUNT_UPDATE_SERVER_SETTINGS;
            case 1034: return ACCOUNT_ACCOUNT_OVERVIEW;
            case 1035: return ACCOUNT_ACCESS_CONTROL;
            case 1036: return ACCOUNT_TRANSCODING_SETTINGS;
            case 1037: return ACCOUNT_ACCOUNT_UPGRADE;
            case 1038: return ACCOUNT_SAVE_SERVER_SETTINGS;
            case 1039: return ACCOUNT_ACCESS_CONTROL_DELETE;
            case 1040: return ACCOUNT_SAVE_TRANSCODING_SETTINGS;
            case 1041: return LOGIN;
            case 1042: return DASHBOARD_IMPORT_CONTENT;
            case 1043: return DASHBOARD_UPDATE_CONTENT;
            case 1044: return DASHBOARD_ACCOUNT_CONTACT_US;
            case 1045: return DASHBOARD_VIEW_REPORTS;
            case 1046: return DASHBOARD_EMBED_PLAYER;
            case 1047: return DASHBOARD_EMBED_PLAYLIST;
            case 1048: return DASHBOARD_CUSTOMIZE_PLAYERS;
            case 1050: return APP_STUDIO_NEW_PLAYER_SINGLE_VIDEO;
            case 1051: return APP_STUDIO_NEW_PLAYER_PLAYLIST;
            case 1052: return APP_STUDIO_NEW_PLAYER_MULTI_TAB_PLAYLIST;
            case 1053: return APP_STUDIO_EDIT_PLAYER_SINGLE_VIDEO;
            case 1054: return APP_STUDIO_EDIT_PLAYER_PLAYLIST;
            case 1055: return APP_STUDIO_EDIT_PLAYER_MULTI_TAB_PLAYLIST;
            case 1056: return APP_STUDIO_DUPLICATE_PLAYER;
            case 1057: return CONTENT_CONTENT_GO_TO_PAGE;
            case 1058: return CONTENT_DELETE_ITEM;
            case 1059: return CONTENT_DELETE_MIX;
            case 1070: return REPORTS_AND_ANALYTICS_BANDWIDTH_USAGE_TAB;
            case 1071: return REPORTS_AND_ANALYTICS_CONTENT_REPORTS_TAB;
            case 1072: return REPORTS_AND_ANALYTICS_USERS_AND_COMMUNITY_REPORTS_TAB;
            case 1073: return REPORTS_AND_ANALYTICS_TOP_CONTRIBUTORS;
            case 1074: return REPORTS_AND_ANALYTICS_MAP_OVERLAYS;
            case 1075: return REPORTS_AND_ANALYTICS_TOP_SYNDICATIONS;
            case 1076: return REPORTS_AND_ANALYTICS_TOP_CONTENT;
            case 1077: return REPORTS_AND_ANALYTICS_CONTENT_DROPOFF;
            case 1078: return REPORTS_AND_ANALYTICS_CONTENT_INTERACTIONS;
            case 1079: return REPORTS_AND_ANALYTICS_CONTENT_CONTRIBUTIONS;
            case 1080: return REPORTS_AND_ANALYTICS_VIDEO_DRILL_DOWN;
            case 1081: return REPORTS_AND_ANALYTICS_CONTENT_DRILL_DOWN_INTERACTION;
            case 1082: return REPORTS_AND_ANALYTICS_CONTENT_CONTRIBUTIONS_DRILLDOWN;
            case 1083: return REPORTS_AND_ANALYTICS_VIDEO_DRILL_DOWN_DROPOFF;
            case 1084: return REPORTS_AND_ANALYTICS_MAP_OVERLAYS_DRILLDOWN;
            case 1085: return REPORTS_AND_ANALYTICS_TOP_SYNDICATIONS_DRILL_DOWN;
            case 1086: return REPORTS_AND_ANALYTICS_BANDWIDTH_USAGE_VIEW_MONTHLY;
            case 1087: return REPORTS_AND_ANALYTICS_BANDWIDTH_USAGE_VIEW_YEARLY;
            case 1088: return CONTENT_ENTRY_DRILLDOWN;
            case 1089: return CONTENT_OPEN_PREVIEW_AND_EMBED;
            default: return CONTENT_PAGE_VIEW;
        }
    }
}
