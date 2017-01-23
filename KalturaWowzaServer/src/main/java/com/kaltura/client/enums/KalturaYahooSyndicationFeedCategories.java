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
public enum KalturaYahooSyndicationFeedCategories implements KalturaEnumAsString {
    ACTION ("Action"),
    ANIMALS ("Animals"),
    ART_AND_ANIMATION ("Art &amp; Animation"),
    COMMERCIALS ("Commercials"),
    ENTERTAINMENT_AND_TV ("Entertainment &amp; TV"),
    FAMILY ("Family"),
    FOOD ("Food"),
    FUNNY_VIDEOS ("Funny Videos"),
    GAMES ("Games"),
    HEALTH_AND_BEAUTY ("Health &amp; Beauty"),
    HOW_TO ("How-To"),
    MOVIES_AND_SHORTS ("Movies &amp; Shorts"),
    MUSIC ("Music"),
    NEWS_AND_POLITICS ("News &amp; Politics"),
    PEOPLE_AND_VLOGS ("People &amp; Vlogs"),
    PRODUCTS_AND_TECH ("Products &amp; Tech."),
    SCIENCE_AND_ENVIRONMENT ("Science &amp; Environment"),
    SPORTS ("Sports"),
    TRANSPORTATION ("Transportation"),
    TRAVEL ("Travel");

    public String hashCode;

    KalturaYahooSyndicationFeedCategories(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaYahooSyndicationFeedCategories get(String hashCode) {
        if (hashCode.equals("Action"))
        {
           return ACTION;
        }
        else 
        if (hashCode.equals("Animals"))
        {
           return ANIMALS;
        }
        else 
        if (hashCode.equals("Art &amp; Animation"))
        {
           return ART_AND_ANIMATION;
        }
        else 
        if (hashCode.equals("Commercials"))
        {
           return COMMERCIALS;
        }
        else 
        if (hashCode.equals("Entertainment &amp; TV"))
        {
           return ENTERTAINMENT_AND_TV;
        }
        else 
        if (hashCode.equals("Family"))
        {
           return FAMILY;
        }
        else 
        if (hashCode.equals("Food"))
        {
           return FOOD;
        }
        else 
        if (hashCode.equals("Funny Videos"))
        {
           return FUNNY_VIDEOS;
        }
        else 
        if (hashCode.equals("Games"))
        {
           return GAMES;
        }
        else 
        if (hashCode.equals("Health &amp; Beauty"))
        {
           return HEALTH_AND_BEAUTY;
        }
        else 
        if (hashCode.equals("How-To"))
        {
           return HOW_TO;
        }
        else 
        if (hashCode.equals("Movies &amp; Shorts"))
        {
           return MOVIES_AND_SHORTS;
        }
        else 
        if (hashCode.equals("Music"))
        {
           return MUSIC;
        }
        else 
        if (hashCode.equals("News &amp; Politics"))
        {
           return NEWS_AND_POLITICS;
        }
        else 
        if (hashCode.equals("People &amp; Vlogs"))
        {
           return PEOPLE_AND_VLOGS;
        }
        else 
        if (hashCode.equals("Products &amp; Tech."))
        {
           return PRODUCTS_AND_TECH;
        }
        else 
        if (hashCode.equals("Science &amp; Environment"))
        {
           return SCIENCE_AND_ENVIRONMENT;
        }
        else 
        if (hashCode.equals("Sports"))
        {
           return SPORTS;
        }
        else 
        if (hashCode.equals("Transportation"))
        {
           return TRANSPORTATION;
        }
        else 
        if (hashCode.equals("Travel"))
        {
           return TRAVEL;
        }
        else 
        {
           return ACTION;
        }
    }
}
