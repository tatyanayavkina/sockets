import java.io.*;
import java.util.ArrayList;


public class MessageInHandler implements Runnable{
    private BufferedWriter writer;
    private ObjectInputStream reader;
    private TcpClient tcpClient;

    public MessageInHandler(InputStream in, OutputStream out, TcpClient tcpClient) throws IOException{
        this.reader = new ObjectInputStream( in );
        this.writer =  new BufferedWriter( new OutputStreamWriter( out ) );
        this.tcpClient = tcpClient;
    }

    public void run() {
        ArrayList<Message> messageList;
        String outStr;
        try {
            while ( (messageList = (ArrayList<Message>) reader.readObject()) != null ) {

                for(Message message: messageList){
                    outStr = message.toOutStr();
                    writer.write(outStr);
                    writer.flush();
                }
            }

            reader.close();
            writer.close();

        } catch (IOException e) {
            System.out.println("Îøèáêà ïðè çàïèñè ñîîáùåíèÿ.");
        } catch (ClassNotFoundException e) {
            System.out.println("Îøèáêà ïðè ÷òåíèè ñîîáùåíèÿ.");
        } finally {
            tcpClient.close();
        }
    }

    public Object readObject() throws IOException, ClassNotFoundException{
        return reader.readObject();
    }

    public void write(String text) throws IOException{
        writer.write(text);
        writer.flush();
    }

}
