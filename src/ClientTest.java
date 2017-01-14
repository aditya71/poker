import com.sun.org.apache.xpath.internal.SourceTree;

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

            System.out.println("what is the host's address");
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
        Thread t1 = new Thread(new ClientRunnable("bob", "localhost"), "T1");
        Thread t2 = new Thread(new ClientRunnable("sally", "localhost"), "T2");
        Thread t3 = new Thread(new ClientRunnable("rodger", "localhost"), "T3");
        Thread t4 = new Thread(new ClientRunnable("cali", "localhost"), "T4");

        t1.start();
        t2.start();
        t3.start();
        t4.start();

    }

    static class ClientRunnable implements Runnable {
        private String name, host;

        public ClientRunnable(String name, String host) {
            this.name = name;
            this.host = host;
        }

        @Override
        public void run() {
            try {
                PokerClient client = new PokerClient(name, host);
                System.out.println(client.name + ": " + client.chips);
                System.out.println(name); printArray(client.playerChips);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void printArray(int[] a){
            for(int i : a){
                System.out.println(i);
            }
        }
        public void printStatus(){

        }
    }
}
