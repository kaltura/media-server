package com.kaltura.media_server.services;

/**
 * Created by ron.yadgar on 02/06/2016.
 */
public class Constants {

    public final static String HTTP_PROVIDER_KEY = "diagnostics";
    public final static int KALTURA_REJECTED_STEAMS_SIZE = 100;
    public final static String KALTURA_PERMANENT_SESSION_KEY = "kalturaWowzaPermanentSessionKey";
    public final static String CLIENT_PROPERTY_CONNECT_URL = "connecttcUrl";
    public final static String CLIENT_PROPERTY_ENCODER = "connectflashVer";
    public final static String CLIENT_PROPERTY_SERVER_INDEX = "serverIndex";
    public final static String CLIENT_PROPERTY_KALTURA_LIVE_ENTRY = "KalturaLiveEntry";
    public final static String CLIENT_PROPERTY_KALTURA_LIVE_ASSET_LIST = "KalturaLiveAssetList";
    public final static String REQUEST_PROPERTY_PARTNER_ID = "p";
    public final static String REQUEST_PROPERTY_ENTRY_ID = "e";
    public final static String REQUEST_PROPERTY_SERVER_INDEX = "i";
    public final static String REQUEST_PROPERTY_TOKEN = "t";
    public final static String KALTURA_SERVER_URL = "KalturaServerURL";
    public final static String KALTURA_SERVER_ADMIN_SECRET = "KalturaServerAdminSecret";
    public final static String KALTURA_SERVER_PARTNER_ID = "KalturaPartnerId";
    public final static String KALTURA_SERVER_TIMEOUT = "KalturaServerTimeout";
    public final static String KALTURA_SERVER_UPLOAD_XML_SAVE_PATH = "uploadXMLSavePath";
    public final static String KALTURA_SERVER_WOWZA_WORK_MODE = "KalturaWorkMode";
    public final static String WOWZA_WORK_MODE_KALTURA = "kaltura";
    public final static String KALTURA_RECORDED_FILE_GROUP = "KalturaRecordedFileGroup";
    public final static String DEFAULT_RECORDED_FILE_GROUP = "kaltura";
    public final static String DEFAULT_RECORDED_SEGMENT_DURATION_FIELD_NAME = "DefaultRecordedSegmentDuration";
    public final static String COPY_SEGMENT_TO_LOCATION_FIELD_NAME = "CopySegmentToLocation";
    public final static String INVALID_SERVER_INDEX = "-1";
    public final static String LIVE_STREAM_EXCEEDED_MAX_RECORDED_DURATION = "LIVE_STREAM_EXCEEDED_MAX_RECORDED_DURATION";
    public final static String RECORDING_ANCHOR_TAG_VALUE = "recording_anchor";
    public final static int DEFAULT_RECORDED_SEGMENT_DURATION = 900000; //~15 minutes
    public final static int MEDIA_SERVER_PARTNER_ID = -5;
    public static final String AMFSETDATAFRAME = "amfsetdataframe";
    public static final String ONMETADATA_AUDIODATARATE = "audiodatarate";
    public static final String ONMETADATA_VIDEODATARATE = "videodatarate";
    public static final String ONMETADATA_WIDTH = "width";
    public static final String ONMETADATA_HEIGHT = "height";
    public static final String ONMETADATA_FRAMERATE= "framerate";
    public static final String ONMETADATA_VIDEOCODECIDSTR = "videocodecidstring";
    public static final String ONMETADATA_AUDIOCODECIDSTR = "audiocodecidstring";
    public static final String[] streamParams = {ONMETADATA_AUDIODATARATE, ONMETADATA_VIDEODATARATE, ONMETADATA_WIDTH,
            ONMETADATA_HEIGHT, ONMETADATA_FRAMERATE, ONMETADATA_VIDEOCODECIDSTR, ONMETADATA_AUDIOCODECIDSTR};
    public static final int DEFAULT_CHUNK_DURATION_MILLISECONDS = 10000;
    public static final String STREAM_ACTION_LISTENER_PROPERTY = "KalturaStreamActionListenerProperty";
    public static final int KALTURA_SYNC_POINTS_INTERVAL_PROPERTY = 60 * 1000;
    public static final String KALTURA_LIVE_ENTRY_ID = "KalturaLiveEntryId";
    public static final String KALTURA_ENTRY_VALIDATED_TIME = "KalturaEntryValidatedTime";
    public static final String KALTURA_ENTRY_AUTHENTICATION_LOCK = "KalturaEntryAuthenticationLock";
    public static final String KALTURA_ENTRY_AUTHENTICATION_ERROR_FLAG = "KalturaEntryAuthenticationFlag";
    public static final String KALTURA_ENTRY_AUTHENTICATION_ERROR_MSG = "KalturaEntryAuthenticationMsg";
    public static final String KALTURA_ENTRY_AUTHENTICATION_ERROR_TIME = "KalturaEntryAuthenticationTime";
    public static final int KALTURA_PERSISTENCE_DATA_MIN_ENTRY_TIME = 30000;
    public static final int KALTURA_ENTRY_PERSISTENCE_CLEANUP_START = 10000;
    public static final int KALTURA_TIME_BETWEEN_PERSISTENCE_CLEANUP = 60000;
    public static final int KALTURA_MIN_TIME_BETWEEN_AUTHENTICATIONS = 10000;
}
