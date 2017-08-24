package com.ymt.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sunsheng on 2017/5/10.
 */
public class AdbUtils extends Thread {

    private static final Logger logger = LoggerFactory.getLogger("adbLog");

    private CmdUtil cmdUtil;

    //单个设备，可不传入参数deviceName
    private String deviceName = null;

    private String findUtil = CmdUtil.isWindows() ? "findstr" : "grep";


    public AdbUtils() {

        init();

    }

    public AdbUtils(String deviceName) {

        this.deviceName = deviceName;

        init();
    }

    private void init() {

        cmdUtil = new CmdUtil();

        //启动adb 服务
        cmdUtil.runAdbCmd("start-server");

    }


    private String runAdbShell(String cmd) {

        return cmdUtil.runAdbCmd(String.format("-s %s shell %s", this.deviceName, cmd));

    }


    // 获取连接的设备列表
    public List<String> getDevices() {

        logger.info("获取当前活动的device列表");

        String cmd = String.format("adb devices|%s -v List", findUtil);

        String line;

        List<String> uuidList = new ArrayList<String>();

        BufferedReader br = null;

        try {

            br = cmdUtil.getBufferedReader(cmd);

            while ((line = br.readLine()) != null) {
                if (line.length() > 0) {
                    line = line.split("\\t")[0];
                    uuidList.add(line.trim());
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
        return uuidList;

    }


    @Override
    public void run() {

        String cmd = String.format("adb -s %s logcat -b main -b system -b events -b radio *:I|%s com.ymatou.shop",
                this.deviceName, findUtil);

        cleanLogcat();

        getLogcatLog(cmd);

    }

    public void cleanLogcat() {

        cmdUtil.runAdbCmd(String.format("-s %s logcat -c", this.deviceName));

    }

    private void getLogcatLog(String cmd) {

        BufferedReader br = null;

        try {

            br = cmdUtil.getBufferedReader(cmd);

            String line;

            while ((line = br.readLine()) != null) {

                logger.info(line);

                if (line.toLowerCase().contains("crash")) {

                    //发生crash,停止线程

                    ThreadPoolManage.stop = true;
                    this.stop();

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
     * 根据app packageName，获取版本号
     */
    public String getAppVersion(String packageName) {

        String appVersion = null;

        String lines = runAdbShell(String.format("dumpsys package %s", packageName));

        if (!lines.isEmpty()) {

            String appVerdions[] = lines.split(System.getProperty("line.separator"));

            for (String line : appVerdions) {

                if (line.trim().contains("versionName")) {

                    appVersion = line.split("=")[1].trim();

                }
            }


        }

        return appVersion;

    }

    /**
     * 获取 Device 设备名
     */
    public String getDeviceName() {

        return runAdbShell("getprop ro.product.model");

    }


    /**
     * 获取设备中的Android版本号
     */
    public String getAndroidVersion() {

        return runAdbShell("getprop ro.build.version.release");

    }

    /**
     * 删除tmp 下的截图文件
     */
    public void delTmpScreenFile() {

        runAdbShell("rm -r /data/local/tmp/*.png");
    }

    /**
     * 获取设备屏幕分辨率，return (width, high)
     */
    public String getScreenResolution() {

        String resolution = "";

        Pattern pattern = Pattern.compile("\\d+");

        String lines = runAdbShell(String.format("dumpsys display | %s PhysicalDisplayInfo", findUtil));

        Matcher matcher = pattern.matcher(lines);

        int i = 0;

        while (matcher.find()) {

            resolution = resolution + matcher.group();

            if (i == 1) break;

            resolution = resolution + "x";

            i++;
        }

        return resolution;

    }


    /**
     * 获取当前应用界面的包名和Activity，返回的字符串格式为：packageName/activityName
     */
    public String getFocusedPackageAndActivity() {
        String line = null;

        line = runAdbShell(String.format("dumpsys activity activities | %s mFocusedActivity", findUtil)).trim();

        if (!line.isEmpty()) {
            return line.split(" ")[3];
        }

        return line;
    }

    /**
     * 获取当前应用界面的包名和Activity，返回的字符串格式为：packageName/activityName
     */
    public String getCurrentPackageName() {

        String line = null;

        line = getFocusedPackageAndActivity();

        if (!line.isEmpty()) {
            return line.split("/")[0];
        }

        return line;
    }

    /**
     * 通过adb 截图
     */
    public void screencap(String fileName) {

        runAdbShell(String.format("/system/bin/screencap -p /data/local/tmp/%s", fileName));

    }

    /**
     * 将截图文件从手机pull到本地
     */
    public void pullScreen(String filename, String computerPath) {

        computerPath = computerPath.replace("\\", "/");

        cmdUtil.runAdbCmd(String.format("-s %s pull /data/local/tmp/%s %s", this.deviceName, filename, computerPath));

    }

    /**
     * 启动一个Activity
     *
     * @param component "com.android.settinrs/.Settings"
     */
    public void startActivity(String component) {

        runAdbShell(String.format("%s am start -n %s", this.deviceName, component));

    }

    /**
     * 根据app 包名 获取内存,cpu 信息
     * 内存 单位 KB，CPU 占用%
     *
     * @param packageName
     */
    public int[] getCpuAndMem(String packageName) {

        int[] cpuAndMem = new int[2];

        String line = runAdbShell(String.format("top -n 1 -d 0.5 | %s %s", findUtil, packageName));

        String firstLine = line.split(System.getProperty("line.separator"))[0].trim();

        if (firstLine.endsWith(packageName)) {

            String st = firstLine.replace(" ", "#");

            int mem = Integer.valueOf(st.split("K#")[1].trim());

            st = firstLine.split("  ")[2];

            int cpu = Integer.valueOf(st.split("%")[0].trim());

            cpuAndMem[0] = cpu;
            cpuAndMem[1] = mem;

        }
        return cpuAndMem;
    }

    /**
     * 获取总内存 单位 KB
     */
    public String getMemTotal() {

        String line = runAdbShell("cat proc/meminfo");

        for (String s : line.split(System.getProperty("line.separator"))) {

            if (s.contains("MemTotal")) {

                String kb = s.trim().split("        ")[1].replace(" ", "");

                return String.valueOf(kb.split("k")[0]);

            }

        }

        return "0";
    }

    /**
     * kill adb 进程
     */
    public void killAdb() {

        cmdUtil.run("taskkill /f /t /im cmd.exe");
        cmdUtil.run("taskkill /f /t /im adb.exe");
        cmdUtil.run("taskkill /f /t /im conhost.exe");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String... args) {

        AdbUtils adbUtils = new AdbUtils("2bad9d02");

        for (int i = 0; i < 2; i++) {

            System.out.println("*************i :****************" + i);
            System.out.println("cpu :" + adbUtils.getCpuAndMem("com.ymatou.shop")[0]);
            System.out.println("mem :" + adbUtils.getCpuAndMem("com.ymatou.shop")[1]);
            System.out.println("memTotal :" + adbUtils.getMemTotal());


            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

