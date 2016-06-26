package services;

/**
 * Created by ron.yadgar on 02/06/2016.
 */
public class Constants {
//todo ask yossi about server xml properties
    public final static String CLIENT_PROPERTY_CONNECT_URL = "connecttcUrl";
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
    public final static String KALTURA_RECORDED_FILE_GROUP = "g";
    public final static String DEFAULT_RECORDED_FILE_GROUP = "kaltura";
    public final static String DEFAULT_RECORDED_SEGMENT_DURATION_FIELD_NAME = "DefaultRecordedSegmentDuration";
    public final static String COPY_SEGMENT_TO_LOCATION_FIELD_NAME = "CopySegmentToLocation";
    public final static String INVALID_SERVER_INDEX = "-1";
    public final static String LIVE_STREAM_EXCEEDED_MAX_RECORDED_DURATION = "LIVE_STREAM_EXCEEDED_MAX_RECORDED_DURATION";
    public final static String RECORDING_ANCHOR_TAG_VALUE = "recording_anchor";
    public final static int DEFAULT_RECORDED_SEGMENT_DURATION = 900000; //~15 minutes
    public final static int MEDIA_SERVER_PARTNER_ID = -5;
}
