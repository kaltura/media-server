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
public class KalturaSchedulerWorker extends KalturaObjectBase {
	/**  The id of the Worker  */
    public int id = Integer.MIN_VALUE;
	/**  The id as configured in the batch config  */
    public int configuredId = Integer.MIN_VALUE;
	/**  The id of the Scheduler  */
    public int schedulerId = Integer.MIN_VALUE;
	/**  The id of the scheduler as configured in the batch config  */
    public int schedulerConfiguredId = Integer.MIN_VALUE;
	/**  The worker type  */
    public KalturaBatchJobType type;
	/**  The friendly name of the type  */
    public String typeName;
	/**  The scheduler name  */
    public String name;
	/**  Array of the last statuses  */
    public ArrayList<KalturaSchedulerStatus> statuses;
	/**  Array of the last configs  */
    public ArrayList<KalturaSchedulerConfig> configs;
	/**  Array of jobs that locked to this worker  */
    public ArrayList<KalturaBatchJob> lockedJobs;
	/**  Avarage time between creation and queue time  */
    public int avgWait = Integer.MIN_VALUE;
	/**  Avarage time between queue time end finish time  */
    public int avgWork = Integer.MIN_VALUE;
	/**  last status time  */
    public int lastStatus = Integer.MIN_VALUE;
	/**  last status formated  */
    public String lastStatusStr;

    public KalturaSchedulerWorker() {
    }

    public KalturaSchedulerWorker(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("configuredId")) {
                this.configuredId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("schedulerId")) {
                this.schedulerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("schedulerConfiguredId")) {
                this.schedulerConfiguredId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("type")) {
                this.type = KalturaBatchJobType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("typeName")) {
                this.typeName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("name")) {
                this.name = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("statuses")) {
                this.statuses = ParseUtils.parseArray(KalturaSchedulerStatus.class, aNode);
                continue;
            } else if (nodeName.equals("configs")) {
                this.configs = ParseUtils.parseArray(KalturaSchedulerConfig.class, aNode);
                continue;
            } else if (nodeName.equals("lockedJobs")) {
                this.lockedJobs = ParseUtils.parseArray(KalturaBatchJob.class, aNode);
                continue;
            } else if (nodeName.equals("avgWait")) {
                this.avgWait = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("avgWork")) {
                this.avgWork = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("lastStatus")) {
                this.lastStatus = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("lastStatusStr")) {
                this.lastStatusStr = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaSchedulerWorker");
        kparams.add("configuredId", this.configuredId);
        kparams.add("schedulerId", this.schedulerId);
        kparams.add("schedulerConfiguredId", this.schedulerConfiguredId);
        kparams.add("type", this.type);
        kparams.add("typeName", this.typeName);
        kparams.add("name", this.name);
        kparams.add("statuses", this.statuses);
        kparams.add("configs", this.configs);
        kparams.add("lockedJobs", this.lockedJobs);
        kparams.add("avgWait", this.avgWait);
        kparams.add("avgWork", this.avgWork);
        kparams.add("lastStatus", this.lastStatus);
        kparams.add("lastStatusStr", this.lastStatusStr);
        return kparams;
    }

}

