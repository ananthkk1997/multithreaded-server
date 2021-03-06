import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public ServerSocket serverSocket;

    /***
     * Initialize Server with port number and client handling
     */
    public Server() {
        try {
            ServerSocket server = new ServerSocket(Constants.portNumber);
            displayServerInfo();

            // Repeatedly accept user connections
            while(!server.isClosed()) {
                Socket socket = server.accept();
                if (canAcceptMoreClients()) {
                    // Start a new Thread for new client
		            System.out.println("Now hosting " + Thread.activeCount() + " client(s).");
                    Thread clientThread = new Thread(new ClientRunnable(socket, this));
                    clientThread.start();

                } else {
                    // We reached the max number of clients. Send data to client indicating they can't connect
                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                    printWriter.println(Constants.SERVER_FULL_CODE + Constants.SERVER_CODE_CONNECTOR
                            + "Sorry! The server is full right now.");
                    socket.close();
                }
            }

        } catch (IOException e) {
            // Error listening to port
            System.out.println("Could not listen on port: " + Constants.portNumber + ". Another server may " +
                    "be running!");
        }
    }


    /***
     *
     * @return True if we can accept more clients, False otherwise
     */
    public boolean canAcceptMoreClients() {
        return Thread.activeCount() <= Constants.maxClientNum;
    }

    /***
     *
     * @return Number of clients being hosted at this time
     */
    public int getNumClientsHosting() {
        return Thread.activeCount() - 1;
    }

    /***
     * Displays server info when launching the server
     */
    public void displayServerInfo() {
        System.out.println(Constants.serverName +" hosted on port number: " + Constants.portNumber);
        System.out.println("Clients can now begin connecting.");
	System.out.println("Server can support " + Constants.maxClientNum + " client(s).");
    }

    public static void main(String[] args) {
        Server server = new Server();
        // Server is now running
    }
}
