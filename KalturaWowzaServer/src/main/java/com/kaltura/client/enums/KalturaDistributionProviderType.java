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
public enum KalturaDistributionProviderType implements KalturaEnumAsString {
    ATT_UVERSE ("attUverseDistribution.ATT_UVERSE"),
    AVN ("avnDistribution.AVN"),
    COMCAST_MRSS ("comcastMrssDistribution.COMCAST_MRSS"),
    CROSS_KALTURA ("crossKalturaDistribution.CROSS_KALTURA"),
    DAILYMOTION ("dailymotionDistribution.DAILYMOTION"),
    DOUBLECLICK ("doubleClickDistribution.DOUBLECLICK"),
    FACEBOOK ("facebookDistribution.FACEBOOK"),
    FREEWHEEL ("freewheelDistribution.FREEWHEEL"),
    FREEWHEEL_GENERIC ("freewheelGenericDistribution.FREEWHEEL_GENERIC"),
    FTP ("ftpDistribution.FTP"),
    FTP_SCHEDULED ("ftpDistribution.FTP_SCHEDULED"),
    HULU ("huluDistribution.HULU"),
    IDETIC ("ideticDistribution.IDETIC"),
    METRO_PCS ("metroPcsDistribution.METRO_PCS"),
    MSN ("msnDistribution.MSN"),
    NDN ("ndnDistribution.NDN"),
    PODCAST ("podcastDistribution.PODCAST"),
    PUSH_TO_NEWS ("pushToNewsDistribution.PUSH_TO_NEWS"),
    QUICKPLAY ("quickPlayDistribution.QUICKPLAY"),
    SYNACOR_HBO ("synacorHboDistribution.SYNACOR_HBO"),
    TIME_WARNER ("timeWarnerDistribution.TIME_WARNER"),
    TVCOM ("tvComDistribution.TVCOM"),
    TVINCI ("tvinciDistribution.TVINCI"),
    UNICORN ("unicornDistribution.UNICORN"),
    UVERSE_CLICK_TO_ORDER ("uverseClickToOrderDistribution.UVERSE_CLICK_TO_ORDER"),
    UVERSE ("uverseDistribution.UVERSE"),
    VERIZON_VCAST ("verizonVcastDistribution.VERIZON_VCAST"),
    YAHOO ("yahooDistribution.YAHOO"),
    YOUTUBE ("youTubeDistribution.YOUTUBE"),
    YOUTUBE_API ("youtubeApiDistribution.YOUTUBE_API"),
    GENERIC ("1"),
    SYNDICATION ("2");

    public String hashCode;

    KalturaDistributionProviderType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaDistributionProviderType get(String hashCode) {
        if (hashCode.equals("attUverseDistribution.ATT_UVERSE"))
        {
           return ATT_UVERSE;
        }
        else 
        if (hashCode.equals("avnDistribution.AVN"))
        {
           return AVN;
        }
        else 
        if (hashCode.equals("comcastMrssDistribution.COMCAST_MRSS"))
        {
           return COMCAST_MRSS;
        }
        else 
        if (hashCode.equals("crossKalturaDistribution.CROSS_KALTURA"))
        {
           return CROSS_KALTURA;
        }
        else 
        if (hashCode.equals("dailymotionDistribution.DAILYMOTION"))
        {
           return DAILYMOTION;
        }
        else 
        if (hashCode.equals("doubleClickDistribution.DOUBLECLICK"))
        {
           return DOUBLECLICK;
        }
        else 
        if (hashCode.equals("facebookDistribution.FACEBOOK"))
        {
           return FACEBOOK;
        }
        else 
        if (hashCode.equals("freewheelDistribution.FREEWHEEL"))
        {
           return FREEWHEEL;
        }
        else 
        if (hashCode.equals("freewheelGenericDistribution.FREEWHEEL_GENERIC"))
        {
           return FREEWHEEL_GENERIC;
        }
        else 
        if (hashCode.equals("ftpDistribution.FTP"))
        {
           return FTP;
        }
        else 
        if (hashCode.equals("ftpDistribution.FTP_SCHEDULED"))
        {
           return FTP_SCHEDULED;
        }
        else 
        if (hashCode.equals("huluDistribution.HULU"))
        {
           return HULU;
        }
        else 
        if (hashCode.equals("ideticDistribution.IDETIC"))
        {
           return IDETIC;
        }
        else 
        if (hashCode.equals("metroPcsDistribution.METRO_PCS"))
        {
           return METRO_PCS;
        }
        else 
        if (hashCode.equals("msnDistribution.MSN"))
        {
           return MSN;
        }
        else 
        if (hashCode.equals("ndnDistribution.NDN"))
        {
           return NDN;
        }
        else 
        if (hashCode.equals("podcastDistribution.PODCAST"))
        {
           return PODCAST;
        }
        else 
        if (hashCode.equals("pushToNewsDistribution.PUSH_TO_NEWS"))
        {
           return PUSH_TO_NEWS;
        }
        else 
        if (hashCode.equals("quickPlayDistribution.QUICKPLAY"))
        {
           return QUICKPLAY;
        }
        else 
        if (hashCode.equals("synacorHboDistribution.SYNACOR_HBO"))
        {
           return SYNACOR_HBO;
        }
        else 
        if (hashCode.equals("timeWarnerDistribution.TIME_WARNER"))
        {
           return TIME_WARNER;
        }
        else 
        if (hashCode.equals("tvComDistribution.TVCOM"))
        {
           return TVCOM;
        }
        else 
        if (hashCode.equals("tvinciDistribution.TVINCI"))
        {
           return TVINCI;
        }
        else 
        if (hashCode.equals("unicornDistribution.UNICORN"))
        {
           return UNICORN;
        }
        else 
        if (hashCode.equals("uverseClickToOrderDistribution.UVERSE_CLICK_TO_ORDER"))
        {
           return UVERSE_CLICK_TO_ORDER;
        }
        else 
        if (hashCode.equals("uverseDistribution.UVERSE"))
        {
           return UVERSE;
        }
        else 
        if (hashCode.equals("verizonVcastDistribution.VERIZON_VCAST"))
        {
           return VERIZON_VCAST;
        }
        else 
        if (hashCode.equals("yahooDistribution.YAHOO"))
        {
           return YAHOO;
        }
        else 
        if (hashCode.equals("youTubeDistribution.YOUTUBE"))
        {
           return YOUTUBE;
        }
        else 
        if (hashCode.equals("youtubeApiDistribution.YOUTUBE_API"))
        {
           return YOUTUBE_API;
        }
        else 
        if (hashCode.equals("1"))
        {
           return GENERIC;
        }
        else 
        if (hashCode.equals("2"))
        {
           return SYNDICATION;
        }
        else 
        {
           return ATT_UVERSE;
        }
    }
}
