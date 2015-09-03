import java.util.HashMap;

/**
 * Created by Татьяна on 21.08.2015.
 */
public class StartTcpServer {

    public static void main(String[] args) {
        Config config = XMLReader.readParams();
        HashMap<String, String> users = XMLReader.readUsers();

        TcpServer tcpServer = new TcpServer(config, users);
        tcpServer.start();
    }
}
