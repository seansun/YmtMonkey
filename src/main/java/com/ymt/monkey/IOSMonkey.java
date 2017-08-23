package com.ymt.monkey;

import com.ymt.engine.IOSEngine;
import com.ymt.entity.IOSCapability;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sunsheng on 2017/6/27.
 */
public class IOSMonkey extends Monkey {

    private static final Logger logger = LoggerFactory.getLogger(IOSMonkey.class);
    //ios 配置信息
    public IOSCapability iosCapability;

    private IOSDriver driver;

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


        try {

            this.driver = new IOSDriver(new URL(iosCapability.getAppium()),
                    capabilities);

            super.driver=(AppiumDriver)driver;

            engine = new IOSEngine(driver,results);


        } catch (MalformedURLException e) {

            //engine = new Engine(null, results);

            logger.error("加载 IOSDriver 失败,{}", e);
        }

        record.setAppInfo(String.format("PackageName %s,Version %s", iosCapability.getBundleId(), "3.9.1"));

        //record.setDeviceName(String.format("DeviceName %s,SystemVersion %s,Resolution %s", adbUtils.getDeviceName(), adbUtils.getAndroidVersion(), adbUtils.getScreenResolution()));

    }


    @Override
    public void getLog() {

    }


    public static void main(String... args) {

    }
}
