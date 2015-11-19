package com.kaltura.media.quality.event;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.log4j.Logger;

abstract  public class Persistable implements Serializable {
	private static final long serialVersionUID = 6338640398194832955L;
	private static final Logger log = Logger.getLogger(Persistable.class);

	protected abstract String getPath();
	protected abstract String getExtension();
	protected abstract String getTitle();

	public void save() {
		String path = getPath() + "/" + this.hashCode();
		File file;
		do{
			file = new File(path + "." + getExtension());
			path += "_";
		} while(file.exists());
		
		try {
	        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
	        objectOutputStream.writeObject(this);
	        objectOutputStream.close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}
