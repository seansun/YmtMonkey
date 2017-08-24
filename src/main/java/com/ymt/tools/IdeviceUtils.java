package com.ymt.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RunnableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sunsheng on 2017/5/10.
 */
public class IdeviceUtils {

    private static final Logger logger = LoggerFactory.getLogger("ideviceLog");

    private CmdUtil cmdUtil;

    private String udid = null;

    public IdeviceUtils(String udid) {

        this.udid = udid;

        cmdUtil = new CmdUtil();

        killIdevice();
    }

    /**
     * 获取连接到 mac 上的设备列表
     */
    public List<String> getDevices() {

        List<String> udidList=new ArrayList<>();

        String cmd = "instruments -s devices|grep -v Simulator";

        String lines = cmdUtil.run(cmd);

        String[] lineArray = lines.split(System.getProperty("line.separator"));

        for (int i = 1; i < lineArray.length; i++) {

            String line=lineArray[i];

            Pattern pattern = Pattern.compile("(?<=[).*?(?=])");

            Matcher matcher = pattern.matcher(line);

            while (matcher.find()) {

                String udid = matcher.group();

                udidList.add(udid);

            }

        }

        return udidList;

    }

    /**
     * kill idevicesyslog 进程
     */
    public void killIdevice() {

        cmdUtil.run("ps -A|grep idevicesyslog |grep -v grep|awk 'NR=1 {print $1}'|xargs kill -9");

    }

    public void getIdevicesyslog() {

        String cmd = String.format("idevicesyslog -d -u %s |grep 'iPhone Buyer'", udid);

        BufferedReader br = null;

        try {

            br = cmdUtil.getBufferedReader(cmd);

            String line;

            while ((line = br.readLine()) != null) {

                logger.info(line);

                if (line.toLowerCase().contains("crash")) {

                    //发生crash,停止线程
                    ThreadPoolManage.stop = true;

                }

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
     * 获取app版本号
     */
    public String getAppVersion() {

        String appVersion=null;

        String cmd = String.format("idevicesyslog -d -u %s |grep 'iPhone Buyer'", udid);

        BufferedReader br = null;

        try {

            br = cmdUtil.getBufferedReader(cmd);

            String line;

            while ((line = br.readLine()) != null) {

                if (line.toLowerCase().contains("appbuildversion")) {

                    Pattern pattern = Pattern.compile("(?<=\"appbuildversion\":\").*?(?=\")");

                    Matcher matcher = pattern.matcher(line);

                    while (matcher.find()) {
                        appVersion = matcher.group();
                    }

                    break;
                }

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

        return appVersion;

    }

    /**
     *  获取页面信息
     */
    public void getPageInfo(Map<String, Integer> pageCount) {

        String pageInfo=null;

        String cmd = String.format("idevicesyslog -d -u %s |grep 'iPhone Buyer'", udid);

        BufferedReader br = null;

        try {

            br = cmdUtil.getBufferedReader(cmd);

            String line;

            while ((line = br.readLine()) != null) {

                if (line.toLowerCase().contains("pagetype")) {

                    Pattern pattern = Pattern.compile("(?<=\"pagetype\":\").*?(?=\")");

                    Matcher matcher = pattern.matcher(line);

                    while (matcher.find()) {

                        pageInfo = matcher.group();

                        if (pageCount.containsKey(pageInfo)) {

                            pageCount.put(pageInfo, pageCount.get(pageInfo) + 1);

                        } else
                            pageCount.put(pageInfo, 1);

                    }

                }

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
     * 通过 idevicescreenshot 截图
     *
     * @param fileName 截图保存的位置带路径
     */
    public void screencap(String fileName) {

        cmdUtil.run(String.format("idevicescreenshot -u %s '%s'", udid,
                fileName));

    }


    public static void main(String... args) {

        IdeviceUtils adbUtils = new IdeviceUtils("2bad9d02");


        adbUtils.getPageInfo(new HashMap<>());

    }
}

