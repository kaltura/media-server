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
package com.kaltura.client.enums;

/**
 * This class was generated using exec.php
 * against an XML schema provided by Kaltura.
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaBulkUploadType implements KalturaEnumAsString {
    CSV ("bulkUploadCsv.CSV"),
    FILTER ("bulkUploadFilter.FILTER"),
    XML ("bulkUploadXml.XML"),
    DROP_FOLDER_XML ("dropFolderXmlBulkUpload.DROP_FOLDER_XML"),
    ICAL ("scheduleBulkUpload.ICAL"),
    DROP_FOLDER_ICAL ("scheduleDropFolder.DROP_FOLDER_ICAL");

    public String hashCode;

    KalturaBulkUploadType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaBulkUploadType get(String hashCode) {
        if (hashCode.equals("bulkUploadCsv.CSV"))
        {
           return CSV;
        }
        else 
        if (hashCode.equals("bulkUploadFilter.FILTER"))
        {
           return FILTER;
        }
        else 
        if (hashCode.equals("bulkUploadXml.XML"))
        {
           return XML;
        }
        else 
        if (hashCode.equals("dropFolderXmlBulkUpload.DROP_FOLDER_XML"))
        {
           return DROP_FOLDER_XML;
        }
        else 
        if (hashCode.equals("scheduleBulkUpload.ICAL"))
        {
           return ICAL;
        }
        else 
        if (hashCode.equals("scheduleDropFolder.DROP_FOLDER_ICAL"))
        {
           return DROP_FOLDER_ICAL;
        }
        else 
        {
           return CSV;
        }
    }
}
