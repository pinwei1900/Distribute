package robin.backup;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import robin.protobuf.SlaveProto;
import robin.protobuf.SlaveProto.SlaveRequest;
import robin.protobuf.SlaveProto.SlaveResponse;
import robin.protobuf.SlaveProto.SlaveResponse.Builder;
import robin.storage.service.StorageService;

/**
 * 作为slave请求的服务器，slave传递一个版本号，通过对比版本号，将传输从对应版本号开始的所有数据
 */
@Service
public class SlaveService {

    @Value("${robin.slave.port}")
    Integer port;

    @Autowired
    StorageService storageService;

    @PostConstruct
    public void run() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket;
                while (true) {
                    try {
                        socket = serverSocket.accept();
                        slaveHandler(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void slaveHandler(Socket socket) throws IOException {
        InputStream in = socket.getInputStream();
        SlaveRequest request = SlaveProto.SlaveRequest.parseDelimitedFrom(in);

        Builder resBuilder = SlaveResponse.newBuilder();
        if (storageService.version() <= request.getVersion()) {
            //master不可能小于请求端，所以salve应该重新建立
        }
        if (storageService.version() == request.getVersion()) {
            //返回类型为保持不变化
        }
        if (storageService.version() >= request.getVersion()) {
            //获取从低版本到高版本之间对内存中的数据
        }

    }
}
