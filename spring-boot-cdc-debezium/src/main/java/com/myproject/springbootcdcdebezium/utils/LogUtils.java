package com.myproject.springbootcdcdebezium.utils;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author nguyenle
 * @since 9:53 PM Sun 2/16/2025
 */
@UtilityClass
public class LogUtils {

    private static Logger logger = LoggerFactory.getLogger(LogUtils.class);

    public static void info(String msg) {
        logger.info(msg);
    }

    public static void warn(String msg) {
        logger.warn(msg);
    }

    public static void error(String msg) {
        logger.error(msg);
    }

    public static void error(Throwable e) {
        logger.error(e.getMessage(), e);
    }

}
