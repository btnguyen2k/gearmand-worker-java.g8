package com.github.btnguyen2k.gearmanworker.samples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.btnguyen2k.gearmanworker.JobExecResult;
import com.github.btnguyen2k.gearmanworker.jobhandler.RunAllJobHandler;

/**
 * Sample: no-op job handler.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since template-0.1.0
 */
public class NoopRunAllJobHandler extends RunAllJobHandler<String> {

    private final Logger LOGGER = LoggerFactory.getLogger(NoopRunAllJobHandler.class);

    /**
     * {@inheritDoc}
     */
    @Override
    protected JobExecResult doJob(String runId, String data) throws Exception {
        LOGGER.info("[" + runId + "] Received: " + data);
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
