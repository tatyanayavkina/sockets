import java.io.*;
import java.util.ArrayList;

/**
 * Created by Татьяна on 01.09.2015.
 */
public class MessageInHandler implements Runnable{
    private BufferedWriter writer;
    private ObjectInputStream reader;

    public MessageInHandler(InputStream in, OutputStream out) throws IOException{
        this.reader = new ObjectInputStream( in );
        this.writer =  new BufferedWriter( new OutputStreamWriter( out ) );
    }

    public void run(){
        ArrayList<Message> messageList;
        String outStr;
        try {
            while ( ( messageList = (ArrayList<Message>) reader.readObject() ) != null ) {
                for(Message message: messageList){
                    outStr = message.toOutStr();
                    writer.write(outStr);
                    writer.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи сообщения.");
            System.exit(-1);
        }
        catch (ClassNotFoundException e) {
            System.out.println("Ошибка при чтении сообщения.");
            System.exit(-1);
        }
    }
}
