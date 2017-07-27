package com.ymt.monkey;

import com.ymt.engine.AndroidEngine;
import com.ymt.entity.*;
import com.ymt.tools.AdbUtils;
import com.ymt.tools.FileUtil;
import com.ymt.tools.MathRandom;
import com.ymt.tools.ThreadPoolManage;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.collections.CollectionUtils;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;

/**
 * Created by sunsheng on 2017/5/26.
 */
public class AndroidMonkey extends Monkey {

    private static final Logger logger = LoggerFactory.getLogger(AndroidMonkey.class);

    private AdbUtils adbUtils;

    public AndroidCapability androidCapability;
    // 种子值
    public long seed = System.currentTimeMillis();

    // 随机数生成器
    public Random random = new Random(seed);

    String deviceName = null;

    String appPackage = null;

    public AndroidMonkey() {

        this.androidCapability = config.getAndroidCapability();


    }


    @Override
    public void setupDriver() {

        super.setupDriver();

        List<Device> deviceList = androidCapability.getDeviceNames();

        String appActivity = androidCapability.getAppActivity();

        appPackage = androidCapability.getAppPackage();

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability(CapabilityType.BROWSER_NAME, "");

        capabilities.setCapability("platformName", "Android");

        //capabilities.setCapability("automationName", "Selendroid");
        //
        capabilities.setCapability("automationName", "Appium");

        //capabilities.setCapability("unicodeKeyboard", "true");

        //设置收到下一条命令的超时时间,超时appium会自动关闭session 30s
        capabilities.setCapability("newCommandTimeout", "30");

        String platformVersion = null;
        String url = null;

        //获取连接电脑活动的 Devices list
        List<String> activityDevices = new AdbUtils().getDevices();

        if (CollectionUtils.isEmpty(activityDevices)){

            logger.info("当前活动的 Devices为 0");

            System.exit(1);

        }

        for (Device device : deviceList) {

            if (device.getDeviceName().equals(activityDevices.get(0))) {

                deviceName = device.getDeviceName();

                platformVersion = device.getPlatformVersion();

                url = device.getAppium();

            }

        }

        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("platformVersion", platformVersion);

        //不需要再次安装
        capabilities.setCapability("noReset", true);
        // 自动接受提示信息
        capabilities.setCapability("autoAcceptAlerts", true);

        //capabilities.setCapability("app", app.getAbsolutePath());

        capabilities.setCapability("appPackage", appPackage);
        capabilities
                .setCapability("appActivity", appActivity);


        try {

            driver = new AndroidDriver(new URL(url),
                    capabilities);

            driver.launchApp();
            engine = new AndroidEngine(driver, results);

            adbUtils = new AdbUtils(deviceName);

            //抓取adb logcat 日志
            adbUtils.start();

            //统计页面访问信息
            ThreadPoolManage.joinScheduledThreadPool(handlePageInfo(), 20, 3);

            //设备的总内存大小
            record.setTotalMem(adbUtils.getMemTotal());
            //采集cpu,内存信息
            ThreadPoolManage.joinScheduledThreadPool(handleCpuAndMem(), 20, 2);

        } catch (MalformedURLException e) {

            //engine = new Engine(null, results);

            logger.error("加载 AndroidDriver 失败,{}", e);

        }

        record.setAppInfo(String.format("PackageName %s,Version %s", appPackage, adbUtils.getAppVersion(appPackage)));

        record.setDeviceName(String.format("DeviceName %s,SystemVersion %s,Resolution %s", adbUtils.getDeviceName(), adbUtils.getAndroidVersion(), adbUtils.getScreenResolution()));

    }


    /**
     * android 统计页面 activity 访问次数
     */
    @Override
    public Runnable handlePageInfo() {

        Runnable runnable = new Runnable() {

            public void run() {
                try {

                    logger.info("**********app 后台异步统计页面访问**********");

                    String pageUrl = adbUtils.getFocusedPackageAndActivity().split("/")[1];


                    if (pageCount.containsKey(pageUrl)) {

                        pageCount.put(pageUrl, pageCount.get(pageUrl) + 1);

                    } else
                        pageCount.put(pageUrl, 1);


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    logger.error("app后台异步统计页面进程 error:{}", e);
                }
            }
        };

        return runnable;
    }

    /**
     * android 采集 cpu,memery
     */
    @Override
    public Runnable handleCpuAndMem() {

        Runnable runnable = new Runnable() {

            public void run() {
                try {

                    logger.info("**********app 后台异步统计Cpu,Memery信息**********");

                    int[] cpuAndMem = adbUtils.getCpuAndMem(appPackage);
                    int cpu = cpuAndMem[0];
                    int mem = cpuAndMem[1];

                    perfromances.offer(new Performance(cpu, mem));

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    logger.error("app后台异步统计Cpu,Memery信息 error:{}", e);
                }
            }
        };

        return runnable;
    }

    /**
     * 开始随机遍历
     */
    @Override
    public boolean start() {

        boolean isNeedRetry = true;

        setupDriver();

        beforeTravel();

        //后台开一线程监控当前运行的android app 包名
        ThreadPoolManage.joinScheduledThreadPool(HandleApp.launchAPP(androidCapability.getAppPackage(), deviceName, driver), 10, 10);

        logger.info("开始随机Monkey测试");
        try {
            //主页面 activity name
            String mainActivity = driver.currentActivity();

            int width = engine.getScreenWidth();
            int height = engine.getScreenHeight();

            logger.info("主界面 mainActivity:{}", mainActivity);

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


    /***
     * android 提取appium,adb 日志
     */
    @Override
    public void getLog() {

        String logPath = Constant.getResultPath().getPath() + File.separator + "logs/";

        String appiumLogPath = logPath + "appium.log";

        String adbLogPath = logPath + "adb.log";

        int lastLineNum = 20;
        //处理appium log 日志
        List<String> appiumLog = FileUtil.readLastNLine(new File(appiumLogPath), lastLineNum);

        StringBuilder sbApp = new StringBuilder();

        sbApp.append(String.format("**********appium log最后%s行日志**********<br/>\n", lastLineNum));

        appiumLog.forEach(s -> {
            sbApp.append(s);
            sbApp.append("<br/>");

        });

        //处理app log 日志
        List<String> adbLog = FileUtil.getErrorLine(new File(adbLogPath));
        List<String> adbLog2 = FileUtil.readLastNLine(new File(adbLogPath), lastLineNum);

        StringBuilder sbAdb = new StringBuilder();

        if (!CollectionUtils.isEmpty(adbLog)) {
            sbAdb.append("**********adb log日志**********<br/>\n");
            adbLog.forEach(s -> {
                sbAdb.append(s);
                sbAdb.append("<br/>");
            });
        }

        sbAdb.append(String.format("**********adb log最后%s行日志**********<br/>\n", lastLineNum));
        adbLog2.forEach(s -> {
            sbAdb.append(s);
            sbAdb.append("<br/>");
        });

        record.setAppiumLog(sbApp.toString());
        logger.info(sbApp.toString());
        record.setAppLog(sbAdb.toString());
        logger.info(sbAdb.toString());

    }

    /**
     *
     */
    @Override
    public void beforeTravel() {

        super.beforeTravel();

        adbUtils.delTmpScreenFile();

    }

    /**
     * 清理环境
     */
    @Override
    public void cleanEnv() {

        adbUtils.killAdb();

    }

    public static void main(String... args) {
    }


}
