package com.github.btnguyen2k.gearmanworker.jobhandler;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.btnguyen2k.gearmanworker.IJobHandler;
import com.github.btnguyen2k.gearmanworker.JobExecResult;

/**
 * This {@link IJobHandler} runs jobs only when it is not busy.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since template-0.1.0
 */
public abstract class RunIfNotBusyJobHandler<T> extends RunAllJobHandler<T> {

    private static Logger LOGGER = LoggerFactory.getLogger(RunIfNotBusyJobHandler.class);

    private AtomicBoolean LOCK = new AtomicBoolean(false);

    /**
     * {@inheritDoc}
     */
    @Override
    public JobExecResult handle(String function, byte[] data) {
        if (!LOCK.compareAndSet(false, true)) {
            LOGGER.warn("I am busy, can not perform job [" + function + "]!");
            return null;
        }
        try {
            return super.handle(function, data);
        } finally {
            LOCK.set(false);
        }
    }

}
