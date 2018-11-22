/*
 * Copyright (c) 2018年11月22日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package robin.utils;

import java.util.concurrent.CountDownLatch;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/22
 * @Version 1.0.0
 */
public abstract class BlockingRunable implements Runnable {

    private final CountDownLatch countDown;

    public BlockingRunable() {
        this(1);
    }

    public BlockingRunable(int downnum) {
        countDown = new CountDownLatch(downnum);
    }

    public void await() throws InterruptedException {
        countDown.await();
    }

    public void countDown() {
        countDown.countDown();
    }
}
