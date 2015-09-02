import java.io.*;
import java.util.ArrayList;

/**
 * Created by ������� on 01.09.2015.
 */
public class MessageOutHandler implements Runnable {
    private ObjectOutputStream writer;
    private BufferedReader reader;
    private String author;
    private String IP;

    public MessageOutHandler(InputStream in, OutputStream out, String author, String IP) throws IOException{
        this.reader = new BufferedReader( new InputStreamReader( in ) );
        this.writer =  new ObjectOutputStream( out );
        this.author = author;
        this.IP = IP;
    }

    public void run(){
        String ln;
        try {
            System.out.println("in run ...");
            while ( ( ln = reader.readLine() ) != null ) {
                System.out.println("in while run ...");
                Message message = new Message(author, IP, ln);
                ArrayList<Message> messageList = new ArrayList<Message>();
                messageList.add(message);
                writer.writeObject(messageList);
                writer.flush();
            }

            reader.close();
            writer.close();
        } catch (IOException e) {
            System.out.println("������ ��� ������ ���������.");
            System.exit(-1);
        }
    }


}

