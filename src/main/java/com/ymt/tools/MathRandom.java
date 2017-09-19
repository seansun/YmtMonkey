package com.ymt.tools;

import com.ymt.entity.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by sunsheng on 2017/6/27.
 */
public class MathRandom {

    private static final Logger logger = LoggerFactory.getLogger(MathRandom.class);

    // 种子值
    public static long seed = System.currentTimeMillis();

    // 随机数生成器
    public static Random random = new Random(seed);


    // [4,5,10,20,10,1,50];

    /**
     * 向左滑动 出现的概率为%4
     */
    public static final int EVENT_TYPE_SWIPE_LEFT = 4;

    /**
     * 向右滑动 出现的概率为%5
     */
    public static final int EVENT_TYPE_SWIPE_RIGHT = 5;

    /**
     * 向上滑动 出现的概率为%10
     */
    public static final int EVENT_TYPE_SWIPE_UP = 10;

    /**
     * 向下滑动 刷新 出现的概率为%20
     */
    public static final int EVENT_TYPE_SWIPE_DOWN = 20;


    /**
     * 点击返回 出现的概率为%10
     */
    public static final int EVENT_TYPE_BACK = 10;

    /**
     * 点击home键 回到桌面 出现的概率为%2
     */
    public static final int EVENT_TYPE_HOMEKEY = 2;

    /**
     * 点击屏幕坐标 出现的概率为%49
     */
    public static final int EVENT_TYPE_TAP = 49;



    /**
     * 获取随机动作
     *
     * @return
     */
    public String getRandomOperation() {

        int action = random.nextInt(99);

        if (action <EVENT_TYPE_SWIPE_LEFT) {

            return Constant.SWIPE_LEFT;

        } else if (action>=EVENT_TYPE_SWIPE_LEFT && action < (EVENT_TYPE_SWIPE_LEFT +EVENT_TYPE_SWIPE_RIGHT)) {

            return Constant.SWIPE_RIGHT;

        } else if (action >=(EVENT_TYPE_SWIPE_LEFT+EVENT_TYPE_SWIPE_RIGHT)  && action< (EVENT_TYPE_SWIPE_LEFT+EVENT_TYPE_SWIPE_RIGHT+EVENT_TYPE_SWIPE_UP) ){

            return Constant.SWIPE_UP;

        } else if (action >=(EVENT_TYPE_SWIPE_LEFT+EVENT_TYPE_SWIPE_RIGHT+EVENT_TYPE_SWIPE_UP)  && action<(EVENT_TYPE_SWIPE_LEFT+EVENT_TYPE_SWIPE_RIGHT+EVENT_TYPE_SWIPE_UP+EVENT_TYPE_SWIPE_DOWN) ) {

            return Constant.SWIPE_DOWN;

        } else if (action >=(EVENT_TYPE_SWIPE_LEFT+EVENT_TYPE_SWIPE_RIGHT+EVENT_TYPE_SWIPE_UP+EVENT_TYPE_SWIPE_DOWN)  && action <(EVENT_TYPE_SWIPE_LEFT+EVENT_TYPE_SWIPE_RIGHT+EVENT_TYPE_SWIPE_UP+EVENT_TYPE_SWIPE_DOWN+EVENT_TYPE_BACK) ) {

            return Constant.CLICK_BACK;

        } else if (action >=(EVENT_TYPE_SWIPE_LEFT+EVENT_TYPE_SWIPE_RIGHT+EVENT_TYPE_SWIPE_UP+EVENT_TYPE_SWIPE_DOWN+EVENT_TYPE_BACK)  && action <(EVENT_TYPE_SWIPE_LEFT+EVENT_TYPE_SWIPE_RIGHT+EVENT_TYPE_SWIPE_UP+EVENT_TYPE_SWIPE_DOWN+EVENT_TYPE_BACK+EVENT_TYPE_HOMEKEY) ) {

            return Constant.HOME_PRESS;
        } else {

            return Constant.CLICK_SCREEN;
        }

    }


    public static void main(String... args) {

        Map<String,Integer>  map=new HashMap<String,Integer>();

        MathRandom mathRandom= new MathRandom();

        for (int i = 0; i < 1000;i++) {

            String key=mathRandom.getRandomOperation();

            if (map.containsKey(key)){
                map.put(key,map.get(key)+1);
            }
            else {
                map.put(key,1);
            }

        }

        List<Map.Entry<String, Integer>> list =
                new ArrayList<Map.Entry<String, Integer>>(map.entrySet());

        for (Map.Entry entry : list) {

            System.out.println(String.format("key:%s,vlues:%s",entry.getKey(),entry.getValue()));


        }



    }

}
