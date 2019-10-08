package coinLogUtil;

import org.apache.log4j.Logger;

public class LogUtils {

    public static Logger logger = Logger.getLogger(LogUtils.class);

    public static Logger getLogger(){
        return logger;
    }
}
