import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Татьяна on 21.08.2015.
 */
public class TcpServer {
    private static final int DEFAULT_PORT = 9999;

    private final int port;
    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;
    private InputStream in = null;
    BufferedReader reader;
    /**********************************************************************************/
    /* Constructors */

    /* default constructor*/
    public TcpServer(){
        this.port = this.DEFAULT_PORT;
    }

    /* construct TcpServer with port number */
    public TcpServer( int port ){
        this.port = port;
    }

    /**********************************************************************************/
    /* Private methods */

    private void createServerSocket(){
        try {
            serverSocket = new ServerSocket( port );
        } catch (IOException e) {
            System.out.println( "Порт занят: " + port ); System.exit( -1 );
        }
    }

    private void createClientSocket(){
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.out.println( "Ошибка при подключении к порту: " + port );
            System.exit(-1);
        }
    }

    private void getInputStream (){
        try {
            in = clientSocket.getInputStream();
        } catch (IOException e) {
            System.out.println( "Не удалось получить поток ввода." );
            System.exit(-1);
        }
    }

    private void readInputStream (){
        reader = new BufferedReader( new InputStreamReader( in ) );
        String ln = null;

        try {
            while ( ( ln = reader.readLine() ) != null ) {
                System.out.println( ln );
                System.out.flush();
            }
        } catch (IOException e) {
            System.out.println( "Ошибка при чтении сообщения." );
            System.exit( -1 );
        }
    }
    /**********************************************************************************/
    /* Public methods */

    public void start(){
        createServerSocket();
        createClientSocket();
    }

    public void accept(){
        getInputStream();
        readInputStream();
    }
}
