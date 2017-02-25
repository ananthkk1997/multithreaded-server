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

    public ClientRunnable(Socket socket) {
        this.socket = socket;
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            // Problem connecting client socket
            System.out.println("Error connecting to client socket from Runnable");
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
                // Problem connecting client socket
                System.out.println("Error connecting to client socket from Runnable");
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
                // If we receive the quit message from the user, kill thread
                kill();
                break;
            case Constants.SERVER_OK_CODE:
                // Ok to display on main server screen
                System.out.println("Client " + socket.getRemoteSocketAddress().toString() + ": " + message);
                printWriter.println(Constants.SERVER_OK_CODE + Constants.SERVER_CODE_CONNECTOR + " Received!");

        }
    }

    /***
     * Stop ClientRunnable from running
     */
    public void kill() {
        isRunning = false;
    }
}
