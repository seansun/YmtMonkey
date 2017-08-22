package com.ymt.monkey;

import com.ymt.engine.Engine;
import com.ymt.entity.*;
import com.ymt.tools.*;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by sunsheng on 2017/5/26.
 */
public class Monkey {

    private static final Logger logger = LoggerFactory.getLogger(Monkey.class);

    // 种子值
    public long seed = System.currentTimeMillis();

    // 随机数生成器
    public Random random = new Random(seed);

    public AppiumDriver driver;

    public DesiredCapabilities capabilities;

    public DataRecord record = new DataRecord();

    public LimitQueue<Step> results = new LimitQueue<Step>();

    public LimitQueue<Performance> perfromances = new LimitQueue<Performance>(120);

    public Engine engine;

    public static int eventcount = 0;

    public long startTime;

    public static Map<String, Integer> pageCount = new HashMap<String, Integer>();

    public Config config;

    //monkey 测试最大运行时间,单位分钟
    public int TIMING;


    public Monkey() {

        loadConfig();

        capabilities= new DesiredCapabilities();

        capabilities.setCapability(CapabilityType.BROWSER_NAME, "");

        //设置收到下一条命令的超时时间,超时appium会自动关闭session
        capabilities.setCapability("newCommandTimeout", config.getCapability().getNewCommandTimeout());

        //capabilities.setCapability("unicodeKeyboard", "true");

        //不需要再次安装
        capabilities.setCapability("noReset", true);
        // 自动接受提示信息
        capabilities.setCapability("autoAcceptAlerts", true);

        TIMING = config.getCapability().getRunTime();

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


    public void handleApp(){


    }


    /**
     * 开始随机遍历
     */
    public boolean run() {

        boolean isNeedRetry = true;

        setupDriver();

        beforeTravel();

        logger.info("开始随机Monkey测试");

        handleApp();

        try {

            int width = engine.getScreenWidth();
            int height = engine.getScreenHeight();


            MathRandom mathRandom = new MathRandom();

            while (true) {

                switch (mathRandom.percentageRandom()) {

                    case MathRandom.EVENT_TYPE_SWIPE_LEFT: {

                        engine.swip(Action.SWIP_LEFT, 100);

                        break;

                    }
                    case MathRandom.EVENT_TYPE_SWIPE_RIGHT: {

                        engine.swip(Action.SWIP_RIGHT, 100);

                        break;
                    }
                    case MathRandom.EVENT_TYPE_SWIPE_UP: {
                        engine.swip(Action.SWIP_UP, 100);

                        break;
                    }
                    case MathRandom.EVENT_TYPE_SWIPE_DOWN: {

                        engine.swip(Action.SWIP_DOWN, 100);

                        break;
                    }
                    case MathRandom.EVENT_TYPE_BACK: {
                        engine.back();
                        break;
                    }
                    case MathRandom.EVENT_TYPE_HOMEKEY: {

                        engine.homePress();
                        break;
                    }
                    case MathRandom.EVENT_TYPE_TAP: {

                        int x = random.nextInt(width);
                        int y = random.nextInt(height);

                        engine.clickScreen(x, y);

                        break;
                    }

                }

                eventcount++;

                logger.info("---EVENT执行了：{} 次---", eventcount);


                long endTime = System.currentTimeMillis();


                if (ThreadPoolManage.stop) {


                    logger.info("************发生crash，当前任务即将结束*************");


                    ThreadPoolManage.stopScheduledThreadPool();


                    isNeedRetry = true;

                    break;

                }

                if ((endTime - startTime) > (TIMING * 60 * 1000)) {

                    logger.info("已运行{}分钟，任务即将结束", (endTime - startTime) / 60 / 1000);

                    ThreadPoolManage.stopScheduledThreadPool();

                    break;
                }


                //if (eventcount>10) ThreadPoolManage.stop=true;


            }

        } catch (Exception e) {

            logger.error("Monkey 测试出现异常:{}", e);


        } finally {

            afterTravel();

        }

        return isNeedRetry;

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

