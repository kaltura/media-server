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
public enum KalturaITunesSyndicationFeedCategories implements KalturaEnumAsString {
    ARTS ("Arts"),
    ARTS_DESIGN ("Arts/Design"),
    ARTS_FASHION_BEAUTY ("Arts/Fashion &amp; Beauty"),
    ARTS_FOOD ("Arts/Food"),
    ARTS_LITERATURE ("Arts/Literature"),
    ARTS_PERFORMING_ARTS ("Arts/Performing Arts"),
    ARTS_VISUAL_ARTS ("Arts/Visual Arts"),
    BUSINESS ("Business"),
    BUSINESS_BUSINESS_NEWS ("Business/Business News"),
    BUSINESS_CAREERS ("Business/Careers"),
    BUSINESS_INVESTING ("Business/Investing"),
    BUSINESS_MANAGEMENT_MARKETING ("Business/Management &amp; Marketing"),
    BUSINESS_SHOPPING ("Business/Shopping"),
    COMEDY ("Comedy"),
    EDUCATION ("Education"),
    EDUCATION_TECHNOLOGY ("Education/Education Technology"),
    EDUCATION_HIGHER_EDUCATION ("Education/Higher Education"),
    EDUCATION_K_12 ("Education/K-12"),
    EDUCATION_LANGUAGE_COURSES ("Education/Language Courses"),
    EDUCATION_TRAINING ("Education/Training"),
    GAMES_HOBBIES ("Games &amp; Hobbies"),
    GAMES_HOBBIES_AUTOMOTIVE ("Games &amp; Hobbies/Automotive"),
    GAMES_HOBBIES_AVIATION ("Games &amp; Hobbies/Aviation"),
    GAMES_HOBBIES_HOBBIES ("Games &amp; Hobbies/Hobbies"),
    GAMES_HOBBIES_OTHER_GAMES ("Games &amp; Hobbies/Other Games"),
    GAMES_HOBBIES_VIDEO_GAMES ("Games &amp; Hobbies/Video Games"),
    GOVERNMENT_ORGANIZATIONS ("Government &amp; Organizations"),
    GOVERNMENT_ORGANIZATIONS_LOCAL ("Government &amp; Organizations/Local"),
    GOVERNMENT_ORGANIZATIONS_NATIONAL ("Government &amp; Organizations/National"),
    GOVERNMENT_ORGANIZATIONS_NON_PROFIT ("Government &amp; Organizations/Non-Profit"),
    GOVERNMENT_ORGANIZATIONS_REGIONAL ("Government &amp; Organizations/Regional"),
    HEALTH ("Health"),
    HEALTH_ALTERNATIVE_HEALTH ("Health/Alternative Health"),
    HEALTH_FITNESS_NUTRITION ("Health/Fitness &amp; Nutrition"),
    HEALTH_SELF_HELP ("Health/Self-Help"),
    HEALTH_SEXUALITY ("Health/Sexuality"),
    KIDS_FAMILY ("Kids &amp; Family"),
    MUSIC ("Music"),
    NEWS_POLITICS ("News &amp; Politics"),
    RELIGION_SPIRITUALITY ("Religion &amp; Spirituality"),
    RELIGION_SPIRITUALITY_BUDDHISM ("Religion &amp; Spirituality/Buddhism"),
    RELIGION_SPIRITUALITY_CHRISTIANITY ("Religion &amp; Spirituality/Christianity"),
    RELIGION_SPIRITUALITY_HINDUISM ("Religion &amp; Spirituality/Hinduism"),
    RELIGION_SPIRITUALITY_ISLAM ("Religion &amp; Spirituality/Islam"),
    RELIGION_SPIRITUALITY_JUDAISM ("Religion &amp; Spirituality/Judaism"),
    RELIGION_SPIRITUALITY_OTHER ("Religion &amp; Spirituality/Other"),
    RELIGION_SPIRITUALITY_SPIRITUALITY ("Religion &amp; Spirituality/Spirituality"),
    SCIENCE_MEDICINE ("Science &amp; Medicine"),
    SCIENCE_MEDICINE_MEDICINE ("Science &amp; Medicine/Medicine"),
    SCIENCE_MEDICINE_NATURAL_SCIENCES ("Science &amp; Medicine/Natural Sciences"),
    SCIENCE_MEDICINE_SOCIAL_SCIENCES ("Science &amp; Medicine/Social Sciences"),
    SOCIETY_CULTURE ("Society &amp; Culture"),
    SOCIETY_CULTURE_HISTORY ("Society &amp; Culture/History"),
    SOCIETY_CULTURE_PERSONAL_JOURNALS ("Society &amp; Culture/Personal Journals"),
    SOCIETY_CULTURE_PHILOSOPHY ("Society &amp; Culture/Philosophy"),
    SOCIETY_CULTURE_PLACES_TRAVEL ("Society &amp; Culture/Places &amp; Travel"),
    SPORTS_RECREATION ("Sports &amp; Recreation"),
    SPORTS_RECREATION_AMATEUR ("Sports &amp; Recreation/Amateur"),
    SPORTS_RECREATION_COLLEGE_HIGH_SCHOOL ("Sports &amp; Recreation/College &amp; High School"),
    SPORTS_RECREATION_OUTDOOR ("Sports &amp; Recreation/Outdoor"),
    SPORTS_RECREATION_PROFESSIONAL ("Sports &amp; Recreation/Professional"),
    TV_FILM ("TV &amp; Film"),
    TECHNOLOGY ("Technology"),
    TECHNOLOGY_GADGETS ("Technology/Gadgets"),
    TECHNOLOGY_PODCASTING ("Technology/Podcasting"),
    TECHNOLOGY_SOFTWARE_HOW_TO ("Technology/Software How-To"),
    TECHNOLOGY_TECH_NEWS ("Technology/Tech News");

    public String hashCode;

    KalturaITunesSyndicationFeedCategories(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaITunesSyndicationFeedCategories get(String hashCode) {
        if (hashCode.equals("Arts"))
        {
           return ARTS;
        }
        else 
        if (hashCode.equals("Arts/Design"))
        {
           return ARTS_DESIGN;
        }
        else 
        if (hashCode.equals("Arts/Fashion &amp; Beauty"))
        {
           return ARTS_FASHION_BEAUTY;
        }
        else 
        if (hashCode.equals("Arts/Food"))
        {
           return ARTS_FOOD;
        }
        else 
        if (hashCode.equals("Arts/Literature"))
        {
           return ARTS_LITERATURE;
        }
        else 
        if (hashCode.equals("Arts/Performing Arts"))
        {
           return ARTS_PERFORMING_ARTS;
        }
        else 
        if (hashCode.equals("Arts/Visual Arts"))
        {
           return ARTS_VISUAL_ARTS;
        }
        else 
        if (hashCode.equals("Business"))
        {
           return BUSINESS;
        }
        else 
        if (hashCode.equals("Business/Business News"))
        {
           return BUSINESS_BUSINESS_NEWS;
        }
        else 
        if (hashCode.equals("Business/Careers"))
        {
           return BUSINESS_CAREERS;
        }
        else 
        if (hashCode.equals("Business/Investing"))
        {
           return BUSINESS_INVESTING;
        }
        else 
        if (hashCode.equals("Business/Management &amp; Marketing"))
        {
           return BUSINESS_MANAGEMENT_MARKETING;
        }
        else 
        if (hashCode.equals("Business/Shopping"))
        {
           return BUSINESS_SHOPPING;
        }
        else 
        if (hashCode.equals("Comedy"))
        {
           return COMEDY;
        }
        else 
        if (hashCode.equals("Education"))
        {
           return EDUCATION;
        }
        else 
        if (hashCode.equals("Education/Education Technology"))
        {
           return EDUCATION_TECHNOLOGY;
        }
        else 
        if (hashCode.equals("Education/Higher Education"))
        {
           return EDUCATION_HIGHER_EDUCATION;
        }
        else 
        if (hashCode.equals("Education/K-12"))
        {
           return EDUCATION_K_12;
        }
        else 
        if (hashCode.equals("Education/Language Courses"))
        {
           return EDUCATION_LANGUAGE_COURSES;
        }
        else 
        if (hashCode.equals("Education/Training"))
        {
           return EDUCATION_TRAINING;
        }
        else 
        if (hashCode.equals("Games &amp; Hobbies"))
        {
           return GAMES_HOBBIES;
        }
        else 
        if (hashCode.equals("Games &amp; Hobbies/Automotive"))
        {
           return GAMES_HOBBIES_AUTOMOTIVE;
        }
        else 
        if (hashCode.equals("Games &amp; Hobbies/Aviation"))
        {
           return GAMES_HOBBIES_AVIATION;
        }
        else 
        if (hashCode.equals("Games &amp; Hobbies/Hobbies"))
        {
           return GAMES_HOBBIES_HOBBIES;
        }
        else 
        if (hashCode.equals("Games &amp; Hobbies/Other Games"))
        {
           return GAMES_HOBBIES_OTHER_GAMES;
        }
        else 
        if (hashCode.equals("Games &amp; Hobbies/Video Games"))
        {
           return GAMES_HOBBIES_VIDEO_GAMES;
        }
        else 
        if (hashCode.equals("Government &amp; Organizations"))
        {
           return GOVERNMENT_ORGANIZATIONS;
        }
        else 
        if (hashCode.equals("Government &amp; Organizations/Local"))
        {
           return GOVERNMENT_ORGANIZATIONS_LOCAL;
        }
        else 
        if (hashCode.equals("Government &amp; Organizations/National"))
        {
           return GOVERNMENT_ORGANIZATIONS_NATIONAL;
        }
        else 
        if (hashCode.equals("Government &amp; Organizations/Non-Profit"))
        {
           return GOVERNMENT_ORGANIZATIONS_NON_PROFIT;
        }
        else 
        if (hashCode.equals("Government &amp; Organizations/Regional"))
        {
           return GOVERNMENT_ORGANIZATIONS_REGIONAL;
        }
        else 
        if (hashCode.equals("Health"))
        {
           return HEALTH;
        }
        else 
        if (hashCode.equals("Health/Alternative Health"))
        {
           return HEALTH_ALTERNATIVE_HEALTH;
        }
        else 
        if (hashCode.equals("Health/Fitness &amp; Nutrition"))
        {
           return HEALTH_FITNESS_NUTRITION;
        }
        else 
        if (hashCode.equals("Health/Self-Help"))
        {
           return HEALTH_SELF_HELP;
        }
        else 
        if (hashCode.equals("Health/Sexuality"))
        {
           return HEALTH_SEXUALITY;
        }
        else 
        if (hashCode.equals("Kids &amp; Family"))
        {
           return KIDS_FAMILY;
        }
        else 
        if (hashCode.equals("Music"))
        {
           return MUSIC;
        }
        else 
        if (hashCode.equals("News &amp; Politics"))
        {
           return NEWS_POLITICS;
        }
        else 
        if (hashCode.equals("Religion &amp; Spirituality"))
        {
           return RELIGION_SPIRITUALITY;
        }
        else 
        if (hashCode.equals("Religion &amp; Spirituality/Buddhism"))
        {
           return RELIGION_SPIRITUALITY_BUDDHISM;
        }
        else 
        if (hashCode.equals("Religion &amp; Spirituality/Christianity"))
        {
           return RELIGION_SPIRITUALITY_CHRISTIANITY;
        }
        else 
        if (hashCode.equals("Religion &amp; Spirituality/Hinduism"))
        {
           return RELIGION_SPIRITUALITY_HINDUISM;
        }
        else 
        if (hashCode.equals("Religion &amp; Spirituality/Islam"))
        {
           return RELIGION_SPIRITUALITY_ISLAM;
        }
        else 
        if (hashCode.equals("Religion &amp; Spirituality/Judaism"))
        {
           return RELIGION_SPIRITUALITY_JUDAISM;
        }
        else 
        if (hashCode.equals("Religion &amp; Spirituality/Other"))
        {
           return RELIGION_SPIRITUALITY_OTHER;
        }
        else 
        if (hashCode.equals("Religion &amp; Spirituality/Spirituality"))
        {
           return RELIGION_SPIRITUALITY_SPIRITUALITY;
        }
        else 
        if (hashCode.equals("Science &amp; Medicine"))
        {
           return SCIENCE_MEDICINE;
        }
        else 
        if (hashCode.equals("Science &amp; Medicine/Medicine"))
        {
           return SCIENCE_MEDICINE_MEDICINE;
        }
        else 
        if (hashCode.equals("Science &amp; Medicine/Natural Sciences"))
        {
           return SCIENCE_MEDICINE_NATURAL_SCIENCES;
        }
        else 
        if (hashCode.equals("Science &amp; Medicine/Social Sciences"))
        {
           return SCIENCE_MEDICINE_SOCIAL_SCIENCES;
        }
        else 
        if (hashCode.equals("Society &amp; Culture"))
        {
           return SOCIETY_CULTURE;
        }
        else 
        if (hashCode.equals("Society &amp; Culture/History"))
        {
           return SOCIETY_CULTURE_HISTORY;
        }
        else 
        if (hashCode.equals("Society &amp; Culture/Personal Journals"))
        {
           return SOCIETY_CULTURE_PERSONAL_JOURNALS;
        }
        else 
        if (hashCode.equals("Society &amp; Culture/Philosophy"))
        {
           return SOCIETY_CULTURE_PHILOSOPHY;
        }
        else 
        if (hashCode.equals("Society &amp; Culture/Places &amp; Travel"))
        {
           return SOCIETY_CULTURE_PLACES_TRAVEL;
        }
        else 
        if (hashCode.equals("Sports &amp; Recreation"))
        {
           return SPORTS_RECREATION;
        }
        else 
        if (hashCode.equals("Sports &amp; Recreation/Amateur"))
        {
           return SPORTS_RECREATION_AMATEUR;
        }
        else 
        if (hashCode.equals("Sports &amp; Recreation/College &amp; High School"))
        {
           return SPORTS_RECREATION_COLLEGE_HIGH_SCHOOL;
        }
        else 
        if (hashCode.equals("Sports &amp; Recreation/Outdoor"))
        {
           return SPORTS_RECREATION_OUTDOOR;
        }
        else 
        if (hashCode.equals("Sports &amp; Recreation/Professional"))
        {
           return SPORTS_RECREATION_PROFESSIONAL;
        }
        else 
        if (hashCode.equals("TV &amp; Film"))
        {
           return TV_FILM;
        }
        else 
        if (hashCode.equals("Technology"))
        {
           return TECHNOLOGY;
        }
        else 
        if (hashCode.equals("Technology/Gadgets"))
        {
           return TECHNOLOGY_GADGETS;
        }
        else 
        if (hashCode.equals("Technology/Podcasting"))
        {
           return TECHNOLOGY_PODCASTING;
        }
        else 
        if (hashCode.equals("Technology/Software How-To"))
        {
           return TECHNOLOGY_SOFTWARE_HOW_TO;
        }
        else 
        if (hashCode.equals("Technology/Tech News"))
        {
           return TECHNOLOGY_TECH_NEWS;
        }
        else 
        {
           return ARTS;
        }
    }
}
