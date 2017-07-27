package com.ymt.entity;

/**
 * Created by sunsheng on 2017/4/14.
 */
public class IOSCapability {

    private  String deviceName;
    private  String bundleId;
    private  String screenshotWaitTimeout;
    private  String platformVersion;
    private  String autoAcceptAlerts;
    private  String app;
    private  String appium;
    private String logCmd;

    public String getLogCmd() {
        return logCmd;
    }

    public void setLogCmd(String logCmd) {
        this.logCmd = logCmd;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getScreenshotWaitTimeout() {
        return screenshotWaitTimeout;
    }

    public void setScreenshotWaitTimeout(String screenshotWaitTimeout) {
        this.screenshotWaitTimeout = screenshotWaitTimeout;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    public String getAutoAcceptAlerts() {
        return autoAcceptAlerts;
    }

    public void setAutoAcceptAlerts(String autoAcceptAlerts) {
        this.autoAcceptAlerts = autoAcceptAlerts;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getAppium() {
        return appium;
    }

    public void setAppium(String appium) {
        this.appium = appium;
    }
}
