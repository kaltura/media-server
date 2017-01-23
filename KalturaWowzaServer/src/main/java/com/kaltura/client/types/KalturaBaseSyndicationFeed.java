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
import com.kaltura.client.enums.KalturaSyndicationFeedStatus;
import com.kaltura.client.enums.KalturaSyndicationFeedType;
import com.kaltura.client.enums.KalturaSyndicationFeedEntriesOrderBy;
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
public abstract class KalturaBaseSyndicationFeed extends KalturaObjectBase {
    public String id;
    public String feedUrl;
    public int partnerId = Integer.MIN_VALUE;
	/**  link a playlist that will set what content the feed will include   if empty, all
	  content will be included in feed  */
    public String playlistId;
	/**  feed name  */
    public String name;
	/**  feed status  */
    public KalturaSyndicationFeedStatus status;
	/**  feed type  */
    public KalturaSyndicationFeedType type;
	/**  Base URL for each video, on the partners site   This is required by all
	  syndication types.  */
    public String landingPage;
	/**  Creation date as Unix timestamp (In seconds)  */
    public int createdAt = Integer.MIN_VALUE;
	/**  allow_embed tells google OR yahoo weather to allow embedding the video on google
	  OR yahoo video results   or just to provide a link to the landing page.   it is
	  applied on the video-player_loc property in the XML (google)   and addes
	  media-player tag (yahoo)  */
    public boolean allowEmbed;
	/**  Select a uiconf ID as player skin to include in the kwidget url  */
    public int playerUiconfId = Integer.MIN_VALUE;
    public int flavorParamId = Integer.MIN_VALUE;
    public boolean transcodeExistingContent;
    public boolean addToDefaultConversionProfile;
    public String categories;
    public int storageId = Integer.MIN_VALUE;
    public KalturaSyndicationFeedEntriesOrderBy entriesOrderBy;
	/**  Should enforce entitlement on feed entries  */
    public boolean enforceEntitlement;
	/**  Set privacy context for search entries that assiged to private and public
	  categories within a category privacy context.  */
    public String privacyContext;
	/**  Update date as Unix timestamp (In seconds)  */
    public int updatedAt = Integer.MIN_VALUE;
    public boolean useCategoryEntries;
	/**  Feed content-type header value  */
    public String feedContentTypeHeader;

    public KalturaBaseSyndicationFeed() {
    }

    public KalturaBaseSyndicationFeed(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("feedUrl")) {
                this.feedUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerId")) {
                this.partnerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("playlistId")) {
                this.playlistId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("name")) {
                this.name = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaSyndicationFeedStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("type")) {
                this.type = KalturaSyndicationFeedType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("landingPage")) {
                this.landingPage = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("allowEmbed")) {
                this.allowEmbed = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("playerUiconfId")) {
                this.playerUiconfId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("flavorParamId")) {
                this.flavorParamId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("transcodeExistingContent")) {
                this.transcodeExistingContent = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("addToDefaultConversionProfile")) {
                this.addToDefaultConversionProfile = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("categories")) {
                this.categories = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("storageId")) {
                this.storageId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("entriesOrderBy")) {
                this.entriesOrderBy = KalturaSyndicationFeedEntriesOrderBy.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("enforceEntitlement")) {
                this.enforceEntitlement = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("privacyContext")) {
                this.privacyContext = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("updatedAt")) {
                this.updatedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("useCategoryEntries")) {
                this.useCategoryEntries = ParseUtils.parseBool(txt);
                continue;
            } else if (nodeName.equals("feedContentTypeHeader")) {
                this.feedContentTypeHeader = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaBaseSyndicationFeed");
        kparams.add("playlistId", this.playlistId);
        kparams.add("name", this.name);
        kparams.add("type", this.type);
        kparams.add("landingPage", this.landingPage);
        kparams.add("allowEmbed", this.allowEmbed);
        kparams.add("playerUiconfId", this.playerUiconfId);
        kparams.add("flavorParamId", this.flavorParamId);
        kparams.add("transcodeExistingContent", this.transcodeExistingContent);
        kparams.add("addToDefaultConversionProfile", this.addToDefaultConversionProfile);
        kparams.add("categories", this.categories);
        kparams.add("storageId", this.storageId);
        kparams.add("entriesOrderBy", this.entriesOrderBy);
        kparams.add("enforceEntitlement", this.enforceEntitlement);
        kparams.add("privacyContext", this.privacyContext);
        kparams.add("useCategoryEntries", this.useCategoryEntries);
        kparams.add("feedContentTypeHeader", this.feedContentTypeHeader);
        return kparams;
    }

}

