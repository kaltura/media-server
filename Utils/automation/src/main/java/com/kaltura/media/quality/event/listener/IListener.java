package com.kaltura.media.quality.event.listener;

import java.io.Serializable;

public interface IListener extends Comparable<IListener>, Serializable {
	boolean isDeffered();
	String getTitle();
}
