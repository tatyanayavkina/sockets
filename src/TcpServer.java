import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Татьяна on 21.08.2015.
 */
public class TcpServer {
    private static final int DEFAULT_PORT = 9999;

    private final int port;
    private ServerSocket serverSocket = null;
    /**********************************************************************************/
    /* Constructors */

    /* default constructor*/
    public TcpServer(){
        this.port = DEFAULT_PORT;
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
            System.out.println( "Port has been already used: " + port ); System.exit( -1 );
        }
    }

    private void createClientSocket(){
        while (true) {
            try {
               Socket clientSocket = serverSocket.accept();
               System.out.println( "Client connected.");
               new Thread( new TcpServerSocketProcessor( clientSocket ) ).start();
            } catch (IOException e) {
                System.out.println( "Error with port connection: " + port );
                System.exit(-1);
            }catch (Throwable e){
                System.out.println( "Error with port connection: " + port + ". Input/Output streams are unavailable." );
                System.exit(-1);
            }

        }

    }


    /**********************************************************************************/
    /* Public methods */

    public void start(){
        createServerSocket();
        createClientSocket();
    }

}
