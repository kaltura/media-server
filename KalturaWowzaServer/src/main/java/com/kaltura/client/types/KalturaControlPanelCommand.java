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
import com.kaltura.client.enums.KalturaControlPanelCommandType;
import com.kaltura.client.enums.KalturaControlPanelCommandTargetType;
import com.kaltura.client.enums.KalturaControlPanelCommandStatus;
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
public class KalturaControlPanelCommand extends KalturaObjectBase {
	/**  The id of the Category  */
    public int id = Integer.MIN_VALUE;
	/**  Creation date as Unix timestamp (In seconds)  */
    public int createdAt = Integer.MIN_VALUE;
	/**  Creator name  */
    public String createdBy;
	/**  Update date as Unix timestamp (In seconds)  */
    public int updatedAt = Integer.MIN_VALUE;
	/**  Updater name  */
    public String updatedBy;
	/**  Creator id  */
    public int createdById = Integer.MIN_VALUE;
	/**  The id of the scheduler that the command refers to  */
    public int schedulerId = Integer.MIN_VALUE;
	/**  The id of the scheduler worker that the command refers to  */
    public int workerId = Integer.MIN_VALUE;
	/**  The id of the scheduler worker as configured in the ini file  */
    public int workerConfiguredId = Integer.MIN_VALUE;
	/**  The name of the scheduler worker that the command refers to  */
    public int workerName = Integer.MIN_VALUE;
	/**  The index of the batch process that the command refers to  */
    public int batchIndex = Integer.MIN_VALUE;
	/**  The command type - stop / start / config  */
    public KalturaControlPanelCommandType type;
	/**  The command target type - data center / scheduler / job / job type  */
    public KalturaControlPanelCommandTargetType targetType;
	/**  The command status  */
    public KalturaControlPanelCommandStatus status;
	/**  The reason for the command  */
    public String cause;
	/**  Command description  */
    public String description;
	/**  Error description  */
    public String errorDescription;

    public KalturaControlPanelCommand() {
    }

    public KalturaControlPanelCommand(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("createdBy")) {
                this.createdBy = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("updatedAt")) {
                this.updatedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedBy")) {
                this.updatedBy = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("createdById")) {
                this.createdById = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("schedulerId")) {
                this.schedulerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("workerId")) {
                this.workerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("workerConfiguredId")) {
                this.workerConfiguredId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("workerName")) {
                this.workerName = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("batchIndex")) {
                this.batchIndex = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("type")) {
                this.type = KalturaControlPanelCommandType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("targetType")) {
                this.targetType = KalturaControlPanelCommandTargetType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaControlPanelCommandStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("cause")) {
                this.cause = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("description")) {
                this.description = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("errorDescription")) {
                this.errorDescription = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaControlPanelCommand");
        kparams.add("createdBy", this.createdBy);
        kparams.add("updatedBy", this.updatedBy);
        kparams.add("createdById", this.createdById);
        kparams.add("schedulerId", this.schedulerId);
        kparams.add("workerId", this.workerId);
        kparams.add("workerConfiguredId", this.workerConfiguredId);
        kparams.add("workerName", this.workerName);
        kparams.add("batchIndex", this.batchIndex);
        kparams.add("type", this.type);
        kparams.add("targetType", this.targetType);
        kparams.add("status", this.status);
        kparams.add("cause", this.cause);
        kparams.add("description", this.description);
        kparams.add("errorDescription", this.errorDescription);
        return kparams;
    }

}

