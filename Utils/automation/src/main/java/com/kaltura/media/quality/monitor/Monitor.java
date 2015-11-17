package com.kaltura.media.quality.monitor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.kaltura.client.KalturaClient;
import com.kaltura.media.quality.actions.StartSession;
import com.kaltura.media.quality.configurations.TestConfig;

abstract public class Monitor {
	protected TestConfig config;
	protected KalturaClient client;
	protected StartSession session;
	protected int partnerId;
	protected static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	
	public Monitor(String[] args) throws Exception {
		config = TestConfig.init(args);
		partnerId = Integer.valueOf(config.getPartnerId());
		session = new StartSession(partnerId,
				config.getServiceUrl(), config.getAdminSecret());
		client = session.execute();
	}

	abstract protected void execute() throws Exception;

	protected String createDefaultDownloadDir(String entryId) {
		String reportDate = DATE_FORMAT.format(new Date());
		return TestConfig.get().getDestinationFolder() + "/" + entryId + "/" + reportDate;
	}
}
