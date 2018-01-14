package com.github.btnguyen2k.gearmanworker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.gearman.Gearman;
import org.gearman.GearmanFunction;
import org.gearman.GearmanServer;
import org.gearman.GearmanWorker;
import org.gearman.impl.GearmanImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.btnguyen2k.gearmanworker.utils.ConfigUtils;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigResolveOptions;

/**
 * Application's bootstrap class.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since template-0.1.0
 */
public class Bootstrap {

    private static Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

    private static Config loadConfig() {
        final String DEFAULT_CONF_FILE = "conf/application.conf";
        String cmdConfigFile = System.getProperty("config.file", DEFAULT_CONF_FILE);
        File configFile = new File(cmdConfigFile);
        if (!configFile.isFile() || !configFile.canRead()) {
            if (StringUtils.equals(cmdConfigFile, DEFAULT_CONF_FILE)) {
                String msg = "Cannot read from config file [" + configFile.getAbsolutePath() + "]!";
                LOGGER.error(msg);
                throw new RuntimeException(msg);
            } else {
                LOGGER.warn("Configuration file [" + configFile.getAbsolutePath()
                        + "], is invalid or not readable, fallback to default!");
                configFile = new File(DEFAULT_CONF_FILE);
            }
        }
        LOGGER.info("Loading configuration from [" + configFile + "]...");
        if (!configFile.isFile() || !configFile.canRead()) {
            String msg = "Cannot read from config file [" + configFile.getAbsolutePath() + "]!";
            LOGGER.error(msg);
            throw new RuntimeException(msg);
        }
        Config config = ConfigFactory.parseFile(configFile)
                .resolve(ConfigResolveOptions.defaults().setUseSystemEnvironment(true));
        LOGGER.info("Application config: " + config);
        return config;
    }

    private static String parseServers(Config config) {
        final String CONF_PATH = "gearman.servers";
        String servers = ConfigUtils.getConfigAsString(config, CONF_PATH);
        if (StringUtils.isBlank(servers)) {
            String msg = "Configuration [" + CONF_PATH + "] not found or invalid!";
            LOGGER.error(msg);
            throw new RuntimeException(msg);
        }
        String[] tokens = servers.split("[,;\\s]+");
        if (tokens == null || tokens.length < 1) {
            String msg = "Configuration [" + CONF_PATH + "] has invalid value: " + servers;
            LOGGER.error(msg);
            throw new RuntimeException(msg);
        }
        return servers;
    }

    private static List<String> parseFunctions(Config config) {
        final String CONF_PATH = "gearman.functions";
        List<String> functions = ConfigUtils.getConfigAsStringList(config, CONF_PATH);
        if (functions == null || functions.size() < 1) {
            String msg = "Configuration [" + CONF_PATH + "] not found or invalid!";
            LOGGER.error(msg);
            throw new RuntimeException(msg);
        }
        return functions;
    }

    private static List<GearmanServer> buildGearmanServers(Gearman gearman, String servers) {
        List<GearmanServer> gearmanServers = new ArrayList<>();
        String[] tokens = servers.split("[,;\\s]+");
        for (String token : tokens) {
            String[] hostAndPort = token.split(":");
            String host = hostAndPort[0];
            int port = Integer.parseInt(hostAndPort.length > 1 ? hostAndPort[1] : "4730");
            gearmanServers.add(gearman.createGearmanServer(host, port));
        }
        return gearmanServers;
    }

    private static Map<String, IJobHandler> buildHandlers(Config config) {
        String CONF_PATH = "gearman.handlers";
        Map<String, IJobHandler> result = new HashMap<>();
        Config configHandlers = ConfigUtils.getConfig(config, CONF_PATH);
        if (configHandlers == null) {
            String msg = "Configuration [" + CONF_PATH + "] not found or invalid!";
            LOGGER.error(msg);
            throw new RuntimeException(msg);
        }
        configHandlers.entrySet().forEach(entry -> {
            String function = entry.getKey();
            String className = entry.getValue().unwrapped().toString();
            try {
                Class<?> clazz = Class.forName(className);
                result.put(function, (IJobHandler) clazz.newInstance());
            } catch (Exception e) {
                String msg = "Error: " + e.getMessage();
                LOGGER.error(msg);
                throw new RuntimeException(msg, e);
            }
        });
        return result;
    }

    private static GearmanWorker buildWorker(Gearman gearman, Config config) {
        LOGGER.info("Building Gearman Worker...");

        String servers = parseServers(config);
        List<String> functions = parseFunctions(config);
        GearmanWorker worker = gearman.createGearmanWorker();

        {
            int maxConcurency = ConfigUtils.getConfigAsInt(config, "gearman.max_concurency", 1);
            worker.setMaximumConcurrency(maxConcurency);
            LOGGER.info("\tMax concurency: " + maxConcurency + " jobs");
        }

        {
            worker.setLostConnectionPolicy(ReconnectLostConnectionPolicy.INSTANCE);
            long reconnectPeriod = ConfigUtils.getConfigAsMilliseconds(config,
                    "gearman.reconnect_period", 1000);
            worker.setReconnectPeriod(reconnectPeriod, TimeUnit.MILLISECONDS);
            LOGGER.info("\tReconnect period: " + reconnectPeriod + "ms");
        }

        {
            Map<String, IJobHandler> handlers = buildHandlers(config);
            handlers.forEach((function, handler) -> LOGGER
                    .info("\tJobs [" + function + "] will be handled by " + handler));
            GearmanFunction gearmanFunction = (function, data, callback) -> {
                IJobHandler handler = handlers.get(function);
                if (handler == null) {
                    handler = handlers.get("_");
                }
                if (handler == null) {
                    LOGGER.warn("No handler to execute job [" + function + "]!");
                } else {
                    try {
                        @SuppressWarnings("unused")
                        JobExecResult result = handler.handle(function, data);
                        // TODO: do something with the result
                    } catch (Exception e) {
                        LOGGER.error("Exception while executing job [" + function + "]: "
                                + e.getMessage(), e);
                    }
                }
                return null;
            };
            for (String function : functions) {
                worker.addFunction(function, gearmanFunction);
                LOGGER.info("\tRegistered function [" + function + "].");
            }
        }

        {
            List<GearmanServer> gearmanServers = buildGearmanServers(gearman, servers);
            for (GearmanServer gearmanServer : gearmanServers) {
                worker.addServer(gearmanServer);
                LOGGER.info("\tListened jobs from server [" + gearmanServer.getHostName() + ":"
                        + gearmanServer.getPort() + "].");
            }
        }

        return worker;
    }

    private static Gearman buildGearman() throws IOException {
        int numThreads = Runtime.getRuntime().availableProcessors();
        return new GearmanImpl(numThreads < 2 ? 2 : numThreads);
    }

    public static void main(String[] args) throws Exception {
        Config appConfig = loadConfig();
        Gearman gearman = buildGearman();
        GearmanWorker gearmanWorker = buildWorker(gearman, appConfig);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    if (gearmanWorker != null && !gearmanWorker.isShutdown()) {
                        LOGGER.info("Shutting down Gearman worker...");
                        gearmanWorker.shutdown();
                    }
                } catch (Exception e) {
                    LOGGER.warn(e.getMessage(), e);
                }

                try {
                    if (gearman != null && !gearman.isShutdown()) {
                        LOGGER.info("Shutting down Gearman...");
                        gearman.shutdown();
                    }
                } catch (Exception e) {
                    LOGGER.warn(e.getMessage(), e);
                }
            }
        });
    }
}
