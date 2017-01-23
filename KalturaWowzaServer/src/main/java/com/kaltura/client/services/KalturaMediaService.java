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
import java.util.ArrayList;
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

/**  Media service lets you upload and manage media files (images / videos &amp;
  audio)  */
@SuppressWarnings("serial")
public class KalturaMediaService extends KalturaServiceBase {
    public KalturaMediaService(KalturaClient client) {
        this.kalturaClient = client;
    }

	/**  Add entry  */
    public KalturaMediaEntry add(KalturaMediaEntry entry) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("entry", entry);
        this.kalturaClient.queueServiceCall("media", "add", kparams, KalturaMediaEntry.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMediaEntry.class, resultXmlElement);
    }

    public KalturaMediaEntry addContent(String entryId) throws KalturaApiException {
        return this.addContent(entryId, null);
    }

	/**  Add content to media entry which is not yet associated with content (therefore
	  is in status NO_CONTENT).      If the requirement is to replace the entry's
	  associated content, use action updateContent.  */
    public KalturaMediaEntry addContent(String entryId, KalturaResource resource) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("entryId", entryId);
        kparams.add("resource", resource);
        this.kalturaClient.queueServiceCall("media", "addContent", kparams, KalturaMediaEntry.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMediaEntry.class, resultXmlElement);
    }

	/**  Adds new media entry by importing an HTTP or FTP URL.   The entry will be queued
	  for import and then for conversion.   This action should be exposed only to the
	  batches  */
    public KalturaMediaEntry addFromBulk(KalturaMediaEntry mediaEntry, String url, int bulkUploadId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("mediaEntry", mediaEntry);
        kparams.add("url", url);
        kparams.add("bulkUploadId", bulkUploadId);
        this.kalturaClient.queueServiceCall("media", "addFromBulk", kparams, KalturaMediaEntry.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMediaEntry.class, resultXmlElement);
    }

	/**  Adds new media entry by importing an HTTP or FTP URL.   The entry will be queued
	  for import and then for conversion.  */
    public KalturaMediaEntry addFromUrl(KalturaMediaEntry mediaEntry, String url) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("mediaEntry", mediaEntry);
        kparams.add("url", url);
        this.kalturaClient.queueServiceCall("media", "addFromUrl", kparams, KalturaMediaEntry.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMediaEntry.class, resultXmlElement);
    }

    public KalturaMediaEntry addFromSearchResult() throws KalturaApiException {
        return this.addFromSearchResult(null);
    }

    public KalturaMediaEntry addFromSearchResult(KalturaMediaEntry mediaEntry) throws KalturaApiException {
        return this.addFromSearchResult(mediaEntry, null);
    }

	/**  Adds new media entry by importing the media file from a search provider.   This
	  action should be used with the search service result.  */
    public KalturaMediaEntry addFromSearchResult(KalturaMediaEntry mediaEntry, KalturaSearchResult searchResult) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("mediaEntry", mediaEntry);
        kparams.add("searchResult", searchResult);
        this.kalturaClient.queueServiceCall("media", "addFromSearchResult", kparams, KalturaMediaEntry.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMediaEntry.class, resultXmlElement);
    }

	/**  Add new entry after the specific media file was uploaded and the upload token id
	  exists  */
    public KalturaMediaEntry addFromUploadedFile(KalturaMediaEntry mediaEntry, String uploadTokenId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("mediaEntry", mediaEntry);
        kparams.add("uploadTokenId", uploadTokenId);
        this.kalturaClient.queueServiceCall("media", "addFromUploadedFile", kparams, KalturaMediaEntry.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMediaEntry.class, resultXmlElement);
    }

	/**  Add new entry after the file was recored on the server and the token id exists  */
    public KalturaMediaEntry addFromRecordedWebcam(KalturaMediaEntry mediaEntry, String webcamTokenId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("mediaEntry", mediaEntry);
        kparams.add("webcamTokenId", webcamTokenId);
        this.kalturaClient.queueServiceCall("media", "addFromRecordedWebcam", kparams, KalturaMediaEntry.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMediaEntry.class, resultXmlElement);
    }

    public KalturaMediaEntry addFromEntry(String sourceEntryId) throws KalturaApiException {
        return this.addFromEntry(sourceEntryId, null);
    }

    public KalturaMediaEntry addFromEntry(String sourceEntryId, KalturaMediaEntry mediaEntry) throws KalturaApiException {
        return this.addFromEntry(sourceEntryId, mediaEntry, Integer.MIN_VALUE);
    }

	/**  Copy entry into new entry  */
    public KalturaMediaEntry addFromEntry(String sourceEntryId, KalturaMediaEntry mediaEntry, int sourceFlavorParamsId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("sourceEntryId", sourceEntryId);
        kparams.add("mediaEntry", mediaEntry);
        kparams.add("sourceFlavorParamsId", sourceFlavorParamsId);
        this.kalturaClient.queueServiceCall("media", "addFromEntry", kparams, KalturaMediaEntry.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMediaEntry.class, resultXmlElement);
    }

    public KalturaMediaEntry addFromFlavorAsset(String sourceFlavorAssetId) throws KalturaApiException {
        return this.addFromFlavorAsset(sourceFlavorAssetId, null);
    }

	/**  Copy flavor asset into new entry  */
    public KalturaMediaEntry addFromFlavorAsset(String sourceFlavorAssetId, KalturaMediaEntry mediaEntry) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("sourceFlavorAssetId", sourceFlavorAssetId);
        kparams.add("mediaEntry", mediaEntry);
        this.kalturaClient.queueServiceCall("media", "addFromFlavorAsset", kparams, KalturaMediaEntry.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMediaEntry.class, resultXmlElement);
    }

    public long convert(String entryId) throws KalturaApiException {
        return this.convert(entryId, Integer.MIN_VALUE);
    }

    public long convert(String entryId, int conversionProfileId) throws KalturaApiException {
        return this.convert(entryId, conversionProfileId, null);
    }

	/**  Convert entry  */
    public long convert(String entryId, int conversionProfileId, ArrayList<KalturaConversionAttribute> dynamicConversionAttributes) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("entryId", entryId);
        kparams.add("conversionProfileId", conversionProfileId);
        kparams.add("dynamicConversionAttributes", dynamicConversionAttributes);
        this.kalturaClient.queueServiceCall("media", "convert", kparams);
        if (this.kalturaClient.isMultiRequest())
            return 0;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = resultXmlElement.getTextContent();
        return ParseUtils.parseBigint(resultText);
    }

    public KalturaMediaEntry get(String entryId) throws KalturaApiException {
        return this.get(entryId, -1);
    }

	/**  Get media entry by ID.  */
    public KalturaMediaEntry get(String entryId, int version) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("entryId", entryId);
        kparams.add("version", version);
        this.kalturaClient.queueServiceCall("media", "get", kparams, KalturaMediaEntry.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMediaEntry.class, resultXmlElement);
    }

    public String getMrss(String entryId) throws KalturaApiException {
        return this.getMrss(entryId, null);
    }

    public String getMrss(String entryId, ArrayList<KalturaExtendingItemMrssParameter> extendingItemsArray) throws KalturaApiException {
        return this.getMrss(entryId, extendingItemsArray, null);
    }

	/**  Get MRSS by entry id      XML will return as an escaped string  */
    public String getMrss(String entryId, ArrayList<KalturaExtendingItemMrssParameter> extendingItemsArray, String features) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("entryId", entryId);
        kparams.add("extendingItemsArray", extendingItemsArray);
        kparams.add("features", features);
        this.kalturaClient.queueServiceCall("media", "getMrss", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = resultXmlElement.getTextContent();
        return ParseUtils.parseString(resultText);
    }

	/**  Update media entry. Only the properties that were set will be updated.  */
    public KalturaMediaEntry update(String entryId, KalturaMediaEntry mediaEntry) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("entryId", entryId);
        kparams.add("mediaEntry", mediaEntry);
        this.kalturaClient.queueServiceCall("media", "update", kparams, KalturaMediaEntry.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMediaEntry.class, resultXmlElement);
    }

    public KalturaMediaEntry updateContent(String entryId, KalturaResource resource) throws KalturaApiException {
        return this.updateContent(entryId, resource, Integer.MIN_VALUE);
    }

    public KalturaMediaEntry updateContent(String entryId, KalturaResource resource, int conversionProfileId) throws KalturaApiException {
        return this.updateContent(entryId, resource, conversionProfileId, null);
    }

	/**  Replace content associated with the media entry.  */
    public KalturaMediaEntry updateContent(String entryId, KalturaResource resource, int conversionProfileId, KalturaEntryReplacementOptions advancedOptions) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("entryId", entryId);
        kparams.add("resource", resource);
        kparams.add("conversionProfileId", conversionProfileId);
        kparams.add("advancedOptions", advancedOptions);
        this.kalturaClient.queueServiceCall("media", "updateContent", kparams, KalturaMediaEntry.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMediaEntry.class, resultXmlElement);
    }

	/**  Delete a media entry.  */
    public void delete(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("entryId", entryId);
        this.kalturaClient.queueServiceCall("media", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return ;
        this.kalturaClient.doQueue();
    }

	/**  Approves media replacement  */
    public KalturaMediaEntry approveReplace(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("entryId", entryId);
        this.kalturaClient.queueServiceCall("media", "approveReplace", kparams, KalturaMediaEntry.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMediaEntry.class, resultXmlElement);
    }

	/**  Cancels media replacement  */
    public KalturaMediaEntry cancelReplace(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("entryId", entryId);
        this.kalturaClient.queueServiceCall("media", "cancelReplace", kparams, KalturaMediaEntry.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMediaEntry.class, resultXmlElement);
    }

    public KalturaMediaListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaMediaListResponse list(KalturaMediaEntryFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

	/**  List media entries by filter with paging support.  */
    public KalturaMediaListResponse list(KalturaMediaEntryFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("filter", filter);
        kparams.add("pager", pager);
        this.kalturaClient.queueServiceCall("media", "list", kparams, KalturaMediaListResponse.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMediaListResponse.class, resultXmlElement);
    }

    public int count() throws KalturaApiException {
        return this.count(null);
    }

	/**  Count media entries by filter.  */
    public int count(KalturaMediaEntryFilter filter) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("filter", filter);
        this.kalturaClient.queueServiceCall("media", "count", kparams);
        if (this.kalturaClient.isMultiRequest())
            return 0;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = resultXmlElement.getTextContent();
        return ParseUtils.parseInt(resultText);
    }

    public String upload(File fileData) throws KalturaApiException {
        return this.upload(new KalturaFile(fileData));
    }

    public String upload(InputStream fileData, String fileDataName, long fileDataSize) throws KalturaApiException {
        return this.upload(new KalturaFile(fileData, fileDataName, fileDataSize));
    }

    public String upload(FileInputStream fileData, String fileDataName) throws KalturaApiException {
        return this.upload(new KalturaFile(fileData, fileDataName));
    }

	/**  Upload a media file to Kaltura, then the file can be used to create a media
	  entry.  */
    public String upload(KalturaFile fileData) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.add("fileData", fileData);
        this.kalturaClient.queueServiceCall("media", "upload", kparams, kfiles);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = resultXmlElement.getTextContent();
        return ParseUtils.parseString(resultText);
    }

    public KalturaMediaEntry updateThumbnail(String entryId, int timeOffset) throws KalturaApiException {
        return this.updateThumbnail(entryId, timeOffset, Integer.MIN_VALUE);
    }

	/**  Update media entry thumbnail by a specified time offset (In seconds)   If flavor
	  params id not specified, source flavor will be used by default  */
    public KalturaMediaEntry updateThumbnail(String entryId, int timeOffset, int flavorParamsId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("entryId", entryId);
        kparams.add("timeOffset", timeOffset);
        kparams.add("flavorParamsId", flavorParamsId);
        this.kalturaClient.queueServiceCall("media", "updateThumbnail", kparams, KalturaMediaEntry.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMediaEntry.class, resultXmlElement);
    }

    public KalturaMediaEntry updateThumbnailFromSourceEntry(String entryId, String sourceEntryId, int timeOffset) throws KalturaApiException {
        return this.updateThumbnailFromSourceEntry(entryId, sourceEntryId, timeOffset, Integer.MIN_VALUE);
    }

	/**  Update media entry thumbnail from a different entry by a specified time offset
	  (In seconds)   If flavor params id not specified, source flavor will be used by
	  default  */
    public KalturaMediaEntry updateThumbnailFromSourceEntry(String entryId, String sourceEntryId, int timeOffset, int flavorParamsId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("entryId", entryId);
        kparams.add("sourceEntryId", sourceEntryId);
        kparams.add("timeOffset", timeOffset);
        kparams.add("flavorParamsId", flavorParamsId);
        this.kalturaClient.queueServiceCall("media", "updateThumbnailFromSourceEntry", kparams, KalturaMediaEntry.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMediaEntry.class, resultXmlElement);
    }

    public KalturaMediaEntry updateThumbnailJpeg(String entryId, File fileData) throws KalturaApiException {
        return this.updateThumbnailJpeg(entryId, new KalturaFile(fileData));
    }

    public KalturaMediaEntry updateThumbnailJpeg(String entryId, InputStream fileData, String fileDataName, long fileDataSize) throws KalturaApiException {
        return this.updateThumbnailJpeg(entryId, new KalturaFile(fileData, fileDataName, fileDataSize));
    }

    public KalturaMediaEntry updateThumbnailJpeg(String entryId, FileInputStream fileData, String fileDataName) throws KalturaApiException {
        return this.updateThumbnailJpeg(entryId, new KalturaFile(fileData, fileDataName));
    }

	/**  Update media entry thumbnail using a raw jpeg file  */
    public KalturaMediaEntry updateThumbnailJpeg(String entryId, KalturaFile fileData) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("entryId", entryId);
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.add("fileData", fileData);
        this.kalturaClient.queueServiceCall("media", "updateThumbnailJpeg", kparams, kfiles, KalturaMediaEntry.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaMediaEntry.class, resultXmlElement);
    }

	/**  Update entry thumbnail using url  */
    public KalturaBaseEntry updateThumbnailFromUrl(String entryId, String url) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("entryId", entryId);
        kparams.add("url", url);
        this.kalturaClient.queueServiceCall("media", "updateThumbnailFromUrl", kparams, KalturaBaseEntry.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaBaseEntry.class, resultXmlElement);
    }

	/**  Request a new conversion job, this can be used to convert the media entry to a
	  different format  */
    public int requestConversion(String entryId, String fileFormat) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("entryId", entryId);
        kparams.add("fileFormat", fileFormat);
        this.kalturaClient.queueServiceCall("media", "requestConversion", kparams);
        if (this.kalturaClient.isMultiRequest())
            return 0;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = resultXmlElement.getTextContent();
        return ParseUtils.parseInt(resultText);
    }

	/**  Flag inappropriate media entry for moderation  */
    public void flag(KalturaModerationFlag moderationFlag) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("moderationFlag", moderationFlag);
        this.kalturaClient.queueServiceCall("media", "flag", kparams);
        if (this.kalturaClient.isMultiRequest())
            return ;
        this.kalturaClient.doQueue();
    }

	/**  Reject the media entry and mark the pending flags (if any) as moderated (this
	  will make the entry non playable)  */
    public void reject(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("entryId", entryId);
        this.kalturaClient.queueServiceCall("media", "reject", kparams);
        if (this.kalturaClient.isMultiRequest())
            return ;
        this.kalturaClient.doQueue();
    }

	/**  Approve the media entry and mark the pending flags (if any) as moderated (this
	  will make the entry playable)  */
    public void approve(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("entryId", entryId);
        this.kalturaClient.queueServiceCall("media", "approve", kparams);
        if (this.kalturaClient.isMultiRequest())
            return ;
        this.kalturaClient.doQueue();
    }

    public KalturaModerationFlagListResponse listFlags(String entryId) throws KalturaApiException {
        return this.listFlags(entryId, null);
    }

	/**  List all pending flags for the media entry  */
    public KalturaModerationFlagListResponse listFlags(String entryId, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("entryId", entryId);
        kparams.add("pager", pager);
        this.kalturaClient.queueServiceCall("media", "listFlags", kparams, KalturaModerationFlagListResponse.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaModerationFlagListResponse.class, resultXmlElement);
    }

	/**  Anonymously rank a media entry, no validation is done on duplicate rankings  */
    public void anonymousRank(String entryId, int rank) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.add("entryId", entryId);
        kparams.add("rank", rank);
        this.kalturaClient.queueServiceCall("media", "anonymousRank", kparams);
        if (this.kalturaClient.isMultiRequest())
            return ;
        this.kalturaClient.doQueue();
    }

    public KalturaBulkUpload bulkUploadAdd(KalturaFile fileData) throws KalturaApiException {
        return this.bulkUploadAdd(fileData, null);
    }

    public KalturaBulkUpload bulkUploadAdd(File fileData) throws KalturaApiException {
        return this.bulkUploadAdd(new KalturaFile(fileData), null);
    }

    public KalturaBulkUpload bulkUploadAdd(InputStream fileData, String fileDataName, long fileDataSize) throws KalturaApiException {
        return this.bulkUploadAdd(new KalturaFile(fileData, fileDataName, fileDataSize), null);
    }

    public KalturaBulkUpload bulkUploadAdd(FileInputStream fileData, String fileDataName) throws KalturaApiException {
        return this.bulkUploadAdd(new KalturaFile(fileData, fileDataName), null);
    }

    public KalturaBulkUpload bulkUploadAdd(KalturaFile fileData, KalturaBulkUploadJobData bulkUploadData) throws KalturaApiException {
        return this.bulkUploadAdd(fileData, bulkUploadData, null);
    }

    public KalturaBulkUpload bulkUploadAdd(File fileData, KalturaBulkUploadJobData bulkUploadData) throws KalturaApiException {
        return this.bulkUploadAdd(new KalturaFile(fileData), bulkUploadData, null);
    }

    public KalturaBulkUpload bulkUploadAdd(InputStream fileData, String fileDataName, long fileDataSize, KalturaBulkUploadJobData bulkUploadData) throws KalturaApiException {
        return this.bulkUploadAdd(new KalturaFile(fileData, fileDataName, fileDataSize), bulkUploadData, null);
    }

    public KalturaBulkUpload bulkUploadAdd(FileInputStream fileData, String fileDataName, KalturaBulkUploadJobData bulkUploadData) throws KalturaApiException {
        return this.bulkUploadAdd(new KalturaFile(fileData, fileDataName), bulkUploadData, null);
    }

    public KalturaBulkUpload bulkUploadAdd(File fileData, KalturaBulkUploadJobData bulkUploadData, KalturaBulkUploadEntryData bulkUploadEntryData) throws KalturaApiException {
        return this.bulkUploadAdd(new KalturaFile(fileData), bulkUploadData, bulkUploadEntryData);
    }

    public KalturaBulkUpload bulkUploadAdd(InputStream fileData, String fileDataName, long fileDataSize, KalturaBulkUploadJobData bulkUploadData, KalturaBulkUploadEntryData bulkUploadEntryData) throws KalturaApiException {
        return this.bulkUploadAdd(new KalturaFile(fileData, fileDataName, fileDataSize), bulkUploadData, bulkUploadEntryData);
    }

    public KalturaBulkUpload bulkUploadAdd(FileInputStream fileData, String fileDataName, KalturaBulkUploadJobData bulkUploadData, KalturaBulkUploadEntryData bulkUploadEntryData) throws KalturaApiException {
        return this.bulkUploadAdd(new KalturaFile(fileData, fileDataName), bulkUploadData, bulkUploadEntryData);
    }

	/**  Add new bulk upload batch job   Conversion profile id can be specified in the
	  API or in the CSV file, the one in the CSV file will be stronger.   If no
	  conversion profile was specified, partner's default will be used  */
    public KalturaBulkUpload bulkUploadAdd(KalturaFile fileData, KalturaBulkUploadJobData bulkUploadData, KalturaBulkUploadEntryData bulkUploadEntryData) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.add("fileData", fileData);
        kparams.add("bulkUploadData", bulkUploadData);
        kparams.add("bulkUploadEntryData", bulkUploadEntryData);
        this.kalturaClient.queueServiceCall("media", "bulkUploadAdd", kparams, kfiles, KalturaBulkUpload.class);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return ParseUtils.parseObject(KalturaBulkUpload.class, resultXmlElement);
    }
}
