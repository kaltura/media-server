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
public enum KalturaPlayReadyAnalogVideoOPId implements KalturaEnumAsString {
    EXPLICIT_ANALOG_TV ("2098DE8D-7DDD-4BAB-96C6-32EBB6FABEA3"),
    BEST_EFFORT_EXPLICIT_ANALOG_TV ("225CD36F-F132-49EF-BA8C-C91EA28E4369"),
    IMAGE_CONSTRAINT_VIDEO ("811C5110-46C8-4C6E-8163-C0482A15D47E"),
    AGC_AND_COLOR_STRIPE ("C3FD11C6-F8B7-4D20-B008-1DB17D61F2DA"),
    IMAGE_CONSTRAINT_MONITOR ("D783A191-E083-4BAF-B2DA-E69F910B3772");

    public String hashCode;

    KalturaPlayReadyAnalogVideoOPId(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaPlayReadyAnalogVideoOPId get(String hashCode) {
        if (hashCode.equals("2098DE8D-7DDD-4BAB-96C6-32EBB6FABEA3"))
        {
           return EXPLICIT_ANALOG_TV;
        }
        else 
        if (hashCode.equals("225CD36F-F132-49EF-BA8C-C91EA28E4369"))
        {
           return BEST_EFFORT_EXPLICIT_ANALOG_TV;
        }
        else 
        if (hashCode.equals("811C5110-46C8-4C6E-8163-C0482A15D47E"))
        {
           return IMAGE_CONSTRAINT_VIDEO;
        }
        else 
        if (hashCode.equals("C3FD11C6-F8B7-4D20-B008-1DB17D61F2DA"))
        {
           return AGC_AND_COLOR_STRIPE;
        }
        else 
        if (hashCode.equals("D783A191-E083-4BAF-B2DA-E69F910B3772"))
        {
           return IMAGE_CONSTRAINT_MONITOR;
        }
        else 
        {
           return EXPLICIT_ANALOG_TV;
        }
    }
}
