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
public enum KalturaTubeMogulSyndicationFeedCategories implements KalturaEnumAsString {
    ANIMALS_AND_PETS ("Animals &amp; Pets"),
    ARTS_AND_ANIMATION ("Arts &amp; Animation"),
    AUTOS ("Autos"),
    COMEDY ("Comedy"),
    COMMERCIALS_PROMOTIONAL ("Commercials/Promotional"),
    ENTERTAINMENT ("Entertainment"),
    FAMILY_AND_KIDS ("Family &amp; Kids"),
    HOW_TO_INSTRUCTIONAL_DIY ("How To/Instructional/DIY"),
    MUSIC ("Music"),
    NEWS_AND_BLOGS ("News &amp; Blogs"),
    SCIENCE_AND_TECHNOLOGY ("Science &amp; Technology"),
    SPORTS ("Sports"),
    TRAVEL_AND_PLACES ("Travel &amp; Places"),
    VIDEO_GAMES ("Video Games"),
    VLOGS_PEOPLE ("Vlogs &amp; People");

    public String hashCode;

    KalturaTubeMogulSyndicationFeedCategories(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaTubeMogulSyndicationFeedCategories get(String hashCode) {
        if (hashCode.equals("Animals &amp; Pets"))
        {
           return ANIMALS_AND_PETS;
        }
        else 
        if (hashCode.equals("Arts &amp; Animation"))
        {
           return ARTS_AND_ANIMATION;
        }
        else 
        if (hashCode.equals("Autos"))
        {
           return AUTOS;
        }
        else 
        if (hashCode.equals("Comedy"))
        {
           return COMEDY;
        }
        else 
        if (hashCode.equals("Commercials/Promotional"))
        {
           return COMMERCIALS_PROMOTIONAL;
        }
        else 
        if (hashCode.equals("Entertainment"))
        {
           return ENTERTAINMENT;
        }
        else 
        if (hashCode.equals("Family &amp; Kids"))
        {
           return FAMILY_AND_KIDS;
        }
        else 
        if (hashCode.equals("How To/Instructional/DIY"))
        {
           return HOW_TO_INSTRUCTIONAL_DIY;
        }
        else 
        if (hashCode.equals("Music"))
        {
           return MUSIC;
        }
        else 
        if (hashCode.equals("News &amp; Blogs"))
        {
           return NEWS_AND_BLOGS;
        }
        else 
        if (hashCode.equals("Science &amp; Technology"))
        {
           return SCIENCE_AND_TECHNOLOGY;
        }
        else 
        if (hashCode.equals("Sports"))
        {
           return SPORTS;
        }
        else 
        if (hashCode.equals("Travel &amp; Places"))
        {
           return TRAVEL_AND_PLACES;
        }
        else 
        if (hashCode.equals("Video Games"))
        {
           return VIDEO_GAMES;
        }
        else 
        if (hashCode.equals("Vlogs &amp; People"))
        {
           return VLOGS_PEOPLE;
        }
        else 
        {
           return ANIMALS_AND_PETS;
        }
    }
}
