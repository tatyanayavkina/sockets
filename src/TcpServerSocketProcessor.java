import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Татьяна on 21.08.2015.
 */
public class TcpServerSocketProcessor implements Runnable{

    private final int id;
    private boolean connected;
    private Socket clientSocket;
    private TcpServer tcpServer;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;

    public TcpServerSocketProcessor(Socket clientSocket, int id, TcpServer tcpServer) throws Throwable{
        this.id = id;
        this.connected = true;
        this.clientSocket = clientSocket;
        this.tcpServer = tcpServer;

        InputStream in = clientSocket.getInputStream();
        OutputStream out = clientSocket.getOutputStream();

        this.reader = new ObjectInputStream( in );
        this.writer = new ObjectOutputStream( out );
    }

    public void run(){
        ArrayList<Message> messageList;

        // client has just connected to server, send him last 10 messages
        tcpServer.sendLastMessages(id);

        try {
            while (connected) {
                messageList = (ArrayList<Message>) reader.readObject();
                tcpServer.sendMessageToConnectedClients(id, messageList);

                for(Message message : messageList){
                    tcpServer.storeMessage(message);

                    System.out.println( message.toOutStr() );
                    System.out.flush();
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

}
