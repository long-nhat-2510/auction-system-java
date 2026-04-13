package client.controller;

import client.utils.SceneManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ConnectController {

    // ==========================================
    // 1. LIÊN KẾT GIAO DIỆN (Bắt buộc phải khớp fx:id)
    // ==========================================
    @FXML private TextField ipField;
    @FXML private TextField portField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Label lblMessage;
    @FXML private Button btnLogin;
    @FXML private Hyperlink forgotPasswordLink;
    @FXML private Hyperlink registerLink;

    // (Tuỳ chọn) Nếu bạn muốn code cả tính năng Lưu tài khoản thì vào FXML
    // đặt thêm fx:id="rememberMeCheckbox" cho cái CheckBox nhé.
    // @FXML private CheckBox rememberMeCheckbox;

    // ==========================================
    // 2. HÀM KHỞI TẠO (Chạy ngay khi mở màn hình)
    // ==========================================
    @FXML
    public void initialize() {
        // Điền sẵn localhost và 1234 cho tiện lúc test, đỡ phải gõ lại nhiều lần
        ipField.setText("localhost");
        portField.setText("1234");

        // Ẩn cái nhãn lỗi đi lúc mới mở lên
        errorLabel.setVisible(false);
    }

    // ==========================================
    // 3. XỬ LÝ SỰ KIỆN: NÚT ĐĂNG NHẬP
    // ==========================================
    @FXML
    void handleLogin(ActionEvent event) {
        // Lấy dữ liệu người dùng nhập và xóa khoảng trắng 2 đầu (.trim())
        String ip = ipField.getText().trim();
        String portStr = portField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // 1. Kiểm tra xem có bỏ trống ô nào không
        if (ip.isEmpty() || portStr.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showError("❌ Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        try {
            // Kiểm tra xem Port có phải là số không
            int port = Integer.parseInt(portStr);

            // 2. Giao diện báo đang xử lý
            errorLabel.setVisible(false); // Tắt báo lỗi cũ
            lblMessage.setText("⏳ Đang kết nối...");
            lblMessage.setStyle("-fx-text-fill: #3498db;"); // Đổi màu xanh dương

            // Khóa nút Đăng nhập lại để tránh khách bấm 2-3 lần liên tục
            btnLogin.setDisable(true);

            // 3. TIẾN HÀNH KẾT NỐI VÀ GỬI GÓI TIN ĐĂNG NHẬP (Lắp logic Mạng của bạn vào đây)
            System.out.println("Đang kết nối tới " + ip + ":" + port + " bằng tài khoản: " + username);

            /* TODO: BẠN BỎ COMMENT ĐOẠN NÀY VÀ ĐIỀN CODE CỦA BẠN VÀO NHÉ
             * * ServerConnection.getInstance().connect(ip, port);
             * * payload.request.LoginRequest req = new payload.request.LoginRequest(username, password);
             * packets.NetworkMessage msg = new packets.NetworkMessage(packets.RequestType.LOGIN_REQUEST, req);
             * * ServerConnection.getInstance().sendMessage(msg);
             */

            // Lưu ý: Việc mở khóa lại nút btnLogin.setDisable(false) sẽ được thực hiện
            // ở bên hàm lắng nghe luồng phản hồi từ Server trả về nhé!

        } catch (NumberFormatException e) {
            showError("❌ Port kết nối bắt buộc phải là số!");
        }
    }

    // ==========================================
    // 4. XỬ LÝ SỰ KIỆN: NÚT ĐĂNG KÝ
    // ==========================================
    @FXML
    void handleRegister(ActionEvent event) {
        System.out.println("Chuyển sang màn hình Đăng ký...");
        SceneManager.switchScene("/client/view/RegisterAccount.fxml");
    }

    // ==========================================
    // 5. XỬ LÝ SỰ KIỆN: QUÊN MẬT KHẨU
    // ==========================================
    @FXML
    void handleForgotPassword(ActionEvent event) {
        System.out.println("Mở popup Quên mật khẩu...");
        // TODO: Hiện một cái Alert (showInfoAlert) bảo là "Tính năng đang phát triển" chẳng hạn
    }

    // ==========================================
    // HÀM PHỤ TRỢ: HIỂN THỊ LỖI
    // ==========================================
    private void showError(String message) {
        // Hàm này bắt buộc phải chạy trên luồng giao diện (Platform.runLater)
        Platform.runLater(() -> {
            lblMessage.setText(""); // Xóa dòng "Đang kết nối..."
            errorLabel.setText(message);
            errorLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;"); // Màu đỏ
            errorLabel.setVisible(true);
            btnLogin.setDisable(false); // Mở khóa lại nút Đăng nhập
        });
    }
}