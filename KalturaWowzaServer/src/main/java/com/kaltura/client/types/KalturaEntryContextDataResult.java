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
import java.util.HashMap;
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
public class KalturaEntryContextDataResult extends KalturaContextDataResult {
    public boolean isSiteRestricted;
    public boolean isCountryRestricted;
    public boolean isSessionRestricted;
    public boolean isIpAddressRestricted;
    public boolean isUserAgentRestricted;
    public int previewLength = Integer.MIN_VALUE;
    public boolean isScheduledNow;
    public boolean isAdmin;
	/**  http/rtmp/hdnetwork  */
    public String streamerType;
	/**  http/https, rtmp/rtmpe  */
    public String mediaProtocol;
    public String storageProfilesXML;
	/**  Array of messages as received from the access control rules that invalidated  */
    public ArrayList<KalturaString> accessControlMessages;
	/**  Array of actions as received from the access control rules that invalidated  */
    public ArrayList<KalturaRuleAction> accessControlActions;
	/**  Array of allowed flavor assets according to access control limitations and
	  requested tags  */
    public ArrayList<KalturaFlavorAsset> flavorAssets;
	/**  The duration of the entry in milliseconds  */
    public int msDuration = Integer.MIN_VALUE;
	/**  Array of allowed flavor assets according to access control limitations and
	  requested tags  */
    public HashMap<String, KalturaPluginData> pluginData;

    public KalturaEntryContextDataResult() {
    }

    public KalturaEntryContextDataResult(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("isSiteRestricted")) {
                this.isSiteRestricted = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("isCountryRestricted")) {
                this.isCountryRestricted = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("isSessionRestricted")) {
                this.isSessionRestricted = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("isIpAddressRestricted")) {
                this.isIpAddressRestricted = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("isUserAgentRestricted")) {
                this.isUserAgentRestricted = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("previewLength")) {
                this.previewLength = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("isScheduledNow")) {
                this.isScheduledNow = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("isAdmin")) {
                this.isAdmin = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("streamerType")) {
                this.streamerType = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("mediaProtocol")) {
                this.mediaProtocol = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("storageProfilesXML")) {
                this.storageProfilesXML = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("accessControlMessages")) {
                this.accessControlMessages = ParseUtils.parseArray(KalturaString.class, aNode);
                continue;
            } else if (nodeName.equals("accessControlActions")) {
                this.accessControlActions = ParseUtils.parseArray(KalturaRuleAction.class, aNode);
                continue;
            } else if (nodeName.equals("flavorAssets")) {
                this.flavorAssets = ParseUtils.parseArray(KalturaFlavorAsset.class, aNode);
                continue;
            } else if (nodeName.equals("msDuration")) {
                this.msDuration = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("pluginData")) {
                this.pluginData = ParseUtils.parseMap(KalturaPluginData.class, aNode);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaEntryContextDataResult");
        kparams.add("isSiteRestricted", this.isSiteRestricted);
        kparams.add("isCountryRestricted", this.isCountryRestricted);
        kparams.add("isSessionRestricted", this.isSessionRestricted);
        kparams.add("isIpAddressRestricted", this.isIpAddressRestricted);
        kparams.add("isUserAgentRestricted", this.isUserAgentRestricted);
        kparams.add("previewLength", this.previewLength);
        kparams.add("isScheduledNow", this.isScheduledNow);
        kparams.add("isAdmin", this.isAdmin);
        kparams.add("streamerType", this.streamerType);
        kparams.add("mediaProtocol", this.mediaProtocol);
        kparams.add("storageProfilesXML", this.storageProfilesXML);
        kparams.add("accessControlMessages", this.accessControlMessages);
        kparams.add("accessControlActions", this.accessControlActions);
        kparams.add("flavorAssets", this.flavorAssets);
        kparams.add("msDuration", this.msDuration);
        kparams.add("pluginData", this.pluginData);
        return kparams;
    }

}

