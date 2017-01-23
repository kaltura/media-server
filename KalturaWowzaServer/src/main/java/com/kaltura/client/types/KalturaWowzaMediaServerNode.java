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
public class KalturaWowzaMediaServerNode extends KalturaMediaServerNode {
	/**  Wowza Media server app prefix  */
    public String appPrefix;
	/**  Wowza Media server transcoder configuration overide  */
    public String transcoder;
	/**  Wowza Media server GPU index id  */
    public int GPUID = Integer.MIN_VALUE;
	/**  Live service port  */
    public int liveServicePort = Integer.MIN_VALUE;
	/**  Live service protocol  */
    public String liveServiceProtocol;
	/**  Wowza media server live service internal domain  */
    public String liveServiceInternalDomain;

    public KalturaWowzaMediaServerNode() {
    }

    public KalturaWowzaMediaServerNode(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("appPrefix")) {
                this.appPrefix = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("transcoder")) {
                this.transcoder = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("GPUID")) {
                this.GPUID = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("liveServicePort")) {
                this.liveServicePort = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("liveServiceProtocol")) {
                this.liveServiceProtocol = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("liveServiceInternalDomain")) {
                this.liveServiceInternalDomain = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaWowzaMediaServerNode");
        kparams.add("appPrefix", this.appPrefix);
        kparams.add("transcoder", this.transcoder);
        kparams.add("GPUID", this.GPUID);
        kparams.add("liveServicePort", this.liveServicePort);
        kparams.add("liveServiceProtocol", this.liveServiceProtocol);
        kparams.add("liveServiceInternalDomain", this.liveServiceInternalDomain);
        return kparams;
    }

}

