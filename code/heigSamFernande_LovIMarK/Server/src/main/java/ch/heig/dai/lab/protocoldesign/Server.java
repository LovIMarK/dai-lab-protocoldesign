package ch.heig.dai.lab.protocoldesign;


import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;


public class Server {
    final int SERVER_PORT = 1234;
    private static final String WELCOME_MESSAGE = "WELCOME! Supported operations: ADD, MULTIPLY (QUIT)\n";
    private static final String GOODBYE_MESSAGE = "GOODBYE\n";
    private static final String ERROR_MALFORMED = "ERROR: Malformed request\n";
    private static final String ERROR_OPERANDS = "ERROR: Operands must be integers\n";
    private static final String ERROR_UNSUPPORTED = "ERROR: Unsupported operation\n";


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
                     BufferedReader in = new BufferedReader(
                             new InputStreamReader(
                                     clientSocket.getInputStream(), StandardCharsets.UTF_8));
                     BufferedWriter out = new BufferedWriter(
                             new OutputStreamWriter(
                                     clientSocket.getOutputStream(), StandardCharsets.UTF_8))) {

                    // Send welcome message with supported operations
                    out.write(WELCOME_MESSAGE);
                    out.flush();


                    // Read client input and handle operations
                    String clientInput;
                    while ((clientInput = in.readLine()) != null) {
                        if (clientInput.equals("QUIT")) {
                            out.write(GOODBYE_MESSAGE);
                            out.flush();
                            break;
                        }

                        String[] parts = clientInput.split(" ");

                        // Check if the request is well-formed
                        if (parts.length != 3) {
                            out.write(ERROR_MALFORMED);
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
                            out.write(ERROR_OPERANDS);
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
                return "RESULT " + (value1 + value2);
            case "MULTIPLY":
                return "RESULT " + (value1 * value2);
            default:
                return ERROR_UNSUPPORTED;
        }
    }
}