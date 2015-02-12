package com.kaltura.media.server.wowza;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.kaltura.infra.StringUtils;
import com.kaltura.infra.XmlUtils;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.medialist.MediaList;
import com.wowza.wms.medialist.MediaListRendition;
import com.wowza.wms.medialist.MediaListSegment;
import com.wowza.wms.util.MediaListUtils;

public class SmilManager {

	protected final static String SMIL_VIDEOS_XPATH = "/smil/body/switch";
	protected final static String SMIL_VIDEOS_IDENTIFIER = "src";

	protected static Logger logger = Logger.getLogger(SmilManager.class);

	protected static Map<String, Set<String>> smils = new HashMap<String, Set<String>>();
	
	public static void removeSmils(String entryId) {
		synchronized(smils){
			if(smils.containsKey(entryId)){
				smils.remove(entryId);
			}
		}
	}
	
	public static void removeGroupSmils(String entryId, String groupName) {
		synchronized(smils){
			if(smils.containsKey(entryId)){
				for (String filePath : smils.get(entryId)) {
					if (filePath.endsWith(entryId + "_" + groupName + ".smil")) {
						File file = new File(filePath);
						if(file.exists()){
							file.delete();
						}
						smils.get(entryId).remove(filePath);
					}
				}
			}
		}
	}

	public static void updateSmils(String entryId) {
		Date date = new Date();
		long now = date.getTime();
		File file;
		
		synchronized(smils){
			if(!smils.containsKey(entryId)){
				return;
			}
				
			Set<String> files = smils.get(entryId);
			for(String filePath : files){
				file = new File(filePath);
				if(file.exists()){
					file.setLastModified(now);
				}
			}
		}
	}

	protected static void addSmil(String entryId, String filePath) {
		Set<String> files;
		synchronized(smils){
			if(smils.containsKey(entryId)){
				files = smils.get(entryId);
			}
			else{
				files = new HashSet<String>();
				smils.put(entryId, files);
			}
			files.add(filePath);
		}
	}

	public static synchronized void merge(IApplicationInstance appInstance, String entryId, String filePath, String[] flavorParamsIds) {

		List<String> renditions = new ArrayList<String>();
		for(String flavorParamsId : flavorParamsIds){
			renditions.add("mp4:" + entryId + "_" + flavorParamsId);
		}

		String smil = "<smil>\n";
		smil += "	<head></head>\n";
		smil += "	<body>\n";
		smil += "		<switch>\n";
		
		MediaList mediaList = MediaListUtils.parseMediaList(appInstance, entryId + "_all.smil", "smil", null);
		MediaListSegment mediaListSegment = mediaList.getFirstSegment();
		for(MediaListRendition rendition : mediaListSegment.getRenditions()){
			if(renditions.contains(rendition.getName())){
				smil += rendition.toSMILString();
			}
		}
		
		smil += "		</switch>\n";
		smil += "	</body>\n";
		smil += "</smil>";

		try {
			PrintWriter out = new PrintWriter(filePath);
			out.print(smil);
			out.close();
			
			addSmil(entryId, filePath);

			logger.info("Generated smil file [" + filePath + "]");
		} catch (FileNotFoundException e) {
			logger.error("Failed writing to file [" + filePath + "]: " + e.getMessage());
		}
	}

	public static synchronized void generate(IApplicationInstance appInstance, String entryId, String destGroupName, Map<String, Long> bitrates) {

		String smil = "<smil>\n";
		smil += "	<head></head>\n";
		smil += "	<body>\n";
		smil += "		<switch>\n";
		
		long bitrate;
		for(String rendition : bitrates.keySet()){
			bitrate = bitrates.get(rendition);
			smil += "			<video src=\"" + rendition + "\" system-bitrate=\"" + bitrate + "\" />\n";				
		}
		
		smil += "		</switch>\n";
		smil += "	</body>\n";
		smil += "</smil>";

		String filePath = appInstance.getStreamStoragePath() + File.separator + destGroupName + ".smil";
		try {
			PrintWriter out = new PrintWriter(filePath);
			out.print(smil);
			out.close();
			
			addSmil(entryId, filePath);

			logger.info("Generated smil file [" + filePath + "]");
		} catch (FileNotFoundException e) {
			logger.error("Failed writing to file [" + filePath + "]: " + e.getMessage());
		}
	}
	
	public static synchronized void generate(IApplicationInstance appInstance, String entryId, String destGroupName, String sourceName) {
		String appName = appInstance.getContextStr();
		logger.debug("Generate [" + appName + "/" + destGroupName + "] from source [" + sourceName + "]");

		MediaList mediaList = MediaListUtils.parseMediaList(appInstance, sourceName, "ngrp", null);
		if (mediaList == null) {
			logger.error("MediaList not found: " + appName + "/" + sourceName);
			return;
		}

		String smil = mediaList.toSMILString();
		save(appInstance, entryId, destGroupName, smil);
	}
	
	protected static synchronized void save(IApplicationInstance appInstance, String entryId, String destGroupName, String smil) {
		String appName = appInstance.getContextStr();
		
		String filePath = appInstance.getStreamStoragePath() + File.separator + destGroupName + ".smil";
		File file = new File(filePath);
		if(file.exists()){
			File tmpFile;
			try {
				tmpFile = File.createTempFile(destGroupName, ".smil");
				PrintWriter out = new PrintWriter(tmpFile);
				out.print(smil);
				out.close();
			} catch (Exception e) {
				logger.error("Failed writing to temp file [" + filePath + "]: " + e.getMessage());
				return;
			}
			
			try {
				XmlUtils.merge(file, SmilManager.SMIL_VIDEOS_XPATH, SmilManager.SMIL_VIDEOS_IDENTIFIER, file, tmpFile);
			} catch (Exception e) {
				logger.error("Failed merging files [" + filePath + ", " + tmpFile.getAbsolutePath() + "]: " + e.getMessage());
				return;
			}
			
			tmpFile.delete();
		}
		else{
			try {
				PrintWriter out = new PrintWriter(file);
				out.print(smil);
				out.close();
			} catch (FileNotFoundException e) {
				logger.error("Failed writing to file [" + filePath + "]: " + e.getMessage());
				return;
			}
		}
		
		addSmil(entryId, filePath);

		logger.info("Created smil file [" + filePath + "] for stream " + appName + "/" + destGroupName + ":\n" + smil + "\n\n");
	}

	public static void generateCombinations(IApplicationInstance appInstance, String entryId, String streamStoragePath, Integer[] assetParamsIds) {
		SortedSet<Integer> assetParamsIdsSubset;
		for(int i = 0; i < assetParamsIds.length; i++){
			assetParamsIdsSubset = new TreeSet<Integer>();
			for(int j = 0; j < assetParamsIds.length; j++){
				// Add all assetParamsIds except for i
				if(j != i){
					assetParamsIdsSubset.add(assetParamsIds[j]);
				}
			}
			String flavorsStr = StringUtils.join(assetParamsIdsSubset, "_");
			String filePath = streamStoragePath + File.separator + entryId + "_" + flavorsStr + ".smil";
			merge(appInstance, entryId, filePath, flavorsStr.split("_"));
			
			if(assetParamsIds.length > 3){
				generateCombinations(appInstance, entryId, streamStoragePath, assetParamsIdsSubset.toArray(new Integer[0]));
			}
		}
	}
}
