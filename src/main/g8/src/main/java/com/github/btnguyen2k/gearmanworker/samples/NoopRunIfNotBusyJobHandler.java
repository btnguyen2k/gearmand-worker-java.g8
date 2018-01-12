package com.github.btnguyen2k.gearmanworker.samples;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.btnguyen2k.gearmanworker.JobExecResult;
import com.github.btnguyen2k.gearmanworker.jobhandler.RunIfNotBusyJobHandler;

/**
 * Sample: no-op job handler.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since template-0.1.0
 */
public class NoopRunIfNotBusyJobHandler extends RunIfNotBusyJobHandler<String> {

    private final Logger LOGGER = LoggerFactory.getLogger(NoopRunIfNotBusyJobHandler.class);
    private Random RAND = new Random(System.currentTimeMillis());

    /**
     * {@inheritDoc}
     */
    @Override
    protected JobExecResult doJob(String runId, String data) throws Exception {
        LOGGER.info("[" + runId + "] Received: " + data);
        Thread.sleep(RAND.nextInt(1024));
        return JobExecResult.SUCCESSFUL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String decode(byte[] data) throws Exception {
        return new String(data, "UTF-8");
    }

}
