import javafx.util.Pair;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created  on 05.09.2015.
 */
public class ChatServerProcessor {
    private HashMap<String,String> users;
    private final int messageStoreLimit;
    private final ArrayList<Message> messageList;
    private TcpServer tcpServer;

    /**********************************************************************************/

    public ChatServerProcessor( Config config, HashMap<String,String> users){
        this.messageStoreLimit = config.getMessageLimit();
        this.users = users;
        this.messageList = new ArrayList<Message>(messageStoreLimit);
        this.tcpServer = new TcpServer(config.getPort(), config.getConnectionLimit(), this);
    }

    /**********************************************************************************/
    /* Public methods */

    public boolean handleNewClient(TcpServerSocketProcessor tcpServerSocketProcessor) throws IOException, ClassNotFoundException{
        if (authenticate(tcpServerSocketProcessor)){
            sendLastMessages(tcpServerSocketProcessor);
            return true;
        }

        return false;
    }

    public void handleInputMessage(int clientId, TcpServerSocketProcessor tcpServerSocketProcessor) {
        // read an input message
        // store it
        // send it to all connections except the one
    }

    /**********************************************************************************/
    /* Private methods */
    private UserAuthenticationData parseCredentials(TcpServerSocketProcessor tcpServerSocketProcessor)
            throws IOException, ClassNotFoundException{

        UserAuthenticationData credentials = null;
        Object rawCredentials = tcpServerSocketProcessor.readObject();

        if (rawCredentials instanceof UserAuthenticationData){
            credentials = (UserAuthenticationData) rawCredentials;
        }

        return credentials;
    }

    private boolean authenticate(TcpServerSocketProcessor tcpServerSocketProcessor) throws IOException, ClassNotFoundException{
        UserAuthenticationData credentials = this.parseCredentials(tcpServerSocketProcessor);
        return ( credentials != null && this.isUserRegistered(credentials) );
    }

    private void sendMessageToConnectedClients(int clientId,  ArrayList<Message> messageList) throws IOException{
        //send message to all connected clients except client with id
        Iterable<TcpServerSocketProcessor> connections = this.tcpServer.getAllConnectionsExceptOne(clientId);
        for (TcpServerSocketProcessor connection : connections) {
            connection.sendMessage(messageList);
        }
    }

    private void sendLastMessages(TcpServerSocketProcessor tcpServerSocketProcessor) throws IOException{
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


    public boolean isUserRegistered(UserAuthenticationData credentials){
        boolean isRegistered = false;
        String name = credentials.getName();

        if (users.containsKey(name)){
            String userPass = users.get(name);
            String password = credentials.getPassword();

            if (userPass.equals(password)){
                isRegistered = true;
            }
        }

        return isRegistered;
    }

    public void start(){
       tcpServer.start();
    }

}


