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
public enum KalturaResponseType implements KalturaEnumAsInt {
    RESPONSE_TYPE_JSON (1),
    RESPONSE_TYPE_XML (2),
    RESPONSE_TYPE_PHP (3),
    RESPONSE_TYPE_PHP_ARRAY (4),
    RESPONSE_TYPE_HTML (7),
    RESPONSE_TYPE_MRSS (8),
    RESPONSE_TYPE_JSONP (9);

    public int hashCode;

    KalturaResponseType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaResponseType get(int hashCode) {
        switch(hashCode) {
            case 1: return RESPONSE_TYPE_JSON;
            case 2: return RESPONSE_TYPE_XML;
            case 3: return RESPONSE_TYPE_PHP;
            case 4: return RESPONSE_TYPE_PHP_ARRAY;
            case 7: return RESPONSE_TYPE_HTML;
            case 8: return RESPONSE_TYPE_MRSS;
            case 9: return RESPONSE_TYPE_JSONP;
            default: return RESPONSE_TYPE_JSON;
        }
    }
}
