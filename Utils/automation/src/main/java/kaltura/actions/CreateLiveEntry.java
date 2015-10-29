package kaltura.actions;

import java.util.Map;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.enums.KalturaDVRStatus;
import com.kaltura.client.enums.KalturaMediaType;
import com.kaltura.client.enums.KalturaRecordStatus;
import com.kaltura.client.enums.KalturaSourceType;
import com.kaltura.client.types.KalturaLiveStreamEntry;

import utils.StringUtils;

/**
 * Created by asher.saban on 3/8/2015.
 */
public class CreateLiveEntry {

    private KalturaClient client;
    private String name = "test-live-";
    private boolean isDvr = true;
    private boolean isRecording = false;
    private int conversionProfileId = Integer.MIN_VALUE;

    public CreateLiveEntry(KalturaClient client, String name, boolean isRandom, boolean isDvr, boolean isRecording, int conversionProfileId) {
        this.client = client;
        this.name = name;
        this.isDvr = isDvr;
        this.isRecording = isRecording;
        if (isRandom) {
            this.name += StringUtils.generateRandomSuffix();
        }
        this.conversionProfileId = conversionProfileId;
    }

    public CreateLiveEntry(KalturaClient client, Map<String, Object> properties) {
    	if(properties.containsKey("entry-name")){
    		this.name = (String) properties.get("entry-name");
    	}
    	if(properties.containsKey("entry-name-is-random")){
            this.name += StringUtils.generateRandomSuffix();
    	}
    	if(properties.containsKey("entry-is-dvr")){
    		this.isDvr = (boolean) properties.get("entry-is-dvr");
    	}
    	if(properties.containsKey("entry-is-recording")){
    		this.isRecording = (boolean) properties.get("entry-is-recording");
    	}
    	if(properties.containsKey("entry-conversion-profile-id")){
    		this.conversionProfileId = (int) properties.get("entry-conversion-profile-id");
    	}
	}

	public KalturaLiveStreamEntry execute() throws KalturaApiException {
        KalturaLiveStreamEntry liveStreamEntry = new KalturaLiveStreamEntry();
        liveStreamEntry.name = name;
        liveStreamEntry.mediaType = KalturaMediaType.LIVE_STREAM_FLASH;
        liveStreamEntry.sourceType = KalturaSourceType.LIVE_STREAM;
        liveStreamEntry.dvrStatus = isDvr ? KalturaDVRStatus.ENABLED : KalturaDVRStatus.DISABLED;
        liveStreamEntry.recordStatus = isRecording ? KalturaRecordStatus.APPENDED : KalturaRecordStatus.DISABLED;
        liveStreamEntry.conversionProfileId = conversionProfileId;
        KalturaSourceType sourceType = null;
        Object result = client.getLiveStreamService().add(liveStreamEntry, sourceType);
        return (KalturaLiveStreamEntry) result;
    }
}
