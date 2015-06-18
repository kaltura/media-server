package utils;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;

/**
 * Created by asher.saban on 2/24/2015.
 */
public class ProcessHandler {

    private static final ExecutorService threadPool = Executors.newCachedThreadPool();
    private static final Logger log = Logger.getLogger(ProcessHandler.class);

    private static <T> T timedCall(Callable<T> c, long timeout, TimeUnit timeUnit)
            throws InterruptedException, ExecutionException, TimeoutException
    {
        FutureTask<T> task = new FutureTask<>(c);
        threadPool.execute(task);   //TODO or submit?
        return task.get(timeout, timeUnit);
    }

    public static ProcessBuilder createProcess(String command) {
        log.info("Create process with command: " + command);
        String[] splitCommand = command.split(" ");
        ProcessBuilder pb = new ProcessBuilder(splitCommand);
        pb.redirectErrorStream(true);
        return pb;
    }

    public static Process start(ProcessBuilder pb) throws IOException {
        final Process p = pb.start();

        //must continuously read input stream, otherwise the buffer will be full and the process will block.
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                try {
                    while ((line = bri.readLine()) != null) {
//                    while (bri.readLine() != null) {
                        log.info(line);   //todo
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        bri.close();
                    } catch (IOException e) {}
                }
            }
        });
        return p;
    }

    public static int waitFor(final Process process, long timeout, TimeUnit timeUnit) {
        int code = -1;
        try {
            code = timedCall(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return process.waitFor();
                }
            }, timeout, timeUnit);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            log.error("Process failed to finish after " + timeout + " seconds");
            process.destroy();
        }
        return code;
    }

    public static boolean isRunning(Process process) {
        try {
            process.exitValue();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static void destroy(Process process) {
        process.destroy();
    }

    public static void shutdown() {
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(10000, TimeUnit.NANOSECONDS);   //TODO magic number
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
