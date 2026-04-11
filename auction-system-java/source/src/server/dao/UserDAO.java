package server.dao;

import server.database.DatabaseConnection;
import CommonClasses.User;
import CommonClasses.UserRole;
import java.sql.*;
import java.util.UUID; // Thư viện để sinh ID ngẫu nhiên

public class UserDAO {

    /**
     * Đăng ký người dùng mới (ID tự sinh trên Server)
     */
    public boolean register(User user, String password, UserRole role) throws SQLException {
        // 1. Tự sinh ID ngẫu nhiên (lấy 8 ký tự cho gọn)
        String uniqueID = UUID.randomUUID().toString().substring(0, 8);
        user.setUser_id(uniqueID);

        // 2. Câu lệnh SQL phải có user_id vì mình tự truyền vào
        String sql = "INSERT INTO auction_db.users (user_id, username, password, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUser_id());
            ps.setString(2, user.getUsername());
            ps.setString(3, password);
            ps.setString(4, role.toString());

            return ps.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Lỗi: Username đã tồn tại!");
            return false;
        }
    }

    /**
     * Xác thực đăng nhập
     */
    public User login(String username, String password) throws SQLException {
        // LỖI CŨ: Bạn SELECT thiếu role nhưng bên dưới lại dùng rs.getString("role")
        // FIX: SELECT đầy đủ các cột cần thiết
        String sql = "SELECT user_id, username, role FROM auction_db.users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Lấy dữ liệu ra
                    String id = rs.getString("user_id");
                    String name = rs.getString("username");
                    // Chuyển String từ DB thành Enum UserRole (viết hoa để khớp)
                    UserRole role = UserRole.valueOf(rs.getString("role").toUpperCase());

                    // Trả về đối tượng User (Dùng Constructor: ID, Name, Role)
                    return new User(id, name, role);
                }
            }
        }
        return null;
    }
}