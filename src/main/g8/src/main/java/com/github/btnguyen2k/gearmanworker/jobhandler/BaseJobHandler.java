package com.github.btnguyen2k.gearmanworker.jobhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.btnguyen2k.gearmanworker.IJobHandler;
import com.github.btnguyen2k.gearmanworker.JobExecResult;
import com.github.btnguyen2k.gearmanworker.utils.IdUtils;

/**
 * Base class to implement {@link IJobHandler}.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since template-0.1.0
 */
public abstract class BaseJobHandler<T> implements IJobHandler {

    private static Logger LOGGER = LoggerFactory.getLogger("job-handler");
    protected final static String SEPARATOR = "\t";

    /**
     * {@inheritDoc}
     */
    @Override
    public JobExecResult handle(String function, byte[] data) {
        final long t = System.currentTimeMillis();
        final String ID = IdUtils.nextId();
        JobExecResult result = null;
        try {
            LOGGER.info(System.currentTimeMillis() + SEPARATOR + ID + SEPARATOR + "START"
                    + SEPARATOR + function);
            T obj = decode(data);
            if (obj == null) {
                String msg = System.currentTimeMillis() + SEPARATOR + ID + SEPARATOR + "ERROR"
                        + SEPARATOR + "Cannot decode received data";
                LOGGER.warn(msg);
                return new JobExecResult(JobExecResult.Status.ERROR, msg);
            } else {
                LOGGER.info(System.currentTimeMillis() + SEPARATOR + ID + SEPARATOR + "DATA"
                        + SEPARATOR + obj);
            }
            result = doJob(ID, obj);
            return result;
        } catch (Exception e) {
            String msg = System.currentTimeMillis() + SEPARATOR + ID + SEPARATOR + "ERROR"
                    + SEPARATOR + e.getMessage();
            LOGGER.error(msg, e);
            return new JobExecResult(JobExecResult.Status.ERROR, msg, e);
        } finally {
            final long d = System.currentTimeMillis() - t;
            LOGGER.info(System.currentTimeMillis() + SEPARATOR + ID + SEPARATOR + "FINISH"
                    + SEPARATOR + d + SEPARATOR + result);
        }
    }

    /**
     * Sub-class override this method to implement its own business logic.
     * 
     * @param runId
     *            unique id for each run
     * @param data
     *            the supplied data
     * @return
     * @throws Exception
     */
    protected abstract JobExecResult doJob(String runId, T data) throws Exception;

    /**
     * Decode the supplied {@code byte[]} to designated type.
     * 
     * @param data
     * @return
     * @throws Exception
     */
    protected abstract T decode(byte[] data) throws Exception;

}
