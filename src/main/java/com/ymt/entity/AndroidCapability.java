package com.ymt.entity;

import java.util.List;

/**
 * Created by sunsheng on 2017/4/14.
 */
public class AndroidCapability {

    private List<Device> deviceNames;
    private String appPackage;
    private String appActivity;
    private String logCmd;

    public String getLogCmd() {
        return logCmd;
    }

    public void setLogCmd(String logCmd) {
        this.logCmd = logCmd;
    }

    public List<Device> getDeviceNames() {
        return deviceNames;
    }

    public void setDeviceNames(List<Device> deviceNames) {
        this.deviceNames = deviceNames;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getAppActivity() {
        return appActivity;
    }

    public void setAppActivity(String appActivity) {
        this.appActivity = appActivity;
    }

}
