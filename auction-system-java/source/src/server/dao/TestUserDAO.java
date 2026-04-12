package server.dao;

import CommonClasses.User;
import CommonClasses.UserRole;
import java.sql.SQLException;

public class TestUserDAO {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();

        // 1. Test tính năng Đăng ký (Register)
        System.out.println("--- Đang test Đăng ký ---");
        // Tạo đối tượng User để test (ID sẽ tự sinh trong hàm register của bạn)
        User newUser = new User("ninhbinh_user", "Mai", "1234", UserRole.BIDDER, 10000000);

        boolean isRegistered = userDAO.register(newUser, "password123", UserRole.BIDDER);

        if (isRegistered) {
            System.out.println("✅ Đăng ký thành công! ID mới sinh: " + newUser.getUser_id());
        } else {
            System.out.println("❌ Đăng ký thất bại (có thể trùng username).");
        }

        // 2. Test tính năng Đăng nhập (Login)
        System.out.println("\n--- Đang test Đăng nhập ---");
        User loggedInUser = userDAO.login("ninhbinh_user", "password123");

        if (loggedInUser != null) {
            System.out.println("✅ Đăng nhập thành công!");
            System.out.println("Thông tin: ID=" + loggedInUser.getUser_id() +
                    ", Name=" + loggedInUser.getUsername() +
                    ", Role=" + loggedInUser.getRole());
        } else {
            System.out.println("❌ Sai tài khoản hoặc mật khẩu.");
        }


    }
}