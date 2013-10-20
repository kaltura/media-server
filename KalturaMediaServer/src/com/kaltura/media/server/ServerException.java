package com.kaltura.media.server;

@SuppressWarnings("serial")
public class ServerException extends Exception {

	public ServerException(String message) {
		super(message);
	}

	public ServerException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
