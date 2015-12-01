package com.kaltura.media.quality.model;

public interface IFrame {

	public String getMediaType();

	public int getKeyFrame();

	public double getPts();

	public double getDts();

	public double getDuration();

	public int getPosition();

	public int getSize();

	public String getSampleFormat();

}
