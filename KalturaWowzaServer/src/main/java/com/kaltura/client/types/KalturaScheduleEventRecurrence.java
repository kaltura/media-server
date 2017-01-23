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
import com.kaltura.client.enums.KalturaScheduleEventRecurrenceFrequency;
import com.kaltura.client.enums.KalturaScheduleEventRecurrenceDay;
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
public class KalturaScheduleEventRecurrence extends KalturaObjectBase {
    public String name;
    public KalturaScheduleEventRecurrenceFrequency frequency;
    public int until = Integer.MIN_VALUE;
	/**  TimeZone String  */
    public String timeZone;
    public int count = Integer.MIN_VALUE;
    public int interval = Integer.MIN_VALUE;
	/**  Comma separated numbers between 0 to 59  */
    public String bySecond;
	/**  Comma separated numbers between 0 to 59  */
    public String byMinute;
	/**  Comma separated numbers between 0 to 23  */
    public String byHour;
	/**  Comma separated of KalturaScheduleEventRecurrenceDay   Each byDay value can also
	  be preceded by a positive (+n) or negative (-n) integer.   If present, this
	  indicates the nth occurrence of the specific day within the MONTHLY or YEARLY
	  RRULE.   For example, within a MONTHLY rule, +1MO (or simply 1MO) represents the
	  first Monday within the month, whereas -1MO represents the last Monday of the
	  month.   If an integer modifier is not present, it means all days of this type
	  within the specified frequency.   For example, within a MONTHLY rule, MO
	  represents all Mondays within the month.  */
    public String byDay;
	/**  Comma separated of numbers between -31 to 31, excluding 0.   For example, -10
	  represents the tenth to the last day of the month.  */
    public String byMonthDay;
	/**  Comma separated of numbers between -366 to 366, excluding 0.   For example, -1
	  represents the last day of the year (December 31st) and -306 represents the
	  306th to the last day of the year (March 1st).  */
    public String byYearDay;
	/**  Comma separated of numbers between -53 to 53, excluding 0.   This corresponds to
	  weeks according to week numbering.   A week is defined as a seven day period,
	  starting on the day of the week defined to be the week start.   Week number one
	  of the calendar year is the first week which contains at least four (4) days in
	  that calendar year.   This rule part is only valid for YEARLY frequency.   For
	  example, 3 represents the third week of the year.  */
    public String byWeekNumber;
	/**  Comma separated numbers between 1 to 12  */
    public String byMonth;
	/**  Comma separated of numbers between -366 to 366, excluding 0.   Corresponds to
	  the nth occurrence within the set of events specified by the rule.   It must
	  only be used in conjunction with another byrule part.   For example "the last
	  work day of the month" could be represented as:
	  frequency=MONTHLY;byDay=MO,TU,WE,TH,FR;byOffset=-1   Each byOffset value can
	  include a positive (+n) or negative (-n) integer.   If present, this indicates
	  the nth occurrence of the specific occurrence within the set of events specified
	  by the rule.  */
    public String byOffset;
    public KalturaScheduleEventRecurrenceDay weekStartDay;

    public KalturaScheduleEventRecurrence() {
    }

    public KalturaScheduleEventRecurrence(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("name")) {
                this.name = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("frequency")) {
                this.frequency = KalturaScheduleEventRecurrenceFrequency.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("until")) {
                this.until = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("timeZone")) {
                this.timeZone = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("count")) {
                this.count = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("interval")) {
                this.interval = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("bySecond")) {
                this.bySecond = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("byMinute")) {
                this.byMinute = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("byHour")) {
                this.byHour = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("byDay")) {
                this.byDay = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("byMonthDay")) {
                this.byMonthDay = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("byYearDay")) {
                this.byYearDay = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("byWeekNumber")) {
                this.byWeekNumber = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("byMonth")) {
                this.byMonth = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("byOffset")) {
                this.byOffset = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("weekStartDay")) {
                this.weekStartDay = KalturaScheduleEventRecurrenceDay.get(ParseUtils.parseString(txt));
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaScheduleEventRecurrence");
        kparams.add("name", this.name);
        kparams.add("frequency", this.frequency);
        kparams.add("until", this.until);
        kparams.add("timeZone", this.timeZone);
        kparams.add("count", this.count);
        kparams.add("interval", this.interval);
        kparams.add("bySecond", this.bySecond);
        kparams.add("byMinute", this.byMinute);
        kparams.add("byHour", this.byHour);
        kparams.add("byDay", this.byDay);
        kparams.add("byMonthDay", this.byMonthDay);
        kparams.add("byYearDay", this.byYearDay);
        kparams.add("byWeekNumber", this.byWeekNumber);
        kparams.add("byMonth", this.byMonth);
        kparams.add("byOffset", this.byOffset);
        kparams.add("weekStartDay", this.weekStartDay);
        return kparams;
    }

}

