import java.io.IOException;
import java.net.InetAddress;

/**
 * Created on 07.09.2015.
 */
public class ChatClientProcessor {
    private String username;
    private String password;
    private String IP;

    private TcpClient tcpClient;

    public ChatClientProcessor(String host, int port, String username, String password) throws IOException {
        this.username = username;
        this.password = password;
        this.IP = InetAddress.getLocalHost().toString();
        this.tcpClient = new TcpClient(host, port, this);
    }

    public void start(){
        tcpClient.start();
    }

    public void handleConnection( MessageOutHandler writer, MessageInHandler reader )throws IOException,ClassNotFoundException{
        if ( authorize( writer, reader ) ){
            handleUserInput(writer);
            handleServerInput(reader);
        }
    }

    private boolean authorize( MessageOutHandler writer, MessageInHandler reader ) throws IOException,ClassNotFoundException{
        sendAuthorizationMessage( writer );
        return readAuthorizationMessage(reader);
    }

    private void sendAuthorizationMessage( MessageOutHandler writer ) throws IOException{
        UserAuthenticationData credentials = new UserAuthenticationData(username, password);
        writer.writeObject(credentials);
    }

    private boolean readAuthorizationMessage( MessageInHandler reader ) throws IOException, ClassNotFoundException{
        boolean isAuthorized = false;
        UtilityMessage utilityMessage = parseServerAuthorizationMessage( reader );
        UtilityMessage.StatusCodes statusCode = utilityMessage.getCode();
        reader.write( statusCode.getDescription() );

        if( statusCode == UtilityMessage.StatusCodes.AUTHORIZED){
            isAuthorized = true;
        }

        return isAuthorized;
    }

    private UtilityMessage parseServerAuthorizationMessage(MessageInHandler reader) throws IOException, ClassNotFoundException{
        UtilityMessage utilityMessage = null;
        Object rowMessage = reader.readObject();

        if( rowMessage instanceof UtilityMessage ){
            utilityMessage = (UtilityMessage) rowMessage;
        }

        return utilityMessage;
    }

    private void handleUserInput(MessageOutHandler writer){
        writer.setAuthor(username);
        writer.setIP(IP);

        new Thread(writer).start();
    }

    private void handleServerInput(MessageInHandler reader){
        new Thread(reader).start();
    }
}

