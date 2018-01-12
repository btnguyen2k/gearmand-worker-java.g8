package com.github.btnguyen2k.gearmanworker.jobhandler;

import com.github.btnguyen2k.gearmanworker.IJobHandler;
import com.github.btnguyen2k.gearmanworker.JobExecResult;

/**
 * Base class to implement {@link IJobHandler}.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since template-0.1.0
 */
public abstract class BaseJobHandler<T> implements IJobHandler {
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
