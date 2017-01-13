import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Created by adity on 1/10/2017.
 */
public class PokerClient {

    Scanner scanner = new Scanner(System.in);

    String name;
    String hostname;

    Socket clientSocket;
    InputStream in;
    OutputStream out;
    int numPlayers;
    String[] playerNames;

    public PokerClient(String name, String hostname) throws IOException {
        this.name = name;
        this.hostname = hostname;
        connect();
        sendName();

    }

    public PokerClient() throws IOException {
        this.name = requestName();
        this.hostname = requestHostname();
        connect();
        sendName();

    }


    private String requestName() {
        System.out.println("Please enter your name:");
        String input = scanner.nextLine();
        return input;
    }

    private String requestHostname() {
        System.out.println("Please enter the hostname of the server:");
        String input = scanner.nextLine();
        return input;
    }

    private void connect() throws IOException {
        clientSocket = new Socket(hostname, 24999);
        in = clientSocket.getInputStream();
        out = clientSocket.getOutputStream();
    }

    private void sendName() throws IOException {
        byte[] nameBytes = name.getBytes(Charset.forName("US-ASCII"));
        out.write(nameBytes.length);
        out.flush();
        out.write(nameBytes);
        out.flush();
    }

    public String[] readNames() throws IOException {
        numPlayers = in.read();
        playerNames = new String[numPlayers];

        for (int i = 0; i < numPlayers; i++) {
            int length = in.read();
            byte[] nameBytes = new byte[length];
            while (length > 0) {
                length -= in.read(nameBytes, nameBytes.length - length, length);
            }
            playerNames[i] = new String(nameBytes);

            System.out.println(playerNames[i]);

        }
        return playerNames;
    }

}