/*
 * Copyright (c) 2018年11月22日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package robin.backup.config;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import robin.storage.entry.ObjectEntry;
import robin.utils.CurrentTimerMap;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/22
 * @Version 1.0.0
 */
@Configuration
public class SlaveConfiguration {

    @Bean
    public CurrentTimerMap slaveServers() {
        return new CurrentTimerMap<String ,String >(30 * 1000);
    }

    @Bean
    public ConcurrentHashMap<String, ObjectEntry> memStore() {
        return new ConcurrentHashMap<>();
    }
}
