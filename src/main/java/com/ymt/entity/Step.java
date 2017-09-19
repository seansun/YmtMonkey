package com.ymt.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sunsheng on 2017/5/5.
 */
public class Step {


    private String action;

    //截图文件名
    private String screenShotName;
    private String elementName;

    private String result;

    //点击屏幕的坐标位置 location x,y,
    private int x;
    private int y;

    private String time;

    public Step() {

        this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getScreenShotName() {
        return screenShotName;
    }

    public void setScreenShotName(String screenShotName) {
        this.screenShotName = screenShotName;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
