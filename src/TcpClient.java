import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created on 21.08.2015.
 */
public class TcpClient {
    private String host;
    private int port;

    private String username;
    private String password;
    private String IP;
    private Socket socket;
    private MessageInHandler reader;
    private MessageOutHandler writer;

    /************************************************************************/

    public TcpClient(String host, int port, String username, String password) throws IOException{
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.IP = InetAddress.getLocalHost().toString();
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
           out = socket.getOutputStream();
           writer = new MessageOutHandler(System.in, out, username, IP);
           writer.flush();

           in = socket.getInputStream();
           reader = new MessageInHandler(in, System.out);
       } catch( SocketException ex){
           System.out.println("Sorry, server is busy.");
           System.exit(-1);
       } catch( IOException ex ){
           ex.printStackTrace();
       }
   }

   private void authorize(){
       UserAuthenticationData credentials = new UserAuthenticationData(username, password);
       writer.sendCredentials(credentials);
       UtilityMessage.StatusCodes code = reader.getServerResponse();

       if (code.equals(UtilityMessage.StatusCodes.NONAUTHORIZED)){
           try{
               socket.close();
           } catch(IOException e){
           }
           System.exit(-1);
       }
   }

    /**********************************************************************/
    /* Public methods */

    public void start(){
        createSocket();
        createReaderWriter();
        authorize();

        new Thread(reader).start();
        new Thread(writer).start();
    }
}