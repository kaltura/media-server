package downloaders.hls;

import java.util.Set;

/**
 * Created by asher.saban on 7/15/2015.
 */
public interface PlaylistEnhancer {

	public Set<String> enhanceStreamsSet(Set<String> streams);
}
