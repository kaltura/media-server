package com.kaltura.media.server.managers;


public interface IManager {

	public void init() throws KalturaManagerException;

	public void stop();
}
