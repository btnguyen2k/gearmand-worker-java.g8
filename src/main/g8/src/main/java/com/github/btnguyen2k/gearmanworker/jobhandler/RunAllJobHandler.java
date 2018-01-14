package com.github.btnguyen2k.gearmanworker.jobhandler;

import com.github.btnguyen2k.gearmanworker.IJobHandler;
import com.github.btnguyen2k.gearmanworker.JobExecResult;

/**
 * This {@link IJobHandler} runs all jobs when they arrive.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since template-0.1.0
 */
public abstract class RunAllJobHandler<T> extends BaseJobHandler<T> {

    /**
     * {@inheritDoc}
     */
    @Override
    public JobExecResult handle(String function, byte[] data) {
        return super.handle(function, data);
    }

}
