import java.io.*;
import java.net.*;

public class ServerThread extends Thread {

    private Socket socket;
    private String name;

    private BufferedReader reader;
    private InputStream input;
    private OutputStream output;
    private PrintWriter writer;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
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
            } while(!text.equals("exit"));
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
