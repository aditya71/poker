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
        PokerClient one = new PokerClient("bob", "192.168.0.100");
        PokerClient two = new PokerClient("sally", "192.168.0.100");
        PokerClient three = new PokerClient("rodger", "192.168.0.100");
        PokerClient four = new PokerClient("california", "192.168.0.100");

        one.chips = one.receiveChips();
        two.chips = two.receiveChips();
        three.chips = three.receiveChips();
        four.chips = four.receiveChips();

        System.out.println(one.chips);
        System.out.println(two.chips);
        System.out.println(three.chips);
        System.out.println(four.chips);

        one.updateAllChips();
        two.updateAllChips();
        three.updateAllChips();
        four.updateAllChips();

        System.out.println(one.name); printArray(one.playerChips);
        System.out.println(two.name); printArray(two.playerChips);
        System.out.println(three.name); printArray(three.playerChips);
        System.out.println(four.name); printArray(four.playerChips);

    }

    public static void printArray(int[] a){
        for(int s: a){
            System.out.println(s);
        }
    }
}
