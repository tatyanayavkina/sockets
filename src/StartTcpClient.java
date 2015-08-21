/**
 * Created by Татьяна on 21.08.2015.
 */
public class StartTcpClient {

    public static void main(String[] args) {
        TcpClient tcpClient = new TcpClient("localhost", 9999);
        tcpClient.start();
    }
}
