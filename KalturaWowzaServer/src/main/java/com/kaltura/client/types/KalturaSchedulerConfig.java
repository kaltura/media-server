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
public class KalturaSchedulerConfig extends KalturaObjectBase {
	/**  The id of the Category  */
    public int id = Integer.MIN_VALUE;
	/**  Creator name  */
    public String createdBy;
	/**  Updater name  */
    public String updatedBy;
	/**  Id of the control panel command that created this config item  */
    public String commandId;
	/**  The status of the control panel command  */
    public String commandStatus;
	/**  The id of the scheduler  */
    public int schedulerId = Integer.MIN_VALUE;
	/**  The configured id of the scheduler  */
    public int schedulerConfiguredId = Integer.MIN_VALUE;
	/**  The name of the scheduler  */
    public String schedulerName;
	/**  The id of the job worker  */
    public int workerId = Integer.MIN_VALUE;
	/**  The configured id of the job worker  */
    public int workerConfiguredId = Integer.MIN_VALUE;
	/**  The name of the job worker  */
    public String workerName;
	/**  The name of the variable  */
    public String variable;
	/**  The part of the variable  */
    public String variablePart;
	/**  The value of the variable  */
    public String value;

    public KalturaSchedulerConfig() {
    }

    public KalturaSchedulerConfig(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("createdBy")) {
                this.createdBy = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("updatedBy")) {
                this.updatedBy = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("commandId")) {
                this.commandId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("commandStatus")) {
                this.commandStatus = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("schedulerId")) {
                this.schedulerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("schedulerConfiguredId")) {
                this.schedulerConfiguredId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("schedulerName")) {
                this.schedulerName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("workerId")) {
                this.workerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("workerConfiguredId")) {
                this.workerConfiguredId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("workerName")) {
                this.workerName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("variable")) {
                this.variable = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("variablePart")) {
                this.variablePart = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("value")) {
                this.value = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaSchedulerConfig");
        kparams.add("createdBy", this.createdBy);
        kparams.add("updatedBy", this.updatedBy);
        kparams.add("commandId", this.commandId);
        kparams.add("commandStatus", this.commandStatus);
        kparams.add("schedulerId", this.schedulerId);
        kparams.add("schedulerConfiguredId", this.schedulerConfiguredId);
        kparams.add("schedulerName", this.schedulerName);
        kparams.add("workerId", this.workerId);
        kparams.add("workerConfiguredId", this.workerConfiguredId);
        kparams.add("workerName", this.workerName);
        kparams.add("variable", this.variable);
        kparams.add("variablePart", this.variablePart);
        kparams.add("value", this.value);
        return kparams;
    }

}

