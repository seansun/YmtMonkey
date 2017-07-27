package com.ymt.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 */
public class CmdUtil {

    private static final Logger logger = LoggerFactory.getLogger(CmdUtil.class);

    public CmdUtil() {

    }

    /**
     * 调用并执行控制台命令
     *
     * @param cmd 控制台命令
     * @return output
     */
    public String runAdbCmd(String cmd) {

        String adbCmd = String.format("adb %s", cmd);

        logger.debug("执行的adb 命令为:{}", adbCmd);

        return this.run(adbCmd);
    }

    /**
     * 调用并执行控制台命令
     *
     * @param cmd 控制台命令
     * @return output
     */
    public String run(String cmd) {
        String line;
        String cmdOut = "";
        BufferedReader br = null;
        try {

            br = getBufferedReader(cmd);

            while ((line = br.readLine()) != null) {
                cmdOut = cmdOut + line + System.getProperty("line.separator");
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

        logger.debug("执行的cmd 命令为 : {}", cmd);
        logger.debug("执行的cmd 命令结果为 : {}", cmdOut);
        return null == cmdOut ? null : cmdOut.trim();
    }

    /**
     * 判断是否Windows操作系统
     *
     * @return 是否windows系统
     */
    public static boolean isWindows() {
        String os = System.getProperty("os.name");
        return (os.toLowerCase().startsWith("win")) ? true : false;
    }

    public BufferedReader getBufferedReader(String cmd) {

        BufferedReader br = null;
        Process p;
        try {
            if (isWindows()) {
                String command = "cmd /c " + cmd;

                p = Runtime.getRuntime().exec(command);

            } else {
                String[] shell = {"sh", "-c", cmd};

                p = Runtime.getRuntime().exec(shell);

            }
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return br;

    }


    public static void main(String args[]) {

        CmdUtil cmd = new CmdUtil();

        cmd.run("taskkill /f /t /im cmd.exe");
        cmd.run("taskkill /f /t /im adb.exe");
        cmd.run("taskkill /f /t /im conhost.exe");


    }
}