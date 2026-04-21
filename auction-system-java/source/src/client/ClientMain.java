package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import client.utils.SceneManager;


public class ClientMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Giao cửa sổ cho SceneManager quản lý
        SceneManager.setPrimaryStage(primaryStage);

        // 2. Đặt tiêu đề cho cửa sổ
        primaryStage.setTitle("AuctionLive - Hệ thống đấu giá trực tuyến");
        primaryStage.setResizable(false); // Khóa phóng to thu nhỏ (tuỳ chọn)

        // 3. Mở màn hình đầu tiên (Login)
        // LƯU Ý: Đường dẫn bắt đầu bằng dấu "/" trỏ từ thư mục resources hoặc thư mục gốc
        SceneManager.switchScene("/client/view/ConnectView.fxml");
    }

    public static void main(String[] args) {
        // Hàm launch() của JavaFX sẽ tự động khởi tạo hệ thống đồ họa và gọi hàm start() ở trên
        launch(args);
    }
}