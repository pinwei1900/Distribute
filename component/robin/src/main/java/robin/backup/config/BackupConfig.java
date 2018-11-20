/*
 * Copyright (c) 2018年11月20日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package robin.backup.config;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import robin.backup.BinLogger;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/20
 * @Version 1.0.0
 */
@Configuration
public class BackupConfig {

    @Value("${robin.binlog.file}")
    String path;

    @Bean
    public BinLogger binLogger() throws IOException {
        return new BinLogger(path);
    }
}
