import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;

/**
 * Created on 01.09.2015.
 */
public class MessageOutHandler implements Runnable {
    private ObjectOutputStream writer;
    private BufferedReader reader;
    private TcpClient tcpClient;

    private String author;
    private String IP;
    private boolean connected;
    private final String CLOSE = "@close";

    public MessageOutHandler(InputStream in, OutputStream out, TcpClient tcpClient) throws IOException{
        this.reader = new BufferedReader( new InputStreamReader( in ) );
        this.writer =  new ObjectOutputStream( out );
        this.tcpClient = tcpClient;
        this.connected = true;
    }

    public void flush() throws IOException{
        writer.flush();
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public void setIP(String IP){
        this.IP = IP;
    }

    public void run(){
        String ln;
        try {
            while ( connected ) {
                ln = reader.readLine();

                if ( ln.equals(CLOSE) ){
                    connected = false;
                    continue;
                }

                Message message = new Message(author, IP, ln);
                ArrayList<Message> messageList = new ArrayList<Message>();
                messageList.add(message);
                writer.writeObject(messageList);
                writer.flush();
            }

            reader.close();
            writer.close();
        } catch (IOException e) {
            System.out.println("Ошибка при записи сообщения.");
        } finally {
            tcpClient.close();
        }
    }

    public void writeObject(Object obj) throws IOException{
        writer.writeObject(obj);
        writer.flush();
    }

}

