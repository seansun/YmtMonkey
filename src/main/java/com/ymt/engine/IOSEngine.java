package com.ymt.engine;

import com.ymt.entity.Action;
import com.ymt.entity.Step;
import com.ymt.tools.AdbUtils;
import com.ymt.tools.IdeviceUtils;
import com.ymt.tools.LimitQueue;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by sunsheng on 2017/6/27.
 */
public class IOSEngine extends Engine {

    private static final Logger logger = LoggerFactory.getLogger(IOSEngine.class);

    private IOSDriver driver;

    private IdeviceUtils ideviceUtils;


    public IOSEngine(IOSDriver driver, LimitQueue<Step> results) {

        super(driver, results);

        this.driver = driver;

        ideviceUtils=new IdeviceUtils("",driver.getCapabilities().getCapability("udid").toString());

    }

    /**
     * 截图
     */
    @Override
    public void screenShot(String fileName) {

        String screenshotPath = String.format("%s%s", Engine.SCREENSHOT_PATH, fileName);

        logger.info("截图开始");
        ideviceUtils.screencap(screenshotPath);
        logger.info("截图结束");


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

        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

        try {

            WebElement backButton = driver.findElement(By.xpath("//*[@name='back']"));

            backButton.click();


        } catch (Exception e) {

            logger.error("find back button time out");
        }

        step.setElementName("Page");
        step.setAction(Action.BACK);
        step.setScreenShotName(screenShotName);
        step.setResult(result);

        results.offer(step);

    }
}
