package client.controller;

import client.ServerConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

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

        errorLabel.setVisible(false);

        if (ip.isEmpty() || portStr.isEmpty() || username.isEmpty()) {
            showError("Vui lòng điền đầy đủ IP, Port và Tên đăng nhập!");
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
            // Gọi ServerConnection (đã được sửa thành Singleton ở bước 3)
            boolean isConnected = ServerConnection.getInstance().connect(ip, port);

            // Bất cứ khi nào muốn cập nhật lại UI từ luồng mạng, PHẢI dùng Platform.runLater
            Platform.runLater(() -> {
                loadingBox.setVisible(false);
                btnLogin.setDisable(false);

                if (isConnected) {
                    errorLabel.setText("Kết nối thành công! Mở console để xem tiếp.");
                    errorLabel.setStyle("-fx-text-fill: #00ff04;");
                    errorLabel.setVisible(true);

                    // TODO: Gửi gói tin LOGIN/REGISTER (NetworkMessage) lên server ở đây
                    // ServerConnection.getInstance().sendRequest(...);

                    // TODO: Code chuyển sang màn hình đấu giá chính (AuctionView) ở đây
                } else {
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