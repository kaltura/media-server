package com.kaltura.media.server;

@SuppressWarnings("serial")
public class KalturaServerException extends Exception {

	public KalturaServerException(String message) {
		super(message);
	}

	public KalturaServerException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
