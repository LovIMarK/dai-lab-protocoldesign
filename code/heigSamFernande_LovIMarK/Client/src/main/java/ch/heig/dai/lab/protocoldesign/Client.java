package ch.heig.dai.lab.protocoldesign;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class Client {
    private final String SERVER_ADDRESS = "127.0.0.1";
    private final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    /**
     * Runs the client application, connecting to the server and allowing user input to send commands.
     */
    private void run() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            String command;
            while (true) {
                System.out.print("Enter a command (or QUIT to quit): ");

                // Read command from user
                command = consoleReader.readLine();

                if (command == null || command.trim().equalsIgnoreCase("QUIT")) {
                    sendCommand(out, "QUIT"); // Sends quit command to server
                    break;
                }

                sendCommand(out, command); // Sends user command to server
                System.out.println(readResponse(in)); // Reads and displays server response
            }
        } catch (UnknownHostException e) {
            System.err.println("Error: Unknown host - " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error: Unable to connect to the server - " + e.getMessage());
        }
    }

    /**
     * Sends a command to the server.
     *
     * @param out     BufferedWriter to send data to the server
     * @param command The command to send
     * @throws IOException if an I/O error occurs
     */
    private void sendCommand(BufferedWriter out, String command) throws IOException {
        out.write(command + "\n");
        out.flush();
    }

    /**
     * Reads the server's response.
     *
     * @param in BufferedReader to receive data from the server
     * @return The server's response as a string
     * @throws IOException if an I/O error occurs
     */
    private String readResponse(BufferedReader in) throws IOException {
        return in.readLine();
    }
}
