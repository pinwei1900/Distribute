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
public class ServiceBootstrap {

    @Value("${robin.port}")
    Integer port;

    @Value("1024")
    int BUFFER_LENGTH;

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
                RobinRequest request = readChannel();
                Builder resBuild = RobinResponse.newBuilder();
                switch (request.getType()) {
                    case 1:  //读取
                        resBuild.setContent(ByteString.copyFrom(storageService.get(request.getKey()).getContent()));
                        resBuild.setType(1);
                        break;
                    case 2:   //写入
                        storageService.put(request.getKey(),new ObjectEntry(request.getContent().toByteArray()));
                        resBuild.setType(2);
                        resBuild.setContent(ByteString.copyFromUtf8("write ok"));
                        break;
                    default:
                        resBuild.setType(3);
                }
                RobinResponse response = resBuild.build();
                response.writeDelimitedTo(socket.getOutputStream());
                socket.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private RobinRequest readChannel() throws IOException {
            InputStream in = socket.getInputStream();
            return RobinRequestProto.RobinRequest.parseDelimitedFrom(in);
        }
    }
}
