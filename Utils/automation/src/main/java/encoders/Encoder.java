package encoders;

import org.apache.log4j.Logger;

import utils.ProcessHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by asher.saban on 2/26/2015.
 */
public class Encoder implements Runnable {

    private static final Logger log = Logger.getLogger(Encoder.class);

    private String name;
    private String pathToEncoder;
    private String args;
    private Process process;

	private boolean isAlive = false;

    private static final Set<String> encodersMap;

    //initialize encoders map
    static {
        encodersMap = new HashSet<>();
        encodersMap.add("FMLE");
        encodersMap.add("FFMPEG");
    }

    public Encoder(String name, String pathToExecutable, String args) {
        //validate supported encoder
        if (!encodersMap.contains(name.toUpperCase())) {
            throw new IllegalArgumentException("Encoder with name " + name + " is not supported");
        }
        this.name = name;
        this.pathToEncoder = pathToExecutable;
        this.args = args;
    }

    protected void startStream() throws IOException {
        final String command = pathToEncoder + " " + args;
        System.out.println("Invoking stream:");
        System.out.println(command);

        ProcessBuilder pb = ProcessHandler.createProcess(command);
        process = ProcessHandler.start(pb);
    }

    public void stop() {
        log.info("stopping streaming");
        ProcessHandler.destroy(process);
    }

    public String getName() {
        return name;
    }

	@Override
	public void run() {
		isAlive  = true;
		try {
			startStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		isAlive = false;
	}
	
	public boolean isAlive(){
		return isAlive;
	}
}
