import java.io.*;

/**
 * Created by Татьяна on 28.08.2015.
 */
public class MessageTransfer implements Runnable {
    private BufferedWriter writer;
    private BufferedReader reader;
    private String author;


    public MessageTransfer(InputStream in, OutputStream out){
        this.reader = new BufferedReader( new InputStreamReader( in ) );
        this.writer =  new BufferedWriter( new OutputStreamWriter( out ) );
        this.author = "";
    }

    public MessageTransfer(InputStream in, OutputStream out, String author){
        this.reader = new BufferedReader( new InputStreamReader( in ) );
        this.writer =  new BufferedWriter( new OutputStreamWriter( out ) );
        this.author = author;
    }

    public void run(){
        String ln;

        try {
            while ( ( ln = reader.readLine() ) != null ) {
                if( author.length() > 0 ){
                    writer.write( author + " : " + ln + "\n" );
                }else{
                    writer.write(ln + "\n");
                }

                writer.flush();
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи сообщения.");
            System.exit(-1);
        }
    }
}
