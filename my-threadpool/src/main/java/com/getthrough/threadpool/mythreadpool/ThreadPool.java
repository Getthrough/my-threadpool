package com.getthrough.threadpool.mythreadpool;

/**
 * <p>This interface is a top interface that defined several necessary methods,
 * it imitates {@link java.util.concurrent.ExecutorService},
 * {@link java.util.concurrent.ThreadPoolExecutor}
 * for personal learning.</p>
 * @author: getthrough
 * @date: 2018/5/20
 * @description:
 * @version:
 */
public interface ThreadPool {

    /**
     * to execute the given task in the future,
     * it can be executed by a thread or a thread pool.
     * @param runnable the given task
     */
    void execute(Runnable runnable);

    /**
     * It will close the thread pool after all submitted tasked are executed,
     * and will not accept new tasks.
     */
    void shutdown();

    /**
     * test whether the thread pool has been shut down.
     * @return the boolean result.
     */
    boolean isShutdown();

}
