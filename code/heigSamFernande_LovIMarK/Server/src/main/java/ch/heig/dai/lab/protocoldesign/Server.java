package ch.heig.dai.lab.protocoldesign;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;


public class Server {
    final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        // Create a new server and run it
        Server server = new Server();
        server.run();
    }


    private void run() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server is running on port " + SERVER_PORT);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     var in = new BufferedReader(
                             new InputStreamReader(
                                     clientSocket.getInputStream(), StandardCharsets.UTF_8));
                     var out = new BufferedWriter(
                             new OutputStreamWriter(
                                     clientSocket.getOutputStream(), StandardCharsets.UTF_8))) {

                    // Send welcome message with supported operations
                    out.write("Welcome! Supported operations: ADD, MULTIPLY" + "\n" );
                    out.flush();

                    // Read client input and handle operations
                    String clientInput;
                    while ((clientInput = in.readLine()) != null) {
                        if (clientInput.equals("QUIT")) {
                            out.write("Goodbye!" + "\n");
                            out.flush();
                            break;
                        }

                        String[] parts = clientInput.split(" ");

                        // Check if the request is well-formed
                        if (parts.length != 3) {
                            out.write("Error: Malformed request"+ "\n");
                            out.flush();
                            continue;
                        }
                        String operation = parts[0].toUpperCase();
                        try {
                            int value1 = Integer.parseInt(parts[1]);
                            int value2 = Integer.parseInt(parts[2]);

                            // Call the arithmetic method to process the request
                            String response = arithmetic(operation, value1, value2);
                            out.write(response + "\n");
                            out.flush();


                        } catch (NumberFormatException e) {
                            out.write("Error: Operands must be integers" + "\n");
                            out.flush();
                        }
                    }

                } catch (Exception e) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    /**
     * Processes an arithmetic operation.
     *
     * @param operation The type of operation.
     * @param value1 The first operand.
     * @param value2 The second operand.
     * @return The result as a string, or an error message if the operation is unsupported.
     */
    private String arithmetic(String operation, int value1, int value2) {
        switch (operation) {
            case "ADD":
                return "Result: " + (value1 + value2);
            case "MULTIPLY":
                return "Result: " + (value1 * value2);
            default:
                return "Error: Unsupported operation";
        }
    }
}