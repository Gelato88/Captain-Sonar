package com.mygdx.sonar_client;

import java.io.*;
import java.net.*;
import java.util.*;

public class OutputThread extends Thread {


    private Socket socket;
    private OutputStream output;
    private PrintWriter writer;
    private Scanner scanner;
    private String text;
    public static boolean active;

    public OutputThread(Socket socket) {
        this.socket = socket;
        scanner = new Scanner(System.in);
        try {
            output = this.socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch(IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }

        active = true;

    }

    public void run() {
        while(true) {
            if(active) {
                text = scanner.nextLine();
                writer.println(text);
            }
        }
    }

}
