/*
 * Copyright (c) 2018年11月15日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package robin;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/15
 * @Version 1.0.0
 */
@SpringBootApplication
public class RobinApplication implements CommandLineRunner {
    Logger logger = Logger.getLogger(this.getClass().getName());

    public static void main(String[] args) {
        SpringApplication.run(RobinApplication.class, args);
    }

    @Override
    public void run(String... args) {
        logger.log(Level.INFO, "application running ...");
    }
}
