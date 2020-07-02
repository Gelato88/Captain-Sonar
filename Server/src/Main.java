import java.io.*;
import java.net.*;
import java.util.*;
public class Main {

    final static int PORT = 6000;
    private static ServerSocket ss;
    private static boolean running;
    public static ArrayList<ServerThread> users;

    public static void main(String[] args) {
        running = true;
        try {
            ss = new ServerSocket(PORT);
            users = new ArrayList<ServerThread>();
            System.out.println("Listening on port " + PORT);
            while(running) {
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

    public static void quit() {
        running = false;
        try {
            for (ServerThread thread : users) {
                thread.close();
                ss.close();
                System.exit(-1);
            }
        } catch(IOException e) {
            System.out.println("Server exception: " +  e.getMessage());
            e.printStackTrace();
        }
    }

    public static void sendFromServerToAll(String str) {
        for(ServerThread thread : users) {
            thread.sendMessage("Server: " + str);
        }
    }

}