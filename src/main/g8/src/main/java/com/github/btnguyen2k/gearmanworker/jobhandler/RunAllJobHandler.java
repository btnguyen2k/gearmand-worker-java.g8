package com.github.btnguyen2k.gearmanworker.jobhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.btnguyen2k.gearmanworker.IJobHandler;
import com.github.btnguyen2k.gearmanworker.JobExecStatus;

/**
 * This {@link IJobHandler} runs all jobs when they arrive.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since template-0.1.0
 */
public abstract class RunAllJobHandler implements IJobHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(RunAllJobHandler.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public JobExecStatus handle(String function, byte[] data) {
        long t = System.currentTimeMillis();
        LOGGER.info("Starting job [" + function + "]...");
        try {
            return doJob(data);
        } catch (Exception e) {
            return null;
        } finally {
            long d = System.currentTimeMillis() - t;
            LOGGER.info("Finished job [" + function + "] in " + d + "ms.");
        }
    }

    /**
     * Sub-class override this method to implement its own business logic.
     * 
     * @param data
     * @return
     * @throws Exception
     */
    protected abstract JobExecStatus doJob(byte[] data) throws Exception;
}
