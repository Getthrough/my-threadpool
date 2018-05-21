package com.getthrough.threadpool.exception;

/**
 * @author: getthrough
 * @date: 2018/5/21
 * @description:
 * @version:
 */
public class ThreadPoolClosedException extends RuntimeException {
    public ThreadPoolClosedException() {
    }

    public ThreadPoolClosedException(String message) {
        super(message);
    }

    public ThreadPoolClosedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThreadPoolClosedException(Throwable cause) {
        super(cause);
    }
}
