package com.kaltura.media.quality.monitor;

import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.kaltura.client.KalturaClient;
import com.kaltura.media.quality.actions.StartSession;
import com.kaltura.media.quality.configurations.DataProvider;
import com.kaltura.media.quality.configurations.DataValidator;
import com.kaltura.media.quality.configurations.LoggerConfig;
import com.kaltura.media.quality.configurations.TestConfig;
import com.kaltura.media.quality.provider.Provider;
import com.kaltura.media.quality.validator.Validator;
import com.kaltura.media.quality.validator.logger.ResultsLogger;

abstract public class Monitor {
	private static final Logger log = Logger.getLogger(Monitor.class);
	
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
		return TestConfig.get().getDestinationFolder() + "/content/" + entryId + "/" + reportDate;
	}
	
	protected void handleStream(String uniqueId) throws Exception {
		for(DataProvider dataProvider : config.getDataProviders()){
			log.info("Create provider [" + dataProvider.getType().getName() + "] for entry [" + uniqueId + "]");
			Constructor<Provider> constructor = dataProvider.getType().getConstructor(String.class, DataProvider.class);
			Provider provider = constructor.newInstance(uniqueId, dataProvider);
			provider.start();
		}

		for(DataValidator dataValidator : config.getDataValidators()){
			log.info("Create validator [" + dataValidator.getType().getName() + "] for entry [" + uniqueId + "]");
			Constructor<Validator> constructor = dataValidator.getType().getConstructor(String.class, DataValidator.class);
			constructor.newInstance(uniqueId, dataValidator);
		}

		for(LoggerConfig loggerConfig : config.getResultLoggers()){
			log.info("Create logger [" + loggerConfig.getType().getName() + "] for entry [" + uniqueId + "]");
			Constructor<ResultsLogger> constructor = loggerConfig.getType().getConstructor(String.class, LoggerConfig.class);
			constructor.newInstance(uniqueId, loggerConfig);
		}
	}
}
