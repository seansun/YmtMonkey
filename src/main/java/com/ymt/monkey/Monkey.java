package com.ymt.monkey;

import com.ymt.engine.Engine;
import com.ymt.entity.Config;
import com.ymt.entity.DataRecord;
import com.ymt.entity.Performance;
import com.ymt.entity.Step;
import com.ymt.tools.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunsheng on 2017/5/26.
 */
public class Monkey {

    private static final Logger logger = LoggerFactory.getLogger(Monkey.class);

    public AppiumDriver driver;


    public DataRecord record = new DataRecord();

    public LimitQueue<Step> results = new LimitQueue<Step>();

    public LimitQueue<Performance> perfromances = new LimitQueue<Performance>(120);

    public Engine engine;

    public static int eventcount = 0;

    public long startTime;

    public static Map<String, Integer> pageCount = new HashMap<String, Integer>();

    public Config config;

    //monkey 测试最大运行时间,单位分钟  1800分钟
    public int TIMING;

    public Monkey() {

        loadConfig();

        TIMING = config.getCapability().getTime();

    }

    /**
     * 加载yaml 配置文件
     */
    public void loadConfig() {

        config = YamlUtil.loadYaml();

    }


    public void setupDriver() {

        startAppiumServer();


    }


    public void afterTravel() {

        try {

            driver.quit();

        } catch (Exception e) {

            logger.error("driver.quit 出现异常，{}", e);

        } finally {

            ThreadPoolManage.stopScheduledThreadPool();

            record.setDuringTime(String.format("%s s", (System.currentTimeMillis() - startTime) / 1000));

            getLog();

            record.setTotalStep(eventcount);

            record.setPageCount(pageCount);

            record.setResults(results);

            record.setPerfromance(perfromances);

            new Report().generateReport(record, "report");

            cleanEnv();

            //重置计数器
            eventcount=0;
        }
    }

    public void beforeTravel() {

        startTime = System.currentTimeMillis();

    }

    /**
     * 开始随机遍历
     */
    public boolean start() {
        return true;

    }

    /**
     * 统计页面 page 访问次数
     */
    public Runnable handlePageInfo() {
        return null;

    }

    public Runnable handleCpuAndMem() {
        return null;
    }

    /**
     * 启动appium server 服务
     */
    public void startAppiumServer() {

        logger.info("启动appium server 服务");

        new AppiumServer("appium").start();

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 清理环境
     */
    public void cleanEnv() {

    }


    public void getLog() {
    }


    public static void main(String... args) {


    }

}

