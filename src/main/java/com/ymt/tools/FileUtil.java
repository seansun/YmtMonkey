package com.ymt.tools;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sunsheng on 2017/6/16.
 */
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    //fileupload.iapi.ymatou.com
    private static final String FILE_UPLOAD="http://fileupload.iapi.ymatou.com/api/File/Upload";

    private static final String FILE_UPLOAD_IP="http://172.16.101.38:88/api/File/Upload";
    //file.iapi.ymatou.com
    private static final String FILE_SERVERS="file.iapi.ymatou.com";

    private static final String FILE_SERVERS_IP="172.16.101.15";

    /**
     * 读取文件最后N行
     * <p>
     * 根据换行符判断当前的行数，
     * 使用统计来判断当前读取第N行
     * <p>
     *
     * @param file    待文件
     * @param numRead 读取的行数
     * @return List<String>
     */
    public static List<String> readLastNLine(File file, long numRead) {

        // 定义结果集
        List<String> result = new ArrayList<String>();
        //行数统计
        long count = 0;

        // 排除不可读状态
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            return null;
        }

        // 使用随机读取
        RandomAccessFile fileRead = null;
        try {
            //使用读模式
            fileRead = new RandomAccessFile(file, "r");
            //读取文件长度
            long length = fileRead.length();
            //如果是0，代表是空文件，直接返回空结果
            if (length == 0L) {
                return result;
            } else {
                //初始化游标
                long pos = length - 1;
                while (pos > 0) {
                    pos--;
                    //开始读取
                    fileRead.seek(pos);
                    //如果读取到\n代表是读取到一行
                    if (fileRead.readByte() == '\n') {

                        //使用readLine获取当前行
                        String line = fileRead.readLine().trim() + "\n";


                        //保存结果
                        result.add(line);
                        //行数统计，如果到达了numRead指定的行数，就跳出循环
                        count++;

                        if (count == numRead) {
                            break;
                        }
                    }
                }
                if (pos == 0) {
                    fileRead.seek(0);
                    result.add(fileRead.readLine());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("读取日志文件异常{}", e);
        } finally {
            if (fileRead != null) {
                try {
                    //关闭资源
                    fileRead.close();
                } catch (Exception e) {
                }
            }
        }

        Collections.reverse(result);

        return result;
    }

    /**
     * 如果是adb 日志,先去扫描是否有Exception日志
     *
     * @param file 待文件
     * @return List<String>
     */
    public static List<String> getErrorLine(File file) {

        // 定义结果集
        List<String> result = new ArrayList<String>();

        // 排除不可读状态
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            return null;
        }

        // 使用随机读取
        RandomAccessFile fileRead = null;
        try {
            //使用读模式
            fileRead = new RandomAccessFile(file, "r");
            //读取文件长度
            long length = fileRead.length();
            //如果是0，代表是空文件，直接返回空结果
            if (length == 0L) {
                return result;
            } else {
                //初始化游标
                long pos = 0;
                while (pos < length) {
                    pos++;
                    //开始读取
                    fileRead.seek(pos);
                    //如果读取到\n代表是读取到一行
                    if (fileRead.readByte() == '\n') {

                        //使用readLine获取当前行
                        String line = fileRead.readLine().trim() + "\n";

                        if (line.contains("crash") || line.contains("System.err")||line.contains("Exception")) {

                            result.add(line);

                        }


                    }
                }
            }
        } catch (NullPointerException e) {

        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("读取日志文件异常{}", e);
        } finally {
            if (fileRead != null) {
                try {
                    //关闭资源
                    fileRead.close();
                } catch (Exception e) {
                }
            }
        }

        return result;

    }


    /**
     * 上传图片 到文件服务器
     * @param filePath 本地文件地址
     * @return
     */
    public static String upload(String filePath){

        String url = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(FILE_UPLOAD_IP);

            MultipartEntity reqEntity = new MultipartEntity();

            FileBody fb = new FileBody(new File(filePath));

            reqEntity.addPart("file", fb);

            httppost.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(httppost);

            int statusCode = response.getStatusLine().getStatusCode();

            if(statusCode == HttpStatus.SC_OK){

                logger.info("文件服务器正常响应.....");

                HttpEntity resEntity = response.getEntity();

                String returnData=EntityUtils.toString(resEntity);//httpclient自带的工具类读取返回数据

                logger.info("文件服务器响应报文:{}",returnData);
                JSONObject data= new JSONObject(returnData).getJSONObject("Result");

                url = data.getString("FileUrl");

                url=url.replace(FILE_SERVERS,FILE_SERVERS_IP);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }


    /**
     * @param args
     */
    public static void main(String[] args) {

        upload("/Users/ymt/IdeaProjects/YmtMonkey/results/20170919/report1.html");

       // upload("C:\\Users\\sunsheng\\Desktop\\YmtMonkey\\results\\20170704\\screenshots\\1\\screenShot1_ps.png");
    }

}

