package com.ymt.monkey;

/**
 * Created by sunsheng on 2017/5/26.
 */
public class AppMonkey {


    public static void main(String... args) {

        Monkey monkey = null;

        try {
            if (args.length == 1) {
                String platForm = args[0].toLowerCase();


                if (platForm.equals("android")) {
                    monkey = new AndroidMonkey();

                } else if (platForm.equals("ios")) {
                    monkey = new IOSMonkey();
                }

            } else {
                System.err.println("参数类型错误,请指定系统: android/ios!");
            }

            boolean result = monkey.start();

            while (result) {

                result = monkey.start();

            }


        } catch (Exception e) {

            e.printStackTrace();

        }


    }

}
