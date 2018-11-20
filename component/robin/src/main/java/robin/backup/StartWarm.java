/*
 * Copyright (c) 2018年11月20日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package robin.backup;

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import robin.protobuf.RobinRequestProto.RobinRequest;
import robin.storage.entry.ObjectEntry;
import robin.storage.service.StorageService;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/20
 * @Version 1.0.0
 */
@Service
public class StartWarm {

    @Autowired
    private BinLogger binLogger;

    @Autowired
    private StorageService storageService;

    @PostConstruct
    public void warm() {
        try (InputStream in = binLogger.in()) {
            RobinRequest req;
            while ((req = RobinRequest.parseDelimitedFrom(in)) != null) {
                storageService.put(req.getKey(), new ObjectEntry(req.getContent().toByteArray()));
            }
        } catch (IOException e) {
            e.printStackTrace();//启动预热失败
        }
    }
}
