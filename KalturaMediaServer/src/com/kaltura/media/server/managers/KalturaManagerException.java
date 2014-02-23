package com.kaltura.media.server.managers;

import com.kaltura.media.server.KalturaServerException;

@SuppressWarnings("serial")
public class KalturaManagerException extends KalturaServerException {

	public KalturaManagerException(String message) {
		super(message);
	}

	public KalturaManagerException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
