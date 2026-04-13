package client.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import client.utils.SceneManager;
// Thêm các thư viện mạng của bạn nếu cần
// import packets.NetworkMessage;
// import packets.RequestType;
// import payload.request.RegisterRequest;

public class RegisterController {

    // ==========================================
    // 1. LIÊN KẾT GIAO DIỆN (Khớp với fx:id trong FXML)
    // ==========================================
    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtUsername;
    @FXML private TextField txtNumber;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private Label lblMessage;
    @FXML private Button btnRegister;
    @FXML private Hyperlink registerLink; // Nút "Đăng nhập" ở dưới cùng

    // ==========================================
    // 2. HÀM KHỞI TẠO
    // ==========================================
    @FXML
    public void initialize() {
        lblMessage.setText(""); // Xóa trắng câu thông báo lúc mới mở
    }

    // ==========================================
    // 3. XỬ LÝ NÚT: ĐĂNG KÝ
    // ==========================================
    @FXML
    void handleRegister(ActionEvent event) {
        // Lấy dữ liệu và xóa khoảng trắng
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String username = txtUsername.getText().trim();
        String phone = txtNumber.getText().trim();
        String password = txtPassword.getText().trim();
        String confirmPass = txtConfirmPassword.getText().trim();

        // 1. Kiểm tra rỗng
        if (name.isEmpty() || email.isEmpty() || username.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            showError("❌ Vui lòng điền đầy đủ các trường bắt buộc (*)");
            return;
        }

        // 2. Kiểm tra mật khẩu nhập lại có khớp không
        if (!password.equals(confirmPass)) {
            showError("❌ Mật khẩu xác nhận không khớp!");
            return;
        }

        // 3. (Tuỳ chọn) Kiểm tra độ dài mật khẩu
        if (password.length() < 6) {
            showError("❌ Mật khẩu phải có ít nhất 6 ký tự!");
            return;
        }

        // 4. Báo hiệu đang xử lý và khóa nút bấm
        lblMessage.setText("⏳ Đang gửi yêu cầu đăng ký...");
        lblMessage.setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;"); // Màu xanh dương
        btnRegister.setDisable(true);

        System.out.println("Chuẩn bị đăng ký tài khoản: " + username);

        // ========================================================
        // 5. GỬI GÓI TIN LÊN SERVER (Mở comment đoạn này ra khi ráp code)
        // ========================================================
        /*
        try {
            // Đóng gói dữ liệu
            RegisterRequest req = new RegisterRequest(name, email, username, phone, password);
            NetworkMessage msg = new NetworkMessage(RequestType.REGISTER_REQUEST, req);

            // Gửi qua ServerConnection
            client.ServerConnection.getInstance().sendMessage(msg);

            // Lưu ý: Không mở khóa nút (btnRegister.setDisable(false)) ở đây.
            // Hãy để hàm nhận phản hồi từ Server (LoginResponse/RegisterResponse) mở khóa hoặc chuyển màn hình.
        } catch (Exception e) {
            showError("❌ Lỗi kết nối Server!");
        }
        */
    }

    // ==========================================
    // 4. XỬ LÝ NÚT: QUAY LẠI ĐĂNG NHẬP
    // ==========================================
    @FXML
    void handleBackToLogin(ActionEvent event) {
        System.out.println("Quay lại màn hình Đăng nhập...");
        // TODO: Dùng FXMLLoader để load lại file ConnectView.fxml (Màn hình đăng nhập)
        SceneManager.switchScene("/client/view/ConnectView.fxml");
    }

    // ==========================================
    // HÀM PHỤ TRỢ: HIỂN THỊ LỖI
    // ==========================================
    private void showError(String message) {
        Platform.runLater(() -> {
            lblMessage.setText(message);
            lblMessage.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;"); // Màu đỏ
            btnRegister.setDisable(false); // Mở khóa cho khách bấm lại
        });
    }
}