/*
 * Copyright (c) 2018年11月15日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package robin.storage.service;

import java.util.concurrent.atomic.AtomicLong;
import robin.storage.entry.ObjectEntry;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/15
 * @Version 1.0.0
 */
public interface StorageService {

    void put(String key, ObjectEntry value);

    ObjectEntry get(String key);

    long version();
}
