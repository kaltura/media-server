package com.kaltura.media.server.wowza;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kaltura.client.types.KalturaFlavorAsset;
import com.kaltura.client.types.KalturaFlavorAssetListResponse;
import com.kaltura.client.types.KalturaLiveAsset;
import org.apache.log4j.Logger;

import com.wowza.wms.stream.*;
import com.wowza.wms.application.WMSProperties;

/**
 * Created by ron.yadgar on 26/05/2016.
 */
public class Utils {


    private static Logger logger = Logger.getLogger(Utils.class);
    public static String getRtmpUrlParameters(String rtmpUrl) {

        final String OldPattern= "\\/kLive\\/\\?(p=[0-9]+)&(e=[01]_[\\d\\w]{8})&(i=[01])&(t=[\\d\\w]+)";
        final String NewPattern= "rtmp:\\/\\/([01]_[\\d\\w]{8}).([pb])\\.kpublish\\.kaltura\\.com:\\d*\\/kLive\\/\\?(p=[0-9]+)&(t=[\\d\\w]+)";
        Matcher matcher;


        //first, try the old url pattern
        matcher = getMatches(rtmpUrl, OldPattern);

        if (matcher != null && matcher.groupCount() ==4) {
            return  matcher.group(1)+'&'+matcher.group(2)+'&'+matcher.group(3)+'&'+matcher.group(4);
        }
        else{
            //if not match, try the new pattrn
            matcher = getMatches(rtmpUrl, NewPattern);
            if (matcher != null  && matcher.groupCount() ==4) {

                String i= matcher.group(2).equals("p") ? "i=0" : "i=1" ;
                return  "e="+matcher.group(1)+'&'+i+'&'+matcher.group(3)+'&'+matcher.group(4);
            }

        }
        return null;
    }
    private static Matcher getMatches(String streamName, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(streamName);
        if (!matcher.find()) {
            logger.info("Stream [" + streamName + "] does not match regex");
            return null;
        }

        return matcher;
    }

    public static String getEntryIdFromStreamName(String streamName) {
        Matcher matcher = getStreamNameMatches(streamName);
        if (matcher == null) {
            return null;
        }

        return matcher.group(1);
    }

    public static Matcher getStreamNameMatches(String streamName) {
        return getMatches(streamName, "^([01]_[\\d\\w]{8})_(.+)$");
    }
    private static WMSProperties getConnectionProperties(IMediaStream stream) {

        WMSProperties properties = null;

        if (stream.getClient() != null) {
            properties = stream.getClient().getProperties();
        } else if (stream.getRTPStream() != null && stream.getRTPStream().getSession() != null) {
            properties = stream.getRTPStream().getSession().getProperties();
        } else {
            return null;
        }

        return properties;
    }


    public static WMSProperties getEntryProperties(IMediaStream stream) {

        WMSProperties properties = null;
        String streamName = stream.getName();
        String entryId = Utils.getEntryIdFromStreamName (streamName);
        properties = getConnectionProperties(stream);

        if (properties != null) {
            return properties;
        }
        // For loop over all published mediaStream (source and transcoded) in order to find the corresponding source stream
        for (IMediaStream mediaStream : stream.getStreams().getStreams()) {
            properties = getConnectionProperties(mediaStream);

            if (properties != null && mediaStream.getName().startsWith(entryId)) {
                return properties;
            }
        }
        logger.error("Cannot find properties for entry [" + entryId + "]  for stream [" + streamName + "]");
        return null;

    }

    public static KalturaLiveAsset getliveAsset(KalturaFlavorAssetListResponse liveAssetList, int assetParamsId){
        //todo is there better way?
        for(KalturaFlavorAsset liveAsset :  liveAssetList.objects){
            if(liveAsset instanceof KalturaLiveAsset){
                if (liveAsset.flavorParamsId == assetParamsId){
                    return (KalturaLiveAsset)liveAsset;
                }
            }
        }
        return null;
    }
}
