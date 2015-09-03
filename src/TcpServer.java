import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Татьяна on 21.08.2015.
 */
public class TcpServer {
    private final int port;
    private ServerSocket serverSocket = null;
    private Map<Integer, TcpServerSocketProcessor> connectionsMap;
    private final ArrayList<Message> messageList;
    private final int messageStoreLimit;
    private  final int connectionLimit;
    private  int connectionCounter;
    private ThreadPoolExecutor threadPoolExecutor;


    /**********************************************************************************/

    public TcpServer( Config config ){
        this.port = config.getPort();
        this.messageStoreLimit = config.getMessageLimit();
        this.connectionLimit = config.getConnectionLimit();
        this.connectionsMap = new HashMap<Integer, TcpServerSocketProcessor>();
        this.connectionCounter = 0;
        this.messageList = new ArrayList<Message>(messageStoreLimit);
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
        while (true) {
            try {
               Socket clientSocket = serverSocket.accept();
                // do not connect new clients when all threads are busy
                if ( threadPoolExecutor.getActiveCount() == connectionLimit){
                    clientSocket.close();
                    continue;
                }

               int connectionId = connectionCounter++;
               System.out.println("Client connected " + connectionId);
               TcpServerSocketProcessor connection = new TcpServerSocketProcessor( clientSocket, connectionId, this );
               connection.flush();
               connectionsMap.put(connectionId, connection);
               threadPoolExecutor.execute( connection );

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

    public void sendMessageToConnectedClients(int clientId,  ArrayList<Message> messageList){
        //send message to all connected clients except client with id
        Set<Map.Entry<Integer, TcpServerSocketProcessor>> set = connectionsMap.entrySet();

        for ( Map.Entry<Integer, TcpServerSocketProcessor> s : set ) {
            Integer key = s.getKey();

            if(key != clientId){
                TcpServerSocketProcessor socketProcessor = s.getValue();
                socketProcessor.sendMessage( messageList );
            }
        }

    }

    public void sendLastMessages(int id){
        TcpServerSocketProcessor tcpServerSocketProcessor = connectionsMap.get(id);

        synchronized ( messageList ){
            if( !messageList.isEmpty() ){
                tcpServerSocketProcessor.sendMessage(messageList);
            }
        }
    }

    public void storeMessage(Message message){
        synchronized ( messageList ){
            if ( messageList.size() == messageStoreLimit){
                messageList.remove(0);
            }

            messageList.add( message );
        }
    }

    public void removeConnection(int connectionId){
        connectionsMap.remove( connectionId );
    }

    public void start(){
        createServerSocket();
        createClientSocket();
    }

}
