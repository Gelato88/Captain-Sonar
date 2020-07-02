package com.mygdx.sonar_client;

import java.io.*;
import java.net.*;

public class InputThread extends Thread {

    private Socket socket;
    private Client client;
    public boolean stop;

    public InputThread(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
        stop = false;
    }

    public void run() {
        while(!stop) {

            String text;

            try {
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                text = reader.readLine();
                if(text != null) {
                    client.addMessage(text);
                }
                if(text.equals("Server: Shutting down...")) {
                    stop = true;
                }
            } catch (IOException e) {
                System.out.println("Exception: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void exit() {
        stop = true;
    }
}
