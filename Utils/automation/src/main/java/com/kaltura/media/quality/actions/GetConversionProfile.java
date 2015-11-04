package com.kaltura.media.quality.actions;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.types.KalturaConversionProfile;

/**
 * Created by asher.saban on 3/8/2015.
 */
public class GetConversionProfile {

    private int id;
    private KalturaClient client;

    public KalturaConversionProfile execute() throws KalturaApiException {
        return client.getConversionProfileService().get(id);
    }
}
