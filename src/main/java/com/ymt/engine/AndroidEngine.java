package com.ymt.engine;

import com.ymt.entity.Action;
import com.ymt.entity.Step;
import com.ymt.tools.AdbUtils;
import com.ymt.tools.LimitQueue;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by sunsheng on 2017/6/27.
 */
public class AndroidEngine extends Engine {

    private static final Logger logger = LoggerFactory.getLogger(AndroidEngine.class);

    private static final int KEYCODE_HOME = 3, KEYCODE_MENU = 82, KEYCODE_BACK = 4;

    private static AndroidDriver driver;

    private AdbUtils adbUtils;


    public AndroidEngine(AndroidDriver driver, LimitQueue<Step> results) {

        super(driver, results);

        this.driver = driver;

        adbUtils = new AdbUtils(deviceName);

    }


    /**
     * 截图
     */
    @Override
    public void screenShot(String fileName) {
        logger.info("截图开始");
        adbUtils.screencap(fileName);
        logger.info("截图结束");
    }


    /**
     * andriod 按 home 回到桌面操作
     */
    @Override
    public void homePress() {

        String result = "pass";

        //截图
        String screenShotName = takeScreenShot();

        Step step = new Step();

        logger.info("Event KEYCODE_HOME");

        driver.pressKeyCode(KEYCODE_HOME);


        step.setElementName("Page");
        step.setAction(Action.HOME_PRESS);
        step.setScreenShotName(screenShotName);
        step.setResult(result);

        results.offer(step);

        driver.launchApp();

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

        logger.info("Event KEYCODE_BACK");

        driver.pressKeyCode(KEYCODE_BACK);

        step.setElementName("Page");
        step.setAction(Action.BACK);
        step.setScreenShotName(screenShotName);
        step.setResult(result);

        results.offer(step);

    }
}
