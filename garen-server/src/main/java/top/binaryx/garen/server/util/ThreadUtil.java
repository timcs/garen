package top.binaryx.garen.server.util;

import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtil {

    public static void sleep(long millis) {
        sleep(millis, null);
    }

    public static void sleep(long millis, Logger log) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            if (log != null) {
                log.warn("Thread interrupted.", e);
            }
        }
    }

    public static ExecutorService newSingleThreadExecutor() {
        return Executors.newSingleThreadExecutor();
    }

}
