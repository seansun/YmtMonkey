package com.ymt.monkey;

import com.ymt.tools.AdbUtils;
import com.ymt.tools.AppiumServer;
import com.ymt.tools.CmdUtil;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sunsheng on 2017/6/28.
 */
public class HandleApp {

    private static final Logger logger = LoggerFactory.getLogger(HandleApp.class);

    public static Runnable launchAndroidAPP(String appPackageName, String deviceName, AndroidDriver driver) {

        AdbUtils adb = new AdbUtils(deviceName);

        Runnable runnable = new Runnable() {

            public void run() {
                try {

                    logger.info("**********启动Android app守护进程**********");

                    String packageName = adb.getCurrentPackageName();

                    if (!packageName.isEmpty()) {

                        if (!packageName.equals(appPackageName)) {

                            logger.info("**********当前活动的的APP是【{}】，非测试APP，重新呼起测试APP:{}**********", packageName, appPackageName);

                            driver.launchApp();

                            Thread.sleep(200);

                        }

                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    logger.error("Android app守护进程 error:{}", e);
                }
            }
        };

        return runnable;

    }

    public static Runnable launchIosAPP(String appPackageName, String deviceName, IOSDriver driver) {

        String udid=driver.getCapabilities().getCapability("udid").toString();
        String bundleId = driver.getCapabilities().getCapability("bundleId").toString();;


        Runnable runnable = new Runnable() {

            public void run() {
                try {

                    logger.info("**********启动ios app守护进程**********");


                    String cmd = String.format("idevicesyslog -d -u %s'", udid);

                    BufferedReader br = null;

                    try {

                        br = new CmdUtil().getBufferedReader(cmd);

                        String line;
                        String curbundleid;


                        while ((line = br.readLine()) != null) {

                            if(line.contains("HW kbd: currently")){
                                System.out.println("=============="+line);
                                if(line.split(" ")[8].equals("currently")){

                                    curbundleid = line.split(" ")[9];

                                }else{

                                    curbundleid = line.split(" ")[10];
                                }
                                //System.out.println("=============="+curbundleid);

                                if(!curbundleid.equals(bundleId)){

                                    logger.info("**********当前启动的APP bundleid是【{}】，非测试APP，重新呼起测试APP:{}**********", curbundleid);

                                    driver.launchApp();

                                    Thread.sleep(200);

                                }
                            }


                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (br != null) {
                            try {
                                br.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }


                } catch (Exception e) {

                    // TODO Auto-generated catch block
                    logger.error("app守护进程 error:{}", e);
                }
            }
        };

        return runnable;

    }
}
