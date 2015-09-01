import java.io.*;

/**
 * Created by Татьяна on 01.09.2015.
 */
public class MessageOutputHandler implements Runnable{
    private BufferedWriter writer;
    private ObjectInputStream reader;

    public MessageOutputHandler(InputStream in, OutputStream out) throws IOException{
        this.reader = new ObjectInputStream( in );
        this.writer =  new BufferedWriter( new OutputStreamWriter( out ) );
    }

    public void run(){
        Message message;
        String outStr;
        try {
            while ( ( message = (Message) reader.readObject() ) != null ) {
                outStr = message.toOutStr();
                writer.write(outStr);
                writer.flush();
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
