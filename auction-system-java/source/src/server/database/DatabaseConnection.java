package server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Biến lưu trữ Instance duy nhất
    private static DatabaseConnection instance;

    // Biến lưu trữ đường ống nối tới MySQL
    private Connection connection;

    // Sửa lại tên DB, User, Pass cho khớp với máy của bạn nhé
    private final String URL = "jdbc:mysql://127.0.0.1:3306/auction_db";
    private final String USER = "root";
    private final String PASSWORD = "123456";

    // 1. Khóa cửa (Private Constructor) không cho tạo object bừa bãi
    private DatabaseConnection() {
        try {
            // Tải tài xế MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Thiếu thư viện MySQL Connector: " + e.getMessage());
        }
    }

    // 2. Hàm lấy Instance (Chỉ tạo 1 lần duy nhất)
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // 3. Hàm lấy đường ống kết nối
    public Connection getConnection() {
        try {
            // Nếu ống nước chưa được mở, hoặc bị sập mạng thì mở lại
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("🗄️ Đã kết nối thành công tới MySQL Database!");
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi mở kết nối DB: " + e.getMessage());
        }
        return connection;
    }
}