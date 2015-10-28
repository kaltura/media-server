import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ManifestUrlBuilder;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by asher.saban on 2/24/2015.
 */
public class ManifestUrlBuilderTest {

    @Test
    public void shouldBuildCorrectUrl() throws URISyntaxException {
        URI uri = ManifestUrlBuilder.buildManifestUrl("http://host:80/", "ENTRY-ID", "PARTNER");
        Assert.assertEquals(uri.toString(), "http://host:80/p/PARTNER/sp/PARTNER00/playManifest/entryId/ENTRY-ID/format/applehttp");
    }
}
