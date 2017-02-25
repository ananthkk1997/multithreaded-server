/**
 * Created by ananth on 2/24/17.
 */
public class Constants {
    // Server information
    public static final int portNumber = 4444;
    public static final int maxClientNum = 2;
    public static final int maxMessagesToClient = 10;
    public static final String serverName = "ANANTH'S SERVER";

    // Client codes
    public static final String QUIT_MESSAGE = "QUIT";

    // Server codes
    public static final String SERVER_CODE_CONNECTOR = "---";
    public static final String SERVER_QUIT_CODE = serverName + " QUIT";
    public static final String SERVER_OK_CODE = serverName + " TRANSMIT";
    public static final String SERVER_FULL_CODE = serverName + " FULL";
}
