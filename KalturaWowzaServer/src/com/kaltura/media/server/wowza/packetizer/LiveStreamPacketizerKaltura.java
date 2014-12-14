package com.kaltura.media.server.wowza.packetizer;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.kaltura.media.server.KalturaServer;
import com.kaltura.media.server.managers.ILiveStreamManager;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.LiveStreamPacketizerCupertino;

public class LiveStreamPacketizerKaltura extends LiveStreamPacketizerCupertino {

	protected int firstChunkId = 1;
	protected ILiveStreamManager liveStreamManager = null;

	protected static Logger logger = Logger.getLogger(LiveStreamPacketizerKaltura.class);
	
	public LiveStreamPacketizerKaltura() {
		super();
		
		liveStreamManager = (ILiveStreamManager) KalturaServer.getManager(ILiveStreamManager.class);
	}

	@Override
	public int getFirstChunkId() {
		Pattern pattern = Pattern.compile("^(\\d_[\\d\\w]{8})_\\d+_publish");
		Matcher matcher = pattern.matcher(streamName);
		if (matcher.find()) {

			String entryId = matcher.group(1);
			logger.info("Stream [" + streamName + "] entry id [" + entryId + "]");
			
			if(liveStreamManager != null && liveStreamManager.shouldSync(entryId)){
				logger.info("Stream [" + streamName + "] should be synched");
				Date now = new Date();
				firstChunkId = (new Long(now.getTime() / 10000)).intValue();
			}
		}
		logger.info("Stream [" + streamName + "] didn't match entry regex");
		
		return firstChunkId;
	}

	public int calcChunkId(int ref) {
		if(ref == 1){
			return getFirstChunkId();
		}
		return firstChunkId + ref - 1;
	}

	@Override
	public void startChunkVideoTS(int i1, boolean flag, int j1, int k1, int l1, long l2, int i2, String s1, byte abyte0[], byte abyte1[], String s2, String s3, String s4, String s5) {
		i1 = calcChunkId(i1);
		super.startChunkVideoTS(i1, flag, j1, k1, l1, l2, i2, s1, abyte0, abyte1, s2, s3, s4, s5);
	}

	@Override
	public void startChunkAudioTS(int i1, boolean flag, int j1, int k1, int l1, long l2, int i2, String s1, byte abyte0[], byte abyte1[], String s2, String s3, String s4, String s5) {
		i1 = calcChunkId(i1);
		super.startChunkAudioTS(i1, flag, j1, k1, l1, l2, i2, s1, abyte0, abyte1, s2, s3, s4, s5);
	}

	@Override
	public void startChunkTS(int i1, boolean flag, int j1, int k1, int l1, long l2, int i2, String s1, byte abyte0[], byte abyte1[], String s2, String s3, String s4, String s5) {
		i1 = calcChunkId(i1);
		super.startChunkTS(i1, flag, j1, k1, l1, l2, i2, s1, abyte0, abyte1, s2, s3, s4, s5);
	}
}
