/*
 * Copyright (c) 2018年11月15日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package robin.storage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import robin.storage.service.SimpleMemStorageService;
import robin.storage.service.StorageService;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/15
 * @Version 1.0.0
 */
@Configuration
public class StoreConfiguration {

    @Value("${robin.strategy}")
    StrategyEnum strategy;

    @Bean
    StorageService storageService() {
        switch (strategy) {
            case SIMPLE:
                return new SimpleMemStorageService();
            default:
                return new SimpleMemStorageService();
        }
    }
}
