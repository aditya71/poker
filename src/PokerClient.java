import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Created by Aditya and Sai on 1/10/2017.
 */
public class PokerClient {

    //Creating global variables

    //Tool variables
    Scanner scanner = new Scanner(System.in);

    //Client identities
    String name;
    String hostname;
    int numPlayers;
    String[] playerNames;

    int chips;

    //Socket related variables
    Socket clientSocket;
    InputStream in;
    OutputStream out;

    //TEST constructor for PokerClient that connects to server and sends name to server.
    public PokerClient(String name, String hostname) throws IOException {
        this.name = name;
        this.hostname = hostname;
        connect();
        sendName();

    }

    //Constructor for PokerClient that connects to server and sends name to server.
    public PokerClient() throws IOException {
        this.name = requestName();
        this.hostname = requestHostname();
        connect();
        sendName();
    }

    //Asks the client for their name.
    private String requestName() {
        System.out.println("Please enter your name:");
        String input = scanner.nextLine();
        return input;
    }

    //Asks the client for the IP address of the server.
    private String requestHostname() {
        System.out.println("Please enter the hostname of the server:");
        String input = scanner.nextLine();
        return input;
    }

    //Uses the hostname to connect to the server.
    private void connect() throws IOException {
        clientSocket = new Socket(hostname, 24999);
        in = clientSocket.getInputStream();
        out = clientSocket.getOutputStream();
    }

    //Sends client's name to the server.
    private void sendName() throws IOException {
        byte[] nameBytes = name.getBytes(Charset.forName("US-ASCII"));
        out.write(nameBytes.length);
        out.flush();
        out.write(nameBytes);
        out.flush();
    }

    //Reads list of all of the clients connected to the server.
    public String[] readNames() throws IOException {

        //First collects number of names to read
        //Collects length of name, then name as byte array
        //Converts byte array to string and adds to playerNames array
        //Returns array of names
        numPlayers = in.read();
        playerNames = new String[numPlayers];

        for (int i = 0; i < numPlayers; i++) {
            int length = in.read();
            byte[] nameBytes = new byte[length];
            while (length > 0) {
                length -= in.read(nameBytes, nameBytes.length - length, length);
            }
            playerNames[i] = new String(nameBytes, Charset.forName("US-ASCII"));

            System.out.println(playerNames[i]);

        }
        return playerNames;
    }
    public void recieveChips()throws IOException{
        int length = 4;
        byte[] numBytes = new byte[4];
        while (length > 0) {
            length -= in.read(numBytes, numBytes.length - length, length);
        }

        System.out.println(Integer.toBinaryString(numBytes[0] & 0xFF) + "\n" + Integer.toBinaryString(numBytes[1] & 0xFF)
            + "\n" + Integer.toBinaryString(numBytes[2] & 0xFF) + "\n" + Integer.toBinaryString(numBytes[3] & 0xFF));

        chips += (numBytes[0] & 0xFF) << 24;
        chips += (numBytes[1] & 0xFF) << 16;
        chips += (numBytes[2] & 0xFF) << 8;
        chips += (numBytes[3] & 0xFF);
        System.out.println(chips);
    }
}