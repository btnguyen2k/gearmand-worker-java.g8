package com.github.btnguyen2k.gearmanworker.samples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.btnguyen2k.gearmanworker.JobExecStatus;
import com.github.btnguyen2k.gearmanworker.jobhandler.RunAllJobHandler;

/**
 * Sample: no-op job handler.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since template-0.1.0
 */
public class NoopJobHandler extends RunAllJobHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(NoopJobHandler.class);

    /**
     * {@inheritDoc}
     */
    @Override
    protected JobExecStatus doJob(byte[] data) throws Exception {
        LOGGER.info("Received: " + new String(data, "UTF-8"));
        return null;
    }

}
