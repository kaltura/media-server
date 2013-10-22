package com.kaltura.media.server;

@SuppressWarnings("serial")
public class KalturaManagerException extends KalturaServerException {

	public KalturaManagerException(String message) {
		super(message);
	}

	public KalturaManagerException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
