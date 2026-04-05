module auction.server { // Đổi tên module cho ngắn gọn, dễ quản lý
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    // Cho phép JavaFX truy cập vào package client của bạn
    opens client to javafx.fxml;
    exports client;

    // Nếu các class đấu giá nằm ở package khác, bạn phải exports nó ra
    exports CommonClasses;
    exports packets;
    requires java.sql;
    requires mysql.connector.j;
}