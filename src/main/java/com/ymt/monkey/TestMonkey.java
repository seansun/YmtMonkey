package com.ymt.monkey;

import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by sunsheng on 2017/5/26.
 */
public class TestMonkey {

    @Test
    public void testMonkey() {

        Monkey monkey = new AndroidMonkey();

        boolean result = monkey.run();

/*        while (result) {

            result = monkey.run();

        }*/

    }

    @Test
    public void test() {

    }

    @Test
    public void testJsoup() {


    }

    @Test
    public void testIosMonkey() {

        Monkey monkey = new IOSMonkey();

        boolean result = monkey.run();

/*        while (result) {

            result = monkey.start();

        }*/

    }

    @Test
    public void testIOS() throws Exception {


        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability(CapabilityType.BROWSER_NAME, "ios");

        capabilities.setCapability("platformName", "ios");
        capabilities.setCapability("deviceName", "iPhone SE");
        capabilities.setCapability("platformVersion", "9.3.1");

        capabilities.setCapability("bundleId", "com.ymatou.Shopping");
        capabilities.setCapability("udid", "d36d600b4ece53a8bde8daf869790012a46842c3");

        capabilities.setCapability("automationName", "XCUITest");

        capabilities.setCapability("unicodeKeyboard", "True");
        capabilities.setCapability("resetKeyboard", "True");

        IOSDriver driver = new IOSDriver(new URL("http://127.0.0.1:4723/wd/hub"),
                capabilities);


        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);


        for (int i = 0; i < 10; i++) {

            clickScreen(100, 100, driver);

            try {

                System.out.println("pagesource:" + driver.getPageSource());

                WebElement backButton = driver.findElement(By.xpath("//*[@name='back']"));

                backButton.click();

            } catch (Exception e) {

                e.printStackTrace();
                System.out.println("time out NoSuchElementException");
            }

            Thread.sleep(3000);
        }


        //System.out.println("page source:"+driver.getPageSource());

/*
           ymt app 返回 控件

           <XCUIElementTypeButton type="XCUIElementTypeButton" name="back" label="back" enabled="true" visible="true"
            x="5" y="26" width="39" height="30"/>
           <XCUIElementTypeButton type="XCUIElementTypeButton" name="返回" label="返回" enabled="true" visible="false"
           x="8" y="31" width="21" height="22"/>
*/


    }


    /**
     * 点击屏幕坐标点
     *
     * @param x
     * @param y
     */
    public static void clickScreen(int x, int y, IOSDriver driver) {

        TouchAction action = new TouchAction(driver);

        action.tap(x, y).perform();

    }


}
