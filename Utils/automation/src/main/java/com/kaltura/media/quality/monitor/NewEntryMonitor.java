package com.kaltura.media.quality.monitor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.media.quality.actions.CreateLiveEntry;
import com.kaltura.media.quality.configurations.DataProvider;
import com.kaltura.media.quality.configurations.DataValidator;
import com.kaltura.media.quality.configurations.EncoderConfig;
import com.kaltura.media.quality.encoder.Encoder;
import com.kaltura.media.quality.event.IListener;
import com.kaltura.media.quality.event.IProcessCompleteListener;
import com.kaltura.media.quality.provider.Provider;
import com.kaltura.media.quality.utils.ThreadManager;
import com.kaltura.media.quality.validator.Validator;

public class NewEntryMonitor extends Monitor {
	private static final Logger log = Logger.getLogger(NewEntryMonitor.class);

	public static void main(String[] args) throws Exception {
		NewEntryMonitor monitor = new NewEntryMonitor(args);
		monitor.execute();
	}

	public NewEntryMonitor(String[] args) throws Exception {
		super(args);
	}

	@Override
	protected void execute() throws Exception {
		Thread thread = Thread.currentThread();
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.setName("main");
		CreateLiveEntry createLiveEntry = new CreateLiveEntry(client, config.getOtherProperties());
		final KalturaLiveStreamEntry entry = createLiveEntry.execute();

        EncoderConfig encoderConfig = config.getEncoder();
		Constructor<Encoder> encoderConstructor = encoderConfig.getType().getConstructor(KalturaLiveStreamEntry.class, EncoderConfig.class);
		Encoder encoder = encoderConstructor.newInstance(entry, encoderConfig);

		encoder.addProcessCompleteListener(new IProcessCompleteListener() {
			
			@Override
			public void onProcessComplete(int exitCode) {
				try {
					client.getBaseEntryService().delete(entry.id);
				} catch (KalturaApiException e) {
					log.error(e.getMessage(), e); 
				}
				
				if(exitCode != 0){
					log.error("Encoder failed");
					ThreadManager.stop();
				}
			}

			@Override
			public int compareTo(IListener o) {
				if(o == this){
					return 0;
				}
				return 1;
			}
		});
		
		System.out.println("### Create providers for entry - " + entry.id);					
		List<Provider> providers = new ArrayList<Provider>();
		for(DataProvider dataProvider : config.getDataProviders()){
			Constructor<Provider> constructor = dataProvider.getType().getConstructor(KalturaLiveEntry.class, DataProvider.class);
			Provider provider = constructor.newInstance(entry, dataProvider);
			providers.add(provider);
			provider.start();
		}

		System.out.println("### Create validators for entry - " + entry.id);
		for(DataValidator dataValidator : config.getDataValidators()){
			Constructor<Validator> constructor = dataValidator.getType().getConstructor(KalturaLiveEntry.class, List.class);
			constructor.newInstance(entry, providers);
		}

		encoder.start();

		while(ThreadManager.shouldContinue()) {
			Thread.sleep(1000);
		}
		
		log.info("Stopping all threads");
		ThreadManager.stop();
	}
}
