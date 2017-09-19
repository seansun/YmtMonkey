package com.ymt.entity;

import java.io.File;

/**
 * Created by sunsheng
 */
public class Constant {

    private static final String ROOT = System.getProperty("user.dir");

    public static File getResultPath() {
        return new File(ROOT, "results/");
    }

    public static final String CLICK_SCREEN="clickScreen";

    public static final String SWIPE_UP = "swipeUp";

    public static final String SWIPE_DOWN = "swipeDown";

    public static final String SWIPE_LEFT = "swipeLeft";

    public static final String SWIPE_RIGHT = "swipeRight";

    public static final String HOME_PRESS="homePress";

    public static final String CLICK_BACK="back";


    public static void main(String args[]) {

        System.out.println(Constant.getResultPath().getAbsolutePath());
        System.out.print(Constant.getResultPath().getPath());

    }
}
