import java.util.HashMap;

public class StartTcpServer {

    public static void main(String[] args) {
        Config config = XMLReader.readParams();
        HashMap<String, String> users = XMLReader.readUsers();

        ChatServerProcessor chatServerProcessor = new ChatServerProcessor(config, users);
        chatServerProcessor.start();
    }
}
