package client;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Objects;

public class LoginController {
    // 1. Khai báo các linh kiện (fx:id) từ Scene Builder
    @FXML private TextField ipField;
    @FXML private TextField portField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label lblMessage;
    @FXML private Hyperlink forgotPasswordLink;

    // 2. Biến kết nối Server
    private ServerConnection connection = new ServerConnection();

    String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d).{6,}$";

    // 3. Hàm xử lý Đăng nhập
    @FXML
    public void handleLogin() {
        String ip = ipField.getText();
        String port = portField.getText();
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (user.isEmpty() || ip.isEmpty() || port.isEmpty() || pass.isEmpty()) {
            lblMessage.setText("Vui lòng điền đủ thông tin!");
            lblMessage.setStyle("-fx-text-fill: #ff7675;"); // Màu đỏ nhạt
        }
        else if (!pass.matches(passwordPattern)) {
            lblMessage.setText("Mật khẩu phải từ 6 ký tự, gồm cả chữ và số!");
            lblMessage.setStyle("-fx-text-fill: #fab1a0;"); // Màu cam cảnh báo
        } else {
            try {
                // Chuyển port từ String sang int và kết nối
                connection.connect(ip, Integer.parseInt(port));
                System.out.println("Đã kết nối server: " + ip);

                lblMessage.setText("Đang đăng nhập hệ thống...");
                lblMessage.setStyle("-fx-text-fill: #00ffd0;");
                switchToLobby();

                // Sau này Huy gọi lệnh gửi tin nhắn đăng ký/đăng nhập của Nhật ở đây nhé
            } catch (Exception e) {
                lblMessage.setText("Lỗi kết nối: " + e.getMessage());
                lblMessage.setStyle("-fx-text-fill: red;");
            }
        }
    }

    // 4. Hàm xử lý Quên mật khẩu
    @FXML
    public void handleForgotPassword() {
        System.out.println("Người dùng vừa nhấn Quên mật khẩu!");
        lblMessage.setText("Tính năng lấy lại mật khẩu đang được phát triển!");
        lblMessage.setStyle("-fx-text-fill: #e67e22;");
    }
    @FXML
        public void handleRegister(ActionEvent event) { // Phải có (ActionEvent event)
            try {
                // Tải giao diện Đăng ký
                // Thay cho dòng 75
                java.net.URL resource = getClass().getResource("view/RegisterAccount.fxml");
                if (resource == null) {
                    resource = getClass().getResource("/client/view/RegisterAccount.fxml");
                }
                Parent registerRoot = FXMLLoader.load(Objects.requireNonNull(resource));

                // Lấy cửa sổ hiện tại và đổi cảnh
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(registerRoot));
                stage.setTitle("Đăng ký tài khoản");
                stage.show();

            } catch (IOException e) {
                // Thông báo lỗi chung cho người dùng (không đích danh ai)
                System.err.println("Lỗi: Không tìm thấy file RegisterAccount.fxml");
                e.printStackTrace();
            }
            lblMessage.setStyle("-fx-text-fill: #3498db;");// Màu xanh dương cho dịu mắt
    }
    public void switchToLobby() {
        try {
            // 1. Nạp file giao diện sảnh chờ
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/view/LobbyView.fxml"));
            Parent root = loader.load();

            // 2. Lấy Stage hiện tại (cửa sổ đang hiện Login)
            Stage stage = (Stage) lblMessage.getScene().getWindow();

            // 3. Đặt giao diện mới lên Stage đó
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Sảnh Chờ Đấu Giá - Auction Live");
            stage.centerOnScreen(); // Căn giữa màn hình cho đẹp
            stage.show();

        } catch (IOException e) {
            lblMessage.setText("Không thể nạp màn hình sảnh chờ!");
            e.printStackTrace();
        }
    }

}