package Server;

import java.io.PrintWriter;
import java.net.Socket;

public class ConnectedClient {
    private String username;
    private Socket socket;
    private PrintWriter out;
    //khoi tao
    public ConnectedClient(String username, Socket socket, PrintWriter out){
        this.username = username;
        this.socket = socket;
        this.out = out;
    }
    // gui tin nhan rieng

}
