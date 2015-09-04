import javafx.util.Pair;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Татьяна on 21.08.2015.
 */
public class TcpServerSocketProcessor implements Runnable{

    private final int id;
    private Socket clientSocket;
    private TcpServer tcpServer;
    private InputStream in;
    private OutputStream out;

    private ObjectInputStream reader;
    private ObjectOutputStream writer;

    public TcpServerSocketProcessor(Socket clientSocket, int id, TcpServer tcpServer) throws Throwable{
        this.id = id;
        this.clientSocket = clientSocket;
        this.tcpServer = tcpServer;

        this.in = clientSocket.getInputStream();
        this.out = clientSocket.getOutputStream();

        this.reader = new ObjectInputStream( in );
        this.writer = new ObjectOutputStream( out );
    }

    public void flush(){
        try{
            writer.flush();
        } catch (IOException e) {
            System.out.println("Headers sending error");
            System.exit(-1);
        }
    }

    public void run(){
        ArrayList<Message> messageList;

        try {
            boolean isAuthenticated = authenticate();
            sendAuthenticateMessage(isAuthenticated);

            if(isAuthenticated){
                // client has just connected to server, send him last 10 messages
                tcpServer.sendLastMessages(id);

                while ( (messageList = (ArrayList<Message>) reader.readObject()) != null) {
                    tcpServer.sendMessageToConnectedClients(id, messageList);

                    for(Message message : messageList){
                        tcpServer.storeMessage(message);

                        System.out.println( message.toOutStr() );
                        System.out.flush();
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println( "Serialization error." );
        } catch (IOException e){
            System.out.println( "Reading error." );
        }
        finally {
            try{
                System.out.println("Cancel client " + id);
                reader.close();
                writer.close();
                tcpServer.removeConnection(id);
            }catch(IOException e){
                e.printStackTrace();
            }

        }
    }

    public void sendMessage( ArrayList<Message> messageList){
        try{
            writer.writeObject(messageList);
            writer.flush();
        }catch(IOException e){
            System.out.println("Message writing error.");
        }

    }

    private boolean authenticate(){
        Pair<String, String> credentials;
        Boolean isAuthenticated = false;
        try{
            credentials = (Pair<String,String>) reader.readObject();
            isAuthenticated = tcpServer.isUserRegistered(credentials.getKey(),credentials.getValue());

        } catch (ClassNotFoundException e){
            System.out.println("Can not read");
        } catch (IOException e){
            System.out.println("IOException");
        }

        return isAuthenticated;

    }

    private void sendAuthenticateMessage(boolean isAuthenticated){
        UtilityMessage utilityMessage;

        if(isAuthenticated){
            utilityMessage = new UtilityMessage(UtilityMessage.StatusCodes.AUTHORIZED);
        } else {
            utilityMessage = new UtilityMessage(UtilityMessage.StatusCodes.NONAUTHORIZED);
        }

        try{
            writer.writeObject(utilityMessage);
            writer.flush();
        } catch (IOException e){
            System.out.println("System is not available");
        }

    }

}
