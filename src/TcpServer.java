import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by Татьяна on 21.08.2015.
 */
public class TcpServer {
    private static final int DEFAULT_PORT = 9999;
    private static final int MESSAGES_STORE_LIMITATION = 10;
    private  int connectionCounter;

    private final int port;
    private ServerSocket serverSocket = null;
    private Map<Integer, TcpServerSocketProcessor> connectionsMap;
    private final ArrayList<String> messageList;
    /**********************************************************************************/
    /* Constructors */

    /* default constructor*/
    public TcpServer(){
        this.port = DEFAULT_PORT;
        this.connectionsMap = new HashMap<Integer, TcpServerSocketProcessor>();
        this.connectionCounter = 0;
        this.messageList = new ArrayList(MESSAGES_STORE_LIMITATION);
    }

    /* construct TcpServer with port number */
    public TcpServer( int port ){
        this.port = port;
        this.connectionsMap = new HashMap<Integer, TcpServerSocketProcessor>();
        this.connectionCounter = 0;
        this.messageList = new ArrayList(MESSAGES_STORE_LIMITATION);
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
               int connectionId = connectionCounter++;
               System.out.println("Client connected " + connectionId);

               TcpServerSocketProcessor connection = new TcpServerSocketProcessor( clientSocket, connectionId, this );
               connectionsMap.put(connectionId, connection);

               new Thread( connection ).start();
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

    public void sendMessageToConnectedClients(int clientId, String message){
        //send message to all connected clients except client with id
        Set<Map.Entry<Integer, TcpServerSocketProcessor>> set = connectionsMap.entrySet();

        for (Map.Entry<Integer, TcpServerSocketProcessor> s : set) {
            Integer key = s.getKey();

            if(key != clientId){
                TcpServerSocketProcessor socketProcessor = s.getValue();
                socketProcessor.sendMessage(message);
            }
        }

    }

    public void storeMessage(String message){
        synchronized (messageList){
            if (messageList.size() == MESSAGES_STORE_LIMITATION){
                messageList.remove(0);
            }

            messageList.add(message);
        }
    }

    public void removeConnection(int connectionId){
        connectionsMap.remove(connectionId);
    }

    public void start(){
        createServerSocket();
        createClientSocket();
    }

}
