package com.ymt.entity;

/**
 * Created by sunsheng on 2017/4/14.
 */
public class Capability {

    private int newCommandTimeout;
    private int launchTimeout;
    private String platformVersion;
    private String platformName;
    private String autoWebview;
    private String autoLaunch;
    private String noReset;
    private int runTime;



    public int getNewCommandTimeout() {
        return newCommandTimeout;
    }

    public void setNewCommandTimeout(int newCommandTimeout) {
        this.newCommandTimeout = newCommandTimeout;
    }

    public int getLaunchTimeout() {
        return launchTimeout;
    }

    public void setLaunchTimeout(int launchTimeout) {
        this.launchTimeout = launchTimeout;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getAutoWebview() {
        return autoWebview;
    }

    public void setAutoWebview(String autoWebview) {
        this.autoWebview = autoWebview;
    }

    public String getAutoLaunch() {
        return autoLaunch;
    }

    public void setAutoLaunch(String autoLaunch) {
        this.autoLaunch = autoLaunch;
    }

    public String getNoReset() {
        return noReset;
    }

    public void setNoReset(String noReset) {
        this.noReset = noReset;
    }

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }
}
