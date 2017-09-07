package com.ymt.monkey;

import com.ymt.engine.IOSEngine;
import com.ymt.entity.IOSCapability;
import com.ymt.tools.IdeviceUtils;
import com.ymt.tools.ThreadPoolManage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        ideviceUtils = new IdeviceUtils(iosCapability.getUdid());

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

        record.setAppInfo(String.format("PackageName %s,Version %s", iosCapability.getBundleId(), "3.9.1"));

        //record.setDeviceName(String.format("DeviceName %s,SystemVersion %s,Resolution %s", adbUtils.getDeviceName()
        // , adbUtils.getAndroidVersion(), adbUtils.getScreenResolution()));

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


    @Override
    public void getLog() {

    }


    public static void main(String... args) {

    }
}
