package com.kaltura.media_server.services;

@SuppressWarnings("serial")
public class KalturaServerException extends Exception {

	public KalturaServerException(String message) {
		super(message);
	}

	public KalturaServerException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
