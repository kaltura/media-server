package com.kaltura.media.server;

@SuppressWarnings("serial")
public class ManagerException extends ServerException {

	public ManagerException(String message) {
		super(message);
	}

	public ManagerException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
