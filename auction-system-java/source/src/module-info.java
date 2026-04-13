module auction.server {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.gson;
    requires java.desktop; // Thường là tên này, check lại với Alt+Enter nhé

    // 1. Cho phép JavaFX quét và nạp file FXML
    opens client.view to javafx.fxml;

    // 2. Cho phép JavaFX khởi chạy class ClientGUI (nằm trong package client)
    opens client to javafx.graphics, javafx.fxml;

    // 3. Cho phép Gson đọc các class chứa dữ liệu (để fix lỗi Server)
    opens payload to com.google.gson;
    opens packets to com.google.gson;

    // Nếu có class nào trong CommonClasses cần gửi qua mạng, mở luôn cho chắc
    opens CommonClasses to com.google.gson;

    exports client;
    exports server;
    // Cho phép JavaFX "thò tay" vào package client để điều khiển các Controller
}