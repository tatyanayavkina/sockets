import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Татьяна on 21.08.2015.
 */
public class StartTcpClient {

    public static void main(String[] args) {
        System.out.println("Enter your name, please");

        Scanner scanner = new Scanner(System.in);
        String username = scanner.next();
        /* Read user input */
        try{
            TcpClient tcpClient = new TcpClient("localhost", 9999, username);
            tcpClient.start();
        }catch(IOException e){
            System.out.println("Sorry, you can not chatting");
            System.exit(-1);
        }


    }
}
