package com.kaltura.media_server.services;
import java.util.Objects;
import org.apache.log4j.Logger;

// Todo: do not commit!!! => code added for tests purpose only

/**
 * Created by lilach.maliniak on 30/01/2017.
 */
public class KalturaEntryIdKey {

	private static Logger logger = Logger.getLogger(KalturaEntryIdKey.class);
	private String entryId;
	// todo: remove code before commit!!!
	private static int instanceId = 0;
	private static Integer refCount = 0;
	private int thisInstanceId = 0;

	public KalturaEntryIdKey(String entryId) {
		this.entryId = entryId;
		// todo: remove code before commit!!!
		synchronized (refCount) {
			instanceId++;
			thisInstanceId = instanceId;
			refCount++;
		}
	}

	@Override
	public boolean equals(Object o) {

		if (o == this) return true;
		if (!(o instanceof KalturaEntryIdKey)) {
			return false;
		}
		KalturaEntryIdKey key = (KalturaEntryIdKey) o;
		return Objects.equals(entryId, key.entryId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(entryId);
	}

	public String getEntryId() {
		return entryId;
	}

	public void setEntryId() {
		this.entryId = entryId;
	}

	@Override
	protected void finalize() throws Throwable {
		synchronized (refCount) {
			refCount--;
			logger.info("(" + entryId + ") (instance Id: " + instanceId + ") (ref count:" + refCount + ") finalize called, releasing entry key!!!");
		}
	}
}
