import java.io.Serializable;
import java.time.LocalDateTime;


public class Message implements Serializable{
    private String author;
    private String IP;
    private LocalDateTime creationTime;
    private String content;

    public Message(String author, String IP, String content){
        this.author = author;
        this.IP = IP;
        this.content = content;
        this.creationTime = LocalDateTime.now();
    }

    public String toOutStr(){
        StringBuilder builder = new StringBuilder();
        String output;

        // add author
        builder.append("author: ");
        builder.append(author);
        builder.append("\n");
        //add IP
        builder.append("IP: ");
        builder.append(IP);
        builder.append("\n");
        //add time
        builder.append("creation time: ");
        builder.append(creationTime);
        builder.append("\n");
        //add content
        builder.append(content);
        builder.append("\n");

        output = builder.toString();

        return output;
    }

}
