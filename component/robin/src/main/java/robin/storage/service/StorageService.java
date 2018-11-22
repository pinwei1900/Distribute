/*
 * Copyright (c) 2018年11月15日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package robin.storage.service;

import java.io.IOException;
import robin.protobuf.RobinRequestProto.RobinRequest;
import robin.storage.entry.ObjectEntry;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/15
 * @Version 1.0.0
 */
public interface StorageService {

    void store(RobinRequest request) throws IOException;

    ObjectEntry get(String key);

    /** 返回当前已经写入日志的版本 */
    long version();

    void put(String key, ObjectEntry value);
}
