package com.ymt.entity;

import com.ymt.tools.LimitQueue;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by sunsheng
 */
public class DataRecord {

    private String deviceName;
    private String appInfo;
    private String appLog;
    private String appiumLog;
    private String operaterLog;
    private String duringTime;

    private Map<String, Integer> pageCount;

    private LimitQueue<Step> results;

    private int totalStep;

    //测试设备的总内存大小 MB
    private String totalMem;

    private LimitQueue<Performance> perfromance;

    public String getDuringTime() {
        return duringTime;
    }

    public void setDuringTime(String duringTime) {
        this.duringTime = duringTime;
    }


    public Map<String, Integer> getPageCount() {
        return pageCount;
    }

    public void setPageCount(Map<String, Integer> pageCount) {
        this.pageCount = pageCount;
    }

    public LimitQueue<Step> getResults() {
        return results;
    }

    public void setResults(LimitQueue<Step> results) {
        this.results = results;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(String appInfo) {
        this.appInfo = appInfo;
    }


    public String getAppLog() {
        return appLog;
    }

    public void setAppLog(String appLog) {
        this.appLog = appLog;
    }

    public String getAppiumLog() {
        return appiumLog;
    }

    public void setAppiumLog(String appiumLog) {
        this.appiumLog = appiumLog;
    }

    public String getOperaterLog() {
        return operaterLog;
    }

    public void setOperaterLog(String operaterLog) {
        operaterLog = operaterLog;
    }


    public int getTotalStep() {
        return totalStep;
    }

    public void setTotalStep(int totalStep) {
        this.totalStep = totalStep;
    }


    public String getTotalMem() {
        return totalMem;
    }

    public void setTotalMem(String totalMem) {
        this.totalMem = (new BigDecimal(totalMem).divide(new BigDecimal(1024), 2, BigDecimal.ROUND_HALF_UP)
        ).toString();
    }

    public LimitQueue<Performance> getPerfromance() {
        return perfromance;
    }

    public void setPerfromance(LimitQueue<Performance> perfromance) {
        this.perfromance = perfromance;
    }
}
