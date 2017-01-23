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
public enum KalturaBatchJobStatus implements KalturaEnumAsInt {
    PENDING (0),
    QUEUED (1),
    PROCESSING (2),
    PROCESSED (3),
    MOVEFILE (4),
    FINISHED (5),
    FAILED (6),
    ABORTED (7),
    ALMOST_DONE (8),
    RETRY (9),
    FATAL (10),
    DONT_PROCESS (11),
    FINISHED_PARTIALLY (12);

    public int hashCode;

    KalturaBatchJobStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaBatchJobStatus get(int hashCode) {
        switch(hashCode) {
            case 0: return PENDING;
            case 1: return QUEUED;
            case 2: return PROCESSING;
            case 3: return PROCESSED;
            case 4: return MOVEFILE;
            case 5: return FINISHED;
            case 6: return FAILED;
            case 7: return ABORTED;
            case 8: return ALMOST_DONE;
            case 9: return RETRY;
            case 10: return FATAL;
            case 11: return DONT_PROCESS;
            case 12: return FINISHED_PARTIALLY;
            default: return PENDING;
        }
    }
}
