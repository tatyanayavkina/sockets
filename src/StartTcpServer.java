/**
 * Created by Татьяна on 21.08.2015.
 */
public class StartTcpServer {

    public static void main(String[] args) {
        TcpServer tcpServer = new TcpServer(9999);
        tcpServer.start();
    }
}
