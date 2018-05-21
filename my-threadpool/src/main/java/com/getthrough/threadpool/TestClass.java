package com.getthrough.threadpool;

import com.getthrough.threadpool.mythreadpool.ThreadPool;
import com.getthrough.threadpool.mythreadpool.impl.DefaultThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author: getthrough
 * @date: 2018/5/21
 * @description:
 * @version:
 */
public class TestClass {

    private static Logger logger = LoggerFactory.getLogger(DefaultThreadPool.class);

    public static void main(String[] args) throws InterruptedException {
        ThreadPool threadPool = new DefaultThreadPool();

        for (int i = 0; i < 18; i++) {
            threadPool.execute(()-> {
                logger.info("TASK produced");
            });
            try {
                TimeUnit.MILLISECONDS.sleep(50L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        TimeUnit.SECONDS.sleep(1L);
        threadPool.shutdown();
        logger.info("shutdown : {}", threadPool.isShutdown());
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                logger.info("task submit after shutdown");
            }
        });
        TimeUnit.SECONDS.sleep(1L);
        ((DefaultThreadPool)threadPool).start();
        logger.info("thread pool restarted ");

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                logger.info("task submit after restart");
            }
        });
        TimeUnit.SECONDS.sleep(1L);
        threadPool.shutdown();


    }

}

