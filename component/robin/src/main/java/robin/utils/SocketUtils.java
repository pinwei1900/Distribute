package robin.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketUtils {
    public static String readLine(Socket socket) throws IOException {
        BufferedReader bin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return bin.readLine();
    }
}
