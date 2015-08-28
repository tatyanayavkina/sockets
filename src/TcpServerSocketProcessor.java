import java.io.*;
import java.net.Socket;

/**
 * Created by Татьяна on 21.08.2015.
 */
public class TcpServerSocketProcessor implements Runnable{

    private final int id;
    private final Socket clientSocket;
    private TcpServer tcpServer;
    private InputStream in;
    private OutputStream out;
    private BufferedReader reader;
    private BufferedWriter writer;

    public TcpServerSocketProcessor(Socket clientSocket, int id, TcpServer tcpServer) throws Throwable{
        this.id = id;
        this.clientSocket = clientSocket;
        this.tcpServer = tcpServer;

        this.in = clientSocket.getInputStream();
        this.out = clientSocket.getOutputStream();
        this.reader = new BufferedReader( new InputStreamReader( this.in ) );
        this.writer = new BufferedWriter( new OutputStreamWriter( this.out ) );
    }

    public void run(){
        String ln = null;

        try {
            while ( ( ln = reader.readLine() ) != null ) {
                tcpServer.sendMessageToConnectedClients(id, ln);

                System.out.println( ln );
                System.out.flush();
            }
        } catch (IOException e) {
            System.out.println( "Reading error." );
            System.exit( -1 );
        }
        finally {
            try{
                System.out.println( "Cancel client" );
                in.close();
                out.close();
            }catch(IOException e){
                e.printStackTrace();
            }

        }
    }

    public void sendMessage(String message){
        try{
            writer.write(message + "\n");
            writer.flush();
        }catch(IOException e){
            System.out.println("Message writing error.");
        }

    }

}
