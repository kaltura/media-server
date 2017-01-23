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
import com.kaltura.client.enums.KalturaDistributionAction;
import com.kaltura.client.enums.KalturaGenericDistributionProviderStatus;
import com.kaltura.client.enums.KalturaGenericDistributionProviderParser;
import com.kaltura.client.enums.KalturaDistributionProtocol;
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
public class KalturaGenericDistributionProviderAction extends KalturaObjectBase {
	/**  Auto generated  */
    public int id = Integer.MIN_VALUE;
	/**  Generic distribution provider action creation date as Unix timestamp (In
	  seconds)  */
    public int createdAt = Integer.MIN_VALUE;
	/**  Generic distribution provider action last update date as Unix timestamp (In
	  seconds)  */
    public int updatedAt = Integer.MIN_VALUE;
    public int genericDistributionProviderId = Integer.MIN_VALUE;
    public KalturaDistributionAction action;
    public KalturaGenericDistributionProviderStatus status;
    public KalturaGenericDistributionProviderParser resultsParser;
    public KalturaDistributionProtocol protocol;
    public String serverAddress;
    public String remotePath;
    public String remoteUsername;
    public String remotePassword;
    public String editableFields;
    public String mandatoryFields;
    public String mrssTransformer;
    public String mrssValidator;
    public String resultsTransformer;

    public KalturaGenericDistributionProviderAction() {
    }

    public KalturaGenericDistributionProviderAction(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("updatedAt")) {
                this.updatedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("genericDistributionProviderId")) {
                this.genericDistributionProviderId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("action")) {
                this.action = KalturaDistributionAction.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaGenericDistributionProviderStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("resultsParser")) {
                this.resultsParser = KalturaGenericDistributionProviderParser.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("protocol")) {
                this.protocol = KalturaDistributionProtocol.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("serverAddress")) {
                this.serverAddress = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("remotePath")) {
                this.remotePath = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("remoteUsername")) {
                this.remoteUsername = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("remotePassword")) {
                this.remotePassword = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("editableFields")) {
                this.editableFields = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("mandatoryFields")) {
                this.mandatoryFields = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("mrssTransformer")) {
                this.mrssTransformer = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("mrssValidator")) {
                this.mrssValidator = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("resultsTransformer")) {
                this.resultsTransformer = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaGenericDistributionProviderAction");
        kparams.add("genericDistributionProviderId", this.genericDistributionProviderId);
        kparams.add("action", this.action);
        kparams.add("resultsParser", this.resultsParser);
        kparams.add("protocol", this.protocol);
        kparams.add("serverAddress", this.serverAddress);
        kparams.add("remotePath", this.remotePath);
        kparams.add("remoteUsername", this.remoteUsername);
        kparams.add("remotePassword", this.remotePassword);
        kparams.add("editableFields", this.editableFields);
        kparams.add("mandatoryFields", this.mandatoryFields);
        return kparams;
    }

}

