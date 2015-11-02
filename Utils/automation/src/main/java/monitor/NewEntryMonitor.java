package monitor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import provider.Provider;
import validator.Validator;
import kaltura.actions.CreateLiveEntry;

import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.client.types.KalturaLiveStreamEntry;

import configurations.DataProvider;
import configurations.DataValidator;

public class NewEntryMonitor extends Monitor {

	public static void main(String[] args) throws Exception {
		NewEntryMonitor monitor = new NewEntryMonitor(args);
		monitor.execute();
	}

	public NewEntryMonitor(String[] args) throws Exception {
		super(args);
	}

	@Override
	protected void execute() throws Exception {
		Thread.currentThread().setName("main");
		CreateLiveEntry createLiveEntry = new CreateLiveEntry(client, config.getOtherProperties());
		KalturaLiveStreamEntry entry = createLiveEntry.execute();
		
		// TODO create a new encoder

		System.out.println("### Create providers for entry - " + entry.id);					
		List<Provider> providers = new ArrayList<Provider>();
		for(DataProvider dataProvider : config.getDataProviders()){
			Constructor<Provider> constructor = dataProvider.getType().getConstructor(KalturaLiveEntry.class);
			Provider provider = constructor.newInstance(entry);
			providers.add(provider);
			provider.start();
		}

		System.out.println("### Create validators for entry - " + entry.id);
		for(DataValidator dataValidator : config.getDataValidators()){
			Constructor<Validator> constructor = dataValidator.getType().getConstructor(KalturaLiveEntry.class, List.class);
			constructor.newInstance(entry, providers);
		}

		client.getBaseEntryService().delete(entry.id);
	}
}
