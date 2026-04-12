package client.controller;

import client.ServerConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

// Bổ sung import các gói tin
import packets.NetworkMessage;
import packets.RequestType;
import payload.request.LoginRequest;

public class ConnectController {

    // Khai báo các biến khớp với fx:id trong file FXML
    @FXML private TextField ipField;
    @FXML private TextField portField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button btnLogin;
    @FXML private Label errorLabel;
    @FXML private HBox loadingBox;

    @FXML
    public void initialize() {
        // Hàm này tự động chạy khi mở giao diện
        // Gắn sẵn localhost và 1234 để test cho nhanh đỡ phải gõ lại nhiều lần
        ipField.setText("localhost");
        portField.setText("1234");
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String ip = ipField.getText().trim();
        String portStr = portField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        errorLabel.setVisible(false);

        if (ip.isEmpty() || portStr.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showError("Vui lòng điền đầy đủ IP, Port, Tên đăng nhập và mật khẩu!");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            showError("Port bắt buộc phải là số!");
            return;
        }

        // Hiệu ứng UI: Tắt nút bấm, hiện vòng xoay loading
        btnLogin.setDisable(true);
        loadingBox.setVisible(true);

        // QUAN TRỌNG: Phải mở luồng (Thread) riêng để kết nối mạng, nếu không giao diện sẽ bị đơ/treo
        new Thread(() -> {
            // Gọi ServerConnection (đã được sửa thành Singleton)
            boolean isConnected = ServerConnection.getInstance().connect(ip, port);

            // Bất cứ khi nào muốn cập nhật lại UI từ luồng mạng, PHẢI dùng Platform.runLater
            Platform.runLater(() -> {
                if (isConnected) {

                    // 1. Tạo gói dữ liệu (Payload) chứa thông tin đăng nhập
                    LoginRequest loginReq = new LoginRequest(username, password);

                    // 2. Nhét vào phong bì và dán tem báo hiệu đây là gói Đăng nhập
                    NetworkMessage msg = new NetworkMessage(RequestType.LOGIN_REQUEST, loginReq);

                    // 3. Gọi anh Shipper ném qua mạng lên Server!
                    ServerConnection.getInstance().sendRequest(msg);

                    // 4. Thông báo cho người dùng biết là đang chờ máy chủ duyệt
                    errorLabel.setText("Đang chờ Server xác thực...");
                    errorLabel.setStyle("-fx-text-fill: #007BFF;"); // Màu xanh chờ đợi
                    errorLabel.setVisible(true);

                    // LƯU Ý: Không code chuyển màn hình ở đây nữa!
                    // Giao diện cứ để im chữ "Đang chờ..." và loading xoay xoay.
                    // Chuyển màn hình sẽ do hàm listenForMessages của ServerConnection quyết định.


                } else {
                    loadingBox.setVisible(false);
                    btnLogin.setDisable(false);
                    showError("Lỗi kết nối! Kiểm tra lại IP hoặc Server đã bật chưa.");
                }
            });
        }).start();
    }

    @FXML
    public void handleRegister(ActionEvent event) {
        // Xử lý khi bấm nút "Đăng ký ngay"
        System.out.println("Nút đăng ký được bấm!");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #ff4d4d;");
        errorLabel.setVisible(true);
    }
}
