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
import com.kaltura.client.enums.KalturaPlayReadyAnalogVideoOPL;
import com.kaltura.client.enums.KalturaPlayReadyDigitalAudioOPL;
import com.kaltura.client.enums.KalturaPlayReadyCompressedDigitalVideoOPL;
import com.kaltura.client.enums.KalturaPlayReadyUncompressedDigitalVideoOPL;
import java.util.ArrayList;
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
public class KalturaPlayReadyPlayRight extends KalturaPlayReadyRight {
    public KalturaPlayReadyAnalogVideoOPL analogVideoOPL;
    public ArrayList<KalturaPlayReadyAnalogVideoOPIdHolder> analogVideoOutputProtectionList;
    public KalturaPlayReadyDigitalAudioOPL compressedDigitalAudioOPL;
    public KalturaPlayReadyCompressedDigitalVideoOPL compressedDigitalVideoOPL;
    public ArrayList<KalturaPlayReadyDigitalAudioOPIdHolder> digitalAudioOutputProtectionList;
    public KalturaPlayReadyDigitalAudioOPL uncompressedDigitalAudioOPL;
    public KalturaPlayReadyUncompressedDigitalVideoOPL uncompressedDigitalVideoOPL;
    public int firstPlayExpiration = Integer.MIN_VALUE;
    public ArrayList<KalturaPlayReadyPlayEnablerHolder> playEnablers;

    public KalturaPlayReadyPlayRight() {
    }

    public KalturaPlayReadyPlayRight(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("analogVideoOPL")) {
                this.analogVideoOPL = KalturaPlayReadyAnalogVideoOPL.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("analogVideoOutputProtectionList")) {
                this.analogVideoOutputProtectionList = ParseUtils.parseArray(KalturaPlayReadyAnalogVideoOPIdHolder.class, aNode);
                continue;
            } else if (nodeName.equals("compressedDigitalAudioOPL")) {
                this.compressedDigitalAudioOPL = KalturaPlayReadyDigitalAudioOPL.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("compressedDigitalVideoOPL")) {
                this.compressedDigitalVideoOPL = KalturaPlayReadyCompressedDigitalVideoOPL.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("digitalAudioOutputProtectionList")) {
                this.digitalAudioOutputProtectionList = ParseUtils.parseArray(KalturaPlayReadyDigitalAudioOPIdHolder.class, aNode);
                continue;
            } else if (nodeName.equals("uncompressedDigitalAudioOPL")) {
                this.uncompressedDigitalAudioOPL = KalturaPlayReadyDigitalAudioOPL.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("uncompressedDigitalVideoOPL")) {
                this.uncompressedDigitalVideoOPL = KalturaPlayReadyUncompressedDigitalVideoOPL.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("firstPlayExpiration")) {
                this.firstPlayExpiration = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("playEnablers")) {
                this.playEnablers = ParseUtils.parseArray(KalturaPlayReadyPlayEnablerHolder.class, aNode);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaPlayReadyPlayRight");
        kparams.add("analogVideoOPL", this.analogVideoOPL);
        kparams.add("analogVideoOutputProtectionList", this.analogVideoOutputProtectionList);
        kparams.add("compressedDigitalAudioOPL", this.compressedDigitalAudioOPL);
        kparams.add("compressedDigitalVideoOPL", this.compressedDigitalVideoOPL);
        kparams.add("digitalAudioOutputProtectionList", this.digitalAudioOutputProtectionList);
        kparams.add("uncompressedDigitalAudioOPL", this.uncompressedDigitalAudioOPL);
        kparams.add("uncompressedDigitalVideoOPL", this.uncompressedDigitalVideoOPL);
        kparams.add("firstPlayExpiration", this.firstPlayExpiration);
        kparams.add("playEnablers", this.playEnablers);
        return kparams;
    }

}

