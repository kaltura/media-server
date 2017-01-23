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
public enum KalturaDropFolderFileErrorCode implements KalturaEnumAsString {
    ERROR_ADDING_BULK_UPLOAD ("dropFolderXmlBulkUpload.ERROR_ADDING_BULK_UPLOAD"),
    ERROR_ADD_CONTENT_RESOURCE ("dropFolderXmlBulkUpload.ERROR_ADD_CONTENT_RESOURCE"),
    ERROR_IN_BULK_UPLOAD ("dropFolderXmlBulkUpload.ERROR_IN_BULK_UPLOAD"),
    ERROR_WRITING_TEMP_FILE ("dropFolderXmlBulkUpload.ERROR_WRITING_TEMP_FILE"),
    LOCAL_FILE_WRONG_CHECKSUM ("dropFolderXmlBulkUpload.LOCAL_FILE_WRONG_CHECKSUM"),
    LOCAL_FILE_WRONG_SIZE ("dropFolderXmlBulkUpload.LOCAL_FILE_WRONG_SIZE"),
    MALFORMED_XML_FILE ("dropFolderXmlBulkUpload.MALFORMED_XML_FILE"),
    XML_FILE_SIZE_EXCEED_LIMIT ("dropFolderXmlBulkUpload.XML_FILE_SIZE_EXCEED_LIMIT"),
    ERROR_UPDATE_ENTRY ("1"),
    ERROR_ADD_ENTRY ("2"),
    FLAVOR_NOT_FOUND ("3"),
    FLAVOR_MISSING_IN_FILE_NAME ("4"),
    SLUG_REGEX_NO_MATCH ("5"),
    ERROR_READING_FILE ("6"),
    ERROR_DOWNLOADING_FILE ("7"),
    ERROR_UPDATE_FILE ("8"),
    ERROR_ADDING_CONTENT_PROCESSOR ("10"),
    ERROR_IN_CONTENT_PROCESSOR ("11"),
    ERROR_DELETING_FILE ("12"),
    FILE_NO_MATCH ("13");

    public String hashCode;

    KalturaDropFolderFileErrorCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaDropFolderFileErrorCode get(String hashCode) {
        if (hashCode.equals("dropFolderXmlBulkUpload.ERROR_ADDING_BULK_UPLOAD"))
        {
           return ERROR_ADDING_BULK_UPLOAD;
        }
        else 
        if (hashCode.equals("dropFolderXmlBulkUpload.ERROR_ADD_CONTENT_RESOURCE"))
        {
           return ERROR_ADD_CONTENT_RESOURCE;
        }
        else 
        if (hashCode.equals("dropFolderXmlBulkUpload.ERROR_IN_BULK_UPLOAD"))
        {
           return ERROR_IN_BULK_UPLOAD;
        }
        else 
        if (hashCode.equals("dropFolderXmlBulkUpload.ERROR_WRITING_TEMP_FILE"))
        {
           return ERROR_WRITING_TEMP_FILE;
        }
        else 
        if (hashCode.equals("dropFolderXmlBulkUpload.LOCAL_FILE_WRONG_CHECKSUM"))
        {
           return LOCAL_FILE_WRONG_CHECKSUM;
        }
        else 
        if (hashCode.equals("dropFolderXmlBulkUpload.LOCAL_FILE_WRONG_SIZE"))
        {
           return LOCAL_FILE_WRONG_SIZE;
        }
        else 
        if (hashCode.equals("dropFolderXmlBulkUpload.MALFORMED_XML_FILE"))
        {
           return MALFORMED_XML_FILE;
        }
        else 
        if (hashCode.equals("dropFolderXmlBulkUpload.XML_FILE_SIZE_EXCEED_LIMIT"))
        {
           return XML_FILE_SIZE_EXCEED_LIMIT;
        }
        else 
        if (hashCode.equals("1"))
        {
           return ERROR_UPDATE_ENTRY;
        }
        else 
        if (hashCode.equals("2"))
        {
           return ERROR_ADD_ENTRY;
        }
        else 
        if (hashCode.equals("3"))
        {
           return FLAVOR_NOT_FOUND;
        }
        else 
        if (hashCode.equals("4"))
        {
           return FLAVOR_MISSING_IN_FILE_NAME;
        }
        else 
        if (hashCode.equals("5"))
        {
           return SLUG_REGEX_NO_MATCH;
        }
        else 
        if (hashCode.equals("6"))
        {
           return ERROR_READING_FILE;
        }
        else 
        if (hashCode.equals("7"))
        {
           return ERROR_DOWNLOADING_FILE;
        }
        else 
        if (hashCode.equals("8"))
        {
           return ERROR_UPDATE_FILE;
        }
        else 
        if (hashCode.equals("10"))
        {
           return ERROR_ADDING_CONTENT_PROCESSOR;
        }
        else 
        if (hashCode.equals("11"))
        {
           return ERROR_IN_CONTENT_PROCESSOR;
        }
        else 
        if (hashCode.equals("12"))
        {
           return ERROR_DELETING_FILE;
        }
        else 
        if (hashCode.equals("13"))
        {
           return FILE_NO_MATCH;
        }
        else 
        {
           return ERROR_ADDING_BULK_UPLOAD;
        }
    }
}
