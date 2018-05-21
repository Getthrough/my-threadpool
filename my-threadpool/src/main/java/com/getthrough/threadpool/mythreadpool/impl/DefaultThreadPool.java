package com.getthrough.threadpool.mythreadpool.impl;

import com.getthrough.threadpool.mythreadpool.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author: getthrough
 * @date: 2018/5/20
 * @description:
 * @version:
 */
public class DefaultThreadPool implements ThreadPool {

    public Logger logger = LoggerFactory.getLogger(DefaultThreadPool.class);

    /**
     * Workers queue, get the task from {@code tasks} and run the task.
     */
    private BlockingQueue<Worker> workers = new LinkedBlockingQueue<>(DEFAULT_POOL_SIZE);

    /**
     * The queue to accept the tasks.
     */
    private BlockingQueue<Runnable> tasks = new LinkedBlockingQueue(MAX_POOL_SIZE);

    private int corePoolSize = 0;

    private int maxPoolSize = 0;

    /**
     * How long will the worker waits(keep alive) for the task if there is no task in tasks.
     */
    private volatile long aliveTime = 0L;

    /**
     * The default pool size.
     */
    private static final int DEFAULT_POOL_SIZE = 20;
    /**
     * The maximum pool size.
     */
    private static final int MAX_POOL_SIZE = 30;

    private volatile boolean isShutdown = false;

    public DefaultThreadPool() throws InterruptedException {
        this.corePoolSize = DEFAULT_POOL_SIZE;
        this.maxPoolSize = MAX_POOL_SIZE;
        new DefaultThreadPool(DEFAULT_POOL_SIZE, MAX_POOL_SIZE);
    }

    public DefaultThreadPool(int corePoolSize, int maxPoolSize) {
        if (corePoolSize <= 0 || maxPoolSize <= 0 || aliveTime < 0)
            throw new IllegalArgumentException("ERROR:arguments must greater than zero!");
        if (corePoolSize > maxPoolSize)
            throw new IllegalArgumentException("ERROR:corePoolSize can't be greater than maxPoolSize!");

        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;

        for (int i = 0; i < corePoolSize; i ++) {
            Worker worker = new Worker(getTask(0L));
            workers.add(worker);
            worker.start();
        }

    }

    @Override
    public void execute(Runnable runnable) {
        if (isShutdown) {
            logger.info("pool is closed, you should call start method");
            return;
        }

        if (workers.size() < corePoolSize) {
            Worker worker = new Worker(runnable);
            workers.add(worker);
            worker.start();
            logger.info("task is immediately got by work : {}", worker.getName());
        } else if (workers.size() == corePoolSize) {
            try {
                tasks.put(runnable);
                logger.info("task waiting in the task queue...");
            } catch (InterruptedException e) {
                logger.info("application is busy, please try again later!");
            }
        }

    }

    @Override
    public void shutdown() {
        // reject the new task
        isShutdown = true;

        for(;;) {
            if (tasks.size() == 0){

                // clear the work queue
                workers.clear();
                break;

            }
        }

        logger.info("shutting down the pool");

    }

    @Override
    public boolean isShutdown() {
        return workers.size() == 0;
    }


    private Runnable getTask(long timeOut) {

        try {
            return tasks.poll(timeOut, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void start() {
        isShutdown = false;
    }


    private class Worker extends Thread{

        private Runnable task;

        Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {

            while ((task != null || (task = getTask(60L)) != null)) {
                try {
//                    if (!Thread.interrupted())
                        task.run();
                    logger.info("worker : {} has finished the task.", getName());
                } finally {
                    task = null;
                }

            }

        }
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

}
