package com.ymt.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;

/**
 * Created by sunsheng on 2017/5/4.
 *
 * 启动appium server
 */
public class AppiumServer extends Thread {

    private static final Logger logger = LoggerFactory.getLogger("appium");

    private String cmd;

    public AppiumServer(String cmd) {

        this.cmd = cmd;

        //主线程执行完后,该线程停止
        this.setDaemon(true);

    }

    @Override
    public void run() {

        killAppiumServer();

        logger.info("start appium");

        System.out.println("start appium");

        cmdInvoke(cmd);

    }


    private void cmdInvoke(String cmd) {

        BufferedReader br = null;

        try {

            br = new CmdUtil().getBufferedReader(cmd);

            String line;

            while ((line = br.readLine()) != null) {

                logger.info(line);

                //System.out.println("line:"+line);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *  清理appium  ，node 进程
     */

    private void killAppiumServer(){

       if (CmdUtil.isWindows()){

           cmdInvoke("taskkill /f /t /im appium");
           cmdInvoke("taskkill /f /t /im node.exe");
       }
       else
           // 杀掉appium 进程，排查grep ,YmtMonkey 进程，
           cmdInvoke("ps -A|grep appium |grep -v grep|grep -v YmtMonkey|awk 'NR=1 {print $1}'|xargs kill -9");

    }


    public static void main(String args[]){


        new AppiumServer("appium").start();

    }

}
