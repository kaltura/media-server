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
public class KalturaConvartableJobData extends KalturaJobData {
    public String srcFileSyncLocalPath;
	/**  The translated path as used by the scheduler  */
    public String actualSrcFileSyncLocalPath;
    public String srcFileSyncRemoteUrl;
    public ArrayList<KalturaSourceFileSyncDescriptor> srcFileSyncs;
    public int engineVersion = Integer.MIN_VALUE;
    public int flavorParamsOutputId = Integer.MIN_VALUE;
    public KalturaFlavorParamsOutput flavorParamsOutput;
    public int mediaInfoId = Integer.MIN_VALUE;
    public int currentOperationSet = Integer.MIN_VALUE;
    public int currentOperationIndex = Integer.MIN_VALUE;
    public ArrayList<KalturaKeyValue> pluginData;

    public KalturaConvartableJobData() {
    }

    public KalturaConvartableJobData(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("srcFileSyncLocalPath")) {
                this.srcFileSyncLocalPath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("actualSrcFileSyncLocalPath")) {
                this.actualSrcFileSyncLocalPath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("srcFileSyncRemoteUrl")) {
                this.srcFileSyncRemoteUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("srcFileSyncs")) {
                this.srcFileSyncs = ParseUtils.parseArray(KalturaSourceFileSyncDescriptor.class, aNode);
                continue;
            } else if (nodeName.equals("engineVersion")) {
                this.engineVersion = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("flavorParamsOutputId")) {
                this.flavorParamsOutputId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("flavorParamsOutput")) {
                this.flavorParamsOutput = ParseUtils.parseObject(KalturaFlavorParamsOutput.class, aNode);
                continue;
            } else if (nodeName.equals("mediaInfoId")) {
                this.mediaInfoId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("currentOperationSet")) {
                this.currentOperationSet = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("currentOperationIndex")) {
                this.currentOperationIndex = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("pluginData")) {
                this.pluginData = ParseUtils.parseArray(KalturaKeyValue.class, aNode);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaConvartableJobData");
        kparams.add("srcFileSyncLocalPath", this.srcFileSyncLocalPath);
        kparams.add("actualSrcFileSyncLocalPath", this.actualSrcFileSyncLocalPath);
        kparams.add("srcFileSyncRemoteUrl", this.srcFileSyncRemoteUrl);
        kparams.add("srcFileSyncs", this.srcFileSyncs);
        kparams.add("engineVersion", this.engineVersion);
        kparams.add("flavorParamsOutputId", this.flavorParamsOutputId);
        kparams.add("flavorParamsOutput", this.flavorParamsOutput);
        kparams.add("mediaInfoId", this.mediaInfoId);
        kparams.add("currentOperationSet", this.currentOperationSet);
        kparams.add("currentOperationIndex", this.currentOperationIndex);
        kparams.add("pluginData", this.pluginData);
        return kparams;
    }

}

