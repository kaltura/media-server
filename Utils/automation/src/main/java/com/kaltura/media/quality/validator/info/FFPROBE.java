package com.kaltura.media.quality.validator.info;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaltura.media.quality.configurations.TestConfig;
import com.kaltura.media.quality.utils.ThreadManager;

public class FFPROBE extends MediaInfoBase {

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
		
	public static class FFPROBEInfo extends MediaInfoBase.Info{

		public FFPROBEInfo(){
			
		}
		
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
		MediaInfoBase.Info info = ffprobe.getInfo();
		System.out.println(info);
		ThreadManager.stop();
		ThreadManager.printAllThreads();
	}
}
