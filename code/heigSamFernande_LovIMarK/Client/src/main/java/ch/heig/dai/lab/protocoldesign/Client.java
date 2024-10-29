package ch.heig.dai.lab.protocoldesign;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class Client {
    final String SERVER_ADDRESS = "127.0.0.1";
    final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        // Create a new client and run it
        Client client = new Client();
        client.run();
    }

    private void run() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

            // Read the server's welcome message
            String welcomeMessage = in.readLine();
            System.out.println(welcomeMessage);

            // Loop to send operations until the user wants to quit
            String command;
            while (true) {
                System.out.print("Enter a command (or QUIT to quit): ");
                command = System.console().readLine(); // Read the command line

                if (command == null || command.trim().equalsIgnoreCase("QUIT")) {
                    out.write("QUIT\n");
                    out.flush();
                    System.out.println("Disconnecting from the server.");
                    break;
                }

                // Send the command to the server
                out.write(command + "\n");
                out.flush();

                // Read and display the server's response
                String response = in.readLine();
                System.out.println(response);
            }
        } catch (IOException e) {
            System.err.println("Error connecting to the server: " + e.getMessage());
        }
    }
}