package com.github.btnguyen2k.gearmanworker;

/**
 * Job execution's result status.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since template-0.1.0
 */
public class JobExecResult {

    public final static JobExecResult SUCCESSFUL = new JobExecResult(Status.SUCCESSFUL);

    public enum Status {
        DISCARDED(0), SUCCESSFUL(1), ERROR(2);

        private int value;

        private Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private Status status;
    private String message;
    private Object data;
    private Throwable error;

    public JobExecResult() {
        this(null, null, null, null);
    }

    public JobExecResult(Status status) {
        this(status, null, null, null);
    }

    public JobExecResult(Status status, String message) {
        this(status, message, null, null);
    }

    public JobExecResult(Status status, String message, Object data) {
        this(status, message, null, data);
    }

    public JobExecResult(Status status, String message, Throwable error) {
        this(status, message, error, null);
    }

    public JobExecResult(Status status, String message, Throwable error, Object data) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public JobExecResult setStatus(Status status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public JobExecResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public JobExecResult setData(Object data) {
        this.data = data;
        return this;
    }

    public Throwable getError() {
        return error;
    }

    public JobExecResult setError(Throwable error) {
        this.error = error;
        return this;
    }
}
