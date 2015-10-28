package kaltura.actions;

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
    private String name;
    private boolean isDvr;
    private boolean isRecording;
    private int conversionProfileId;

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
