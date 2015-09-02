import java.io.*;
import java.util.ArrayList;

/**
 * Created by Татьяна on 01.09.2015.
 */
public class MessageInputHandler implements Runnable {
    private ObjectOutputStream writer;
    private BufferedReader reader;
    private String author;
    private String IP;

    public MessageInputHandler(InputStream in, OutputStream out, String author, String IP) throws IOException{
        this.reader = new BufferedReader( new InputStreamReader( in ) );
        this.writer =  new ObjectOutputStream( out );
        this.author = author;
        this.IP = IP;
    }

    public void run(){
        String ln;
        try {
            while ( ( ln = reader.readLine() ) != null ) {
                Message message = new Message(author, IP, ln);
                ArrayList<Message> messageList = new ArrayList<Message>();
                messageList.add(message);
                writer.writeObject(messageList);
                writer.flush();
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи сообщения.");
            System.exit(-1);
        }
    }


}

