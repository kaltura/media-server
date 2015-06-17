package utils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by asher.saban on 2/24/2015.
 */
public class ManifestUrlBuilder {

    public static URI buildManifestUrl(String serviceUrl, String entryId, String partnerId) throws URISyntaxException {

        //url = "$serviceUrl/p/{$partnerId}/sp/{$partnerId}00/playManifest/entryId/$entryId/format/applehttp";
        URI base = new URI(serviceUrl+":1935");

		System.out.println("---------------------- " + base);
		return base.resolve("/kLive/smil:" + entryId + "_all.smil/playlist.m3u8");
//        return base.resolve(String.format("/p/%1$s/sp/%1$s00/playManifest/entryId/%2$s/format/applehttp", partnerId, entryId));
    }

	public static void main(String[] args) throws URISyntaxException {
		System.out.println(buildManifestUrl("http://sabanas","012345","111"));
	}
}
