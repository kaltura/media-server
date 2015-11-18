package com.kaltura.media.quality.validator.info;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openamf.AMFHeader;
import org.openamf.AMFMessage;
import org.openamf.io.AMFDeserializer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaltura.media.quality.configurations.TestConfig;
import com.kaltura.media.quality.utils.ThreadManager;
import com.wowza.wms.amf.AMF3Utils;
import com.wowza.wms.amf.AMFDataContextDeserialize;
import com.wowza.wms.amf.AMFDataObj;
import com.wowza.wms.amf.AMFPacket;

public class FFPROBE extends MediaInfoBase {
    private static final Logger log = Logger.getLogger(FFPROBE.class);

	private String ffprobePath = null;

	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,  include = JsonTypeInfo.As.PROPERTY, property = "codec_type")
	@JsonSubTypes({ 
		@Type(value = DataInfo.class, name = "data"), 
		@Type(value = FFPROBEVideoInfo.class, name = "video"), 
		@Type(value = FFPROBEAudioInfo.class, name = "audio") 
	})
	abstract interface IStreamInfo extends MediaInfoBase.IStreamInfo{

	}

	public static class DispositionInfo{
		@JsonProperty("default")
		private int _default;
		
		@JsonProperty("dub")
		private int dub;
		
		@JsonProperty("original")
		private int original;
		
		@JsonProperty("comment")
		private int comment;
		
		@JsonProperty("lyrics")
		private int lyrics;
		
		@JsonProperty("karaoke")
		private int karaoke;
		
		@JsonProperty("forced")
		private int forced;
		
		@JsonProperty("hearing_impaired")
		private int hearingImpaired;
		
		@JsonProperty("visual_impaired")
		private int visualImpaired;
		
		@JsonProperty("clean_effects")
		private int cleanEffects;
		
		@JsonProperty("attached_pic")
		private int attachedPic;
	}
	
	public static class DataInfo implements IStreamInfo{
		@JsonCreator
	    public DataInfo(@JsonProperty("name") String name) {
	    }
	 
		@JsonProperty("index")
		private int index;
		
		@JsonProperty("codec_name")
		private String codec;
		
		@JsonProperty("codec_type")
		private String type;
		
		@JsonProperty("codec_time_base")
		private String codecTimeBase;
		
		@JsonProperty("codec_tag_string")
		private String codecTagString;
		
		@JsonProperty("codec_tag")
		private String codecTag;
		
		@JsonProperty("id")
		private String id;
		
		@JsonProperty("r_frame_rate")
		private String rFrameRate;
		
		@JsonProperty("avg_frame_rate")
		private String avgFrameRate;
		
		@JsonProperty("time_base")
		private String timeBase;
		
		@JsonProperty("start_pts")
		private long startPts;
		
		@JsonProperty("start_time")
		private double startTime;
		
		@JsonProperty("disposition")
		private DispositionInfo disposition;

		@Override
		public StreamType getType() {
			return StreamType.DATA;
		}

		@Override
		public String getCodec() {
			return codec;
		}
	}
	
	public static class FFPROBEVideoInfo extends MediaInfoBase.VideoInfo implements IStreamInfo{
		@JsonProperty("index")
		private int index;
		
		@JsonProperty("codec_name")
		private String codec;
		
		@JsonProperty("codec_long_name")
		private String codecLongName;
		
		@JsonProperty("profile")
		private String profile;
		
		@JsonProperty("codec_type")
		private String type;
		
		@JsonProperty("codec_time_base")
		private String codecTimeBase;
		
		@JsonProperty("codec_tag_string")
		private String codecTagString;
		
		@JsonProperty("codec_tag")
		private String codecTag;
		
		@JsonProperty("width")
		private int width;
		
		@JsonProperty("height")
		private int height;
		
		@JsonProperty("coded_width")
		private int codedWidth;
		
		@JsonProperty("coded_height")
		private int codedHeight;
		
		@JsonProperty("has_b_frames")
		private int hasBFrames;
		
		@JsonProperty("sample_aspect_ratio")
		private String sampleAspectRatio;
		
		@JsonProperty("display_aspect_ratio")
		private String displayAspectRatio;
		
		@JsonProperty("pix_fmt")
		private String pixFormat;
		
		@JsonProperty("level")
		private int level;
		
		@JsonProperty("color_range")
		private String colorRange;
		
		@JsonProperty("chroma_location")
		private String chromaLocation;
		
		@JsonProperty("refs")
		private int refs;
		
		@JsonProperty("is_avc")
		private boolean isAvc;
		
		@JsonProperty("nal_length_size")
		private String nalLengthSize;
		
		@JsonProperty("id")
		private String id;
		
		@JsonProperty("r_frame_rate")
		private String rFrameRate;
		
		@JsonProperty("avg_frame_rate")
		private String avgFrameRate;
		
		@JsonProperty("time_base")
		private String timeBase;
		
		@JsonProperty("start_pts")
		private long startPts;
		
		@JsonProperty("start_time")
		private double startTime;
		
		@JsonProperty("duration_ts")
		private long durationTs;
		
		@JsonProperty("duration")
		private double duration;
		
		@JsonProperty("bits_per_raw_sample")
		private int bitsPerRawSample;
		
		@JsonProperty("disposition")
		private DispositionInfo disposition;

		@Override
		public StreamType getType() {
			return StreamType.VIDEO;
		}

		@Override
		public String getCodec() {
			return codec;
		}

		@Override
		public double getDuration() {
			return duration;
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

		public Object getCodedHeight() {
			return codedHeight;
		}

		public Object getCodedWidth() {
			return codedWidth;
		}

		public double getStartTime() {
			return startTime;
		}
	}
	
	public static class FFPROBEAudioInfo extends MediaInfoBase.AudioInfo implements IStreamInfo{

		@JsonProperty("index")
		private int index;
		
		@JsonProperty("codec_name")
		private String codec;
		
		@JsonProperty("codec_long_name")
		private String codecLongName;
		
		@JsonProperty("profile")
		private String profile;
		
		@JsonProperty("codec_type")
		private String type;
		
		@JsonProperty("codec_time_base")
		private String codecTimeBase;
		
		@JsonProperty("codec_tag_string")
		private String codecTagString;
		
		@JsonProperty("codec_tag")
		private String codecTag;
		
		@JsonProperty("sample_fmt")
		private String sampleFormat;
		
		@JsonProperty("sample_rate")
		private int sampleRate;
		
		@JsonProperty("channels")
		private int channels;
		
		@JsonProperty("channel_layout")
		private String channel_layout;
		
		@JsonProperty("bits_per_sample")
		private int bits_per_sample;
		
		@JsonProperty("id")
		private String id;
		
		@JsonProperty("r_frame_rate")
		private String rFrameRate;
		
		@JsonProperty("avg_frame_rate")
		private String avgFrameRate;
		
		@JsonProperty("time_base")
		private String timeBase;
		
		@JsonProperty("start_pts")
		private long startPts;
		
		@JsonProperty("start_time")
		private double startTime;
		
		@JsonProperty("duration_ts")
		private long durationTs;
		
		@JsonProperty("duration")
		private double duration;
		
		@JsonProperty("bits_per_raw_sample")
		private int bitsPerRawSample;
		
		@JsonProperty("disposition")
		private DispositionInfo disposition;
		
		@JsonProperty("bit_rate")
		private int bitrate;

		@Override
		public StreamType getType() {
			return StreamType.AUDIO;
		}

		@Override
		public String getCodec() {
			return codec;
		}

		@Override
		public double getDuration() {
			return duration;
		}

		@Override
		public int getBitrate() {
			return bitrate;
		}

		@Override
		public int getChannels() {
			return channels;
		}

		@Override
		public int getSampleRate() {
			return sampleRate;
		}

		public double getStartTime() {
			return startTime;
		}
	}
		
	public static class FormatInfo{
		@JsonProperty("filename")
		private String filename;
		
		@JsonProperty("nb_streams")
		private int nbStreams;
		
		@JsonProperty("nb_programs")
		private int nbPrograms;
				
		@JsonProperty("format_name")
		private String formatName;
						
		@JsonProperty("format_long_name")
		private String formatLongName;
								
		@JsonProperty("start_time")
		private double startTime;
										
		@JsonProperty("duration")
		private double duration;
												
		@JsonProperty("size")
		private long size;
														
		@JsonProperty("bit_rate")
		private int bitrate;
																
		@JsonProperty("probe_score")
		private int probeScore;

		public int getBitrate() {
			return bitrate;
		}

		public double getDuration() {
			return duration;
		}

		public double getStartTime() {
			return startTime;
		}

		public int getProbeScore() {
			return probeScore;
		}
	}
	
	public static class PacketInfo{
		@JsonProperty("codec_type")
		private String codecType;
	
		@JsonProperty("stream_index")
		private int streamIndex;
	
		@JsonProperty("pts")
		private int pts;
	
		@JsonProperty("pts_time")
		private double ptsTime;
	
		@JsonProperty("dts")
		private int dts;
	
		@JsonProperty("dts_time")
		private double dtsTime;
	
		@JsonProperty("duration")
		private int duration;
	
		@JsonProperty("duration_time")
		private double durationTime;
	
		@JsonProperty("size")
		private int size;
	
		@JsonProperty("pos")
		private int pos;
	
		@JsonProperty("flags")
		private String flags;

		@JsonProperty("data")
		private String data;

		private SyncPoint syncPoint;
	
		public String getCodecType() {
			return codecType;
		}
	
		public int getStreamIndex() {
			return streamIndex;
		}
	
		public int getPts() {
			return pts;
		}
	
		public double getPtsTime() {
			return ptsTime;
		}
	
		public int getDts() {
			return dts;
		}
	
		public double getDtsTime() {
			return dtsTime;
		}
	
		public int getDuration() {
			return duration;
		}
	
		public double getDurationTime() {
			return durationTime;
		}
	
		public int getSize() {
			return size;
		}
	
		public int getPos() {
			return pos;
		}
	
		public String getFlags() {
			return flags;
		}

		public String getData() {
			return data;
		}

		public void setData(String data) throws JsonParseException, JsonMappingException, IOException {
			this.data = data;
			
			if(codecType == null || !codecType.equals("data")){
				return;
			}
			
			String str = "";
			BigInteger bigInteger;
			String[] bytes;
			String[] lines = data.split("\n");
			for(String line : lines){
				if(line.length() > 0){
					bytes = line.split(" ");
					for(int index = 1; index <= 8; index++){
						if(bytes[index].length() > 0){
							bigInteger = new BigInteger(bytes[index].substring(0, 2), 16);
							str += Character.toString ((char) bigInteger.byteValue());
						}
						if(bytes[index].length() > 2){
							bigInteger = new BigInteger(bytes[index].substring(2, 4), 16);
							str += Character.toString ((char) bigInteger.byteValue());
						}
					}
				}
			}

			if(str.length() != size){
				log.error("Unexpected size");
				return;
			}
			
			ByteArrayInputStream buffer = new ByteArrayInputStream(str.getBytes());
			buffer.skip(5); // header size

			byte[] abyte = new byte[4];
			buffer.read(abyte, 0, 4);
			String headerType = new String(abyte);
			log.debug("Header type: " + headerType);
			if(!headerType.equals("TEXT")){
				return;
			}
				
			buffer.skip(7); // size and encoding
			
			byte[] bbyte = new byte[buffer.available() - 1];
			buffer.read(bbyte, 0, buffer.available() - 1);
			String json = new String(bbyte);
			log.debug("JSON: " + json);
			
	        ObjectMapper mapper = new ObjectMapper();
	        this.syncPoint = mapper.readValue(json, SyncPoint.class);
		}
		
		public SyncPoint getSyncPoint() {
			return syncPoint;
		}
	}
		
	public static class SyncPoint{
	    @JsonProperty("id")
	    private String id;

	    @JsonProperty("timestamp")
	    private long timestamp;

	    @JsonProperty("offset")
	    private long offset;

	    @JsonProperty("objectType")
	    private String objectType;
	}
		
	public static class FFPROBEInfo extends MediaInfoBase.Info{

		public FFPROBEInfo(){
			
		}
		
	    @JsonProperty("packets")
	    private List<PacketInfo> packets = new ArrayList<PacketInfo>();
		
	    @JsonProperty("streams")
	    private List<IStreamInfo> streams = new ArrayList<IStreamInfo>();

	    @JsonProperty("format")
	    private FormatInfo format = null;

		@Override
		public FFPROBEVideoInfo getVideo() {
			for(IStreamInfo stream : streams){
				if(stream.getType() == StreamType.VIDEO){
					return (FFPROBEVideoInfo) stream;
				}
			}
			return null;
		}

		@Override
		public FFPROBEAudioInfo getAudio() {
			for(IStreamInfo stream : streams){
				if(stream.getType() == StreamType.AUDIO){
					return (FFPROBEAudioInfo) stream;
				}
			}
			return null;
		}

		@Override
		public int getBitrate() {
			return format.getBitrate();
		}

		@Override
		public double getDuration() {
			return format.getDuration();
		}

		public FormatInfo getFormat() {
			return format;
		}

		public List<PacketInfo> getPackets() {
			return packets;
		}
	}
	
	public FFPROBE(File file) throws Exception{
		super(file);
	}

	protected String[] getCommandLine() {
		return new String[]{
			getExecutablePath(),
			"-v", 
			"quiet", 
			"-print_format", 
			"json", 
			"-show_format", 
			"-show_streams", 
			"-show_data", 
			"-show_packets", 
			file.getAbsolutePath()
		};
	}
	
	@Override
	protected FFPROBEInfo parse() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
        FFPROBEInfo info = mapper.readValue(rawData, FFPROBEInfo.class);
        return info;
	}

	@Override
	protected String getExecutablePath() {
		if(ffprobePath == null){
			ffprobePath = TestConfig.get().getPathToFfprobe();
		}
		return ffprobePath;
	}
	
	public static void main(String[] args) throws Exception{
		TestConfig.init(new String[]{});
		File file = new File(args[0]);
		MediaInfoBase ffprobe = new FFPROBE(file);
		FFPROBE.FFPROBEInfo info = (FFPROBEInfo) ffprobe.getInfo();
		for(PacketInfo packet : info.getPackets()){
			if(packet.getCodecType().equals("data")){
				SyncPoint syncPoint = packet.getSyncPoint();
				System.out.println(syncPoint);
			}
		}
		
		ThreadManager.stop();
	}
}
