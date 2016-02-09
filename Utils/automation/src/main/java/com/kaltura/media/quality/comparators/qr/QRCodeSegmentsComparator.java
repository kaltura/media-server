package com.kaltura.media.quality.comparators.qr;

import com.kaltura.media.quality.comparators.BaseSegmentsComparator;
import com.kaltura.media.quality.comparators.ImageComparator;
import com.kaltura.media.quality.configurations.SegmentsComparatorConfig;

public class QRCodeSegmentsComparator extends BaseSegmentsComparator {
	
	public QRCodeSegmentsComparator(SegmentsComparatorConfig comparatorConfig) {
		super(comparatorConfig);
	}

	@Override
	protected ImageComparator getImageComparator(String diffPath)
	{
		return new QRCodeComparator(diffPath);
	}
}
