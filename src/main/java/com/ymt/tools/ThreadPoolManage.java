package com.ymt.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by sunsheng on 2017/7/4.
 */
public class ThreadPoolManage {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolManage.class);

    private static ScheduledExecutorService SERVICE=Executors.newScheduledThreadPool(6);

    private static List<Future> futureList=new ArrayList<Future>();

    public static boolean stop = false;


    public static void joinScheduledThreadPool(Runnable runnable, long initialDelay,
                                               long period) {

        // 首次执行的延时时间，定时执行的间隔时间
        futureList.add(SERVICE.scheduleAtFixedRate(runnable,
                initialDelay,
                period,
                TimeUnit.SECONDS));


    }

    public static void joinScheduledThreadPool(Runnable runnable){

        futureList.add(SERVICE.submit(runnable));

    }


    public static void stopScheduledThreadPool() {

        futureList.forEach(f->f.cancel(true));

        stop = false;

    }


    public static void main(String[] args) {


        try {
            Thread.sleep(3000);

            logger.info("3s 后cancel");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SERVICE.shutdownNow();


    }
}
