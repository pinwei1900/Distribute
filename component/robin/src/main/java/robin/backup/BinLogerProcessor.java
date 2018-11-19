package robin.backup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import robin.protobuf.RobinRequestProto.RobinRequest;

@Service
public class BinLogerProcessor {

    @Value("${robin.binlog.file}")
    String path;

    public void motifyLog(RobinRequest request) {
        try(FileOutputStream out = new FileOutputStream(new File(path))) {
            request.writeDelimitedTo(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
