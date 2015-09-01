import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Татьяна on 21.08.2015.
 */
public class StartTcpClient {

    public static void main(String[] args) {
        String username = "Hohohoho";
        System.out.println("Enter your name, please");

        Scanner scanner = new Scanner(System.in);
        /* Read user input */
        try{
            username = scanner.next();
        }catch(InputMismatchException e){
            System.out.println("Sorry, your input is incorrect. You must enter 1 or 2. Try again later.");
            System.exit(-1);
        }

        TcpClient tcpClient = new TcpClient("localhost", 9999, username);
        tcpClient.start();
    }
}
