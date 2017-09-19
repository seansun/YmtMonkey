package com.ymt.monkey;

import com.ymt.engine.IOSEngine;
import com.ymt.entity.Constant;
import com.ymt.entity.IOSCapability;
import com.ymt.tools.FileUtil;
import com.ymt.tools.IdeviceUtils;
import com.ymt.tools.ThreadPoolManage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.collections.CollectionUtils;
import org.openqa.selenium.remote.CapabilityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;



/**
 * Created by sunsheng on 2017/6/27.
 */
public class IOSMonkey extends Monkey {

    private static final Logger logger = LoggerFactory.getLogger(IOSMonkey.class);
    //ios 配置信息
    public IOSCapability iosCapability;

    private IOSDriver driver;

    private IdeviceUtils ideviceUtils;


    public IOSMonkey() {

        this.iosCapability = config.getIosCapability();


    }

    @Override
    public void setupDriver() {

        super.setupDriver();


        capabilities.setCapability(CapabilityType.BROWSER_NAME, "iOS");

        capabilities.setCapability("platformName", "iOS");

        capabilities.setCapability("deviceName", iosCapability.getDeviceName());

        capabilities.setCapability("platformVersion", iosCapability.getPlatformVersion());

        capabilities.setCapability("bundleId", iosCapability.getBundleId());

        capabilities.setCapability("udid", iosCapability.getUdid());

        capabilities.setCapability("automationName", "XCUITest");

        capabilities.setCapability("clearSystemFiles", true);


        capabilities.setCapability("wdaConnectionTimeout", 600000);


        ideviceUtils = new IdeviceUtils(iosCapability.getDeviceName(),iosCapability.getUdid());


        List<String> activityDevices=ideviceUtils.getDevices();


        if (! activityDevices.contains(iosCapability.getUdid())){

            logger.info("当前活动的 IOS Devices为 0");

            System.exit(1);
        }


        try {

            this.driver = new IOSDriver(new URL(iosCapability.getAppium()),
                    capabilities);

            super.driver = (AppiumDriver) driver;

            engine = new IOSEngine(driver, results);


            //抓取Idevicesys log
            ThreadPoolManage.joinScheduledThreadPool(getIdevicesyslog());

            //统计页面访问信息
            ThreadPoolManage.joinScheduledThreadPool(handlePageInfo());


        } catch (MalformedURLException e) {

            //engine = new Engine(null, results);

            logger.error("加载 IOSDriver 失败,{}", e);
        }


        //获取app 版本号
        Runnable r=new Runnable() {

            @Override
            public void run() {

                String appVersion=ideviceUtils.getAppVersion();

                record.setAppInfo(String.format("PackageName %s,Version %s", iosCapability.getBundleId()
                        , appVersion));

            }
        };

        //获取app 版本号
        ThreadPoolManage.joinScheduledThreadPool(r);


        record.setDeviceName(String.format("DeviceName %s,SystemVersion %s", driver.getCapabilities().getCapability("deviceName\n")
                , driver.getCapabilities().getCapability("platformVersion") ));

    }


    /**
     * ios 后台抓取Idevicesyslog
     */
    public Runnable getIdevicesyslog() {

        Runnable runnable = new Runnable() {

            public void run() {
                try {

                    logger.info("**********app 后台抓取Idevicesyslog **********");

                    ideviceUtils.getIdevicesyslog();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    logger.error("app后台抓取Idevicesyslog进程 error:{}", e);
                }
            }
        };

        return runnable;
    }

    /**
     * ios 统计页面 pagetype 访问次数
     */
    @Override
    public Runnable handlePageInfo() {

        Runnable runnable = new Runnable() {

            public void run() {
                try {

                    logger.info("**********app 后台异步统计页面访问**********");

                    ideviceUtils.getPageInfo(pageCount);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    logger.error("app后台异步统计页面进程 error:{}", e);
                }
            }
        };

        return runnable;
    }

    /**
     * 监控app 运行
     */
    @Override
    public void handleApp() {

        //后台开一线程监控当前运行的android app 包名
        ThreadPoolManage.joinScheduledThreadPool(HandleApp.launchIosAPP(driver), 30, 30);

    }



    /***
     * ios 提取appium,idevice 日志
     */
    @Override
    public void getLog() {

        String logPath = Constant.getResultPath().getPath() + File.separator + "logs/";

        String appiumLogPath = logPath + "appium.log";

        String ideviceLogPath = logPath + "idevice.log";

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
        //error
        List<String> adbLog = FileUtil.getErrorLine(new File(ideviceLogPath));
        List<String> adbLog2 = FileUtil.readLastNLine(new File(ideviceLogPath), lastLineNum);

        StringBuilder sbideviceLog = new StringBuilder();

        if (!CollectionUtils.isEmpty(adbLog)) {
            sbideviceLog.append("**********idevice log日志**********<br/>\n");
            adbLog.forEach(s -> {
                sbideviceLog.append(s);
                sbideviceLog.append("<br/>");
            });
        }

        sbideviceLog.append(String.format("**********idevice log最后%s行日志**********<br/>\n", lastLineNum));

        adbLog2.forEach(s -> {
            sbideviceLog.append(s);
            sbideviceLog.append("<br/>");
        });

        record.setAppiumLog(sbApp.toString());
        logger.info(sbApp.toString());
        record.setAppLog(sbideviceLog.toString());
        logger.info(sbideviceLog.toString());

    }


    /**
     * 清理环境
     */
    @Override
    public void cleanEnv() {

        ideviceUtils.killIdevice();

    }

    public static void main(String... args) {

    }
}
