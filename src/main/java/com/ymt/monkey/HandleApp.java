package com.ymt.monkey;

import com.ymt.tools.AdbUtils;
import io.appium.java_client.android.AndroidDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sunsheng on 2017/6/28.
 */
public class HandleApp {

    private static final Logger logger = LoggerFactory.getLogger(HandleApp.class);

    public static Runnable launchAPP(String appPackageName, String deviceName, AndroidDriver driver) {

        AdbUtils adb = new AdbUtils(deviceName);

        Runnable runnable = new Runnable() {

            public void run() {
                try {

                    logger.info("**********启动app守护进程**********");

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
                    logger.error("app守护进程 error:{}", e);
                }
            }
        };

        return runnable;

    }
}
