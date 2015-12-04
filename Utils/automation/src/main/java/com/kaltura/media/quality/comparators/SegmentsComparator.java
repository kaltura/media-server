package com.kaltura.media.quality.comparators;

import java.util.List;

import com.kaltura.media.quality.model.Segment;

public interface SegmentsComparator {
	void compare(List<Segment> segments);
}
