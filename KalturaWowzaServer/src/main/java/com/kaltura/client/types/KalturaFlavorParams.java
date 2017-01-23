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
import com.kaltura.client.enums.KalturaVideoCodec;
import com.kaltura.client.enums.KalturaAudioCodec;
import com.kaltura.client.enums.KalturaContainerFormat;
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
public class KalturaFlavorParams extends KalturaAssetParams {
	/**  The video codec of the Flavor Params  */
    public KalturaVideoCodec videoCodec;
	/**  The video bitrate (in KBits) of the Flavor Params  */
    public int videoBitrate = Integer.MIN_VALUE;
	/**  The audio codec of the Flavor Params  */
    public KalturaAudioCodec audioCodec;
	/**  The audio bitrate (in KBits) of the Flavor Params  */
    public int audioBitrate = Integer.MIN_VALUE;
	/**  The number of audio channels for "downmixing"  */
    public int audioChannels = Integer.MIN_VALUE;
	/**  The audio sample rate of the Flavor Params  */
    public int audioSampleRate = Integer.MIN_VALUE;
	/**  The desired width of the Flavor Params  */
    public int width = Integer.MIN_VALUE;
	/**  The desired height of the Flavor Params  */
    public int height = Integer.MIN_VALUE;
	/**  The frame rate of the Flavor Params  */
    public double frameRate = Double.MIN_VALUE;
	/**  The gop size of the Flavor Params  */
    public int gopSize = Integer.MIN_VALUE;
	/**  The list of conversion engines (comma separated)  */
    public String conversionEngines;
	/**  The list of conversion engines extra params (separated with "|")  */
    public String conversionEnginesExtraParams;
    public boolean twoPass;
    public int deinterlice = Integer.MIN_VALUE;
    public int rotate = Integer.MIN_VALUE;
    public String operators;
    public int engineVersion = Integer.MIN_VALUE;
	/**  The container format of the Flavor Params  */
    public KalturaContainerFormat format;
    public int aspectRatioProcessingMode = Integer.MIN_VALUE;
    public int forceFrameToMultiplication16 = Integer.MIN_VALUE;
    public int isGopInSec = Integer.MIN_VALUE;
    public int isAvoidVideoShrinkFramesizeToSource = Integer.MIN_VALUE;
    public int isAvoidVideoShrinkBitrateToSource = Integer.MIN_VALUE;
    public int isVideoFrameRateForLowBrAppleHls = Integer.MIN_VALUE;
    public String multiStream;
    public double anamorphicPixels = Double.MIN_VALUE;
    public int isAvoidForcedKeyFrames = Integer.MIN_VALUE;
    public int isCropIMX = Integer.MIN_VALUE;
    public int optimizationPolicy = Integer.MIN_VALUE;
    public int maxFrameRate = Integer.MIN_VALUE;
    public int videoConstantBitrate = Integer.MIN_VALUE;
    public int videoBitrateTolerance = Integer.MIN_VALUE;
    public String watermarkData;
    public String subtitlesData;
    public int isEncrypted = Integer.MIN_VALUE;
    public double contentAwareness = Double.MIN_VALUE;
    public int clipOffset = Integer.MIN_VALUE;
    public int clipDuration = Integer.MIN_VALUE;

    public KalturaFlavorParams() {
    }

    public KalturaFlavorParams(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("videoCodec")) {
                this.videoCodec = KalturaVideoCodec.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("videoBitrate")) {
                this.videoBitrate = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("audioCodec")) {
                this.audioCodec = KalturaAudioCodec.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("audioBitrate")) {
                this.audioBitrate = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("audioChannels")) {
                this.audioChannels = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("audioSampleRate")) {
                this.audioSampleRate = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("width")) {
                this.width = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("height")) {
                this.height = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("frameRate")) {
                this.frameRate = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("gopSize")) {
                this.gopSize = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("conversionEngines")) {
                this.conversionEngines = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("conversionEnginesExtraParams")) {
                this.conversionEnginesExtraParams = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("twoPass")) {
                this.twoPass = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("deinterlice")) {
                this.deinterlice = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("rotate")) {
                this.rotate = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("operators")) {
                this.operators = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("engineVersion")) {
                this.engineVersion = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("format")) {
                this.format = KalturaContainerFormat.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("aspectRatioProcessingMode")) {
                this.aspectRatioProcessingMode = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("forceFrameToMultiplication16")) {
                this.forceFrameToMultiplication16 = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("isGopInSec")) {
                this.isGopInSec = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("isAvoidVideoShrinkFramesizeToSource")) {
                this.isAvoidVideoShrinkFramesizeToSource = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("isAvoidVideoShrinkBitrateToSource")) {
                this.isAvoidVideoShrinkBitrateToSource = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("isVideoFrameRateForLowBrAppleHls")) {
                this.isVideoFrameRateForLowBrAppleHls = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("multiStream")) {
                this.multiStream = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("anamorphicPixels")) {
                this.anamorphicPixels = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("isAvoidForcedKeyFrames")) {
                this.isAvoidForcedKeyFrames = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("isCropIMX")) {
                this.isCropIMX = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("optimizationPolicy")) {
                this.optimizationPolicy = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("maxFrameRate")) {
                this.maxFrameRate = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("videoConstantBitrate")) {
                this.videoConstantBitrate = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("videoBitrateTolerance")) {
                this.videoBitrateTolerance = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("watermarkData")) {
                this.watermarkData = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("subtitlesData")) {
                this.subtitlesData = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("isEncrypted")) {
                this.isEncrypted = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("contentAwareness")) {
                this.contentAwareness = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("clipOffset")) {
                this.clipOffset = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("clipDuration")) {
                this.clipDuration = ParseUtils.parseInt(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaFlavorParams");
        kparams.add("videoCodec", this.videoCodec);
        kparams.add("videoBitrate", this.videoBitrate);
        kparams.add("audioCodec", this.audioCodec);
        kparams.add("audioBitrate", this.audioBitrate);
        kparams.add("audioChannels", this.audioChannels);
        kparams.add("audioSampleRate", this.audioSampleRate);
        kparams.add("width", this.width);
        kparams.add("height", this.height);
        kparams.add("frameRate", this.frameRate);
        kparams.add("gopSize", this.gopSize);
        kparams.add("conversionEngines", this.conversionEngines);
        kparams.add("conversionEnginesExtraParams", this.conversionEnginesExtraParams);
        kparams.add("twoPass", this.twoPass);
        kparams.add("deinterlice", this.deinterlice);
        kparams.add("rotate", this.rotate);
        kparams.add("operators", this.operators);
        kparams.add("engineVersion", this.engineVersion);
        kparams.add("format", this.format);
        kparams.add("aspectRatioProcessingMode", this.aspectRatioProcessingMode);
        kparams.add("forceFrameToMultiplication16", this.forceFrameToMultiplication16);
        kparams.add("isGopInSec", this.isGopInSec);
        kparams.add("isAvoidVideoShrinkFramesizeToSource", this.isAvoidVideoShrinkFramesizeToSource);
        kparams.add("isAvoidVideoShrinkBitrateToSource", this.isAvoidVideoShrinkBitrateToSource);
        kparams.add("isVideoFrameRateForLowBrAppleHls", this.isVideoFrameRateForLowBrAppleHls);
        kparams.add("multiStream", this.multiStream);
        kparams.add("anamorphicPixels", this.anamorphicPixels);
        kparams.add("isAvoidForcedKeyFrames", this.isAvoidForcedKeyFrames);
        kparams.add("isCropIMX", this.isCropIMX);
        kparams.add("optimizationPolicy", this.optimizationPolicy);
        kparams.add("maxFrameRate", this.maxFrameRate);
        kparams.add("videoConstantBitrate", this.videoConstantBitrate);
        kparams.add("videoBitrateTolerance", this.videoBitrateTolerance);
        kparams.add("watermarkData", this.watermarkData);
        kparams.add("subtitlesData", this.subtitlesData);
        kparams.add("isEncrypted", this.isEncrypted);
        kparams.add("contentAwareness", this.contentAwareness);
        kparams.add("clipOffset", this.clipOffset);
        kparams.add("clipDuration", this.clipDuration);
        return kparams;
    }

}

