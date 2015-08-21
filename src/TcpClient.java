import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    /************************************************************************/
    /* Constructors */
    public TcpClient(){
        this.host = DEFAULT_HOST;
        this.port = DEFAULT_PORT;
    }

    public TcpClient(String host, int port){
        this.host = host;
        this.port = port;
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

    private void createReader(){
        reader = new BufferedReader(new InputStreamReader( System.in));
    }

    private void createWriter (){
        OutputStream out = null;
        try {
            out = socket.getOutputStream();
            writer = new BufferedWriter(new OutputStreamWriter(out));
        } catch (IOException e) {
            System.out.println("Can not get output stream.");
            System.exit(-1);
        }
    }

    private void writeMessage(){
        /*
        * Все вводимые пользователем сообщения будем транслировать в поток вывода
        * созданного сокета
        */
        String ln;
        try {
            while ((ln = reader.readLine()) != null) {
                writer.write(ln + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи сообщения.");
            System.exit(-1);
        }
    }

    /**********************************************************************/
    /* Public methods */

    public void start(){
        createSocket();
        createReader();
        createWriter();
        writeMessage();
    }
}
