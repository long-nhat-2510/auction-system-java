package server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Biến static duy nhất của lớp (Singleton)
    private static volatile DatabaseConnection instance;
    private static Connection connection;

    // Thông tin cấu hình (Nên để trong file .properties sau này)
    private String url = "jdbc:mysql://localhost:3306/auction_db";
    private String username = "root";
    private String password = "123456";

    // Constructor private để ngăn việc tạo đối tượng từ bên ngoài
    private DatabaseConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy MySQL Driver: " + e.getMessage());
        }
    }

    // Phương thức public để lấy instance duy nhất
    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            synchronized (DatabaseConnection.class){
                if (instance == null){
                    instance = new DatabaseConnection();
                }
            }

        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Hàm lấy kết nối
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Tải tài xế phiên dịch
                Class.forName("com.mysql.cj.jdbc.Driver");
                // Mở đường ống
                connection = DriverManager.getConnection(getInstance().url, getInstance().password, getInstance().password);
                System.out.println("🗄️ Đã kết nối thành công tới Database!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("❌ Lỗi kết nối Database: " + e.getMessage());
        }
        return connection;
    }

}
