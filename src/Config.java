/**
 * Created by Татьяна on 03.09.2015.
 */
public class Config {
    private final int port;
    private final int messageLimit;
    private final int connectionLimit;

    public Config(String port, String messageLimit, String connectionLimit){
        this.port = Integer.parseInt(port);
        this.messageLimit = Integer.parseInt(messageLimit);
        this.connectionLimit = Integer.parseInt(connectionLimit);
    }

    public int getPort(){
        return port;
    }

    public int getMessageLimit(){
        return messageLimit;
    }

    public int getConnectionLimit(){
        return connectionLimit;
    }

}
