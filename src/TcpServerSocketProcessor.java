import java.io.*;
import java.net.Socket;

/**
 * Created by Татьяна on 21.08.2015.
 */
public class TcpServerSocketProcessor implements Runnable{

    private final int id;
    private final Socket clientSocket;
    private TcpServer tcpServer;
    private BufferedReader reader;
    private BufferedWriter writer;

    public TcpServerSocketProcessor(Socket clientSocket, int id, TcpServer tcpServer) throws Throwable{
        this.id = id;
        this.clientSocket = clientSocket;
        this.tcpServer = tcpServer;

        InputStream in = clientSocket.getInputStream();
        OutputStream out = clientSocket.getOutputStream();

        this.reader = new BufferedReader( new InputStreamReader( in ) );
        this.writer = new BufferedWriter( new OutputStreamWriter( out ) );
    }

    public void run(){
        String ln = null;

        // client has just connected to server, send him last 10 messages
        tcpServer.sendLastMessages(id);

        try {
            while ( ( ln = reader.readLine() ) != null ) {
                tcpServer.sendMessageToConnectedClients(id, ln);
                tcpServer.storeMessage(ln);

                System.out.println( ln );
                System.out.flush();
            }
        } catch (IOException e) {
            System.out.println( "Reading error." );
        }
        finally {
            try{
                System.out.println("Cancel client " + id);
                reader.close();
                writer.close();
                tcpServer.removeConnection(id);
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
