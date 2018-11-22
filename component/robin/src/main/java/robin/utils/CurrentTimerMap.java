/*
 * Copyright (c) 2018年11月22日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package robin.utils;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/22
 * @Version 1.0.0
 */
public class CurrentTimerMap<K, V> {


    private ConcurrentHashMap<K, V> map = new ConcurrentHashMap();
    private DelayQueue<SlaveDelay> delayKeys = new DelayQueue();

    private Timer timer = new Timer();
    private final int validSeconed;

    public CurrentTimerMap(int validSeconed) {
        this(validSeconed, 1);
    }

    public CurrentTimerMap(int validSeconed ,int clearSeconed) {
        this.validSeconed = validSeconed;
        timer.scheduleAtFixedRate(new ClearTask(), 0, clearSeconed * 1000); //六十秒刷新一次注册服务器
    }

    public void put(K key, V value) {
        map.put(key, value);
        delayKeys.put(new SlaveDelay(key, validSeconed));
    }

    public V get(K key) {
        return map.get(key);
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public int size() {
        return map.size();
    }

    class ClearTask extends TimerTask {
        @Override
        public void run() {
            while (true) {
                try {
                    K key = delayKeys.take().getKey();
                    map.remove(key);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 延迟队列中存放的数据
     */
    class SlaveDelay implements Delayed {

        K key;
        long executeTime;

        public SlaveDelay(K key, int delay) {
            this.key = key;
            this.executeTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(delay);
        }

        K getKey() {
            return key;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(executeTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            long d = (getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
            return (d == 0) ? 0 : ((d > 0) ? 1 : -1);
        }
    }
}
