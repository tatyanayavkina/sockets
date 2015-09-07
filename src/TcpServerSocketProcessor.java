import javafx.util.Pair;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created on 21.08.2015.
 */
public class TcpServerSocketProcessor implements Runnable{

    private final int id;
    private Socket clientSocket;
    private TcpServer tcpServer;
    private InputStream in;
    private OutputStream out;

    private ObjectInputStream reader;
    private ObjectOutputStream writer;

    private ChatServerProcessor chatServerProcessor;

    public TcpServerSocketProcessor(int id, Socket clientSocket, ChatServerProcessor chatServerProcessor) throws Throwable{
        this.id = id;
        this.clientSocket = clientSocket;
        this.chatServerProcessor = chatServerProcessor;

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

        try {
            chatServerProcessor.handleNewClient(this);
            chatServerProcessor.handleInputMessage(this.id, this);
        } catch (IOException e) {
            // log
        } catch (ClassNotFoundException e) {
            // log
        }

        closeConnection();
    }

    public boolean sendMessage( ArrayList<Message> messageList) throws IOException{
        writer.writeObject(messageList);
        writer.flush();
        return true;
    }

    public Object readObject() throws ClassNotFoundException, IOException {
        return reader.readObject();
    }

    public boolean writeObject(Object obj) throws IOException{
        writer.writeObject(obj);
        writer.flush();
        return true;
    }

    private void closeConnection() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            // log
        }

        // removeConnection
    }
}
