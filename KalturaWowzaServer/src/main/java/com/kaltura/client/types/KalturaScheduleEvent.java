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
import com.kaltura.client.enums.KalturaScheduleEventStatus;
import com.kaltura.client.enums.KalturaScheduleEventClassificationType;
import com.kaltura.client.enums.KalturaScheduleEventRecurrenceType;
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
public abstract class KalturaScheduleEvent extends KalturaObjectBase {
	/**  Auto-generated unique identifier  */
    public int id = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public int parentId = Integer.MIN_VALUE;
	/**  Defines a short summary or subject for the event  */
    public String summary;
    public String description;
    public KalturaScheduleEventStatus status;
    public int startDate = Integer.MIN_VALUE;
    public int endDate = Integer.MIN_VALUE;
    public String referenceId;
    public KalturaScheduleEventClassificationType classificationType;
	/**  Specifies the global position for the activity  */
    public double geoLatitude = Double.MIN_VALUE;
	/**  Specifies the global position for the activity  */
    public double geoLongitude = Double.MIN_VALUE;
	/**  Defines the intended venue for the activity  */
    public String location;
    public String organizer;
    public String ownerId;
	/**  The value for the priority field.  */
    public int priority = Integer.MIN_VALUE;
	/**  Defines the revision sequence number.  */
    public int sequence = Integer.MIN_VALUE;
    public KalturaScheduleEventRecurrenceType recurrenceType;
	/**  Duration in seconds  */
    public int duration = Integer.MIN_VALUE;
	/**  Used to represent contact information or alternately a reference to contact
	  information.  */
    public String contact;
	/**  Specifies non-processing information intended to provide a comment to the
	  calendar user.  */
    public String comment;
    public String tags;
	/**  Creation date as Unix timestamp (In seconds)  */
    public int createdAt = Integer.MIN_VALUE;
	/**  Last update as Unix timestamp (In seconds)  */
    public int updatedAt = Integer.MIN_VALUE;
    public KalturaScheduleEventRecurrence recurrence;

    public KalturaScheduleEvent() {
    }

    public KalturaScheduleEvent(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("id")) {
                this.id = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerId")) {
                this.partnerId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("parentId")) {
                this.parentId = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("summary")) {
                this.summary = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("description")) {
                this.description = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("status")) {
                this.status = KalturaScheduleEventStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("startDate")) {
                this.startDate = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("endDate")) {
                this.endDate = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("referenceId")) {
                this.referenceId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("classificationType")) {
                this.classificationType = KalturaScheduleEventClassificationType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("geoLatitude")) {
                this.geoLatitude = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("geoLongitude")) {
                this.geoLongitude = ParseUtils.parseDouble(txt);
                continue;
            } else if (nodeName.equals("location")) {
                this.location = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("organizer")) {
                this.organizer = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("ownerId")) {
                this.ownerId = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("priority")) {
                this.priority = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("sequence")) {
                this.sequence = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("recurrenceType")) {
                this.recurrenceType = KalturaScheduleEventRecurrenceType.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("duration")) {
                this.duration = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("contact")) {
                this.contact = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("comment")) {
                this.comment = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("tags")) {
                this.tags = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAt")) {
                this.updatedAt = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("recurrence")) {
                this.recurrence = ParseUtils.parseObject(KalturaScheduleEventRecurrence.class, aNode);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaScheduleEvent");
        kparams.add("summary", this.summary);
        kparams.add("description", this.description);
        kparams.add("startDate", this.startDate);
        kparams.add("endDate", this.endDate);
        kparams.add("referenceId", this.referenceId);
        kparams.add("classificationType", this.classificationType);
        kparams.add("geoLatitude", this.geoLatitude);
        kparams.add("geoLongitude", this.geoLongitude);
        kparams.add("location", this.location);
        kparams.add("organizer", this.organizer);
        kparams.add("ownerId", this.ownerId);
        kparams.add("priority", this.priority);
        kparams.add("sequence", this.sequence);
        kparams.add("recurrenceType", this.recurrenceType);
        kparams.add("duration", this.duration);
        kparams.add("contact", this.contact);
        kparams.add("comment", this.comment);
        kparams.add("tags", this.tags);
        kparams.add("recurrence", this.recurrence);
        return kparams;
    }

}

