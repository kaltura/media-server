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
import com.kaltura.client.enums.KalturaBatchJobType;
import com.kaltura.client.enums.KalturaSchedulerStatusType;
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
public class KalturaSchedulerStatus extends KalturaObjectBase {
	/**  The id of the Category  */
    public int id = Integer.MIN_VALUE;
	/**  The configured id of the scheduler  */
    public int schedulerConfiguredId = Integer.MIN_VALUE;
	/**  The configured id of the job worker  */
    public int workerConfiguredId = Integer.MIN_VALUE;
	/**  The type of the job worker.  */
    public KalturaBatchJobType workerType;
	/**  The status type  */
    public KalturaSchedulerStatusType type;
	/**  The status value  */
    public int value = Integer.MIN_VALUE;
	/**  The id of the scheduler  */
    public int schedulerId = Integer.MIN_VALUE;
	/**  The id of the worker  */
    public int workerId = Integer.MIN_VALUE;

    public KalturaSchedulerStatus() {
    }

    public KalturaSchedulerStatus(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("schedulerConfiguredId")) {
                this.schedulerConfiguredId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("workerConfiguredId")) {
                this.workerConfiguredId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("workerType")) {
                this.workerType = KalturaBatchJobType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("type")) {
                this.type = KalturaSchedulerStatusType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("value")) {
                this.value = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("schedulerId")) {
                this.schedulerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("workerId")) {
                this.workerId = ParseUtils.parseInt(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaSchedulerStatus");
        kparams.add("schedulerConfiguredId", this.schedulerConfiguredId);
        kparams.add("workerConfiguredId", this.workerConfiguredId);
        kparams.add("workerType", this.workerType);
        kparams.add("type", this.type);
        kparams.add("value", this.value);
        return kparams;
    }

}

