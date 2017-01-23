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
public enum KalturaSchedulerStatusType implements KalturaEnumAsInt {
    RUNNING_BATCHES_COUNT (1),
    RUNNING_BATCHES_CPU (2),
    RUNNING_BATCHES_MEMORY (3),
    RUNNING_BATCHES_NETWORK (4),
    RUNNING_BATCHES_DISC_IO (5),
    RUNNING_BATCHES_DISC_SPACE (6),
    RUNNING_BATCHES_IS_RUNNING (7);

    public int hashCode;

    KalturaSchedulerStatusType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaSchedulerStatusType get(int hashCode) {
        switch(hashCode) {
            case 1: return RUNNING_BATCHES_COUNT;
            case 2: return RUNNING_BATCHES_CPU;
            case 3: return RUNNING_BATCHES_MEMORY;
            case 4: return RUNNING_BATCHES_NETWORK;
            case 5: return RUNNING_BATCHES_DISC_IO;
            case 6: return RUNNING_BATCHES_DISC_SPACE;
            case 7: return RUNNING_BATCHES_IS_RUNNING;
            default: return RUNNING_BATCHES_COUNT;
        }
    }
}
