package utils;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asher.saban on 3/4/2015.
 */
public class GlobalContext {

    private final static Map<String, Object> context = new HashMap<>();
    private static final Logger log = Logger.getLogger(GlobalContext.class);

    public static Object getValue(String key) {
        if (!context.containsKey(key)) {
            throw new IllegalArgumentException("key '" + key + "' not found in context");
        }
        Object val = context.get(key);
        log.info("Get from global context. key: " + key + " , value: " + val);
        return val;
    }

    public static void putValue(String key, Object val) {
        log.info("Adding to global context. key: " + key + " , value: " + val);
        context.put(key, val);
    }
}
