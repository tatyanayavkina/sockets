import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created  on 05.09.2015.
 */
public class ChatServerProcessor {
    private HashMap<String,String> users;
    private final int messageStoreLimit;
    private final ArrayList<Message> messageList;
    private TcpServer tcpServer;

    /**********************************************************************************/

    public ChatServerProcessor( Config config, HashMap<String,String> users ){
        this.messageStoreLimit = config.getMessageLimit();
        this.users = users;
        this.messageList = new ArrayList<Message>(messageStoreLimit);
        this.tcpServer = new TcpServer(config.getPort(), config.getConnectionLimit(), this);
    }

    /**********************************************************************************/
    /* Public methods */

    public boolean handleNewClient( TcpServerSocketProcessor tcpServerSocketProcessor ) throws IOException, ClassNotFoundException{
        if ( authenticate( tcpServerSocketProcessor ) ){
            sendLastMessages( tcpServerSocketProcessor );
            return true;
        }

        return false;
    }

    public void handleInputMessage(int clientId, TcpServerSocketProcessor tcpServerSocketProcessor) throws IOException, ClassNotFoundException{
        ArrayList<Message> messages;
        while( ( messages = (ArrayList<Message>) tcpServerSocketProcessor.readObject() )!= null ){
            for( Message message: messages ){
                System.out.println( message.toOutStr() );
                storeMessage( message );
            }
            sendMessageToConnectedClients( clientId, messages );
        }
    }

    /**********************************************************************************/
    /* Private methods */
    private UserAuthenticationData parseCredentials( TcpServerSocketProcessor tcpServerSocketProcessor )
            throws IOException, ClassNotFoundException{

        UserAuthenticationData credentials = null;
        Object rawCredentials = tcpServerSocketProcessor.readObject();

        if (rawCredentials instanceof UserAuthenticationData){
            credentials = (UserAuthenticationData) rawCredentials;
        }

        return credentials;
    }

    private boolean authenticate( TcpServerSocketProcessor tcpServerSocketProcessor ) throws IOException, ClassNotFoundException{
        UserAuthenticationData credentials = parseCredentials( tcpServerSocketProcessor );
        boolean isAuthenticated = ( credentials != null && isUserRegistered( credentials ) );
        sendAuthenticationMessage(tcpServerSocketProcessor, isAuthenticated);

        return isAuthenticated;
    }

    private void sendAuthenticationMessage(TcpServerSocketProcessor tcpServerSocketProcessor, boolean isAuthenticated) throws IOException{
        UtilityMessage utilityMessage;

        if ( isAuthenticated ){
            utilityMessage = new UtilityMessage( UtilityMessage.StatusCodes.AUTHORIZED );
        } else {
            utilityMessage = new UtilityMessage( UtilityMessage.StatusCodes.NONAUTHORIZED );
        }

        tcpServerSocketProcessor.writeObject( utilityMessage );
    }

    private void sendMessageToConnectedClients( int clientId,  ArrayList<Message> messageList ) throws IOException{
        //send message to all connected clients except client with id
        Iterable<TcpServerSocketProcessor> connections = this.tcpServer.getAllConnectionsExceptOne( clientId );
        for ( TcpServerSocketProcessor connection : connections ) {
            connection.sendMessage( messageList );
        }
    }

    private void sendLastMessages(TcpServerSocketProcessor tcpServerSocketProcessor) throws IOException{
        synchronized ( messageList ){
            if( !messageList.isEmpty() ){
                tcpServerSocketProcessor.sendMessage( messageList );
            }
        }

    }

    public void storeMessage( Message message ){
        synchronized ( messageList ){
            if ( messageList.size() == messageStoreLimit ){
                messageList.remove(0);
            }

            messageList.add( message );
        }
    }


    public boolean isUserRegistered( UserAuthenticationData credentials ){
        boolean isRegistered = false;
        String name = credentials.getName();

        if ( users.containsKey( name ) ){
            String userPass = users.get( name );
            String password = credentials.getPassword();

            if ( userPass.equals( password ) ){
                isRegistered = true;
            }
        }

        return isRegistered;
    }

    public void removeConnection( int connectionId ){
        tcpServer.removeConnection( connectionId );
    }

    public void start(){
       tcpServer.start();
    }

}


