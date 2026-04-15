package server.dao;

import CommonClasses.User;
import java.sql.*;
import server.database.DatabaseConnection;
import java.util.UUID;

public class UserDAO {

    // ==========================================
    // HÀM ĐĂNG KÝ
    // ==========================================
    public boolean register(User user, String password) {
        // 1. Tự sinh ID ngẫu nhiên (lấy 8 ký tự cho gọn)
        String uniqueID = UUID.randomUUID().toString().substring(0, 8);
        user.setUser_id(uniqueID);

        // 2. Câu lệnh SQL đã được dọn sạch, gọi đúng cột 'id', BỎ 'role'
        String sql = "INSERT INTO users (id, username, password, fullname, balance) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Truyền đủ 5 tham số cho 5 dấu hỏi chấm
            ps.setString(1, user.getUser_id());
            ps.setString(2, user.getUsername());
            ps.setString(3, password);
            ps.setString(4, user.getFullname()); // Gán tên hiển thị
            ps.setDouble(5, 0.0); // Tài khoản mới khởi tạo mặc định 0 đồng

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("❌ Lỗi: Username đã tồn tại!");
            return false;
        } catch (SQLException e) {
            System.err.println("❌ Lỗi Database khi đăng ký: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // HÀM ĐĂNG NHẬP
    // ==========================================
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        // Đã sửa lại thành getInstance().getConnection()
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("🔍 Đã tìm thấy user trong DB! Đang đóng gói dữ liệu...");

                // Lấy id ra dưới dạng Chuỗi (String)
                String id = rs.getString("id");
                String usernameDB = rs.getString("username");
                String passwordDB = rs.getString("password");
                String fullname = rs.getString("fullname");
                double balance = rs.getDouble("balance");

                // Trả về User với đầy đủ 5 thông số
                return new User(id, usernameDB, passwordDB, fullname, balance);
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi SQL khi login: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}