package com.github.btnguyen2k.gearmanworker.jobhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.btnguyen2k.gearmanworker.IJobHandler;
import com.github.btnguyen2k.gearmanworker.JobExecResult;
import com.github.btnguyen2k.gearmanworker.utils.IdUtils;

/**
 * This {@link IJobHandler} runs all jobs when they arrive.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since template-0.1.0
 */
public abstract class RunAllJobHandler<T> extends BaseJobHandler<T> {

    private static Logger LOGGER = LoggerFactory.getLogger(RunAllJobHandler.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public JobExecResult handle(String function, byte[] data) {
        final long t = System.currentTimeMillis();
        final String ID = IdUtils.nextId();
        try {
            LOGGER.info("[" + ID + "] Starting job [" + function + "]...");
            T obj = decode(data);
            if (obj == null) {
                String msg = "[" + ID + "] Cannot decode received data!";
                LOGGER.warn(msg);
                return new JobExecResult(JobExecResult.Status.ERROR, msg);
            }
            JobExecResult result = doJob(ID, obj);
            return result;
        } catch (Exception e) {
            String msg = "[" + ID + "] Error while running job: " + e.getMessage();
            LOGGER.error(msg, e);
            return new JobExecResult(JobExecResult.Status.ERROR, msg, e);
        } finally {
            final long d = System.currentTimeMillis() - t;
            LOGGER.info("[" + ID + "] Finished job [" + function + "] in " + d + "ms.");
        }
    }

}
