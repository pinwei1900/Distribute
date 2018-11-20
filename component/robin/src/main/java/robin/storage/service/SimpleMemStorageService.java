/*
 * Copyright (c) 2018年11月15日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package robin.storage.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import robin.storage.entry.ObjectEntry;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/15
 * @Version 1.0.0
 */
@Slf4j
public class SimpleMemStorageService implements StorageService {

    private ConcurrentHashMap<String, ObjectEntry> memStore = new ConcurrentHashMap<>();
    private AtomicLong version = new AtomicLong(0);

    @Override
    public void put(String key, ObjectEntry value) {
        memStore.put(key, value);
        version.incrementAndGet();
    }

    @Override
    public ObjectEntry get(String key) {
        return memStore.get(key);
    }

    @Override
    public long version() {
        return version.get();
    }
}
