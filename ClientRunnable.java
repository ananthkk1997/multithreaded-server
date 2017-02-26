import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by ananth on 2/24/17.
 */
public class ClientRunnable implements Runnable{
    // Process info for a particular client on a separate thread
    private Socket socket;
    private boolean isRunning = true;

    public BufferedReader bufferedReader;
    public PrintWriter printWriter;
    private Server server;

    public ClientRunnable(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            // Problem connecting client socket data output stream
            System.out.println("Problem with " + socket.getRemoteSocketAddress().toString() + "'s connection.");
        }
    }

    /***
     * Receive Client data
     */
    @Override
    public void run() {
        printWriter.println(Constants.SERVER_OK_CODE + Constants.SERVER_CODE_CONNECTOR + "Welcome to " + Constants.serverName + "!");
        while (isRunning) {
            try {
                // Get Client data from socket inputStream
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    // Deal with codes and display data
                    parse(line);
                }
            } catch (IOException e) {
                // Problem connecting client socket. Client has disconnected
                printDisconnectedMessages();
                kill();
            }
        }
    }

    /***
     *  Parses inputs from Client socket and deals with Server Codes
     */
    public void parse(String line) {
        String[] strArr = line.split(Constants.SERVER_CODE_CONNECTOR);
        String code = strArr[0];
        String message = strArr[1];
        // Handle all server codes
        switch (code) {
            case Constants.SERVER_QUIT_CODE:
                // If we receive the quit message from the user, disconnect client
                // and kill thread
                printDisconnectedMessages();
                kill();
                break;
            case Constants.SERVER_OK_CODE:
                // Ok to display on main server screen
                System.out.println("Client " + socket.getRemoteSocketAddress().toString() + ": " + message);
                printWriter.println(Constants.SERVER_OK_CODE + Constants.SERVER_CODE_CONNECTOR + " Received!");

        }
    }

    /***
    *  Indicates that this client has disconnected, and displays the current
    *  number of clients being hosted
    */
    public void printDisconnectedMessages() {
        System.out.println("Client " + socket.getRemoteSocketAddress().toString() + " has disconnected.");
        System.out.println("Now hosting " + (this.server.getNumClientsHosting() - 1) + " client(s).");
    }

    /***
     * Stop ClientRunnable from running
     */
    public void kill() {
        isRunning = false;
    }
}
