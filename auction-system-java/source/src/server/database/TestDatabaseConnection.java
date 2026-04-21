package server.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        try {
            DatabaseConnection db = DatabaseConnection.getInstance();
            try (Connection conn = db.getConnection()) {
                System.out.println("Kết nối DB thành công: " + (conn != null && !conn.isClosed()));

                try (Statement stmt = conn.createStatement()) {
                    try (ResultSet rs = stmt.executeQuery("SELECT 1")) {
                        if (rs.next()) {
                            System.out.println("Truy vấn thử thành công, SELECT 1 = " + rs.getInt(1));
                        } else {
                            System.out.println("Truy vấn thử không trả về dòng nào.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối hoặc truy vấn DB: " + e.getMessage());
            e.printStackTrace();
        } catch (RuntimeException e) {
            System.err.println("Lỗi cấu hình hoặc driver: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
