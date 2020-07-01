import java.io.*;
import java.net.*;
import java.util.*;
public class Main {

    final static int PORT = 24644;
    private static ServerSocket ss;

    public static ArrayList<ServerThread> users;

    public static void main(String[] args) {
        try {
            ss = new ServerSocket(PORT);
            users = new ArrayList<ServerThread>();
            System.out.println("Listening on port " + PORT);
            while(true) {
                Socket socket = ss.accept();
                System.out.println("A client has connected.");
                users.add(new ServerThread(socket));
                users.get(users.size()-1).start();
            }
        } catch(IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}