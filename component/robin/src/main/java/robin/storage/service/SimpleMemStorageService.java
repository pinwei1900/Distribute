/*
 * Copyright (c) 2018年11月15日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package robin.storage.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import robin.bootstrap.BinLogger;
import robin.protobuf.RobinRequestProto.RobinRequest;
import robin.protobuf.RonbinLoggerProto.RonbinRecord;
import robin.storage.anotition.SimpleStore;
import robin.storage.entry.ObjectEntry;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/15
 * @Version 1.0.0
 */
@Slf4j
@Service
@ConditionalOnBean(annotation = SimpleStore.class)
public class SimpleMemStorageService implements StorageService {

    @Autowired
    private ConcurrentHashMap<String, ObjectEntry> memStore;

    private AtomicLong version = new AtomicLong(0);

    @Autowired
    private BinLogger binLogger;

    @PostConstruct
    public void warmUp() throws IOException {
        try (InputStream in = binLogger.in()) {
            long initVersion = 0;
            RonbinRecord record;
            while ((record = RonbinRecord.parseDelimitedFrom(in)) != null) {
                initVersion = record.getVersion();
                RobinRequest request = record.getRequest();
                memStore.put(request.getKey(), new ObjectEntry(request.getContent().toByteArray()));
            }
            version.set(initVersion);
        }
    }

    @Override
    public synchronized void store(RobinRequest request) {
        /** 使用同步等待异步执行的方法，只有写入到文件中之后，才能写入到内存之中 */
        try {
            binLogger.append(version.getAndIncrement(), request);
            memStore.put(request.getKey(), new ObjectEntry(request.getContent().toByteArray()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ObjectEntry get(String key) {
        return memStore.get(key);
    }

    @Override
    public synchronized long version() {
        return version.get();
    }

    @Override
    public void put(String key, ObjectEntry value) {
        memStore.put(key, value);
    }
}
