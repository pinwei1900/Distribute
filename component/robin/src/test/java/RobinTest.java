import com.google.protobuf.ByteString;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import robin.protobuf.RobinRequestProto.RobinRequest;
import robin.protobuf.RobinRequestProto.RobinRequest.Builder;
import robin.protobuf.RobinRequestProto.RobinResponse;

public class RobinTest {

    private static Socket socket;
    private static OutputStream out;
    private static InputStream in;
    private static Builder builder;


    @Before
    public void begin() throws IOException {
        socket = new Socket("127.0.0.1", 5010);
        out = socket.getOutputStream();
        in = socket.getInputStream();
        builder = RobinRequest.newBuilder();
    }

    @After
    public void end() throws IOException {
        socket.close();
    }

    @Test
    public void storeTest() throws IOException {
        for (int i = 0; i < 10; i++) {
            builder.setType(2);
            builder.setKey("key" + i);
            builder.setContent(ByteString.copyFromUtf8("value" + i));

            RobinRequest request = builder.build();
            request.writeDelimitedTo(out);
            RobinResponse response = RobinResponse.parseDelimitedFrom(in);
            response.getContent().writeTo(System.out);

            System.out.println(socket.getRemoteSocketAddress());
            System.out.println(socket.getLocalPort());
        }
    }

    @Test
    public void getTest() throws IOException {
        for (int i = 0; i < 10; i++) {
            builder.setType(1);
            builder.setKey("key" + i);

            RobinRequest request = builder.build();
            request.writeDelimitedTo(out);
            RobinResponse response = RobinResponse.parseDelimitedFrom(in);
            if (response != null) {
                response.getContent().writeTo(System.out);
            }
        }
    }
}
