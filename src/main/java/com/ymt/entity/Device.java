package com.ymt.entity;

/**
 * Created by sunsheng on 2017/4/17.
 */
public class Device {

    private String deviceName;
    private String appium;
    private String app;
    private String platformVersion;


    public String getPlatformVersion() {
        return platformVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }


    public String getAppium() {
        return appium;
    }

    public void setAppium(String appium) {
        this.appium = appium;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }
}
