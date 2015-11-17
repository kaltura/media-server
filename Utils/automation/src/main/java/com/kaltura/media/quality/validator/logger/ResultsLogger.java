package com.kaltura.media.quality.validator.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.kaltura.media.quality.configurations.TestConfig;
import com.kaltura.media.quality.utils.StringUtils;

public class ResultsLogger {
    private static final Logger log = Logger.getLogger(ResultsLogger.class);

	private FileWriter writer;
	protected String uniqueId;

	private int count;
	
	interface IResult{
		Object[] getValues();
		String[] getHeaders();
	}
	
	class Close extends Thread{
		@Override
		public void run(){
			try {
				writer.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	public ResultsLogger(String uniqueId, String name) throws IOException{
		this.uniqueId = uniqueId;
		
		writer = new FileWriter(getFilepath(name));
		Runtime.getRuntime().addShutdownHook(new Close());
	}
	
	protected String getFilepath(String name) {
		TestConfig config = TestConfig.get();
		File dir = new File(config.getDestinationFolder() + "/" + uniqueId + "/logs");
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		return dir.getAbsolutePath() + "/" + name + ".csv";
	}
	
	protected void write(IResult result){
		if(count == 0){
			String line = "\"" + StringUtils.join(result.getHeaders(), "\",\"") + "\"\n";
			try {
				writer.write(line);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		
		String line = "\"" + StringUtils.join(result.getValues(), "\",\"") + "\"\n";
		try {
			writer.write(line);
			count++;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}
