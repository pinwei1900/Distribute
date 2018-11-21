/*
 * Copyright (c) 2018年11月20日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */

import com.google.protobuf.ByteString;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.Test;
import robin.backup.BinLogger;
import robin.protobuf.RobinRequestProto.RobinRequest;
import robin.protobuf.SlaveProto.SlaveResponse;
import robin.protobuf.SlaveProto.SlaveResponse.Builder;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/20
 * @Version 1.0.0
 */
public class BinlogTest {

    BinLogger binLogger;

    @Test
    public void writeTest() throws IOException {
        OutputStream out = binLogger.out();
        for (int i = 0; i < 10; i++) {
            Builder builder = SlaveResponse.newBuilder();
            builder.setVersion(i);
            builder.setOp(1);

            RobinRequest.Builder req = RobinRequest.newBuilder();
            req.setKey("key" + i);
            req.setType(2);
            req.setContent(ByteString.copyFromUtf8("string" + i));
            RobinRequest data = req.build();
            builder.addData(data);
            SlaveResponse record = builder.build();
            record.writeDelimitedTo(out);
        }
        out.close();
    }

    @Test
    public void readTest() throws IOException {
        InputStream in = binLogger.in();
        SlaveResponse req;
        while ((req = SlaveResponse.parseDelimitedFrom(in)) != null) {
            System.out.println(req);
        }
        in.close();
    }
}
