package com.ymt.engine;

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
}
