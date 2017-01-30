package com.kaltura.media_server.services;
import java.util.Objects;

/**
 * Created by lilach.maliniak on 30/01/2017.
 */
public class KalturaEntryIdKey {

	private String entryId;

	public KalturaEntryIdKey(String entryId) {
		this.entryId = entryId;
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
}
