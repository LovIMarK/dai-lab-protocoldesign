# Protocol Specification - Arithmetic Client-Server Application

### Naming Conventions
- **Ids Naming**: `heigSamFernande_LovIMarK`
- **File naming**: camelCase
- **Branch naming**: `feature_<whole feature name>`
- **Workflow**: Create a branch for each feature
- **DevTextFormat**: Markdown

---

## 1.Overview
This protocol defines the requirements for a client-server application that performs basic arithmetic operations. The **server** listens for operation requests (addition and multiplication) and returns the result. The **client** reads user input, sends a properly formatted request to the server, and displays the server's response.

The protocol includes agreements on encoding, message format, and error handling to ensure reliable communication between the client and server.

---

## 2.Transport Layer Protocol
- **Protocol**: TCP for reliable data transmission.
- **Port**: The server listens on port `1234`.

### Connection
- **Setup**: The client initiates a connection to the server’s IP address and specified port.
- **Termination**: The server closes the connection after sending the response.

All messages are encoded in UTF-8, and each message is terminated with a newline character (`\n`).

---

## 3.Messages

### Request Message (Client to Server)
The client sends a request message in the format:
  ```
  Welcome! Supported operations: ADD, MULTIPLY\n
  ```
### Request Message (Client to Server)
The client sends a request message in the format:
  ```
  OPERATION VALUE1 VALUE2\n
  ```

- **OPERATION**: Specifies the arithmetic operation (`ADD` or `MULTIPLY`).
- **VALUE1 and VALUE2**: Integers representing the operands for the calculation. **Negative values are not allowed.**

**Examples**:
- `ADD 10 20\n` (addition of 10 and 20)
- `MULTIPLY 5 3\n` (multiplication of 5 and 3)

### Response Message (Server to Client)
The server responds based on the validity of the client’s request.

- **Success Response**:
- `Result: RESULT_VALUE\n`
  **Example**: `Result: 30\n`

- **Error Responses**:
- **Unsupported Operation**: If the operation is invalid, the server returns:
  ```
  Error: Unsupported operation\n
  ```
- **Malformed Request**: If the format is incorrect, the server returns:
  ```
  Error: Malformed request\n
  ```

---

## 4.Example Dialogs

**Example 1 - Successful Operation**
- **Server**: `Welcome! Supported operations: ADD, MULTIPLY\n`
- **Client**: `ADD 15 25\n`
- **Server**: `Result: 40\n`

**Example 2 - Invalid Operation**
- **Server**: `Welcome! Supported operations: ADD, MULTIPLY\n`
- **Client**: `SUBTRACT 10 5\n`
- **Server**: `Error: Unsupported operation\n`

**Example 3 - Malformed Request**
- **Server**: `Welcome! Supported operations: ADD, MULTIPLY\n`
- **Client**: `MULTIPLY 5\n`
- **Server**: `Error: Malformed request\n`



