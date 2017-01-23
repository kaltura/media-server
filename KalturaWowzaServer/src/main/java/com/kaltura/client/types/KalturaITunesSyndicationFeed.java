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
import com.kaltura.client.enums.KalturaITunesSyndicationFeedCategories;
import com.kaltura.client.enums.KalturaITunesSyndicationFeedAdultValues;
import com.kaltura.client.enums.KalturaNullableBoolean;
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
public class KalturaITunesSyndicationFeed extends KalturaBaseSyndicationFeed {
	/**  feed description  */
    public String feedDescription;
	/**  feed language  */
    public String language;
	/**  feed landing page (i.e publisher website)  */
    public String feedLandingPage;
	/**  author/publisher name  */
    public String ownerName;
	/**  publisher email  */
    public String ownerEmail;
	/**  podcast thumbnail  */
    public String feedImageUrl;
    public KalturaITunesSyndicationFeedCategories category;
    public KalturaITunesSyndicationFeedAdultValues adultContent;
    public String feedAuthor;
	/**  true in case you want to enfore the palylist order on the  */
    public KalturaNullableBoolean enforceOrder;

    public KalturaITunesSyndicationFeed() {
    }

    public KalturaITunesSyndicationFeed(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("feedDescription")) {
                this.feedDescription = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("language")) {
                this.language = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("feedLandingPage")) {
                this.feedLandingPage = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("ownerName")) {
                this.ownerName = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("ownerEmail")) {
                this.ownerEmail = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("feedImageUrl")) {
                this.feedImageUrl = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("category")) {
                this.category = KalturaITunesSyndicationFeedCategories.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("adultContent")) {
                this.adultContent = KalturaITunesSyndicationFeedAdultValues.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("feedAuthor")) {
                this.feedAuthor = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("enforceOrder")) {
                this.enforceOrder = KalturaNullableBoolean.get(ParseUtils.parseInt(txt));
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaITunesSyndicationFeed");
        kparams.add("feedDescription", this.feedDescription);
        kparams.add("language", this.language);
        kparams.add("feedLandingPage", this.feedLandingPage);
        kparams.add("ownerName", this.ownerName);
        kparams.add("ownerEmail", this.ownerEmail);
        kparams.add("feedImageUrl", this.feedImageUrl);
        kparams.add("adultContent", this.adultContent);
        kparams.add("feedAuthor", this.feedAuthor);
        kparams.add("enforceOrder", this.enforceOrder);
        return kparams;
    }

}

