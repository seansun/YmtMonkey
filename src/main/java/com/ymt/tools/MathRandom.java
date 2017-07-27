package com.ymt.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by sunsheng on 2017/6/27.
 */
public class MathRandom {

    private static final Logger logger = LoggerFactory.getLogger(MathRandom.class);

    // 种子值
    public long seed = System.currentTimeMillis();

    // 随机数生成器
    public Random random = new Random(seed);

    /**
     * 向左滑动 出现的概率为%4
     */
    public static final int EVENT_TYPE_SWIPE_LEFT = 0;

    /**
     * 向右滑动 出现的概率为%5
     */
    public static final int EVENT_TYPE_SWIPE_RIGHT = 1;

    /**
     * 向上滑动 出现的概率为%10
     */
    public static final int EVENT_TYPE_SWIPE_UP = 2;

    /**
     * 向下滑动 刷新 出现的概率为%20
     */
    public static final int EVENT_TYPE_SWIPE_DOWN = 3;


    /**
     * 点击返回 出现的概率为%10
     */
    public static final int EVENT_TYPE_BACK = 4;

    /**
     * 点击home键 回到桌面 出现的概率为%1
     */
    public static final int EVENT_TYPE_HOMEKEY = 5;

    /**
     * 点击屏幕坐标 出现的概率为%50
     */
    public static final int EVENT_TYPE_TAP = 6;


    /**
     * 获取随机动作
     *
     * @return
     */
    public int percentageRandom() {

        int action = Math.abs(random.nextInt() % 700);
        //0
        if (action <=27) {

            return EVENT_TYPE_SWIPE_LEFT;

        } else if (action>27 && action <= 62) {

            return EVENT_TYPE_SWIPE_RIGHT;

        } else if (action >62  && action<= 132) {

            return EVENT_TYPE_SWIPE_UP;

        } else if (action >132 && action<=272) {

            return EVENT_TYPE_SWIPE_DOWN;

        } else if (action > 272 && action <= 342) {

            return EVENT_TYPE_BACK;

        } else if (action >342 && action <350) {

            return EVENT_TYPE_HOMEKEY;
        } else if (action >= 350) {

            return EVENT_TYPE_TAP;

        } else {
            return -1;
        }
    }


    public static void main(String... args) {

        Map<Integer,Integer>  map=new HashMap<Integer,Integer>();
        MathRandom mathRandom= new MathRandom();
        for (int i = 0; i < 999;i++) {

            Integer key=Integer.valueOf(mathRandom.percentageRandom());
            if (map.containsKey(key)){
                map.put(key,map.get(key)+1);
            }
            else {
                map.put(key,1);
            }

        }

        List<Map.Entry<Integer, Integer>> list =
                new ArrayList<Map.Entry<Integer, Integer>>(map.entrySet());

        for (Map.Entry entry : list) {

            System.out.println(String.format("key:%s,vlues:%s",entry.getKey(),entry.getValue()));


        }



    }

}
