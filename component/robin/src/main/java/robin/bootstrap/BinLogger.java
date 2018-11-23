/*
 * Copyright (c) 2018年11月20日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package robin.bootstrap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import robin.protobuf.RobinRequestProto.RobinRequest;
import robin.protobuf.RonbinLoggerProto.RonbinRecord;
import robin.protobuf.RonbinLoggerProto.RonbinRecord.Builder;
import robin.utils.BlockingRunable;

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
    private LinkedBlockingDeque<BlockingRunable> binQueue;

    private Builder logBuilder;

    /**
     * 输出流只能有一个，用于顺序写操作日志
     */
    private FileOutputStream out;

    @PostConstruct
    public void logger() throws IOException {
        binFile = new File(path);
        if (!binFile.exists()) {
            binFile.createNewFile();
        }
        binQueue = new LinkedBlockingDeque<>();
        out = new FileOutputStream(binFile, true);
        logBuilder = RonbinRecord.newBuilder();

        new Thread(() -> {
            while (true) {
                try {
                    BlockingRunable record = binQueue.take();
                    record.run();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "record-storage-update").start();
    }

    public void append(long version, RobinRequest request) throws InterruptedException {
        logBuilder.setVersion(version);
        logBuilder.setRequest(request);
        RonbinRecord record = logBuilder.build();

        BlockingRunable task = new BlockingRunable() {
            @Override
            public void run() {
                try {
                    record.writeDelimitedTo(out);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    this.countDown();
                }
            }
        };
        binQueue.put(task);
        task.await();
    }

    /**
     * 返回文件的输入流，需要手动关闭此流
     */
    public InputStream in() throws FileNotFoundException {
        return new FileInputStream(binFile);
    }

    /**
     * 读取内容，通过读取顺序数值
     */
    public List<RobinRequest> getRange(long start, long curr_version) throws IOException {
        List<RobinRequest> requestList = new ArrayList<>();
        try (InputStream in = in()) {
            RonbinRecord record;
            while ((record = RonbinRecord.parseDelimitedFrom(in)) != null) {
                if (record.getVersion() > start) {
                    requestList.add(record.getRequest());
                }
                if (record.getVersion() == curr_version) {
                    break;
                }
            }
        }

        return requestList;
    }

    /**
     * 不要调用此方法
     */
    public OutputStream out() {
        return out;
    }

}
