package com.github.btnguyen2k.gearmanworker.utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;

/**
 * Config helper class.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since template-0.1.0
 */
public class ConfigUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConfigUtils.class);

    /**
     * Get a sub-configuration. Return {@code null} if missing or wrong type.
     * 
     * @param config
     * @param path
     * @return
     */
    public static Config getConfig(Config config, String path) {
        try {
            return config.getConfig(path);
        } catch (ConfigException.Missing | ConfigException.WrongType e) {
            if (e instanceof ConfigException.WrongType) {
                LOGGER.warn(e.getMessage(), e);
            }
            return null;
        }
    }

    /**
     * Get a configuration as string. Return {@code null} if missing or wrong type.
     * 
     * @param config
     * @param path
     * @return
     */
    public static String getConfigAsString(Config config, String path) {
        try {
            return config.getString(path);
        } catch (ConfigException.Missing | ConfigException.WrongType e) {
            if (e instanceof ConfigException.WrongType) {
                LOGGER.warn(e.getMessage(), e);
            }
            return null;
        }
    }

    public static List<String> getConfigAsStringList(Config config, String path) {
        try {
            return config.getStringList(path);
        } catch (ConfigException.Missing | ConfigException.WrongType e) {
            if (e instanceof ConfigException.WrongType) {
                LOGGER.warn(e.getMessage(), e);
            }
            return null;
        }
    }

    public static long getConfigAsMilliseconds(Config config, String path, long defaultValue) {
        try {
            return config.getDuration(path, TimeUnit.MILLISECONDS);
        } catch (ConfigException.Missing | ConfigException.WrongType e) {
            if (e instanceof ConfigException.WrongType) {
                LOGGER.warn(e.getMessage(), e);
            }
            return defaultValue;
        }
    }

    public static int getConfigAsInt(Config config, String path, int defaultValue) {
        try {
            return config.getInt(path);
        } catch (ConfigException.Missing | ConfigException.WrongType e) {
            if (e instanceof ConfigException.WrongType) {
                LOGGER.warn(e.getMessage(), e);
            }
            return defaultValue;
        }
    }
}
