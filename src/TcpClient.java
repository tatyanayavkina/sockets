import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created on 21.08.2015.
 */
public class TcpClient {
    private String host;
    private int port;

    private ChatClientProcessor chatClientProcessor;
    private Socket socket;
    private MessageInHandler reader;
    private MessageOutHandler writer;

    /************************************************************************/

    public TcpClient(String host, int port, ChatClientProcessor chatClientProcessor) {
        this.host = host;
        this.port = port;
        this.chatClientProcessor = chatClientProcessor;
    }

    /***********************************************************************/
    /* Private methods */

    private void createSocket() throws IOException{
        socket = new Socket(host, port);
    }

   private void createReaderWriter() throws IOException{
       OutputStream out = socket.getOutputStream();
       writer = new MessageOutHandler(System.in, out, this);
       writer.flush();

       InputStream in = socket.getInputStream();
       reader = new MessageInHandler(in, System.out, this);
   }



    /**********************************************************************/
    /* Public methods */

    public void start(){
        try {
            createSocket();
            createReaderWriter();
            chatClientProcessor.handleConnection(writer, reader);

        } catch (UnknownHostException e) {
            System.out.println("UnknownHostException");
            close();
        } catch (ClassNotFoundException e){
            System.out.println("ClassNotFoundException");
            close();
        } catch (IOException e){
            System.out.println("IOException");
            close();
        }

    }

    public void close(){
        try{
            socket.close();
        } catch (IOException e){
            System.out.println("Socket closing error");
            System.exit(-1);
        }
    }

}