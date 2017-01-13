import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Created by Sai and Aditya on 1/10/2017.
 */
public class ServerMain {

    //Creating global variables
    static ServerSocket ss;
    static byte numPlayers; // unused

    public static void main(String [] args) throws IOException {
        //Creating variables for the main class
        ss = new ServerSocket(24999);
        Client[] client = new Client[9];
        numPlayers = 0; //unused


        new Thread(new PokerConsole()).start();

        //Connects to each PokerClient and gets their name

        for(int i = 0; i < client.length; i++){
            try {
                client[i] =  new Client(ss.accept());
                int length = client[i].in.read();
                byte[] nameBytes = new byte[length];
                while(length > 0){
                    length -= client[i].in.read(nameBytes, nameBytes.length - length, length);
                }

                client[i].name = new String(nameBytes, Charset.forName("US-ASCII"));
                System.out.println(client[i].name);

            //A SocketException is thrown when the server stops accepting connections
            //In this case, it is thrown when the user types "start" into the console
            } catch(SocketException e){

                break;
            }
        }

        //Counts the total number of connected clients.
        for(int i = 0; i < client.length; i++){
            if(client[i] != null)
                numPlayers++; // unused
            else
                break;
        }
        //Sends all of the player names to all of the connected clients.
        sendNames(numPlayers, client);
        for(int i = 0; i < numPlayers; i++){
            sendChips(100000, client[i]);

        }
        updateClientsChips(client);

    }

    private static void updateClientsChips(Client[] client)throws IOException{
        for(int i = 0; i < numPlayers; i++){
            for(int j = 0; j < numPlayers; j++){
                sendChips(client[j].chips,client[i]);
            }
        }
    }

    //When called, it sends all of the player names to all of the connected clients.
    private static void sendNames(int numPlayers, Client[] client) throws IOException{

        //Nested for loop: Inside loops through all clients to send length of name and name.
                         //Outside loops through each client to send them the information from the inside loop.
        for(int i = 0; i < numPlayers; i++){
            client[i].out.write((byte)numPlayers);
            client[i].out.flush();
            for(int j = 0; j < numPlayers; j++){

                byte[] nameBytes = client[j].name.getBytes(Charset.forName("US-ASCII"));
                byte length = (byte) nameBytes.length;
                client[i].out.write(length);
                client[i].out.flush();
                client[i].out.write(nameBytes);
                client[i].out.flush();

            }
        }

    }
    private static void sendChips(int chips,Client c) throws IOException{
        c.chips += chips;
        byte[] chipsBytes = new byte[4];
        chipsBytes[0] = (byte)(chips >> 24);
        chipsBytes[1] = (byte)(chips >> 16);
        chipsBytes[2] = (byte)(chips >> 8);
        chipsBytes[3] = (byte)(chips);
        c.out.write(chipsBytes);
        c.out.flush();
    }


//The client class on the server side: stores socket, name, chips, and two cards of each client
    static class Client {
        public Socket socket;
        public InputStream in;
        public OutputStream out;

        public String name;
        public int chips;

        //Constructor for client, makes sure client is not null and sets values.
        public Client(Socket socket) throws IOException {
            if(socket == null) {
                throw new NullPointerException("Socket can't be null");
            }

            this.socket = socket;
            in = socket.getInputStream();
            out = socket.getOutputStream();
        }

    }

    //Runs on a separate thread
    //Necessary to ensure that commands can be input into the server console
    static class PokerConsole implements Runnable {
        Scanner scanner;
        String input;

        public PokerConsole() {
            scanner = new Scanner(System.in);
        }

        //Stops collecting names and starts the game when "start" is entered in the console.
        @Override
        public void run() {
            while (true) {
                input = scanner.nextLine();
                if (input.equals("start")) {
                    try {
                        ss.close();

                      //Makes sure to do nothing when the input entered is not an actual command.
                    } catch (IOException e) {
                        //No need to do anything
                    }

                    break;
                }
            }
        }
    }
}
