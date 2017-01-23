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
public class KalturaConvertCollectionJobData extends KalturaConvartableJobData {
    public String destDirLocalPath;
    public String destDirRemoteUrl;
    public String destFileName;
    public String inputXmlLocalPath;
    public String inputXmlRemoteUrl;
    public String commandLinesStr;
    public ArrayList<KalturaConvertCollectionFlavorData> flavors;

    public KalturaConvertCollectionJobData() {
    }

    public KalturaConvertCollectionJobData(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("destDirLocalPath")) {
                this.destDirLocalPath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("destDirRemoteUrl")) {
                this.destDirRemoteUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("destFileName")) {
                this.destFileName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("inputXmlLocalPath")) {
                this.inputXmlLocalPath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("inputXmlRemoteUrl")) {
                this.inputXmlRemoteUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("commandLinesStr")) {
                this.commandLinesStr = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("flavors")) {
                this.flavors = ParseUtils.parseArray(KalturaConvertCollectionFlavorData.class, aNode);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaConvertCollectionJobData");
        kparams.add("destDirLocalPath", this.destDirLocalPath);
        kparams.add("destDirRemoteUrl", this.destDirRemoteUrl);
        kparams.add("destFileName", this.destFileName);
        kparams.add("inputXmlLocalPath", this.inputXmlLocalPath);
        kparams.add("inputXmlRemoteUrl", this.inputXmlRemoteUrl);
        kparams.add("commandLinesStr", this.commandLinesStr);
        kparams.add("flavors", this.flavors);
        return kparams;
    }

}

