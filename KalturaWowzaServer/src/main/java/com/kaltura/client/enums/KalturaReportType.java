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
public enum KalturaReportType implements KalturaEnumAsString {
    QUIZ ("quiz.QUIZ"),
    QUIZ_AGGREGATE_BY_QUESTION ("quiz.QUIZ_AGGREGATE_BY_QUESTION"),
    QUIZ_USER_AGGREGATE_BY_QUESTION ("quiz.QUIZ_USER_AGGREGATE_BY_QUESTION"),
    QUIZ_USER_PERCENTAGE ("quiz.QUIZ_USER_PERCENTAGE"),
    TOP_CONTENT ("1"),
    CONTENT_DROPOFF ("2"),
    CONTENT_INTERACTIONS ("3"),
    MAP_OVERLAY ("4"),
    TOP_CONTRIBUTORS ("5"),
    TOP_SYNDICATION ("6"),
    CONTENT_CONTRIBUTIONS ("7"),
    USER_ENGAGEMENT ("11"),
    SPEFICIC_USER_ENGAGEMENT ("12"),
    USER_TOP_CONTENT ("13"),
    USER_CONTENT_DROPOFF ("14"),
    USER_CONTENT_INTERACTIONS ("15"),
    APPLICATIONS ("16"),
    USER_USAGE ("17"),
    SPECIFIC_USER_USAGE ("18"),
    VAR_USAGE ("19"),
    TOP_CREATORS ("20"),
    PLATFORMS ("21"),
    OPERATION_SYSTEM ("22"),
    BROWSERS ("23"),
    LIVE ("24"),
    TOP_PLAYBACK_CONTEXT ("25"),
    VPAAS_USAGE ("26"),
    PARTNER_USAGE ("201");

    public String hashCode;

    KalturaReportType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaReportType get(String hashCode) {
        if (hashCode.equals("quiz.QUIZ"))
        {
           return QUIZ;
        }
        else 
        if (hashCode.equals("quiz.QUIZ_AGGREGATE_BY_QUESTION"))
        {
           return QUIZ_AGGREGATE_BY_QUESTION;
        }
        else 
        if (hashCode.equals("quiz.QUIZ_USER_AGGREGATE_BY_QUESTION"))
        {
           return QUIZ_USER_AGGREGATE_BY_QUESTION;
        }
        else 
        if (hashCode.equals("quiz.QUIZ_USER_PERCENTAGE"))
        {
           return QUIZ_USER_PERCENTAGE;
        }
        else 
        if (hashCode.equals("1"))
        {
           return TOP_CONTENT;
        }
        else 
        if (hashCode.equals("2"))
        {
           return CONTENT_DROPOFF;
        }
        else 
        if (hashCode.equals("3"))
        {
           return CONTENT_INTERACTIONS;
        }
        else 
        if (hashCode.equals("4"))
        {
           return MAP_OVERLAY;
        }
        else 
        if (hashCode.equals("5"))
        {
           return TOP_CONTRIBUTORS;
        }
        else 
        if (hashCode.equals("6"))
        {
           return TOP_SYNDICATION;
        }
        else 
        if (hashCode.equals("7"))
        {
           return CONTENT_CONTRIBUTIONS;
        }
        else 
        if (hashCode.equals("11"))
        {
           return USER_ENGAGEMENT;
        }
        else 
        if (hashCode.equals("12"))
        {
           return SPEFICIC_USER_ENGAGEMENT;
        }
        else 
        if (hashCode.equals("13"))
        {
           return USER_TOP_CONTENT;
        }
        else 
        if (hashCode.equals("14"))
        {
           return USER_CONTENT_DROPOFF;
        }
        else 
        if (hashCode.equals("15"))
        {
           return USER_CONTENT_INTERACTIONS;
        }
        else 
        if (hashCode.equals("16"))
        {
           return APPLICATIONS;
        }
        else 
        if (hashCode.equals("17"))
        {
           return USER_USAGE;
        }
        else 
        if (hashCode.equals("18"))
        {
           return SPECIFIC_USER_USAGE;
        }
        else 
        if (hashCode.equals("19"))
        {
           return VAR_USAGE;
        }
        else 
        if (hashCode.equals("20"))
        {
           return TOP_CREATORS;
        }
        else 
        if (hashCode.equals("21"))
        {
           return PLATFORMS;
        }
        else 
        if (hashCode.equals("22"))
        {
           return OPERATION_SYSTEM;
        }
        else 
        if (hashCode.equals("23"))
        {
           return BROWSERS;
        }
        else 
        if (hashCode.equals("24"))
        {
           return LIVE;
        }
        else 
        if (hashCode.equals("25"))
        {
           return TOP_PLAYBACK_CONTEXT;
        }
        else 
        if (hashCode.equals("26"))
        {
           return VPAAS_USAGE;
        }
        else 
        if (hashCode.equals("201"))
        {
           return PARTNER_USAGE;
        }
        else 
        {
           return QUIZ;
        }
    }
}
