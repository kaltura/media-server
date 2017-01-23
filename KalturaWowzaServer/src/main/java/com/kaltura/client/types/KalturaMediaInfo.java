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
package com.kaltura.client.types;

import org.w3c.dom.Element;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.enums.KalturaBitRateMode;
import com.kaltura.client.utils.ParseUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * This class was generated using exec.php
 * against an XML schema provided by Kaltura.
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

@SuppressWarnings("serial")
public class KalturaMediaInfo extends KalturaObjectBase {
	/**  The id of the media info  */
    public int id = Integer.MIN_VALUE;
	/**  The id of the related flavor asset  */
    public String flavorAssetId;
	/**  The file size  */
    public int fileSize = Integer.MIN_VALUE;
	/**  The container format  */
    public String containerFormat;
	/**  The container id  */
    public String containerId;
	/**  The container profile  */
    public String containerProfile;
	/**  The container duration  */
    public int containerDuration = Integer.MIN_VALUE;
	/**  The container bit rate  */
    public int containerBitRate = Integer.MIN_VALUE;
	/**  The video format  */
    public String videoFormat;
	/**  The video codec id  */
    public String videoCodecId;
	/**  The video duration  */
    public int videoDuration = Integer.MIN_VALUE;
	/**  The video bit rate  */
    public int videoBitRate = Integer.MIN_VALUE;
	/**  The video bit rate mode  */
    public KalturaBitRateMode videoBitRateMode;
	/**  The video width  */
    public int videoWidth = Integer.MIN_VALUE;
	/**  The video height  */
    public int videoHeight = Integer.MIN_VALUE;
	/**  The video frame rate  */
    public double videoFrameRate = Double.MIN_VALUE;
	/**  The video display aspect ratio (dar)  */
    public double videoDar = Double.MIN_VALUE;
    public int videoRotation = Integer.MIN_VALUE;
	/**  The audio format  */
    public String audioFormat;
	/**  The audio codec id  */
    public String audioCodecId;
	/**  The audio duration  */
    public int audioDuration = Integer.MIN_VALUE;
	/**  The audio bit rate  */
    public int audioBitRate = Integer.MIN_VALUE;
	/**  The audio bit rate mode  */
    public KalturaBitRateMode audioBitRateMode;
	/**  The number of audio channels  */
    public int audioChannels = Integer.MIN_VALUE;
	/**  The audio sampling rate  */
    public int audioSamplingRate = Integer.MIN_VALUE;
	/**  The audio resolution  */
    public int audioResolution = Integer.MIN_VALUE;
	/**  The writing library  */
    public String writingLib;
	/**  The data as returned by the mediainfo command line  */
    public String rawData;
    public String multiStreamInfo;
    public int scanType = Integer.MIN_VALUE;
    public String multiStream;
    public int isFastStart = Integer.MIN_VALUE;
    public String contentStreams;
    public int complexityValue = Integer.MIN_VALUE;

    public KalturaMediaInfo() {
    }

    public KalturaMediaInfo(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("flavorAssetId")) {
                this.flavorAssetId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("fileSize")) {
                this.fileSize = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("containerFormat")) {
                this.containerFormat = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("containerId")) {
                this.containerId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("containerProfile")) {
                this.containerProfile = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("containerDuration")) {
                this.containerDuration = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("containerBitRate")) {
                this.containerBitRate = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("videoFormat")) {
                this.videoFormat = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("videoCodecId")) {
                this.videoCodecId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("videoDuration")) {
                this.videoDuration = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("videoBitRate")) {
                this.videoBitRate = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("videoBitRateMode")) {
                this.videoBitRateMode = KalturaBitRateMode.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("videoWidth")) {
                this.videoWidth = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("videoHeight")) {
                this.videoHeight = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("videoFrameRate")) {
                this.videoFrameRate = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("videoDar")) {
                this.videoDar = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("videoRotation")) {
                this.videoRotation = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("audioFormat")) {
                this.audioFormat = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("audioCodecId")) {
                this.audioCodecId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("audioDuration")) {
                this.audioDuration = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("audioBitRate")) {
                this.audioBitRate = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("audioBitRateMode")) {
                this.audioBitRateMode = KalturaBitRateMode.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("audioChannels")) {
                this.audioChannels = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("audioSamplingRate")) {
                this.audioSamplingRate = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("audioResolution")) {
                this.audioResolution = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("writingLib")) {
                this.writingLib = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("rawData")) {
                this.rawData = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("multiStreamInfo")) {
                this.multiStreamInfo = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("scanType")) {
                this.scanType = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("multiStream")) {
                this.multiStream = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("isFastStart")) {
                this.isFastStart = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("contentStreams")) {
                this.contentStreams = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("complexityValue")) {
                this.complexityValue = ParseUtils.parseInt(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaMediaInfo");
        kparams.add("flavorAssetId", this.flavorAssetId);
        kparams.add("fileSize", this.fileSize);
        kparams.add("containerFormat", this.containerFormat);
        kparams.add("containerId", this.containerId);
        kparams.add("containerProfile", this.containerProfile);
        kparams.add("containerDuration", this.containerDuration);
        kparams.add("containerBitRate", this.containerBitRate);
        kparams.add("videoFormat", this.videoFormat);
        kparams.add("videoCodecId", this.videoCodecId);
        kparams.add("videoDuration", this.videoDuration);
        kparams.add("videoBitRate", this.videoBitRate);
        kparams.add("videoBitRateMode", this.videoBitRateMode);
        kparams.add("videoWidth", this.videoWidth);
        kparams.add("videoHeight", this.videoHeight);
        kparams.add("videoFrameRate", this.videoFrameRate);
        kparams.add("videoDar", this.videoDar);
        kparams.add("videoRotation", this.videoRotation);
        kparams.add("audioFormat", this.audioFormat);
        kparams.add("audioCodecId", this.audioCodecId);
        kparams.add("audioDuration", this.audioDuration);
        kparams.add("audioBitRate", this.audioBitRate);
        kparams.add("audioBitRateMode", this.audioBitRateMode);
        kparams.add("audioChannels", this.audioChannels);
        kparams.add("audioSamplingRate", this.audioSamplingRate);
        kparams.add("audioResolution", this.audioResolution);
        kparams.add("writingLib", this.writingLib);
        kparams.add("rawData", this.rawData);
        kparams.add("multiStreamInfo", this.multiStreamInfo);
        kparams.add("scanType", this.scanType);
        kparams.add("multiStream", this.multiStream);
        kparams.add("isFastStart", this.isFastStart);
        kparams.add("contentStreams", this.contentStreams);
        kparams.add("complexityValue", this.complexityValue);
        return kparams;
    }

}

