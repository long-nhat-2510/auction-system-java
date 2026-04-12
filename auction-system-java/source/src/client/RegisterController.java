package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class RegisterController {

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmPassword;
    @FXML
    private CheckBox cbAgree;
    @FXML
    private Label lblMessage;

    @FXML
    void handleRegister(ActionEvent event) {
        // Bước 1: Lấy dữ liệu từ các ô nhập
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String user = txtUsername.getText().trim();
        String pass = txtPassword.getText();
        String confirmPass = txtConfirmPassword.getText();

        // Bước 2: Kiểm tra xem có ô nào để trống không
        if (name.isEmpty() || email.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            lblMessage.setText("Vui lòng điền đầy đủ các thông tin có dấu *");
            lblMessage.setStyle("-fx-text-fill: #ff7675;");
            return;
        }

        // Bước 3: Kiểm tra định dạng Email (cơ bản)
        if (!email.contains("@") || !email.contains(".")) {
            lblMessage.setText("Lỗi!Email không đúng định dạng @");
            lblMessage.setStyle("-fx-text-fill: #ff7675;");
            return;
        }
        //Bước kiểm tra mức độ mật khẩu
        // Bước 4: Kiểm tra độ dài mật khẩu (>= 6 ký tự)
        if (pass.length() < 6) {
            lblMessage.setText("LỖI: Mật khẩu phải có ít nhất 6 ký tự!");
            lblMessage.setStyle("-fx-text-fill: #ff7675;");
            return;
        }

// Bước 4.1: Kiểm tra mật khẩu phải có cả chữ và số
// Regex này kiểm tra: Phải có ít nhất 1 chữ cái và ít nhất 1 con số
        if (!pass.matches("^(?=.*[a-zA-Z])(?=.*\\d).+$")) {
            lblMessage.setText("LỖI: Mật khẩu phải bao gồm chữ và số");
            lblMessage.setStyle("-fx-text-fill: #ff7675;");
            return;
        }

// Bước 4.2: Kiểm tra khớp mật khẩu nhập lại
        if (!pass.equals(confirmPass)) {
            lblMessage.setText("LỖI: Mật khẩu nhập lại không khớp");
            lblMessage.setStyle("-fx-text-fill: #ff7675;");
            return;
        }
        // Bước 5: Kiểm tra xem đã tích đồng ý điều khoản chưa
        if (!cbAgree.isSelected()) {
            lblMessage.setText("LỖI:Phải tích đồng ý với điều khoản dịch vụ!");
            lblMessage.setStyle("-fx-text-fill: #ff7675;");
            return;
        }

        // Bước 6: Nếu vượt qua hết thì Đăng ký thành công
        System.out.println("CHÚC MỪNG: Dữ liệu hợp lệ!");
        System.out.println("Đang gửi đăng ký cho User: " + user);

        // Chỗ này sau này sẽ gọi class Socket để gửi sang Server nhé

    }

    @FXML
    public void handleBackToLogin(ActionEvent event) {
        try {
            // Huy nhớ thêm "view/" nhé vì RegisterController ở ngoài, FXML ở trong folder view
            java.net.URL resource = getClass().getResource("view/ConnectView.fxml");
            if (resource == null) {
                resource = getClass().getResource("/client/view/ConnectView.fxml");
            }

            Parent loginRoot = FXMLLoader.load(Objects.requireNonNull(resource));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.show();
        } catch (Exception e) {
            System.err.println("Lỗi quay lại trang đăng nhập!");
            e.printStackTrace();
        }
    }
}