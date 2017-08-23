package com.ymt.engine;

import com.ymt.entity.Action;
import com.ymt.entity.Step;
import com.ymt.tools.LimitQueue;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sunsheng on 2017/6/27.
 */
public class IOSEngine extends Engine {

    private static final Logger logger = LoggerFactory.getLogger(IOSEngine.class);

    private IOSDriver driver;

    public IOSEngine(IOSDriver driver, LimitQueue<Step> results){

        super(driver,results);

        this.driver=driver;

    }

    /**
     * 截图
     */
    @Override
    public void screenShot(String fileName) {

       // TODO: 2017/6/27   ios 截图实现

    }


    /**
     * ios 按 home 回到桌面操作
     */
    @Override
    public void homePress() {

        String result = "pass";

        //截图
        String screenShotName = takeScreenShot();

        Step step = new Step();

        logger.info("Event KEYCODE_HOME");

        driver.runAppInBackground(1);


        step.setElementName("Page");
        step.setAction(Action.HOME_PRESS);


        step.setScreenShotName(screenShotName);
        step.setResult(result);

        results.offer(step);

        //driver.launchApp();

        try {
            Thread.sleep(200);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 尝试后退
     */
    @Override
    public void back() {
        String result = "pass";

        //截图
        String screenShotName = takeScreenShot();

        Step step = new Step();

        logger.info("Event Click_BACK");


        driver.findElementByXPath("//XCUIElementTypeButton[@name='back']");


        step.setElementName("Page");
        step.setAction(Action.BACK);
        step.setScreenShotName(screenShotName);
        step.setResult(result);

        results.offer(step);

    }
}
