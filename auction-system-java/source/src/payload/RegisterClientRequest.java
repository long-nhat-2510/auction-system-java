package payload;

public class RegisterClientRequest {

    private String clientId;   // ID client (có thể là userId)
    private String username;   // tên hiển thị
    private String role;       // USER / SELLER / ADMIN

    public RegisterClientRequest() {
    }

    public RegisterClientRequest(String clientId, String username, String role) {
        this.clientId = clientId;
        this.username = username;
        this.role = role;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "RegisterClientRequest{" +
                "clientId='" + clientId + '\'' +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}