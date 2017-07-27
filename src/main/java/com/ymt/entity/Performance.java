package com.ymt.entity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sunsheng on 2017/7/3.
 */
public class Performance {
    //cpu 占用%
    private int cpu;
    //单位MB
    private String mem;
    private String time;

    public Performance(int cpu, int mem) {

        this.cpu = cpu;
        this.mem =  (new BigDecimal(mem).divide(new BigDecimal(1024), 2, BigDecimal.ROUND_HALF_UP)
                ).toString();

        time=new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    public int getCpu() {
        return cpu;
    }


    public String getMem() {
        return mem;
    }


    public String getTime() {
        return time;
    }


    public static void main(String args[]) {

        new Performance(1,0);
    }
}
