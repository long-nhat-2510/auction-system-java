package Server;

import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConnectedClient {
    private String username;
    private String ipAddress;
    private LocalDateTime loginTime;
    private String role; //Bidder or Seller
    //khoi tao
    public ConnectedClient(String username,String ipAddress, String role){
        this.username = username;
        this.ipAddress = ipAddress;
        this.role = role;
        this.loginTime = LocalDateTime.now();

    }
    // getter
    public String getUsername(){
        return username;
    }
    public String getIpAddress(){
        return ipAddress;
    }
    public String getRole(){
        return role;
    }
    public LocalDateTime getLoginTime(){
        return loginTime;
    }

    @Override
    public String toString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return String.format("[%s] User: %s (%s) IP: %s",
                loginTime.format(formatter), username, role, ipAddress);
    }


}
