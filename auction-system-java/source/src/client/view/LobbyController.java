package client.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LobbyController {
    @FXML
    private Label lblWelcome;

    @FXML
    public void initialize() {
        // Hàm này tự chạy khi màn hình Lobby hiện lên
        System.out.println("Đã vào Sảnh chờ!");
    }
}