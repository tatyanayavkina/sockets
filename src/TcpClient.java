import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Татьяна on 21.08.2015.
 */
public class TcpClient {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 9999;

    private String host;
    private int port;

    private String username;
    private Socket socket;
    private MessageTransfer reader;
    private MessageTransfer writer;

    /************************************************************************/
    /* Constructors */
    public TcpClient(){
        this.host = DEFAULT_HOST;
        this.port = DEFAULT_PORT;
        this.username = "Hohohoho";
    }

    public TcpClient(String host, int port, String username){
        this.host = host;
        this.port = port;
        this.username = username;
    }

    /***********************************************************************/
    /* Private methods */

    private void createSocket(){
        try {
            socket = new Socket(host, port);
        } catch (UnknownHostException e) {
            System.out.println("Unknonw host: " + host);
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("I/O error while socket creation " + host + ":" + port);
            System.exit(-1);
        }
    }

   private void createReaderWriter (){
       InputStream in = null;
       OutputStream out = null;
       try{
           in = socket.getInputStream();
           out = socket.getOutputStream();

           reader = new MessageTransfer(in, System.out);
           writer = new MessageTransfer(System.in, out, username);
       } catch(IOException ex){
           ex.printStackTrace();
       }
   }

    /**********************************************************************/
    /* Public methods */

    public void start(){
        createSocket();
        createReaderWriter();

        new Thread(reader).start();
        new Thread(writer).start();
    }
}