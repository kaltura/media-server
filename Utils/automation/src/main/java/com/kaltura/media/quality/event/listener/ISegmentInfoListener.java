package com.kaltura.media.quality.event.listener;

import java.util.List;

import com.kaltura.media.quality.model.Segment;
import com.kaltura.media.quality.validator.info.MediaInfoBase.Info;

public interface ISegmentInfoListener extends IListener {

	void onSegmentInfo(Segment segment, List<Info> infos);

}
