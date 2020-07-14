import java.io.*;
import java.net.*;
import java.util.*;
public class Main {

    final static int PORT = 6000;
    private static ServerSocket ss;
    private static boolean running;
    public static ArrayList<ServerThread> users;

    public static ServerThread t1[];
    public static ServerThread t2[];

    public static void main(String[] args) {
        running = true;
        t1 = new ServerThread[4];
        t2 = new ServerThread[4];
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

    /* Adds a player to a role on a team.
     * Role numbers to role conversion:
     * 0 - captain
     * 1 - first mate
     * 2 - radio operator
     * 3 - engineer
     */
    public static void joinTeam(int teamNum, int roleNum, ServerThread player) {
        if(teamNum == 1) {
            t1[roleNum] = player;
        } else if(teamNum == 2) {
            t2[roleNum] = player;
        } else {
            System.out.println("Server exception: received an invalid team number.");
        }

        System.out.println(player.getName() + " has been assigned to team " + teamNum + " with role id " + roleNum + ".");

    }

}