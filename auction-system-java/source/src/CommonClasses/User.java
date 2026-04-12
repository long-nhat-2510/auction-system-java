package CommonClasses;

import java.sql.Timestamp;

public class User {
    private String user_id;
    private String username;
    private String password;
    private UserRole role;
    private Timestamp createdAt;

    // Constructor đầy đủ
    public User(String user_id, String username, String password, UserRole role, Timestamp createdAt) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }

    // Constructor dùng khi mới đăng ký (chưa có ID và thời gian)
    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
        // ID và createdAt thường sẽ do Database hoặc Server tự sinh ra
    }

    // --- Các hàm Getters ---
    public String getUser_id() { return user_id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public UserRole getRole() { return role; }
    public Timestamp getCreatedAt() { return createdAt; }

    // --- Các hàm Setters ---
    public void setUser_id(String user_id) { this.user_id = user_id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(UserRole role) { this.role = role; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}