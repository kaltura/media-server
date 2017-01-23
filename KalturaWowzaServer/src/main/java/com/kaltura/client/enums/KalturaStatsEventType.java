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
public enum KalturaStatsEventType implements KalturaEnumAsInt {
    WIDGET_LOADED (1),
    MEDIA_LOADED (2),
    PLAY (3),
    PLAY_REACHED_25 (4),
    PLAY_REACHED_50 (5),
    PLAY_REACHED_75 (6),
    PLAY_REACHED_100 (7),
    OPEN_EDIT (8),
    OPEN_VIRAL (9),
    OPEN_DOWNLOAD (10),
    OPEN_REPORT (11),
    BUFFER_START (12),
    BUFFER_END (13),
    OPEN_FULL_SCREEN (14),
    CLOSE_FULL_SCREEN (15),
    REPLAY (16),
    SEEK (17),
    OPEN_UPLOAD (18),
    SAVE_PUBLISH (19),
    CLOSE_EDITOR (20),
    PRE_BUMPER_PLAYED (21),
    POST_BUMPER_PLAYED (22),
    BUMPER_CLICKED (23),
    PREROLL_STARTED (24),
    MIDROLL_STARTED (25),
    POSTROLL_STARTED (26),
    OVERLAY_STARTED (27),
    PREROLL_CLICKED (28),
    MIDROLL_CLICKED (29),
    POSTROLL_CLICKED (30),
    OVERLAY_CLICKED (31),
    PREROLL_25 (32),
    PREROLL_50 (33),
    PREROLL_75 (34),
    MIDROLL_25 (35),
    MIDROLL_50 (36),
    MIDROLL_75 (37),
    POSTROLL_25 (38),
    POSTROLL_50 (39),
    POSTROLL_75 (40);

    public int hashCode;

    KalturaStatsEventType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaStatsEventType get(int hashCode) {
        switch(hashCode) {
            case 1: return WIDGET_LOADED;
            case 2: return MEDIA_LOADED;
            case 3: return PLAY;
            case 4: return PLAY_REACHED_25;
            case 5: return PLAY_REACHED_50;
            case 6: return PLAY_REACHED_75;
            case 7: return PLAY_REACHED_100;
            case 8: return OPEN_EDIT;
            case 9: return OPEN_VIRAL;
            case 10: return OPEN_DOWNLOAD;
            case 11: return OPEN_REPORT;
            case 12: return BUFFER_START;
            case 13: return BUFFER_END;
            case 14: return OPEN_FULL_SCREEN;
            case 15: return CLOSE_FULL_SCREEN;
            case 16: return REPLAY;
            case 17: return SEEK;
            case 18: return OPEN_UPLOAD;
            case 19: return SAVE_PUBLISH;
            case 20: return CLOSE_EDITOR;
            case 21: return PRE_BUMPER_PLAYED;
            case 22: return POST_BUMPER_PLAYED;
            case 23: return BUMPER_CLICKED;
            case 24: return PREROLL_STARTED;
            case 25: return MIDROLL_STARTED;
            case 26: return POSTROLL_STARTED;
            case 27: return OVERLAY_STARTED;
            case 28: return PREROLL_CLICKED;
            case 29: return MIDROLL_CLICKED;
            case 30: return POSTROLL_CLICKED;
            case 31: return OVERLAY_CLICKED;
            case 32: return PREROLL_25;
            case 33: return PREROLL_50;
            case 34: return PREROLL_75;
            case 35: return MIDROLL_25;
            case 36: return MIDROLL_50;
            case 37: return MIDROLL_75;
            case 38: return POSTROLL_25;
            case 39: return POSTROLL_50;
            case 40: return POSTROLL_75;
            default: return WIDGET_LOADED;
        }
    }
}
