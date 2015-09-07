import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created on 21.08.2015.
 */
public class TcpServer {
    private final int port;
    private ServerSocket serverSocket = null;
    private final ConnectionAutoIncrementMap connectionsMap;
    private ChatServerProcessor chatServerProcessor;
    private final int connectionLimit;

    private ThreadPoolExecutor threadPoolExecutor;
    /**********************************************************************************/

    public TcpServer(int port, int connectionLimit, ChatServerProcessor chatServerProcessor){
        this.port = port;
        this.connectionLimit = connectionLimit;
        this.connectionsMap = new ConnectionAutoIncrementMap();
        this.chatServerProcessor = chatServerProcessor;
        this.threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(connectionLimit);
    }

    /**********************************************************************************/
    /* Private methods */

    private void createServerSocket(){
        try {
            serverSocket = new ServerSocket( port );
        } catch (IOException e) {
            System.out.println( "Port has been already used: " + port );
            System.exit( -1 );
        }
    }

    private void createClientSocket(){
        int connectionId;

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                // do not connect new clients when all threads are busy
                if ( threadPoolExecutor.getActiveCount() == connectionLimit){
                    //TODO: В идеальном мире нужно как-то сообщать клиенту, что сервер занят.
                    clientSocket.close();
                    continue;
                }
                connectionId = connectionsMap.getNextId();
                System.out.println("Client connected " + connectionId);
                TcpServerSocketProcessor connection = new TcpServerSocketProcessor( connectionId, clientSocket, chatServerProcessor );
                connection.flush();

                synchronized (connectionsMap){
                    connectionsMap.pushConnection(connectionId, connection);
                }
                threadPoolExecutor.execute( connection );

            } catch (IOException e) {

                System.out.println( "Error with port connection: " + port );
                System.exit(-1);

            } catch (Throwable e){

                System.out.println( "Error with port connection: " + port + ". Input/Output streams are unavailable." );
                System.exit(-1);

            }

        }

    }


    /**********************************************************************************/
    /* Public methods */
    public Iterable<TcpServerSocketProcessor> getAllConnectionsExceptOne(int exceptConnectionId){
        return connectionsMap.getAllExceptOne(exceptConnectionId);
    }

    public void removeConnection(int connectionId){
        connectionsMap.remove( connectionId );
    }

    public void start(){
        createServerSocket();
        createClientSocket();
    }

}
