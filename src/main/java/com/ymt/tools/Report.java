package com.ymt.tools;

import com.ymt.engine.Engine;
import com.ymt.entity.*;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by sunsheng on 2017/5/5.
 */
public class Report {

    private static final Logger logger = LoggerFactory.getLogger(Report.class);

    private boolean upload = true;

    public static String REPORT_PATH = Constant.getResultPath().getPath()
            + File.separator + Engine.currentTime + File.separator;

    private String screenshotPath = Engine.SCREENSHOT_PATH;

    public void generateReport(DataRecord record, String reportName) {

        logger.info("开始生成结果report");

        try {

            if (Constant.isAndroid){


                String deviceName = Engine.deviceName;

                AdbUtils adbUtils = new AdbUtils(deviceName);

                if (record.getResults().size() < 1) {
                    logger.info("结果为空,无需生成report");
                    return;
                }
                logger.info("开始从设备 pull 截图到本地路径:{}", screenshotPath);

                for (int i = 0; i < record.getResults().size(); i++) {

                    String fileName = String.format("monkey_screenShot%s.png", i + 1);
                    //将图片导入到本地
                    adbUtils.pullScreen(fileName, String.format("%s%s", screenshotPath, fileName));

                }

            }

            LimitQueue<Step> result = record.getResults();

            InputStream inputStream = (Report.class.getResourceAsStream("/reportTemplete.html"));

            Document doc = Jsoup.parse(inputStream, "UTF-8", "");

            //基本信息
            Element device = doc.getElementById("device");
            Element app = doc.getElementById("app");
            Element steps = doc.getElementById("steps");
            Element time = doc.getElementById("time");

            device.text(record.getDeviceName());
            app.text(record.getAppInfo());
            steps.text(String.valueOf(record.getTotalStep()));
            time.text(record.getDuringTime());

            //页面访问统计信息
            Map<String, Integer> pageInfo = record.getPageCount();

            List<Entry<String, Integer>> list;

            list = new ArrayList<Entry<String, Integer>>(pageInfo.entrySet());

            //倒序排序
            list.sort((a, b) -> {
                return b.getValue().compareTo(a.getValue());
            });

            Elements pageInfoDetail = doc.select("#pageInfo");

            StringBuffer element = new StringBuffer();

            for (Entry entry : list) {

                element.append("<tr>");
                element.append(String.format("<td>%s</td>", entry.getKey()));
                element.append(String.format("<td>%s</td>", entry.getValue()));
                element.append("</tr>");

            }

            pageInfoDetail.append(element.toString());

            //日志
            Elements pageLog = doc.select("#logs");


            pageLog.append(record.getAppLog());

            pageLog.append(record.getAppiumLog());


            if (Constant.isAndroid){

                //android 生成性能图表
                Elements script = doc.getElementsByTag("script").eq(4);

                LimitQueue<Performance> performanceList = record.getPerfromance();

                List<Integer> cpu = new ArrayList<>();
                List<String> mem = new ArrayList<>();
                List<String> timeLines = new ArrayList<>();

                for (int i = 0; i < performanceList.size(); i++) {
                    Performance p = performanceList.get(i);
                    cpu.add(p.getCpu());
                    mem.add(p.getMem());
                    timeLines.add(String.format("'%s'", p.getTime()));
                }

                String html = String.format("var totalMem=%s;var timeLine=%s;var data1=%s;var data2=%s;"
                        , record.getTotalMem(), timeLines.toString(), cpu.toString(), mem.toString());

                script.get(0).append(html);
            }



            //生成详细操作步骤
            writeDetail(result, doc);

            String fileName = REPORT_PATH + String.format("%s%s.html", reportName, Engine.taskId);

            File file = new File(fileName);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

            writer.write(doc.html());

            writer.close();

            logger.info("生成结果report路径为:{}", fileName);

        } catch (Exception e) {

            logger.error("生成report 异常,{}", e);
        }

    }

    public void writeDetail(LimitQueue<Step> result, Document doc) {

        for (int i = 0; i < result.size(); i++) {

            Step step = result.get(i);

            if (null != step.getScreenShotName()) {
                //处理图片
                markPicText(step, Engine.width / 2 - 250, Engine.height / 2);

            }

        }

        Element detailTitle = doc.getElementById("detailTitle");

        detailTitle.text("最后" + result.size() + "步操作步骤明细");

        Element deltail = doc.getElementById("detail");

        String th = "<th style=\"width:1%;font-weight:bold;\">step</th><th style=\"width:1%;font-weight:bold;\">action</th><th style=\"width:8%;font-weight:bold;\">screenShot</th><th style=\"width:1%;font-weight:bold;\">result</th>";

        //先处理header 头
        StringBuffer header = new StringBuffer();

        header.append("<tr>");

        for (int i = 0; i < result.size(); i++) {

            header.append(th);

        }

        header.append("</tr>");

        deltail.append(header.toString());

        StringBuffer element = new StringBuffer();

        element.append("<tr>");

        for (int i = 0; i < result.size(); i++) {

            Step step = result.get(i);

            element.append(String.format("<td>%d</td>", i + 1));
            element.append(String.format("<td>%s</td>", step.getAction()));
            //element.append(String.format("<td><img src='./screenshots/%s/%s_ps.png' align='absmiddle' width='250' height='400'/></td>", Engine.taskId, step.getScreenShotName()));
            element.append(String.format("<td><img src='%s' align='absmiddle' width='250' height='400'/></td>", step.getScreenShotName()));
            element.append(String.format("<td>%s</td>", step.getResult()));

        }
        element.append("</tr>");
        deltail.append(element.toString());

    }

    /**
     * 处理截图图片,添加文字
     *
     * @param step
     * @param x    添加文字，在图片的位置
     * @param y    添加文字，在图片的位置
     */
    private void markPicText(Step step, int x, int y) {

        String oriImageFileName = screenshotPath + step.getScreenShotName();
        String psImageFileName = oriImageFileName.replace(".png", "_ps.png");

        logger.info("markPicText oriImageFile path:{}", oriImageFileName);

        File file = new File(oriImageFileName);

        BufferedImage img = null;
        try {

            img = ImageIO.read(file);

        } catch (Exception e) {

            logger.error("markPicText 读取原始截图文件失败 {}", e);

            return;
        }
        Graphics2D graph = img.createGraphics();
        graph.setStroke(new BasicStroke(5));
        graph.setColor(Color.red);
        graph.setFont(new Font("Serif", Font.PLAIN, 120));

        if (step.getAction().equals(Action.CLICK_SCREEN)) {

            graph.setColor(Color.blue);

            graph.drawOval(step.getX(), step.getY(), 40, 40);

        } else {
            graph.drawString(step.getAction(), x, y);

        }
        graph.dispose();

        //生成ps处理后图片
        try {

            ImageIO.write(img, "png", new File(psImageFileName));

            logger.info("ps截图截图:{}", psImageFileName);

            //C:\Users\sunsheng\Desktop\YmtMonkey\results\20170704\screenshots\1\screenShot1.png
            String picPath="";

            if (upload) {

                picPath=FileUtil.upload(psImageFileName);

            } else {
                //设置为本地的相对位置
                picPath=psImageFileName.replace(REPORT_PATH, "./");

            }

            step.setScreenShotName(picPath);

        } catch (IOException e) {
            logger.error("生成ps截图文件失败 {},{}", psImageFileName, e);
        }


        //删掉原始图片
        FileUtils.deleteQuietly(new File(oriImageFileName));
    }


    public static void main(String args[]) {
        System.out.print(REPORT_PATH);

    }

}
