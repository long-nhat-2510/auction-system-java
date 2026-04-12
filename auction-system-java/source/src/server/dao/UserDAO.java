package server.dao;

import server.database.DatabaseConnection;
import CommonClasses.User;
import CommonClasses.UserRole;
import java.sql.*;
import java.util.UUID;

public class UserDAO {

    /**
     * Đăng ký người dùng mới
     */
    public boolean register(User user, String password, UserRole role) {
        String uniqueID = "USR-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        user.setUser_id(uniqueID);

        String sql = "INSERT INTO users (user_id, username, password, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUser_id());
            ps.setString(2, user.getUsername());
            ps.setString(3, password);
            ps.setString(4, role.toString());

            return ps.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("❌ Lỗi: Username đã tồn tại trong Database!");
            return false;
        } catch (SQLException e) {
            System.err.println("❌ Lỗi SQL khi đăng ký: " + e.getMessage());
            return false;
        }
    }

    /**
     * Xác thực đăng nhập
     */
    public User login(String username, String password) {
        // Cần SELECT thêm password và balance (nếu có) để tạo object User hoàn chỉnh
        String sql = "SELECT user_id, username, password, role FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("user_id");
                    String name = rs.getString("username");
                    String pass = rs.getString("password");
                    UserRole role = UserRole.valueOf(rs.getString("role").toUpperCase());

                    // Trả về User (Tùy thuộc vào Constructor bên CommonClasses của bạn)
                    return new User(id, name, pass, role, 0.0); // Giả sử có thêm số dư
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi SQL khi login: " + e.getMessage());
        }
        return null;
    }
}