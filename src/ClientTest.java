import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Created by adity on 1/10/2017.
 */
public class ClientTest {
    /**
    public static void main(String [] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        while (true)
        {
            System.out.println("Please enter your name: ");
            String name = scanner.nextLine();

            System.out.println("what is the host's adress");
            String hostname = scanner.nextLine();
            if (hostname.equals("exit")) {
                break;
            }
            Socket socket = new Socket(hostname, 24999);
            OutputStream o = socket.getOutputStream();
            InputStream i = socket.getInputStream();

            byte[] nameBytes = name.getBytes(Charset.forName("US-ASCII"));
            o.write(nameBytes.length);
            o.flush();
            o.write(nameBytes);
            o.flush();

            // waiting for number of players from the server
            byte[] b = new byte[10];
            i.read(b);
            String returns = new String(b);
            System.out.println(returns);

        }
    }
     */
    public static void main(String[] args) throws IOException{
        PokerClient one = new PokerClient("bob", "localhost");
        PokerClient two = new PokerClient("sally", "localhost");
        PokerClient three = new PokerClient("rodger", "localhost");
        PokerClient four = new PokerClient("california", "localhost");

        printArray(one.readNames());
        printArray(two.readNames());
        printArray(three.readNames());
        printArray(four.readNames());
    }

    public static void printArray(String[] a){
        for(String s: a){
           // System.out.println(s);
        }
    }
}
