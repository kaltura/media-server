package com.kaltura.media.quality.validator.info;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaltura.media.quality.configurations.TestConfig;
import com.kaltura.media.quality.model.IFrame;
import com.kaltura.media.quality.utils.ThreadManager;

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

	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,  include = JsonTypeInfo.As.PROPERTY, property = "type")
	@JsonSubTypes({ 
		@Type(value = FrameInfo.class, name = "frame"), 
		@Type(value = PacketInfo.class, name = "packet") 
	})
	public static abstract class PacketBaseInfo{
		@JsonProperty("type")
		protected String type;
	
		@JsonProperty("stream_index")
		protected int streamIndex;
		
		public String getType() {
			return type;
		}
	
		public int getStreamIndex() {
			return streamIndex;
		}
	}
	
	public static class FrameInfo extends PacketBaseInfo implements IFrame, Comparable<FrameInfo>{
		@JsonProperty("media_type")
		private String mediaType;

		@JsonProperty("key_frame")
		private int keyFrame;

		@JsonProperty("pkt_pts")
		private long pktPts;

		@JsonProperty("pkt_pts_time")
		private double pktPtsTime;

		@JsonProperty("pkt_dts")
		private long pktDts;

		@JsonProperty("pkt_dts_time")
		private double pktDtsTime;

		@JsonProperty("best_effort_timestamp")
		private long bestEffortTimestamp;

		@JsonProperty("best_effort_timestamp_time")
		private double bestEffortTimestampTime;

		@JsonProperty("pkt_duration")
		private int pktDuration;

		@JsonProperty("pkt_duration_time")
		private double pktDurationTime;

		@JsonProperty("pkt_pos")
		private int pktPos;

		@JsonProperty("pkt_size")
		private int pktSize;

		@JsonProperty("sample_fmt")
		private String sampleFormat;

		@JsonProperty("nb_samples")
		private int nbSamples;

		@JsonProperty("channels")
		private int channels;

		@JsonProperty("channel_layout")
		private String channelLayout;

		public String getMediaType() {
			return mediaType;
		}

		public int getKeyFrame() {
			return keyFrame;
		}

		public long getPktPts() {
			return pktPts;
		}

		public double getPktPtsTime() {
			return pktPtsTime;
		}

		public long getPktDts() {
			return pktDts;
		}

		public double getPktDtsTime() {
			return pktDtsTime;
		}

		public long getBestEffortTimestamp() {
			return bestEffortTimestamp;
		}

		public double getBestEffortTimestampTime() {
			return bestEffortTimestampTime;
		}

		public int getPktDuration() {
			return pktDuration;
		}

		public double getPktDurationTime() {
			return pktDurationTime;
		}

		public int getPktPos() {
			return pktPos;
		}

		public int getPktSize() {
			return pktSize;
		}

		public String getSampleFormat() {
			return sampleFormat;
		}

		public int getNbSamples() {
			return nbSamples;
		}

		public int getChannels() {
			return channels;
		}

		public String getChannelLayout() {
			return channelLayout;
		}

		@Override
		public double getPts() {
			return getPktPtsTime();
		}

		@Override
		public double getDts() {
			return getPktDtsTime();
		}

		@Override
		public double getDuration() {
			return getPktDurationTime();
		}

		@Override
		public int getPosition() {
			return getPktPos();
		}

		@Override
		public int getSize() {
			return getPktSize();
		}

		@Override
		public int compareTo(FrameInfo o) {
			if(o == this){
				return 0;
			}
			return 1;
		}
	}
	
	public static class PacketInfo extends PacketBaseInfo{
		@JsonProperty("codec_type")
		private String codecType;
		
		@JsonProperty("pts")
		private long pts;
	
		@JsonProperty("pts_time")
		private double ptsTime;
	
		@JsonProperty("dts")
		private long dts;
	
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
	
		public long getPts() {
			return pts;
		}
	
		public double getPtsTime() {
			return ptsTime;
		}
	
		public long getDts() {
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
		
	public static class PacketsAndFrames implements List<PacketBaseInfo>{
		private List<PacketInfo> packets = new ArrayList<PacketInfo>();
		private SortedSet<FrameInfo> frames = new TreeSet<FrameInfo>();
		private List<PacketBaseInfo> both = new ArrayList<PacketBaseInfo>();
		
		@Override
		public int size() {
			return both.size();
		}
		
		@Override
		public boolean isEmpty() {
			return both.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			return both.contains(o);
		}

		@Override
		public Iterator<PacketBaseInfo> iterator() {
			return both.iterator();
		}

		@Override
		public Object[] toArray() {
			return both.toArray();
		}

		@Override
		public <T> T[] toArray(T[] a) {
			return (T[]) both.toArray(a);
		}

		@Override
		public boolean add(PacketBaseInfo e) {
			if(e instanceof PacketInfo){
				packets.add((PacketInfo) e);
			}
			if(e instanceof FrameInfo){
				frames.add((FrameInfo) e);
			}
			return both.add(e);
		}

		@Override
		public boolean remove(Object o) {
			if(o instanceof PacketInfo){
				packets.remove((PacketInfo) o);
			}
			if(o instanceof FrameInfo){
				frames.remove((FrameInfo) o);
			}
			return both.remove(o);
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return both.containsAll(c);
		}

		@Override
		public boolean addAll(Collection<? extends PacketBaseInfo> c) {
			for(PacketBaseInfo e : c){
				if(e instanceof PacketInfo){
					packets.add((PacketInfo) e);
				}
				if(e instanceof FrameInfo){
					frames.add((FrameInfo) e);
				}
			}
			return both.addAll(c);
		}

		@Override
		public boolean addAll(int index, Collection<? extends PacketBaseInfo> c) {
			for(PacketBaseInfo e : c){
				if(e instanceof PacketInfo){
					packets.add((PacketInfo) e);
				}
				if(e instanceof FrameInfo){
					frames.add((FrameInfo) e);
				}
			}
			return both.addAll(index, c);
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			packets.removeAll(c);
			frames.removeAll(c);
			return both.removeAll(c);
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			packets.retainAll(c);
			frames.retainAll(c);
			return both.retainAll(c);
		}

		@Override
		public void clear() {
			packets.clear();
			frames.clear();
			both.clear();
		}

		@Override
		public PacketBaseInfo get(int index) {
			return both.get(index);
		}

		@Override
		public PacketBaseInfo set(int index, PacketBaseInfo element) {
			return both.set(index, element);
		}

		@Override
		public void add(int index, PacketBaseInfo element) {
			if(element instanceof PacketInfo){
				packets.add((PacketInfo) element);
			}
			if(element instanceof FrameInfo){
				frames.add((FrameInfo) element);
			}
			both.add(index, element);
		}

		@Override
		public PacketBaseInfo remove(int index) {
			PacketBaseInfo element = both.remove(index);
			if(element instanceof PacketInfo){
				packets.remove((PacketInfo) element);
			}
			if(element instanceof FrameInfo){
				frames.remove((FrameInfo) element);
			}
			return element;
		}

		@Override
		public int indexOf(Object o) {
			return both.indexOf(o);
		}

		@Override
		public int lastIndexOf(Object o) {
			return both.lastIndexOf(o);
		}

		@Override
		public ListIterator<PacketBaseInfo> listIterator() {
			return both.listIterator();
		}

		@Override
		public ListIterator<PacketBaseInfo> listIterator(int index) {
			return both.listIterator(index);
		}

		@Override
		public List<PacketBaseInfo> subList(int fromIndex, int toIndex) {
			return both.subList(fromIndex, toIndex);
		}

		public List<PacketInfo> getPackets() {
			return packets;
		}

		public SortedSet<FrameInfo> getFrames() {
			return frames;
		}
	}
		
	public static class FFPROBEInfo extends MediaInfoBase.Info{

		public FFPROBEInfo(){
			
		}
		
	    @JsonProperty("packets_and_frames")
	    private PacketsAndFrames packetsAndFrames = new PacketsAndFrames();
		
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
			return packetsAndFrames.getPackets();
		}

		public SortedSet<FrameInfo> getFrames() {
			return packetsAndFrames.getFrames();
		}

		public FrameInfo getFirstFrame() {
			return getFrames().first();
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
			"-show_frames", 
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
		
		File file = new File(ffprobePath);
		return file.getAbsolutePath();
	}
	
	public static void main(String[] args) throws Exception{
		TestConfig.init(new String[]{});
		File file = new File(args[0]);
		MediaInfoBase ffprobe = new FFPROBE(file);
		FFPROBE.FFPROBEInfo info = (FFPROBEInfo) ffprobe.getInfo();
		System.out.println(info.getVideo().getStartTime());
		System.out.println(info.getFirstFrame().getPktPts() / 90000);
		System.out.println(info.getFirstFrame().getPktPtsTime());
		
		ThreadManager.stop();
	}
}
