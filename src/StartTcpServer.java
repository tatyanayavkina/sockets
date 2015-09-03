/**
 * Created by Татьяна on 21.08.2015.
 */
public class StartTcpServer {

    public static void main(String[] args) {
        Config config = XMLReader.readParams();

        TcpServer tcpServer = new TcpServer(config);
        tcpServer.start();
    }
}
