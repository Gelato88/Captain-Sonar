import java.io.*;
import java.net.*;

public class ServerThread extends Thread {

    private Socket socket;
    private String name;
    private boolean running;

    private BufferedReader reader;
    private InputStream input;
    private OutputStream output;
    private PrintWriter writer;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        running = true;
        try {
            input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            requestName();

            String text;

            do {
                text = reader.readLine();
                System.out.println(name + ": " + text);
                sendToAll(text);

                switch(text) {
                    case "close server":
                        Main.sendFromServerToAll("Shutting down...");
                        Main.quit();
                        break;
                    case "~assign-role":
                        int teamNum = reader.read();
                        int roleNum = reader.read();
                        Main.joinTeam(teamNum, roleNum, this);
                }

            } while(!text.equals("exit") && running);
            socket.close();
        } catch(IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /* Asks for and assigns the user a name.
     *
     */
    public void requestName() {
        try {
            writer.println("Server: Enter your name.");
            name = reader.readLine();
            System.out.println("Received name " + name + ".");
            writer.println("Server: Received name " + name + ".");
        } catch(IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void close() {
        running = false;
    }

    /* Sends a message to this user.
     *
     */
    public void sendMessage(String str) {
        writer.println(str);
    }

    /* Sends a message to all connected users.
     *
     */
    public void sendToAll(String str) {
        for(ServerThread user : Main.users) {
            user.sendMessage(name + ": " + str);
        }
    }

}
