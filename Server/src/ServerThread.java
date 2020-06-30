import java.io.*;
import java.net.*;

public class ServerThread extends Thread {

    private Socket socket;
    private String name;
    private ServerThread connection;
    private Socket connect;
    private String connected;

    public ServerThread(Socket socket) {
        this.socket = socket;
        connect = null;
    }

    public String getUsername() {
        return name;
    }

    public Socket getSocket() {
        return socket;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            writer.println("Enter your name.");
            name = reader.readLine();
            System.out.println("Received name " + name);

            String text;

            do {

                text = reader.readLine();
                System.out.println(name + ": " + text);

                if(text.equals("connect")) {
                    writer.println("Who do you want to connect to?");
                    connected = reader.readLine();
                    for(ServerThread thread : Main.users) {
                        if (thread.getUsername().equals(connected)) {
                            writer.println("You are now sending messages to: " + connected);
                            connection = thread;
                        }
                    }
                }

                else if(connection != null) {
                    OutputStream send = connection.getSocket().getOutputStream();
                    PrintWriter sender = new PrintWriter(send, true);
                    sender.println(name + ": " + text);
                    writer.println("Your message was sent to " + connected);
                }

                else {
                    writer.println("Server: " + text);
                }

            } while(!text.equals("exit"));
            socket.close();
        } catch(IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
