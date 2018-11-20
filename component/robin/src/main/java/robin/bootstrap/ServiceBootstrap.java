/*
 * Copyright (c) 2018年11月15日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package robin.bootstrap;

import com.google.protobuf.ByteString;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import robin.protobuf.RobinRequestProto;
import robin.protobuf.RobinRequestProto.RobinRequest;
import robin.protobuf.RobinRequestProto.RobinResponse;
import robin.protobuf.RobinRequestProto.RobinResponse.Builder;
import robin.storage.entry.ObjectEntry;
import robin.storage.service.StorageService;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/15
 * @Version 1.0.0
 */
@Service
@Slf4j
public class ServiceBootstrap {

    @Value("${robin.port}")
    Integer port;

    @Autowired
    private StorageService storageService;

    @PostConstruct
    public void run() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket socket = serverSocket.accept();
            new HandlerThread(socket);
        }
    }

    class HandlerThread implements Runnable{
        Socket socket;

        public HandlerThread(Socket socket) {
            this.socket = socket;
            new Thread(this).start();
        }

        @Override
        public void run() {
            try {
                InputStream in = socket.getInputStream();
                RobinRequest request;
                while ((request = RobinRequestProto.RobinRequest.parseDelimitedFrom(in)) != null){
                    Builder resBuild = RobinResponse.newBuilder();
                    switch (request.getType()) {
                        case 1:  //读取
                            ObjectEntry value = storageService.get(request.getKey());
                            if (value != null) {
                                resBuild.setContent(ByteString.copyFrom(storageService.get(request.getKey()).getContent()));
                                resBuild.setType(1);
                            } else {
                                resBuild.setType(100);
                            }
                            break;
                        case 2:   //写入
                            storageService.store(request);
                            resBuild.setType(2);
                            resBuild.setContent(ByteString.copyFromUtf8("write ok"));
                            break;
                        default:
                            resBuild.setType(3);
                    }
                    RobinResponse response = resBuild.build();
                    response.writeDelimitedTo(socket.getOutputStream());
                }
            } catch (IOException e) {
                log.error("远程主机强制关闭了一个连接", e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
