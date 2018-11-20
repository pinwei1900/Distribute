/*
 * Copyright (c) 2018年11月15日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package robin.storage.service;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import robin.backup.BinLogger;
import robin.protobuf.RobinRequestProto.RobinRequest;
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

    private Lock lock = new ReentrantLock();

    @Autowired
    private BinLogger binLogger;

    @Override
    public void restore(RobinRequest request) {
        memStore.put(request.getKey(), new ObjectEntry(request.getContent().toByteArray()));
    }

    @Override
    public void store(RobinRequest request) throws IOException {
        /** 保证日志写入顺序 */
        lock.lock();
        memStore.put(request.getKey(), new ObjectEntry(request.getContent().toByteArray()));
        binLogger.append(request);
        lock.unlock();
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
