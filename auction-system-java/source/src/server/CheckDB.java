package server; // Nhớ giữ nguyên package này của bạn

import java.sql.*;

public class CheckDB {
    public static void main(String[] args) {
        // NHỚ THAY mật khẩu chuẩn của Nhật vào đây
        String url = "jdbc:mysql://localhost:3306/auction_db";
        String user = "root";
        String password = "123456";

        // Dùng try-with-resources để tự động đóng kết nối
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("--- KẾT NỐI DATABASE THÀNH CÔNG ---");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM items");

            while (rs.next()) {
                System.out.println("Vật phẩm: " + rs.getString("item_name"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi rồi Nhật ơi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}