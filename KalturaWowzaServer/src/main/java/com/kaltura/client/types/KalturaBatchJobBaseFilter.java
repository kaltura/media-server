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
import com.kaltura.client.enums.KalturaBatchJobType;
import com.kaltura.client.enums.KalturaBatchJobStatus;
import com.kaltura.client.enums.KalturaBatchJobErrorTypes;
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
public abstract class KalturaBatchJobBaseFilter extends KalturaFilter {
    public long idEqual = Long.MIN_VALUE;
    public long idGreaterThanOrEqual = Long.MIN_VALUE;
    public int partnerIdEqual = Integer.MIN_VALUE;
    public String partnerIdIn;
    public String partnerIdNotIn;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtLessThanOrEqual = Integer.MIN_VALUE;
    public int executionAttemptsGreaterThanOrEqual = Integer.MIN_VALUE;
    public int executionAttemptsLessThanOrEqual = Integer.MIN_VALUE;
    public int lockVersionGreaterThanOrEqual = Integer.MIN_VALUE;
    public int lockVersionLessThanOrEqual = Integer.MIN_VALUE;
    public String entryIdEqual;
    public KalturaBatchJobType jobTypeEqual;
    public String jobTypeIn;
    public String jobTypeNotIn;
    public int jobSubTypeEqual = Integer.MIN_VALUE;
    public String jobSubTypeIn;
    public String jobSubTypeNotIn;
    public KalturaBatchJobStatus statusEqual;
    public String statusIn;
    public String statusNotIn;
    public int priorityGreaterThanOrEqual = Integer.MIN_VALUE;
    public int priorityLessThanOrEqual = Integer.MIN_VALUE;
    public int priorityEqual = Integer.MIN_VALUE;
    public String priorityIn;
    public String priorityNotIn;
    public int batchVersionGreaterThanOrEqual = Integer.MIN_VALUE;
    public int batchVersionLessThanOrEqual = Integer.MIN_VALUE;
    public int batchVersionEqual = Integer.MIN_VALUE;
    public int queueTimeGreaterThanOrEqual = Integer.MIN_VALUE;
    public int queueTimeLessThanOrEqual = Integer.MIN_VALUE;
    public int finishTimeGreaterThanOrEqual = Integer.MIN_VALUE;
    public int finishTimeLessThanOrEqual = Integer.MIN_VALUE;
    public KalturaBatchJobErrorTypes errTypeEqual;
    public String errTypeIn;
    public String errTypeNotIn;
    public int errNumberEqual = Integer.MIN_VALUE;
    public String errNumberIn;
    public String errNumberNotIn;
    public int estimatedEffortLessThan = Integer.MIN_VALUE;
    public int estimatedEffortGreaterThan = Integer.MIN_VALUE;
    public int urgencyLessThanOrEqual = Integer.MIN_VALUE;
    public int urgencyGreaterThanOrEqual = Integer.MIN_VALUE;

    public KalturaBatchJobBaseFilter() {
    }

    public KalturaBatchJobBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("idEqual")) {
                this.idEqual = ParseUtils.parseBigint(txt);
                continue;
            } else if (nodeName.equals("idGreaterThanOrEqual")) {
                this.idGreaterThanOrEqual = ParseUtils.parseBigint(txt);
                continue;
            } else if (nodeName.equals("partnerIdEqual")) {
                this.partnerIdEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("partnerIdIn")) {
                this.partnerIdIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("partnerIdNotIn")) {
                this.partnerIdNotIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("createdAtGreaterThanOrEqual")) {
                this.createdAtGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("createdAtLessThanOrEqual")) {
                this.createdAtLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAtGreaterThanOrEqual")) {
                this.updatedAtGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("updatedAtLessThanOrEqual")) {
                this.updatedAtLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("executionAttemptsGreaterThanOrEqual")) {
                this.executionAttemptsGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("executionAttemptsLessThanOrEqual")) {
                this.executionAttemptsLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("lockVersionGreaterThanOrEqual")) {
                this.lockVersionGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("lockVersionLessThanOrEqual")) {
                this.lockVersionLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("entryIdEqual")) {
                this.entryIdEqual = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("jobTypeEqual")) {
                this.jobTypeEqual = KalturaBatchJobType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("jobTypeIn")) {
                this.jobTypeIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("jobTypeNotIn")) {
                this.jobTypeNotIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("jobSubTypeEqual")) {
                this.jobSubTypeEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("jobSubTypeIn")) {
                this.jobSubTypeIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("jobSubTypeNotIn")) {
                this.jobSubTypeNotIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("statusEqual")) {
                this.statusEqual = KalturaBatchJobStatus.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("statusNotIn")) {
                this.statusNotIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("priorityGreaterThanOrEqual")) {
                this.priorityGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("priorityLessThanOrEqual")) {
                this.priorityLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("priorityEqual")) {
                this.priorityEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("priorityIn")) {
                this.priorityIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("priorityNotIn")) {
                this.priorityNotIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("batchVersionGreaterThanOrEqual")) {
                this.batchVersionGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("batchVersionLessThanOrEqual")) {
                this.batchVersionLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("batchVersionEqual")) {
                this.batchVersionEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("queueTimeGreaterThanOrEqual")) {
                this.queueTimeGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("queueTimeLessThanOrEqual")) {
                this.queueTimeLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("finishTimeGreaterThanOrEqual")) {
                this.finishTimeGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("finishTimeLessThanOrEqual")) {
                this.finishTimeLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("errTypeEqual")) {
                this.errTypeEqual = KalturaBatchJobErrorTypes.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("errTypeIn")) {
                this.errTypeIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("errTypeNotIn")) {
                this.errTypeNotIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("errNumberEqual")) {
                this.errNumberEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("errNumberIn")) {
                this.errNumberIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("errNumberNotIn")) {
                this.errNumberNotIn = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("estimatedEffortLessThan")) {
                this.estimatedEffortLessThan = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("estimatedEffortGreaterThan")) {
                this.estimatedEffortGreaterThan = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("urgencyLessThanOrEqual")) {
                this.urgencyLessThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("urgencyGreaterThanOrEqual")) {
                this.urgencyGreaterThanOrEqual = ParseUtils.parseInt(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaBatchJobBaseFilter");
        kparams.add("idEqual", this.idEqual);
        kparams.add("idGreaterThanOrEqual", this.idGreaterThanOrEqual);
        kparams.add("partnerIdEqual", this.partnerIdEqual);
        kparams.add("partnerIdIn", this.partnerIdIn);
        kparams.add("partnerIdNotIn", this.partnerIdNotIn);
        kparams.add("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.add("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.add("updatedAtGreaterThanOrEqual", this.updatedAtGreaterThanOrEqual);
        kparams.add("updatedAtLessThanOrEqual", this.updatedAtLessThanOrEqual);
        kparams.add("executionAttemptsGreaterThanOrEqual", this.executionAttemptsGreaterThanOrEqual);
        kparams.add("executionAttemptsLessThanOrEqual", this.executionAttemptsLessThanOrEqual);
        kparams.add("lockVersionGreaterThanOrEqual", this.lockVersionGreaterThanOrEqual);
        kparams.add("lockVersionLessThanOrEqual", this.lockVersionLessThanOrEqual);
        kparams.add("entryIdEqual", this.entryIdEqual);
        kparams.add("jobTypeEqual", this.jobTypeEqual);
        kparams.add("jobTypeIn", this.jobTypeIn);
        kparams.add("jobTypeNotIn", this.jobTypeNotIn);
        kparams.add("jobSubTypeEqual", this.jobSubTypeEqual);
        kparams.add("jobSubTypeIn", this.jobSubTypeIn);
        kparams.add("jobSubTypeNotIn", this.jobSubTypeNotIn);
        kparams.add("statusEqual", this.statusEqual);
        kparams.add("statusIn", this.statusIn);
        kparams.add("statusNotIn", this.statusNotIn);
        kparams.add("priorityGreaterThanOrEqual", this.priorityGreaterThanOrEqual);
        kparams.add("priorityLessThanOrEqual", this.priorityLessThanOrEqual);
        kparams.add("priorityEqual", this.priorityEqual);
        kparams.add("priorityIn", this.priorityIn);
        kparams.add("priorityNotIn", this.priorityNotIn);
        kparams.add("batchVersionGreaterThanOrEqual", this.batchVersionGreaterThanOrEqual);
        kparams.add("batchVersionLessThanOrEqual", this.batchVersionLessThanOrEqual);
        kparams.add("batchVersionEqual", this.batchVersionEqual);
        kparams.add("queueTimeGreaterThanOrEqual", this.queueTimeGreaterThanOrEqual);
        kparams.add("queueTimeLessThanOrEqual", this.queueTimeLessThanOrEqual);
        kparams.add("finishTimeGreaterThanOrEqual", this.finishTimeGreaterThanOrEqual);
        kparams.add("finishTimeLessThanOrEqual", this.finishTimeLessThanOrEqual);
        kparams.add("errTypeEqual", this.errTypeEqual);
        kparams.add("errTypeIn", this.errTypeIn);
        kparams.add("errTypeNotIn", this.errTypeNotIn);
        kparams.add("errNumberEqual", this.errNumberEqual);
        kparams.add("errNumberIn", this.errNumberIn);
        kparams.add("errNumberNotIn", this.errNumberNotIn);
        kparams.add("estimatedEffortLessThan", this.estimatedEffortLessThan);
        kparams.add("estimatedEffortGreaterThan", this.estimatedEffortGreaterThan);
        kparams.add("urgencyLessThanOrEqual", this.urgencyLessThanOrEqual);
        kparams.add("urgencyGreaterThanOrEqual", this.urgencyGreaterThanOrEqual);
        return kparams;
    }

}

