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
    static Client[] clients;

    static Deck deck;
    public static final int bigBlind = 5000;
    public static final int littleBlind = bigBlind/2;


    public static void main(String [] args) throws IOException {
        //Creating variables for the main class
        ss = new ServerSocket(24999);
        clients = new Client[9];
        numPlayers = 0; //unused
        new Thread(new PokerConsole()).start();
        acceptPlayers();

        //Counts the total number of connected clients.
        for(int i = 0; i < clients.length; i++) {
            if(clients[i] == null) {
                numPlayers++;
            } else {
                break;
            }
        }

        //Sends all of the player names to all of the connected clients.
        sendNames(numPlayers, clients);
        sendChips(100000);
        updateClientsChips(clients);

        int countingBig = 0; //wtf does this variable name even mean

        while(true) {
            int pot = 0;
            deck = new Deck();

            dealCards();

            pot +=  bigBlind + littleBlind;

            clients[countingBig].chips -= littleBlind;
            if(countingBig == 3){
                countingBig = 0;
            }
            clients[countingBig].chips -= bigBlind;
            if(countingBig == 3){
                countingBig = 0;
            }
            while(true){



                break;
            }




            break;
        }

    }



    private static void acceptPlayers() throws SocketException, IOException {
        for(int i = 0; i < clients.length; i++) {
            clients[i] = new Client(ss.accept());
            int length = clients[i].in.read();
            byte[] nameBytes = new byte[length];
            while (length > 0) {
                length -= clients[i].in.read(nameBytes, nameBytes.length - length, length);
            }

            clients[i].name = new String(nameBytes, Charset.forName("US-ASCII"));
            System.out.println(clients[i].name);
        }
    }

    private static void updateClientsChips(Client[] client)throws IOException{
        for(int i = 0; i < numPlayers; i++) {
            for(int j = 0; j < numPlayers; j++) {
                sendChips(client[j].chips, client[i]);
            }
        }
    }

    private static void sendChips(int chips, Client... clients) throws IOException{
        for(Client c : clients) {
            if(c != null) {
                ByteBuffer chipBytes = ByteBuffer.allocate(4);
                chipBytes.putInt(chips);
                c.out.write(chipBytes.array());
                c.out.flush();
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

    private static void dealCards() throws IOException {
        for(int i = 0; i < numPlayers; i++) {
            Card card1 = deck.pop();
            Card card2 = deck.pop();

            clients[i].cards = new Card[] {card1, card2};
            byte[] cardsStringBytes = (card1.toString() + card2.toString()).getBytes(Charset.forName("US-ASCII"));

            //Write byte array length, then the byte array itself
            clients[i].out.write((byte)cardsStringBytes.length);
            clients[i].out.write(cardsStringBytes);
        }
    }


//The client class on the server side: stores socket, name, chips, and two cards of each client
    static class Client {
        public Socket socket;
        public InputStream in;
        public OutputStream out;
        Card[] cards = new Card[2];
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
