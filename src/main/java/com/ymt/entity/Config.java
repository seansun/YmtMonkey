package com.ymt.entity;


/**
 * Created by sunsheng on 2017/4/14.
 */
public class Config {

    private Capability capability;

    private AndroidCapability androidCapability;

    private IOSCapability iosCapability;
    
    public Capability getCapability() {
        return capability;
    }

    public void setCapability(Capability capability) {
        this.capability = capability;
    }

    public AndroidCapability getAndroidCapability() {
        return androidCapability;
    }

    public void setAndroidCapability(AndroidCapability androidCapability) {
        this.androidCapability = androidCapability;
    }

    public IOSCapability getIosCapability() {
        return iosCapability;
    }

    public void setIosCapability(IOSCapability iosCapability) {
        this.iosCapability = iosCapability;
    }

}
