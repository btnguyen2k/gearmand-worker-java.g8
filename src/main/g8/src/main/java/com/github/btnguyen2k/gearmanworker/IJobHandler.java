package com.github.btnguyen2k.gearmanworker;

/**
 * Interface to handle job from Gearmand server.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since template-0.1.0
 */
public interface IJobHandler {
    /**
     * Run the job with supplied data.
     * 
     * @param function
     * @param data
     * @return
     * @throws Exception
     */
    public JobExecResult handle(String function, byte[] data) throws Exception;
}
