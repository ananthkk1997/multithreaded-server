/**
 * Created by ananth on 2/24/17.
 */

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public boolean running = true;

    // IO with Server
    public Socket socket;
    public BufferedReader bufferedReader;
    public PrintWriter printWriter;
    public Scanner scanner;


    public Client() {
        try {
            this.socket = new Socket("127.0.0.1", Constants.portNumber);
            System.out.println("Connecting to server...");

            // Get input data from user from STDIN
            this.scanner = new Scanner(System.in);

            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);

            String message1 = toServer(Constants.SERVER_OK_CODE + Constants.SERVER_CODE_CONNECTOR +
                    "Client joined");
            System.out.println(message1);
            System.out.println("Type a message to send to the server.");
            System.out.println("Type QUIT to exit.");

            // Display input from server on Client side (chat messages)
            while(running) {
                // Send your commandline inputs to the Server (your chat messages)
                String message;
                while ((message = scanner.nextLine()) != null) {
                    // If user types QUIT_MESSAGE, send Quit server code to ClientRunnable, and break out of loop
                    if (message.equals(Constants.QUIT_MESSAGE)) {
                        running = false;

                        String response = toServer(Constants.SERVER_QUIT_CODE + Constants.SERVER_CODE_CONNECTOR
                                + Constants.QUIT_MESSAGE);
                        System.out.println("Server response: " + response);
                        break;
                    } else {
                        // Send what user typed with Chatting server code
                        String response = toServer(Constants.SERVER_OK_CODE + Constants.SERVER_CODE_CONNECTOR
                                + message);
                        System.out.println("Server response: " + response);
                    }
                }
            }
        } catch (IOException e) {
            // No server running with this IP and this PortNumber, exit.
        }
    }


    /***
     * Sends a message to server and gets a response from it
     * @param message
     * @return
     */
    public String toServer(String message){
        try{
            //send message to server
            this.printWriter.println(message);
            String serverMessage = bufferedReader.readLine();
            //receive what the message is
            //return received message
            String[] str = serverMessage.split(Constants.SERVER_CODE_CONNECTOR);
            String code = str[0];
            if (code.equals(Constants.SERVER_FULL_CODE)) {
                // Server is full;
                running = false;
            }
            if (str.length != 0) {
                serverMessage = str[1];
            }
            return serverMessage;
        }catch(IOException e){
            e.printStackTrace();
        }
        return " ";
    }

    /***
     * Parses line received from Server for server codes and displays message
     * @param line
     */
    public void parseAndDisplay(String line) {
        String[] strArr = line.split(Constants.SERVER_CODE_CONNECTOR);
        String code = strArr[0];
        String message = strArr[1];
        // Handle all server codes
        switch (code) {
            case Constants.SERVER_OK_CODE:
                // Ok to display on main server screen
                System.out.println(message);
        }
    }

    public static void main(String[] args) {
        // Create a client and connect to Server
        Client client = new Client();
    }
}
