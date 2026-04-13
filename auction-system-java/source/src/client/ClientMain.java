package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Nạp file giao diện ConnectView.fxml từ thư mục view
        Parent root = FXMLLoader.load(getClass().getResource("/client/view/ConnectView.fxml"));

        // Tạo khung cảnh (Scene) chứa giao diện vừa nạp
        Scene scene = new Scene(root);

        // Thiết lập các thuộc tính cho cửa sổ (Stage)
        primaryStage.setTitle("AuctionLive - Hệ thống Đấu giá");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Khóa không cho người dùng kéo dãn cửa sổ

        // Hiển thị cửa sổ lên màn hình
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Hàm launch() của JavaFX sẽ tự động khởi tạo hệ thống đồ họa và gọi hàm start() ở trên
        launch(args);
    }
}