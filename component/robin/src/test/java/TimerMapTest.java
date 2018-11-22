/*
 * Copyright (c) 2018年11月22日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */

import org.junit.Test;
import robin.utils.CurrentTimerMap;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/22
 * @Version 1.0.0
 */
public class TimerMapTest {

    CurrentTimerMap<String, String> currentTimerMap = new CurrentTimerMap<>(10);

    @Test
    public void testPire() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                int i = 0;
                while (true) {
                    i++;
                    currentTimerMap.put(i + "", i + "");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        while (true) {
            System.out.println(currentTimerMap.size());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}
