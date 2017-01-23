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
public class KalturaMsnDistributionProfile extends KalturaConfigurableDistributionProfile {
    public String username;
    public String password;
    public String domain;
    public String csId;
    public String source;
    public String sourceFriendlyName;
    public String pageGroup;
    public int sourceFlavorParamsId = Integer.MIN_VALUE;
    public int wmvFlavorParamsId = Integer.MIN_VALUE;
    public int flvFlavorParamsId = Integer.MIN_VALUE;
    public int slFlavorParamsId = Integer.MIN_VALUE;
    public int slHdFlavorParamsId = Integer.MIN_VALUE;
    public String msnvideoCat;
    public String msnvideoTop;
    public String msnvideoTopCat;

    public KalturaMsnDistributionProfile() {
    }

    public KalturaMsnDistributionProfile(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("username")) {
                this.username = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("password")) {
                this.password = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("domain")) {
                this.domain = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("csId")) {
                this.csId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("source")) {
                this.source = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sourceFriendlyName")) {
                this.sourceFriendlyName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("pageGroup")) {
                this.pageGroup = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sourceFlavorParamsId")) {
                this.sourceFlavorParamsId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("wmvFlavorParamsId")) {
                this.wmvFlavorParamsId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("flvFlavorParamsId")) {
                this.flvFlavorParamsId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("slFlavorParamsId")) {
                this.slFlavorParamsId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("slHdFlavorParamsId")) {
                this.slHdFlavorParamsId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("msnvideoCat")) {
                this.msnvideoCat = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("msnvideoTop")) {
                this.msnvideoTop = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("msnvideoTopCat")) {
                this.msnvideoTopCat = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaMsnDistributionProfile");
        kparams.add("username", this.username);
        kparams.add("password", this.password);
        kparams.add("domain", this.domain);
        kparams.add("csId", this.csId);
        kparams.add("source", this.source);
        kparams.add("sourceFriendlyName", this.sourceFriendlyName);
        kparams.add("pageGroup", this.pageGroup);
        kparams.add("sourceFlavorParamsId", this.sourceFlavorParamsId);
        kparams.add("wmvFlavorParamsId", this.wmvFlavorParamsId);
        kparams.add("flvFlavorParamsId", this.flvFlavorParamsId);
        kparams.add("slFlavorParamsId", this.slFlavorParamsId);
        kparams.add("slHdFlavorParamsId", this.slHdFlavorParamsId);
        kparams.add("msnvideoCat", this.msnvideoCat);
        kparams.add("msnvideoTop", this.msnvideoTop);
        kparams.add("msnvideoTopCat", this.msnvideoTopCat);
        return kparams;
    }

}

