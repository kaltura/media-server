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

@SuppressWarnings("serial")
public class KalturaUploadTokenService extends KalturaServiceBase {
    public KalturaUploadTokenService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaUploadToken add() throws KalturaApiException {
        return this.add(null);
    }

	/**  Adds new upload token to upload a file  */
    public KalturaUploadToken add(KalturaUploadToken uploadToken) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("uploadToken", uploadToken);
        this.kalturaClient.queueServiceCall("uploadtoken", "add", kparams, KalturaUploadToken.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaUploadToken.class, resultXmlElement);
    }

	/**  Get upload token by id  */
    public KalturaUploadToken get(String uploadTokenId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("uploadTokenId", uploadTokenId);
        this.kalturaClient.queueServiceCall("uploadtoken", "get", kparams, KalturaUploadToken.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaUploadToken.class, resultXmlElement);
    }

    public KalturaUploadToken upload(String uploadTokenId, KalturaFile fileData) throws KalturaApiException {
        return this.upload(uploadTokenId, fileData, false);
    }

    public KalturaUploadToken upload(String uploadTokenId, File fileData) throws KalturaApiException {
        return this.upload(uploadTokenId, new KalturaFile(fileData), false);
    }

    public KalturaUploadToken upload(String uploadTokenId, InputStream fileData, String fileDataName, long fileDataSize) throws KalturaApiException {
        return this.upload(uploadTokenId, new KalturaFile(fileData, fileDataName, fileDataSize), false);
    }

    public KalturaUploadToken upload(String uploadTokenId, FileInputStream fileData, String fileDataName) throws KalturaApiException {
        return this.upload(uploadTokenId, new KalturaFile(fileData, fileDataName), false);
    }

    public KalturaUploadToken upload(String uploadTokenId, KalturaFile fileData, boolean resume) throws KalturaApiException {
        return this.upload(uploadTokenId, fileData, resume, true);
    }

    public KalturaUploadToken upload(String uploadTokenId, File fileData, boolean resume) throws KalturaApiException {
        return this.upload(uploadTokenId, new KalturaFile(fileData), resume, true);
    }

    public KalturaUploadToken upload(String uploadTokenId, InputStream fileData, String fileDataName, long fileDataSize, boolean resume) throws KalturaApiException {
        return this.upload(uploadTokenId, new KalturaFile(fileData, fileDataName, fileDataSize), resume, true);
    }

    public KalturaUploadToken upload(String uploadTokenId, FileInputStream fileData, String fileDataName, boolean resume) throws KalturaApiException {
        return this.upload(uploadTokenId, new KalturaFile(fileData, fileDataName), resume, true);
    }

    public KalturaUploadToken upload(String uploadTokenId, KalturaFile fileData, boolean resume, boolean finalChunk) throws KalturaApiException {
        return this.upload(uploadTokenId, fileData, resume, finalChunk, -1);
    }

    public KalturaUploadToken upload(String uploadTokenId, File fileData, boolean resume, boolean finalChunk) throws KalturaApiException {
        return this.upload(uploadTokenId, new KalturaFile(fileData), resume, finalChunk, -1);
    }

    public KalturaUploadToken upload(String uploadTokenId, InputStream fileData, String fileDataName, long fileDataSize, boolean resume, boolean finalChunk) throws KalturaApiException {
        return this.upload(uploadTokenId, new KalturaFile(fileData, fileDataName, fileDataSize), resume, finalChunk, -1);
    }

    public KalturaUploadToken upload(String uploadTokenId, FileInputStream fileData, String fileDataName, boolean resume, boolean finalChunk) throws KalturaApiException {
        return this.upload(uploadTokenId, new KalturaFile(fileData, fileDataName), resume, finalChunk, -1);
    }

    public KalturaUploadToken upload(String uploadTokenId, File fileData, boolean resume, boolean finalChunk, double resumeAt) throws KalturaApiException {
        return this.upload(uploadTokenId, new KalturaFile(fileData), resume, finalChunk, resumeAt);
    }

    public KalturaUploadToken upload(String uploadTokenId, InputStream fileData, String fileDataName, long fileDataSize, boolean resume, boolean finalChunk, double resumeAt) throws KalturaApiException {
        return this.upload(uploadTokenId, new KalturaFile(fileData, fileDataName, fileDataSize), resume, finalChunk, resumeAt);
    }

    public KalturaUploadToken upload(String uploadTokenId, FileInputStream fileData, String fileDataName, boolean resume, boolean finalChunk, double resumeAt) throws KalturaApiException {
        return this.upload(uploadTokenId, new KalturaFile(fileData, fileDataName), resume, finalChunk, resumeAt);
    }

	/**  Upload a file using the upload token id, returns an error on failure (an
	  exception will be thrown when using one of the Kaltura clients)   Chunks can be
	  uploaded in parallel and they will be appended according to their resumeAt
	  position.   A parallel upload session should have three stages:   1. A single
	  upload with resume=false and finalChunk=false   2. Parallel upload requests each
	  with resume=true,finalChunk=false and the expected resumetAt position.   If a
	  chunk fails to upload it can be re-uploaded.   3. After all of the chunks have
	  been uploaded a final chunk (can be of zero size) should be uploaded    with
	  resume=true, finalChunk=true and the expected resumeAt position. In case an
	  UPLOAD_TOKEN_CANNOT_MATCH_EXPECTED_SIZE exception   has been returned
	  (indicating not all of the chunks were appended yet) the final request can be
	  retried.  */
    public KalturaUploadToken upload(String uploadTokenId, KalturaFile fileData, boolean resume, boolean finalChunk, double resumeAt) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("uploadTokenId", uploadTokenId);
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.add("fileData", fileData);
        kparams.add("resume", resume);
        kparams.add("finalChunk", finalChunk);
        kparams.add("resumeAt", resumeAt);
        this.kalturaClient.queueServiceCall("uploadtoken", "upload", kparams, kfiles, KalturaUploadToken.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaUploadToken.class, resultXmlElement);
    }

	/**  Deletes the upload token by upload token id  */
    public void delete(String uploadTokenId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("uploadTokenId", uploadTokenId);
        this.kalturaClient.queueServiceCall("uploadtoken", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return ;
        this.kalturaClient.doQueue();
    }

    public KalturaUploadTokenListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaUploadTokenListResponse list(KalturaUploadTokenFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

	/**  List upload token by filter with pager support.    When using a user session the
	  service will be restricted to users objects only.  */
    public KalturaUploadTokenListResponse list(KalturaUploadTokenFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("filter", filter);
        kparams.add("pager", pager);
        this.kalturaClient.queueServiceCall("uploadtoken", "list", kparams, KalturaUploadTokenListResponse.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaUploadTokenListResponse.class, resultXmlElement);
    }
}
