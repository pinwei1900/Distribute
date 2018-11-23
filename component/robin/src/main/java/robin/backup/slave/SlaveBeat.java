/*
 * Copyright (c) 2018年11月23日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package robin.backup.slave;

import static robin.backup.enumtype.ResponseType.HEART_BEAT;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import robin.protobuf.SlaveProto.SlaveRequest;
import robin.protobuf.SlaveProto.SlaveRequest.Builder;
import robin.protobuf.SlaveProto.SlaveResponse;
import robin.storage.service.StorageService;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/23
 * @Version 1.0.0
 */
@Component
public class SlaveBeat {

    @Autowired
    StorageService storageService;

    @Value("${robin.master.backup.port}")
    private Integer port;
    @Value("${robin.master.backup.ip}")
    private String ip;
    @Value("${robin.master.backup.interval}")
    private Integer interval;
    @Value("${robin.master.backup.name}")
    private String name;

    private Builder builder;

    @PostConstruct
    public void beat() {
        builder = SlaveRequest.newBuilder();
        builder.setName(name);
        builder.setType(HEART_BEAT.type);
        new Thread(() -> {
            while (true) {
                try (Socket socket = new Socket(ip, port)) {
                    heartHandler(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(interval * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void heartHandler(Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        builder.setVersion(storageService.version());
        builder.build().writeDelimitedTo(out);
        SlaveResponse res = SlaveResponse.parseDelimitedFrom(socket.getInputStream());
        System.out.println("Ok" +  res.toString());
    }
}
