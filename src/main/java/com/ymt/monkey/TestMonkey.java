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
 * Created by sunsheng
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


        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);


        for (int i = 0; i < 30; i++) {

            //clickScreen(100, 100, driver);

            try {

                    //System.out.println("pagesource:" + driver.getPageSource());


                    WebElement backButton = driver.findElement(By.xpath("//XCUIElementTypeButton[@name='back']"));


                System.out.println("isEnabled:" + backButton.isEnabled());
                System.out.println("isDisplayed:" + backButton.isDisplayed());;

                backButton.click();


                //System.out.println("pagesource:" + driver.getPageSource());

                //System.out.println("is contains 洋码头:"+driver.getPageSource().contains("洋码头"));


                //WebElement backButton = driver.findElementByXPath("//XCUIElementTypeWindow[0]");

                ///System.out.println("app name:"+(backButton instanceof WebElement));


                //System.out.println("app name:"+backButton.getAttribute("label"));

               // backButton.click();
                //System.out.println("pagesource:" + driver.getPageSource());


                //WebElement backButton = driver.findElementByXPath("//XCUIElementTypeWindow[0]");

                ///System.out.println("app name:"+(backButton instanceof WebElement));


                //System.out.println("app name:"+backButton.getAttribute("label"));

               // backButton.click();

            } catch (Exception e) {

                e.printStackTrace();
                System.out.println("time out NoSuchElementException");
            }

            Thread.sleep(1000);
        }


        //System.out.println("page source:"+driver.getPageSource());

/*
           ymt app 返回 控件

           <XCUIElementTypeButton type="XCUIElementTypeButton" name="back" label="back" enabled="true" visible="true"
            x="5" y="26" width="39" height="30"/>
           <XCUIElementTypeButton type="XCUIElementTypeButton" name="返回" label="返回" enabled="true" visible="false"
           x="8" y="31" width="21" height="22"/>
*/

        // pagesource:<?xml version="1.0" encoding="UTF-8"?><AppiumAUT><XCUIElementTypeApplication type="XCUIElementTypeApplication" name=" " label=" " enabled="true" visible="true" x="0" y="0" width="320" height="568">


        // pagesource:<?xml version="1.0" encoding="UTF-8"?><AppiumAUT><XCUIElementTypeApplication type="XCUIElementTypeApplication" name="微信" label="微信" enabled="true" visible="true" x="0" y="0" width="320" height="568">


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
