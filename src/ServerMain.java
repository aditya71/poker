import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 * Created by adity on 1/10/2017.
 */
public class ServerMain {

    static ServerSocket ss;
    static byte numPlayers; // unused

    public static void main(String [] args) throws IOException{
        ss = new ServerSocket(24999);
        Client[] client = new Client[9];
        numPlayers = 0; //unused


        new Thread(new PokerConsole()).start();

        for(int i = 0; i < client.length; i++){
            try {
                client[i] =  new Client(ss.accept());
                int length = client[i].in.read();
                byte[] nameBytes = new byte[length];
                while(length > 0){
                    length -= client[i].in.read(nameBytes, nameBytes.length - length, length);
                }

                client[i].name = new String(nameBytes);
                System.out.println(client[i].name);


            } catch(SocketException e){

                break;
            }
        }

        for(int i = 0; i < client.length; i++){
            if(client[i] != null)
                numPlayers++; // unused
            else
                break;
        }
        sendNames(numPlayers, client);
    }

    private static void sendNames(int numPlayers, Client[] client) throws IOException{

        for(int i = 0; i < numPlayers; i++){
            client[i].out.write((byte)numPlayers);
            client[i].out.flush();
            for(int j = 0; j < numPlayers; j++){

                byte[] nameBytes = client[j].name.getBytes();
                byte length = (byte) nameBytes.length;
                client[i].out.write(length);
                client[i].out.flush();
                client[i].out.write(nameBytes);
                client[i].out.flush();

            }
        }

    }



    static class Client {
        public Socket socket;
        public InputStream in;
        public OutputStream out;

        public String name;
        public int chips;

        public Client(Socket socket) throws IOException {
            if(socket == null) {
                throw new NullPointerException("Socket can't be null");
            }

            this.socket = socket;
            in = socket.getInputStream();
            out = socket.getOutputStream();
        }

    }

    static class PokerConsole implements Runnable {
        Scanner scanner;
        String input;

        public PokerConsole() {
            scanner = new Scanner(System.in);
        }

        @Override
        public void run() {
            while (true) {
                input = scanner.nextLine();
                if (input.equals("start")) {
                    try {
                        ss.close();
                    } catch (IOException e) {
                        //No need to do anything
                    }

                    break;
                }
            }
        }
    }
}
