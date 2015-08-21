import java.io.*;
import java.net.Socket;

/**
 * Created by Татьяна on 21.08.2015.
 */
public class TcpServerSocketProcessor implements Runnable{

    private final Socket clientSocket;
    private InputStream in;
    private OutputStream out;
    private BufferedReader reader;

    public TcpServerSocketProcessor(Socket clientSocket) throws Throwable{
        this.clientSocket = clientSocket;

        this.in = clientSocket.getInputStream();
        this.out = clientSocket.getOutputStream();
        this.reader = new BufferedReader( new InputStreamReader( this.in ) );
    }

    public void run(){
        String ln = null;

        try {
            while ( ( ln = reader.readLine() ) != null ) {
                System.out.println( ln );
                System.out.flush();
            }
        } catch (IOException e) {
            System.out.println( "Ошибка при чтении сообщения." );
            System.exit( -1 );
        }
    }

}
