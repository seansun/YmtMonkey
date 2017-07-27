package com.ymt.monkey;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.*;
import java.util.*;

/**
 * Created by sunsheng on 2017/5/26.
 */
public class TestMonkey {


    @Test
    public void testMonkey() {

        Monkey monkey = new AndroidMonkey();

        boolean result = monkey.start();


        while (result) {

            result = monkey.start();

        }

    }

    @Test
    public void test() {

        Map<String, String> tmp = new LinkedHashMap<String, String>();

        tmp.put("b", "bbb");
        tmp.put("a", "aaa");
        tmp.put("c", "ccc");
        tmp.put("d", "cdc");
        if (tmp.containsKey("b")) {

            tmp.remove("b");
        }
        tmp.put("b", "eeeee");


        Iterator<String> iterator_2 = tmp.keySet().iterator();
        while (iterator_2.hasNext()) {
            Object key = iterator_2.next();
            System.out.println("tmp.get(key) is :" + tmp.get(key));
        }
    }

    @Test
    public void testJsoup() {

        // File input = new File(Report.class.getResource("/reportTemplete.html").getPath());
        InputStream inputStream = (TestMonkey.class.getResourceAsStream("/reportTemplete.html"));
        Document doc = null;
        try {
            doc = Jsoup.parse(inputStream, "UTF-8", "");
        } catch (IOException e) {
            e.printStackTrace();
        }

         /*取得script下面的JS变量*/
        Elements script = doc.getElementsByTag("script").eq(4);

/*        System.out.println("size:" + script.size());
        System.out.println("data:" + script.get(0).data());

        script.forEach(
                e->
                System.out.println("data:" + e.data())

        );*/
        String html1="var data1=[null, null, null, null, null, null, null, null, null, null,\n" +
                "                   5, 25, 50, 120, 150, 200, 426, 660, 869, 1060, 1605, 2471, 3322,\n" +
                "                   4238, 5221, 6129, 7089, 8339, 9399, 10538, 11643, 13092, 14478,\n" +
                "                   15915, 17385, 19055, 21205, 23044, 25393, 27935, 30062, 32049,\n" +
                "                   33952, 35804, 37431, 39197, 45000, 43000, 41000, 39000, 37000,\n" +
                "                   35000, 33000, 31000, 29000, 27000, 25000, 24000, 23000, 22000,\n" +
                "                   21000, 20000, 19000, 18000, 18000, 17000, 16000];";
        String html2="[null, null, null, null, null, 6, 11, 32, 110, 235, 369, 640,\n" +
                "                   1005, 1436, 2063, 3057, 4618, 6444, 9822, 15468, 20434, 24126,\n" +
                "                   27387, 29459, 31056, 31982, 32040, 31233, 29224, 27342, 26662,\n" +
                "                   26956, 27912, 28999, 28965, 27826, 25579, 25722, 24826, 24605,\n" +
                "                   24304, 23464, 23708, 24099, 24357, 24237, 24401, 24344, 23586,\n" +
                "                   22380, 21004, 17287, 14747, 13076, 12555, 12144, 11009, 10950,\n" +
                "                   10871, 10824, 10577, 10527, 10475, 10421, 10358, 10295, 10104];";
        script.get(0).append(html1);

        script.get(0).append(html2);

        String fileName = "C:\\Users\\sunsheng\\Desktop\\testJsoup.html";

        File file = new File(fileName);

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));


            writer.write(doc.html());
            writer.close();

        } catch (Exception e) {
        }


    }

    @Test
    public void testList() {

        List<Integer> cpu=new ArrayList<>();
        List<Integer> mem=new ArrayList<>();

        /*
        for(int i=0;i<10;i++){

            cpu.add(i);
        }
        */

        System.out.println("cpu list:"+1/3);
        System.out.println("cpu list:"+1%3);


    }
}
