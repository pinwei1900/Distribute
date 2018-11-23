/*
 * Copyright (c) 2018年11月22日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package robin.backup.master;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import robin.bootstrap.BinLogger;
import robin.backup.enumtype.RequestType;
import robin.protobuf.RobinRequestProto.RobinRequest;
import robin.protobuf.SlaveProto;
import robin.protobuf.SlaveProto.SlaveRequest;
import robin.protobuf.SlaveProto.SlaveResponse;
import robin.protobuf.SlaveProto.SlaveResponse.Builder;
import robin.storage.service.StorageService;
import robin.utils.CurrentTimerMap;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/22
 * @Version 1.0.0
 */
@Component
public class HearBeat {

    @Autowired
    StorageService storageService;
    @Autowired
    CurrentTimerMap<String, String> slaveServers;
    @Autowired
    BinLogger binLogger;

    @Value("${robin.master.backup.port}")
    Integer port = 5011;

    Builder resBuilder;

    @PostConstruct
    public void beat() {
        resBuilder = SlaveResponse.newBuilder();

        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                while (true) {
                    Socket slaveSocket = serverSocket.accept();
                    slaveHandler(slaveSocket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void slaveHandler(Socket socket) throws IOException {
        try (InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream()
        ) {
            SlaveRequest request = SlaveProto.SlaveRequest.parseDelimitedFrom(in);
            RequestType type = RequestType.getByType(request.getType());
            switch (type) {
                case HEART_BEAT:
                    /**
                     * 心跳检测，只需要更新服务器列表，并且发送返回信息即可，要不要把间隔的数据传递回去？？？
                     */
                    slaveServers.put(request.getName(), request.getVersion() + "");
                    resBuilder.setVersion(storageService.version());
                    resBuilder.setOp(RequestType.SYSC.type);
                    resBuilder.build().writeDelimitedTo(out);
                    break;
                case SYSC:
                    /**
                     * 同步申请，只需要返回目前系统的版本号，以及用户请求区域的版本号即可
                     */
                    long slave_version = request.getVersion();
                    long curr_version = storageService.version();

                    resBuilder.setVersion(curr_version);
                    resBuilder.setOp(RequestType.SYSC.type);

                    if (slave_version < curr_version) {
                        List<RobinRequest> rangeReq = binLogger.getRange(slave_version, curr_version);
                        resBuilder.addAllData(rangeReq);
                    }

                    resBuilder.build().writeDelimitedTo(out);
                    break;
            }
        }


    }
}
