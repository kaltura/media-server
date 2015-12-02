package com.kaltura.media.quality.validator.info;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import com.kaltura.media.quality.configurations.TestConfig;
import com.kaltura.media.quality.utils.ThreadManager;

public class MediaInfo extends MediaInfoBase {
	private static final Logger log = Logger.getLogger(MediaInfo.class);

	private String mediaInfoPath = null;

	@XmlJavaTypeAdapter(StreamInfoAdapter.class)
	abstract interface IStreamInfo extends MediaInfoBase.IStreamInfo {
	}

	@XmlRootElement
	public static class MediaInfoVideoInfo extends MediaInfoBase.VideoInfo implements IStreamInfo {
		private int id;

		private int menuId;
		
		private String format;
		
		private String formatInfo;
		
		private String formatProfile;

		private String formatSettingsCABAC;
		
		private String formatSettingsReFrames;
		
		private int codecId;
		
		private double duration;
		
		private String bitrateMode;
		
		private int maximumBitrate;
		
		private int width;
		
		private int height;
		
		private String displayAspectRatio;
		
		private double frameRate;
		
		private String standard;
		
		private String colorSpace;
		
		private String chromaSubsampling;
		
		private int bitDepth;
		
		private String scanType;
		
		private String colorRange;

		public MediaInfoVideoInfo(StreamInfoAdapted streamInfoAdapted) {
        	this.id = streamInfoAdapted.getId();
        	this.menuId = streamInfoAdapted.getMenuId();
        	this.format = streamInfoAdapted.getFormat();
        	this.formatInfo = streamInfoAdapted.getFormatInfo();
        	this.formatProfile = streamInfoAdapted.getFormatProfile();
        	this.formatSettingsCABAC = streamInfoAdapted.getFormatSettingsCABAC();
        	this.formatSettingsReFrames = streamInfoAdapted.getFormatSettingsReFrames();
        	this.codecId = streamInfoAdapted.getCodecId();
        	this.duration = streamInfoAdapted.getDuration();
        	this.bitrateMode = streamInfoAdapted.getBitrateMode();
        	this.maximumBitrate = streamInfoAdapted.getMaximumBitrate();
        	this.width = streamInfoAdapted.getWidth();
        	this.height = streamInfoAdapted.getHeight();
        	this.displayAspectRatio = streamInfoAdapted.getDisplayAspectRatio();
        	this.frameRate = streamInfoAdapted.getFrameRate();
        	this.standard = streamInfoAdapted.getStandard();
        	this.colorSpace = streamInfoAdapted.getColorSpace();
        	this.chromaSubsampling = streamInfoAdapted.getChromaSubsampling();
        	this.bitDepth = streamInfoAdapted.getBitDepth();
        	this.scanType = streamInfoAdapted.getScanType();
        	this.colorRange = streamInfoAdapted.getColorRange();
		}

		@Override
		public StreamType getType() {
			return StreamType.VIDEO;
		}

		@Override
		public String getCodec() {
			return format;
		}

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}

		@Override
		public String getAspectRatio() {
			return displayAspectRatio;
		}

		@Override
		public double getDuration() {
			return duration;
		}

		public int getMenuId() {
			return menuId;
		}

		public String getFormat() {
			return format;
		}

		public String getFormatInfo() {
			return formatInfo;
		}

		public String getFormatProfile() {
			return formatProfile;
		}

		public String getFormatSettingsCABAC() {
			return formatSettingsCABAC;
		}

		public String getFormatSettingsReFrames() {
			return formatSettingsReFrames;
		}

		public int getCodecId() {
			return codecId;
		}

		public String getBitrateMode() {
			return bitrateMode;
		}

		public int getMaximumBitrate() {
			return maximumBitrate;
		}

		public String getDisplayAspectRatio() {
			return displayAspectRatio;
		}

		public double getFrameRate() {
			return frameRate;
		}

		public String getStandard() {
			return standard;
		}

		public String getColorSpace() {
			return colorSpace;
		}

		public String getChromaSubsampling() {
			return chromaSubsampling;
		}

		public int getBitDepth() {
			return bitDepth;
		}

		public String getScanType() {
			return scanType;
		}

		public String getColorRange() {
			return colorRange;
		}

		public int getId() {
			return id;
		}
	}

	public static class MediaInfoAudioInfo extends MediaInfoBase.AudioInfo implements IStreamInfo {

		private int id;

		private int menuId;

		private String format;
		
		private String formatInfo;
		
		private String formatVersion;
		
		private String formatProfile;
		
		private String muxingMode;
		
		private int codecId;
		
		private double duration;
		
		private int channels;
		
		private String channelPositions;
		
		private int sampleRate;
		
		private String compressionMode;
		
		private double delayRelativeToVideo;

		public MediaInfoAudioInfo(StreamInfoAdapted streamInfoAdapted) {
        	this.id = streamInfoAdapted.getId();
        	this.menuId = streamInfoAdapted.getMenuId();
        	this.format = streamInfoAdapted.getFormat();
        	this.formatInfo = streamInfoAdapted.getFormatInfo();
        	this.formatVersion = streamInfoAdapted.getFormatVersion();
        	this.formatProfile = streamInfoAdapted.getFormatProfile();
        	this.muxingMode = streamInfoAdapted.getMuxingMode();
        	this.codecId = streamInfoAdapted.getCodecId();
        	this.duration = streamInfoAdapted.getDuration();
        	this.channels = streamInfoAdapted.getChannels();
        	this.channelPositions = streamInfoAdapted.getChannelPositions();
        	this.sampleRate = streamInfoAdapted.getSamplingRate();
        	this.compressionMode = streamInfoAdapted.getCompressionMode();
        	this.delayRelativeToVideo = streamInfoAdapted.getDelayRelativeToVideo();
		}

		@Override
		public StreamType getType() {
			return StreamType.AUDIO;
		}

		@Override
		public String getCodec() {
			return format;
		}

		@Override
		public int getChannels() {
			return channels;
		}

		@Override
		public int getSampleRate() {
			return sampleRate;
		}

		@Override
		public int getBitrate() {
			return 0;
		}

		@Override
		public double getDuration() {
			return duration;
		}
		
		public int getId() {
			return id;
		}

		public int getMenuId() {
			return menuId;
		}

		public String getFormat() {
			return format;
		}

		public String getFormatInfo() {
			return formatInfo;
		}

		public String getFormatVersion() {
			return formatVersion;
		}

		public String getFormatProfile() {
			return formatProfile;
		}

		public String getMuxingMode() {
			return muxingMode;
		}

		public int getCodecId() {
			return codecId;
		}

		public String getChannelPositions() {
			return channelPositions;
		}

		public String getCompressionMode() {
			return compressionMode;
		}

		public double getDelayRelativeToVideo() {
			return delayRelativeToVideo;
		}
	}
	
	public static class StreamInfoAdapted{
	    @XmlAttribute
		private String type;

	    @XmlElement(name = "ID")
		private String id;

		@XmlElement(name = "Menu_ID")
		private String menuId;
		
		@XmlElement(name = "Format")
		private String format;
		
		@XmlElement(name = "Format_Info")
		private String formatInfo;
		
		@XmlElement(name = "Format_version")
		private String formatVersion;
		
		@XmlElement(name = "Format_profile")
		private String formatProfile;

		@XmlElement(name = "Format_settings__CABAC")
		private String formatSettingsCABAC;
		
		@XmlElement(name = "Format_settings__ReFrames")
		private String formatSettingsReFrames;
		
		@XmlElement(name = "Muxing_mode")
		private String muxingMode;
		
		@XmlElement(name = "Codec_ID")
		private int codecId;
		
		@XmlElement(name = "Duration")
		private String duration;
		
		@XmlElement(name = "Channel_s_")
		private String channels;
		
		@XmlElement(name = "Channel_positions")
		private String channelPositions;
		
		@XmlElement(name = "Sampling_rate")
		private String samplingRate;
		
		@XmlElement(name = "Compression_mode")
		private String compressionMode;
		
		@XmlElement(name = "Delay_relative_to_video")
		private String delayRelativeToVideo;

		@XmlElement(name = "Bit_rate_mode")
		private String bitrateMode;
		
		@XmlElement(name = "Maximum_bit_rate")
		private String maximumBitrate;
		
		@XmlElement(name = "Width")
		private String width;
		
		@XmlElement(name = "Height")
		private String height;
		
		@XmlElement(name = "Display_aspect_ratio")
		private String displayAspectRatio;
		
		@XmlElement(name = "Frame_rate")
		private String frameRate;
		
		@XmlElement(name = "Standard")
		private String Standard;
		
		@XmlElement(name = "Color_space")
		private String colorSpace;
		
		@XmlElement(name = "Chroma_subsampling")
		private String chromaSubsampling;
		
		@XmlElement(name = "Bit_depth")
		private String bitDepth;
		
		@XmlElement(name = "Scan_type")
		private String scanType;
		
		@XmlElement(name = "Color_range")
		private String colorRange;
	    
	    @XmlElement(name = "Complete_name")
		private String completeName;
		
	    @XmlElement(name = "File_size")
		private String fileSize;
		
	    @XmlElement(name = "Overall_bit_rate_mode")
		private String overallBitrateMode;
		
	    @XmlElement(name = "Overall_bit_rate")
		private String overallBitrate;
	    
	    @XmlElement(name = "List")
		private String list;

		public StreamType getType() {
			return StreamType.valueOf(type.toUpperCase());
		}

		public int getId() {
			if(id == null){
				return 0;
			}
			
			String[] parts = id.split(" ");
			return Integer.valueOf(parts[0]);
		}

		public int getMenuId() {
			if(menuId == null){
				return 0;
			}
			
			String[] parts = menuId.split(" ");
			return Integer.valueOf(parts[0]);
		}

		public String getFormat() {
			return format;
		}

		public String getFormatInfo() {
			return formatInfo;
		}

		public String getFormatVersion() {
			return formatVersion;
		}

		public String getFormatProfile() {
			return formatProfile;
		}

		public String getMuxingMode() {
			return muxingMode;
		}

		public int getCodecId() {
			return codecId;
		}

		public double getDuration() {
			if(duration == null){
				return 0;
			}
			
			return strToSeconds(duration);
		}

		public int getChannels() {
			if(channels == null){
				return 0;
			}
			
			String[] parts = channels.split(" ");
			return Integer.valueOf(parts[0]);
		}

		public String getChannelPositions() {
			return channelPositions;
		}

		public int getSamplingRate() {
			if(channels == null){
				return 0;
			}
			
			String[] parts = channels.split(" ");
			int sampleRate = Integer.valueOf(parts[0]);
			
			if(parts.length > 0 && parts[1].toLowerCase().equals("khz")){
				sampleRate *= 1000;
			}
			return sampleRate;
		}

		public String getCompressionMode() {
			return compressionMode;
		}

		public double getDelayRelativeToVideo() {
			if(delayRelativeToVideo == null){
				return 0;
			}
			
			return strToSeconds(delayRelativeToVideo);
		}

		public String getFormatSettingsCABAC() {
			return formatSettingsCABAC;
		}

		public String getFormatSettingsReFrames() {
			return formatSettingsReFrames;
		}

		public String getBitrateMode() {
			return bitrateMode;
		}

		public int getMaximumBitrate() {
			if(maximumBitrate == null){
				return 0;
			}
			
			String[] parts = maximumBitrate.split(" ");
			int bitrate = Integer.valueOf(parts[0]);
			
			if(parts.length > 0 && parts[1].toLowerCase().equals("kbps")){
				bitrate *= 1000;
			}
			return bitrate;
		}

		public int getWidth() {
			if(width == null){
				return 0;
			}
			
			String[] parts = width.split(" ");
			return Integer.valueOf(parts[0]);
		}

		public int getHeight() {
			if(height == null){
				return 0;
			}
			
			String[] parts = height.split(" ");
			return Integer.valueOf(parts[0]);
		}

		public String getDisplayAspectRatio() {
			return displayAspectRatio;
		}

		public double getFrameRate() {
			if(frameRate == null){
				return 0;
			}
				
			String[] parts = frameRate.split(" ");
			return Double.valueOf(parts[0]);
		}

		public String getStandard() {
			return Standard;
		}

		public String getColorSpace() {
			return colorSpace;
		}

		public String getChromaSubsampling() {
			return chromaSubsampling;
		}

		public int getBitDepth() {
			if(bitDepth == null){
				return 0;
			}
				
			String[] parts = bitDepth.split(" ");
			return Integer.valueOf(parts[0]);
		}

		public String getScanType() {
			return scanType;
		}

		public String getColorRange() {
			return colorRange;
		}

		public String getCompleteName() {
			return completeName;
		}

		public int getFileSize() {
			if(fileSize == null){
				return 0;
			}
				
			String[] parts = fileSize.split(" ");
			double size = Double.valueOf(parts[0]);

			if(parts.length > 0 && parts[1].toLowerCase().equals("kib")){
				size *= 1024;
			}
			if(parts.length > 0 && parts[1].toLowerCase().equals("mib")){
				size *= 1024 * 1024;
			}
			if(parts.length > 0 && parts[1].toLowerCase().equals("gib")){
				size *= 1024 * 1024 * 1024;
			}
			return (int) size;
		}

		public String getOverallBitrateMode() {
			return overallBitrateMode;
		}

		public int getOverallBitrate() {
			if(overallBitrate == null){
				return 0;
			}
				
			String[] parts = overallBitrate.split(" ");
			int bitrate = Integer.valueOf(parts[0]);
			
			if(parts.length > 0 && parts[1].toLowerCase().equals("kbps")){
				bitrate *= 1000;
			}
			return bitrate;
		}

		public String getList() {
			return list;
		}
	}
	
	public static class MenuInfo implements IStreamInfo {
		private int id;

		private int menuId;
	    
		private double duration;
	    
		private String list;

		public MenuInfo(StreamInfoAdapted streamInfoAdapted) {
		    this.id = streamInfoAdapted.getId();

		    this.menuId = streamInfoAdapted.getMenuId();
		    
		    this.duration = streamInfoAdapted.getDuration();
		    
		    this.list = streamInfoAdapted.getList();
		}

		@Override
		public StreamType getType() {
			return StreamType.MENU;
		}

		@Override
		public String getCodec() {
			return null;
		}

		public int getId() {
			return id;
		}

		public int getMenuId() {
			return menuId;
		}

		public double getDuration() {
			return duration;
		}

		public String getList() {
			return list;
		}
	}
	
	public static class GeneralInfo implements IStreamInfo {
		private int id;
	    
		private String completeName;
		
		private String format;
		
		private int fileSize;
		
		private double duration;
		
		private String overallBitrateMode;
		
		private int overallBitrate;

		public GeneralInfo(StreamInfoAdapted streamInfoAdapted) {
		    this.id = streamInfoAdapted.getId();
		    this.completeName = streamInfoAdapted.getCompleteName();
		    this.format = streamInfoAdapted.getFormat();
		    this.fileSize = streamInfoAdapted.getFileSize();
		    this.duration = streamInfoAdapted.getDuration();
		    this.overallBitrateMode = streamInfoAdapted.getOverallBitrateMode();
		    this.overallBitrate = streamInfoAdapted.getOverallBitrate();
		}

		@Override
		public StreamType getType() {
			return StreamType.GENERAL;
		}

		@Override
		public String getCodec() {
			return format;
		}

		public int getId() {
			return id;
		}

		public String getCompleteName() {
			return completeName;
		}

		public String getFormat() {
			return format;
		}

		public int getFileSize() {
			return fileSize;
		}

		public double getDuration() {
			return duration;
		}

		public String getOverallBitrateMode() {
			return overallBitrateMode;
		}

		public int getOverallBitrate() {
			return overallBitrate;
		}
	}
	
	public static class StreamInfoAdapter extends XmlAdapter<StreamInfoAdapted, IStreamInfo>{

		@Override
		public StreamInfoAdapted marshal(IStreamInfo streamInfo) throws Exception {
			return null;
		}

		@Override
		public IStreamInfo unmarshal(StreamInfoAdapted streamInfoAdapted) throws Exception {
			StreamType type = streamInfoAdapted.getType();
			
	        if(type == StreamType.AUDIO) {
	        	return new MediaInfoAudioInfo(streamInfoAdapted);
	        }

	        if(type == StreamType.VIDEO) {
	        	return new MediaInfoVideoInfo(streamInfoAdapted);
	        }

	        if(type == StreamType.GENERAL) {
	        	return new GeneralInfo(streamInfoAdapted);
	        }

	        if(type == StreamType.MENU) {
	        	return new MenuInfo(streamInfoAdapted);
	        }

	        return null;
		}
	}

	@XmlRootElement(name = "File")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class MediaInfoFile {
	    @XmlElement(name="track")
		private List<IStreamInfo> tracks;

		public List<IStreamInfo> getTracks() {
			return tracks;
		}
	}

	@XmlRootElement(name = "Mediainfo")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class MediaInfoInfo extends MediaInfoBase.Info {
		@XmlElement(name = "File")
		private MediaInfoFile file;
		
		@Override
		public MediaInfoVideoInfo getVideo() {
			for(IStreamInfo track : file.getTracks()){
				if(track instanceof MediaInfoVideoInfo){
					return (MediaInfoVideoInfo) track;
				}
			}
			
			return null;
		}

		@Override
		public MediaInfoAudioInfo getAudio() {
			for(IStreamInfo track : file.getTracks()){
				if(track instanceof MediaInfoAudioInfo){
					return (MediaInfoAudioInfo) track;
				}
			}
			
			return null;
		}

		public GeneralInfo getGeneral() {
			for(IStreamInfo track : file.getTracks()){
				if(track instanceof GeneralInfo){
					return (GeneralInfo) track;
				}
			}
			
			return null;
		}

		@Override
		public int getBitrate() {
			return getGeneral().getOverallBitrate();
		}

		@Override
		public double getDuration() {
			return getGeneral().getDuration();
		}

		public MediaInfoFile getFile() {
			return file;
		}
	}

	public MediaInfo(File file) throws Exception {
		super(file);
	}

	protected String[] getCommandLine() {
		return new String[] { getExecutablePath(), "--Output=XML", file.getAbsolutePath() };
	}

	@Override
	protected MediaInfoBase.Info parse() throws Exception {
		JAXBContext jaxbContext = JAXBContext.newInstance(MediaInfoInfo.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		MediaInfoInfo info = (MediaInfoInfo) unmarshaller.unmarshal(new StreamSource(new StringReader(rawData)));

		return info;
	}

	@Override
	protected String getExecutablePath() {
		if (mediaInfoPath == null) {
			mediaInfoPath = TestConfig.get().getPathToMediaInfo();
		}
		
		Path path = Paths.get(mediaInfoPath);
		if(Files.isSymbolicLink(path)){
			try {
				path = Files.readSymbolicLink(path);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		return path.toString();
	}

	protected static double strToSeconds(String str) {
		String[] parts = str.split(" ");
		double seconds = 0;
		
		for(String part : parts){
			if(part.toLowerCase().endsWith("ms")){
				seconds += Double.valueOf(part.substring(0, part.length() - 2)) / 1000;
			}
			else if(part.toLowerCase().endsWith("s")){
				seconds += Double.valueOf(part.substring(0, part.length() - 1));
			}
			else if(part.toLowerCase().endsWith("m")){
				seconds += Double.valueOf(part.substring(0, part.length() - 1)) * 60;
			}
		}
		
		return seconds;
	}

	public static void main(String[] args) throws Exception {
		TestConfig.init(new String[] {});
		File file = new File(args[0]);
		MediaInfoBase mediaInfo = new MediaInfo(file);
		MediaInfoBase.Info info = mediaInfo.getInfo();
		System.out.println(info);
		ThreadManager.stop();
		System.exit(0);
	}
}
