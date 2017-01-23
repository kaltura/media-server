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
package com.kaltura.client.services;

import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaServiceBase;
import com.kaltura.client.types.*;
import com.kaltura.client.enums.*;
import org.w3c.dom.Element;
import com.kaltura.client.utils.ParseUtils;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import com.kaltura.client.KalturaFiles;
import com.kaltura.client.KalturaFile;

/**
 * This class was generated using exec.php
 * against an XML schema provided by Kaltura.
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

/**  Metadata service  */
@SuppressWarnings("serial")
public class KalturaMetadataService extends KalturaServiceBase {
    public KalturaMetadataService(KalturaClient client) {
        this.kalturaClient = client;
    }

	/**  Allows you to add a metadata object and metadata content associated with Kaltura
	  object  */
    public KalturaMetadata add(int metadataProfileId, KalturaMetadataObjectType objectType, String objectId, String xmlData) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("metadataProfileId", metadataProfileId);
        kparams.add("objectType", objectType);
        kparams.add("objectId", objectId);
        kparams.add("xmlData", xmlData);
        this.kalturaClient.queueServiceCall("metadata_metadata", "add", kparams, KalturaMetadata.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMetadata.class, resultXmlElement);
    }

    public KalturaMetadata addFromFile(int metadataProfileId, KalturaMetadataObjectType objectType, String objectId, File xmlFile) throws KalturaApiException {
        return this.addFromFile(metadataProfileId, objectType, objectId, new KalturaFile(xmlFile));
    }

    public KalturaMetadata addFromFile(int metadataProfileId, KalturaMetadataObjectType objectType, String objectId, InputStream xmlFile, String xmlFileName, long xmlFileSize) throws KalturaApiException {
        return this.addFromFile(metadataProfileId, objectType, objectId, new KalturaFile(xmlFile, xmlFileName, xmlFileSize));
    }

    public KalturaMetadata addFromFile(int metadataProfileId, KalturaMetadataObjectType objectType, String objectId, FileInputStream xmlFile, String xmlFileName) throws KalturaApiException {
        return this.addFromFile(metadataProfileId, objectType, objectId, new KalturaFile(xmlFile, xmlFileName));
    }

	/**  Allows you to add a metadata object and metadata file associated with Kaltura
	  object  */
    public KalturaMetadata addFromFile(int metadataProfileId, KalturaMetadataObjectType objectType, String objectId, KalturaFile xmlFile) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("metadataProfileId", metadataProfileId);
        kparams.add("objectType", objectType);
        kparams.add("objectId", objectId);
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.add("xmlFile", xmlFile);
        this.kalturaClient.queueServiceCall("metadata_metadata", "addFromFile", kparams, kfiles, KalturaMetadata.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMetadata.class, resultXmlElement);
    }

	/**  Allows you to add a metadata xml data from remote URL  */
    public KalturaMetadata addFromUrl(int metadataProfileId, KalturaMetadataObjectType objectType, String objectId, String url) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("metadataProfileId", metadataProfileId);
        kparams.add("objectType", objectType);
        kparams.add("objectId", objectId);
        kparams.add("url", url);
        this.kalturaClient.queueServiceCall("metadata_metadata", "addFromUrl", kparams, KalturaMetadata.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMetadata.class, resultXmlElement);
    }

	/**  Allows you to add a metadata xml data from remote URL.   Enables different
	  permissions than addFromUrl action.  */
    public KalturaMetadata addFromBulk(int metadataProfileId, KalturaMetadataObjectType objectType, String objectId, String url) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("metadataProfileId", metadataProfileId);
        kparams.add("objectType", objectType);
        kparams.add("objectId", objectId);
        kparams.add("url", url);
        this.kalturaClient.queueServiceCall("metadata_metadata", "addFromBulk", kparams, KalturaMetadata.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMetadata.class, resultXmlElement);
    }

	/**  Retrieve a metadata object by id  */
    public KalturaMetadata get(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("id", id);
        this.kalturaClient.queueServiceCall("metadata_metadata", "get", kparams, KalturaMetadata.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMetadata.class, resultXmlElement);
    }

    public KalturaMetadata update(int id) throws KalturaApiException {
        return this.update(id, null);
    }

    public KalturaMetadata update(int id, String xmlData) throws KalturaApiException {
        return this.update(id, xmlData, Integer.MIN_VALUE);
    }

	/**  Update an existing metadata object with new XML content  */
    public KalturaMetadata update(int id, String xmlData, int version) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("id", id);
        kparams.add("xmlData", xmlData);
        kparams.add("version", version);
        this.kalturaClient.queueServiceCall("metadata_metadata", "update", kparams, KalturaMetadata.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMetadata.class, resultXmlElement);
    }

    public KalturaMetadata updateFromFile(int id) throws KalturaApiException {
        return this.updateFromFile(id, (KalturaFile)null);
    }

    public KalturaMetadata updateFromFile(int id, File xmlFile) throws KalturaApiException {
        return this.updateFromFile(id, new KalturaFile(xmlFile));
    }

    public KalturaMetadata updateFromFile(int id, InputStream xmlFile, String xmlFileName, long xmlFileSize) throws KalturaApiException {
        return this.updateFromFile(id, new KalturaFile(xmlFile, xmlFileName, xmlFileSize));
    }

    public KalturaMetadata updateFromFile(int id, FileInputStream xmlFile, String xmlFileName) throws KalturaApiException {
        return this.updateFromFile(id, new KalturaFile(xmlFile, xmlFileName));
    }

	/**  Update an existing metadata object with new XML file  */
    public KalturaMetadata updateFromFile(int id, KalturaFile xmlFile) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("id", id);
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.add("xmlFile", xmlFile);
        this.kalturaClient.queueServiceCall("metadata_metadata", "updateFromFile", kparams, kfiles, KalturaMetadata.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMetadata.class, resultXmlElement);
    }

    public KalturaMetadataListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaMetadataListResponse list(KalturaMetadataFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

	/**  List metadata objects by filter and pager  */
    public KalturaMetadataListResponse list(KalturaMetadataFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("filter", filter);
        kparams.add("pager", pager);
        this.kalturaClient.queueServiceCall("metadata_metadata", "list", kparams, KalturaMetadataListResponse.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMetadataListResponse.class, resultXmlElement);
    }

	/**  Delete an existing metadata  */
    public void delete(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("id", id);
        this.kalturaClient.queueServiceCall("metadata_metadata", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return ;
        this.kalturaClient.doQueue();
    }

    public void invalidate(int id) throws KalturaApiException {
        this.invalidate(id, Integer.MIN_VALUE);
    }

	/**  Mark existing metadata as invalid   Used by batch metadata transform  */
    public void invalidate(int id, int version) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("id", id);
        kparams.add("version", version);
        this.kalturaClient.queueServiceCall("metadata_metadata", "invalidate", kparams);
        if (this.kalturaClient.isMultiRequest())
            return ;
        this.kalturaClient.doQueue();
    }

	/**  Index metadata by id, will also index the related object  */
    public int index(String id, boolean shouldUpdate) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("id", id);
        kparams.add("shouldUpdate", shouldUpdate);
        this.kalturaClient.queueServiceCall("metadata_metadata", "index", kparams);
        if (this.kalturaClient.isMultiRequest())
            return 0;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = resultXmlElement.getTextContent();
        return ParseUtils.parseInt(resultText);
    }

	/**  Serves metadata XML file  */
    public String serve(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("id", id);
        this.kalturaClient.queueServiceCall("metadata_metadata", "serve", kparams);
        return this.kalturaClient.serve();
    }

    public KalturaMetadata updateFromXSL(int id, File xslFile) throws KalturaApiException {
        return this.updateFromXSL(id, new KalturaFile(xslFile));
    }

    public KalturaMetadata updateFromXSL(int id, InputStream xslFile, String xslFileName, long xslFileSize) throws KalturaApiException {
        return this.updateFromXSL(id, new KalturaFile(xslFile, xslFileName, xslFileSize));
    }

    public KalturaMetadata updateFromXSL(int id, FileInputStream xslFile, String xslFileName) throws KalturaApiException {
        return this.updateFromXSL(id, new KalturaFile(xslFile, xslFileName));
    }

	/**  Action transforms current metadata object XML using a provided XSL.  */
    public KalturaMetadata updateFromXSL(int id, KalturaFile xslFile) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("id", id);
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.add("xslFile", xslFile);
        this.kalturaClient.queueServiceCall("metadata_metadata", "updateFromXSL", kparams, kfiles, KalturaMetadata.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMetadata.class, resultXmlElement);
    }
}
