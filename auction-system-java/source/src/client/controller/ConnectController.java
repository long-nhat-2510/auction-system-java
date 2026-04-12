package client.controller;

import client.service.ConnectionService;
import client.service.AuctionService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller cho ConnectView.fxml
 *
 * Trách nhiệm:
 *   Đọc input từ TextField
 *   Gọi connectionService.validateInputs()
 *   Gọi connectionService.connect()
 *   Cập nhật UI (error label, loading box)
 *   Điều hướng sang MainView khi thành công
 *
 *
 */
public class ConnectController implements Initializable {

    // ---- FXML ----
    @FXML private TextField ipField;
    @FXML private TextField portField;
    @FXML private TextField usernameField;
    @FXML private Button    btnConnect;
    @FXML private Label     errorLabel;
    @FXML private HBox      loadingBox;

    // ---- Service ----
    private final ConnectionService connectionService = new ConnectionService();

    // ---- Init ----

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        loadingBox.setVisible(false);
        loadingBox.setManaged(false);

        // Enter trên bất kỳ field nào đều trigger kết nối
        ipField.setOnAction(e -> handleConnect());
        portField.setOnAction(e -> handleConnect());
        usernameField.setOnAction(e -> handleConnect());
    }

    // ---- Action ----

    @FXML
    private void handleConnect() {
        String ip       = ipField.getText().trim();
        String portText = portField.getText().trim();
        String username = usernameField.getText().trim();

        // Validate — hoàn toàn trong service
        String validationError = connectionService.validateInputs(ip, portText, username);
        if (validationError != null) {
            showError(validationError);
            return;
        }

        hideError();
        setLoading(true);

        int port = Integer.parseInt(portText);

        // Kết nối — service lo thread riêng
        connectionService.connect(ip, port, username,
                conn -> Platform.runLater(() -> {
                    setLoading(false);
                    navigateToMain();
                }),
                errMsg -> Platform.runLater(() -> {
                    setLoading(false);
                    showError(errMsg);
                })
        );
    }

    // ---- Navigation ----

    private void navigateToMain() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/MainView.fxml"));
            Parent root = loader.load();

            MainController mainCtrl = loader.getController();
            AuctionService auctionService = new AuctionService(connectionService);
            mainCtrl.init(connectionService, auctionService);

            Stage stage = (Stage) btnConnect.getScene().getWindow();
            stage.setScene(new Scene(root, 960, 640));
            stage.setTitle("AuctionLive — " + connectionService.getCurrentUsername());
        } catch (Exception e) {
            showError("Không thể mở màn hình chính: " + e.getMessage());
        }
    }

    // ---- UI helpers (chỉ set visible/text, không có logic) ----

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void hideError() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    private void setLoading(boolean on) {
        btnConnect.setDisable(on);
        loadingBox.setVisible(on);
        loadingBox.setManaged(on);
    }
}
