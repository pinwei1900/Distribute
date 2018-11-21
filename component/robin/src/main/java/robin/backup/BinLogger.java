/*
 * Copyright (c) 2018年11月20日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package robin.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.LinkedBlockingDeque;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import robin.protobuf.RobinRequestProto.RobinRequest;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/20
 * @Version 1.0.0
 */
@Component
public class BinLogger {

    @Value("${robin.binlog.file}")
    private String path;
    private File binFile;
    private LinkedBlockingDeque<RobinRequest> binQueue;

    /** 输出流只能有一个，用于顺序写操作日志 */
    private FileOutputStream out;

    @PostConstruct
    public void logger() throws IOException {
        binFile = new File(path);
        if (!binFile.exists()) {
            binFile.createNewFile();
        }
        binQueue = new LinkedBlockingDeque<>();
        out = new FileOutputStream(binFile, true);

        new Thread(() -> {
            while (true) {
                try {
                    RobinRequest request = binQueue.take();
                    request.writeDelimitedTo(out);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }, "record-storage-update").start();
    }

    public void append(RobinRequest request) {
        try {
            binQueue.put(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回文件的输入流，需要手动关闭此流  */
    public InputStream in() throws FileNotFoundException {
        return new FileInputStream(binFile);
    }
}
