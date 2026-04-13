package client.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    // Lưu trữ "Sân khấu" chính của ứng dụng
    private static Stage primaryStage;

    // Hàm này được gọi 1 lần duy nhất lúc bật app để giao sân khấu cho Manager
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    // Tuyệt chiêu chuyển trang chỉ bằng 1 dòng code
    public static void switchScene(String fxmlPath) {
        try {
            // Đọc file FXML
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            // Tạo phông nền mới và treo lên sân khấu
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen(); // Căn giữa màn hình cho đẹp
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("❌ Không tìm thấy file FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }
}