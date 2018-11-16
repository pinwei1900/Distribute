import com.google.protobuf.ByteString;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import robin.protobuf.RobinRequestProto.RobinRequest;
import robin.protobuf.RobinRequestProto.RobinRequest.Builder;
import robin.protobuf.RobinRequestProto.RobinResponse;

public class Test {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1" , 5010);
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();

        Builder builder = RobinRequest.newBuilder();

        //写入
        builder.setType(2);
        builder.setKey("hhhh2");
        builder.setContent(ByteString.copyFromUtf8("hello world"));
        RobinRequest request = builder.build();
        request.writeDelimitedTo(out);

        RobinResponse response = RobinResponse.parseDelimitedFrom(in);
        response.getContent().writeTo(System.out);
        socket.close();


        socket = new Socket("127.0.0.1" , 5010);
        out = socket.getOutputStream();
        in = socket.getInputStream();
        //读取
        builder.setType(1);
        builder.setKey("hhhh2");
        request = builder.build();
        request.writeDelimitedTo(out);

        response = RobinResponse.parseDelimitedFrom(in);
        response.getContent().writeTo(System.out);
        socket.close();
    }
}
