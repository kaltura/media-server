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
import com.kaltura.client.enums.KalturaNullableBoolean;
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
public class KalturaRule extends KalturaObjectBase {
	/**  Short Rule Description  */
    public String description;
	/**  Rule Custom Data to allow saving rule specific information  */
    public String ruleData;
	/**  Message to be thrown to the player in case the rule is fulfilled  */
    public String message;
	/**  Code to be thrown to the player in case the rule is fulfilled  */
    public String code;
	/**  Actions to be performed by the player in case the rule is fulfilled  */
    public ArrayList<KalturaRuleAction> actions;
	/**  Conditions to validate the rule  */
    public ArrayList<KalturaCondition> conditions;
	/**  Indicates what contexts should be tested by this rule  */
    public ArrayList<KalturaContextTypeHolder> contexts;
	/**  Indicates that this rule is enough and no need to continue checking the rest of
	  the rules  */
    public boolean stopProcessing;
	/**  Indicates if we should force ks validation for admin ks users as well  */
    public KalturaNullableBoolean forceAdminValidation;

    public KalturaRule() {
    }

    public KalturaRule(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("description")) {
                this.description = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("ruleData")) {
                this.ruleData = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("message")) {
                this.message = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("code")) {
                this.code = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("actions")) {
                this.actions = ParseUtils.parseArray(KalturaRuleAction.class, aNode);
                continue;
            } else if (nodeName.equals("conditions")) {
                this.conditions = ParseUtils.parseArray(KalturaCondition.class, aNode);
                continue;
            } else if (nodeName.equals("contexts")) {
                this.contexts = ParseUtils.parseArray(KalturaContextTypeHolder.class, aNode);
                continue;
            } else if (nodeName.equals("stopProcessing")) {
                this.stopProcessing = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("forceAdminValidation")) {
                this.forceAdminValidation = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaRule");
        kparams.add("description", this.description);
        kparams.add("ruleData", this.ruleData);
        kparams.add("message", this.message);
        kparams.add("code", this.code);
        kparams.add("actions", this.actions);
        kparams.add("conditions", this.conditions);
        kparams.add("contexts", this.contexts);
        kparams.add("stopProcessing", this.stopProcessing);
        kparams.add("forceAdminValidation", this.forceAdminValidation);
        return kparams;
    }

}

