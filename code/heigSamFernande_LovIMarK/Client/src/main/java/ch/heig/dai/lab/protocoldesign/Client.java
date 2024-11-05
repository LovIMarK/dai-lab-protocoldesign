package ch.heig.dai.lab.protocoldesign;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    final String SERVER_ADDRESS = "127.0.0.1";
    final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        new Client().run();
    }

    private void run() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

            // Welcome message
            System.out.println(in.readLine());

            while (true) {
                // Read the user's command
                String command = getUserCommand();

                // Check if the user wants to quit
                if (command == null) {
                    disconnect(out);
                    break;
                }

                // Send the command to the server
                out.write(command + "\n");
                out.flush();

                // Read and handle the server's response
                String response = in.readLine();
                if (!handleResponse(response)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error connecting to the server: " + e.getMessage());
        }
    }

    /**
     * Reads the user's command and checks if it is "QUIT".
     *
     * @return the user's command or null if the user wants to quit
     */
    private String getUserCommand() {
        System.out.print("Enter a command (or QUIT to quit): ");
        String command = System.console().readLine();
        return (command == null || command.trim().equalsIgnoreCase("QUIT")) ? null : command;
    }

    /**
     * Sends a disconnect command to the server and displays a message.
     *
     * @param out the output stream to the server
     * @throws IOException if an IO error occurs
     */
    private void disconnect(BufferedWriter out) throws IOException {
        out.write("QUIT\n");
        out.flush();
        System.out.println("Disconnecting from the server.");
    }


    /**
     * Handles the server's response and displays an appropriate message based on the error code.
     *
     * @param response the server's response
     * @return true if the connection should continue, false if it should end
     */
    private boolean handleResponse(String response) {
        switch (response) {
            case "400":
                System.out.println("ERROR: Malformed request. Please follow the format: OPERATION VALUE1 VALUE2.");
                break;
            case "401":
                System.out.println("ERROR: Unsupported operation. Use ADD or MULTIPLY.");
                break;
            case "402":
                System.out.println("ERROR: Operands must be integers.");
                break;
            case "0":
                System.out.println("GOODBYE!");
                return false; // Indicates that the connection should end
            default:
                System.out.println(response);
                break;
        }
        return true;
    }
}
