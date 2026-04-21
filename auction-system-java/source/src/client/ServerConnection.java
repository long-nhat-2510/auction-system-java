package client;

import javafx.scene.control.*;
import packets.NetworkMessage;
import java.io.*;
import java.net.Socket;
import java.util.Optional;

import payload.response.LoginResponse;

public class ServerConnection {

    // 1. Khai báo biến static chứa instance duy nhất
    private static ServerConnection instance;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    // 2. Chặn không cho tạo object mới bừa bãi bằng 'new'
    private ServerConnection() {}

    // 3. ĐÂY CHÍNH LÀ HÀM ĐANG THIẾU: Cung cấp instance dùng chung
    public static ServerConnection getInstance() {
        if (instance == null) {
            instance = new ServerConnection();
        }
        return instance;
    }

    // 4. Hàm connect trả về boolean (true nếu thành công, false nếu lỗi)
    public boolean connect(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connected to the auction server!");

            // Vẫn giữ luồng lắng nghe
            new Thread(this::listenForMessages).start();
            return true; // Kết nối thành công

        } catch (IOException e) {
            System.err.println("Cannot connect to the auction server: " + e.getMessage());
            return false; // Kết nối thất bại
        }
    }

    public void sendRequest(NetworkMessage message) {
        if (out != null) {
            out.println(message.toJson());
        } else {
            System.out.println("Lỗi: luồng out(PrintWriter) đang bị null, mất kết nối!");
        }
    }

    private void listenForMessages() {
        try {
            String response;
            while ((response = in.readLine()) != null) {

                NetworkMessage msg = NetworkMessage.fromJson(response);
                if (msg == null) continue;

                // Cập nhật giao diện dựa trên thông báo từ Server
                switch (msg.getAction()) {
                    // 1. Bóc kết quả từ Server
                    case LOGIN_RESPONSE:
                        payload.response.LoginResponse res = msg.getDataAs(payload.response.LoginResponse.class);

                        // 2. Xin phép luồng UI (Platform.runLater) để thay đổi giao diện
                        javafx.application.Platform.runLater(() -> {
                            if (res.isSuccess()) {
                                // Cất thẻ CCCD Server vừa cấp vào bộ nhớ để hiện lên góc màn hình
                                CommonClasses.User myProfile = res.getUserProfile();
                                System.out.println("🎉 CHÀO MỪNG: " + myProfile.getUsername());

                                client.utils.SceneManager.switchScene("/client/view/LobbyView.fxml");

                            } else {
                                // Nếu sai mật khẩu, hiện bảng thông báo lỗi cho khách
                                showErrorAlert("Đăng nhập thất bại", res.getMessage());
                                // Cho ConnectController tự động hiện lỗi và mở khóa nút bấm
                                if (client.controller.ConnectController.getInstance() != null) {
                                    client.controller.ConnectController.getInstance().onLoginFailed(res.getMessage());
                                }
                            }
                        });
                        break;




                    case WINNER_NOTIFICATION:
                        payload.response.WinnerNotification winnerEvent = msg.getDataAs(payload.response.WinnerNotification.class);

                        // 1. Vẫn giữ lại dòng in ra Console để dễ debug
                        System.out.println("🎉 [KẾT QUẢ ĐẤU GIÁ] Phiên ID: " + winnerEvent.getAuctionId() + " đã khép lại!");

                        // 2. Bắn Popup thông báo trên giao diện JavaFX
                        javafx.application.Platform.runLater(() -> {
                            String formattedPrice = String.format("%,.0f", winnerEvent.getWinningPrice());

                            showInfoAlert("Kết quả đấu giá",
                                    "Phiên đấu giá #" + winnerEvent.getAuctionId() + " đã kết thúc!\n\n" +
                                    "Người chiến thắng: " + winnerEvent.getWinnerName() + "\n" +
                                    "Mức giá cuối cùng: " + formattedPrice + " VNĐ\n\n"
                                    );
                        });
                        break;

                    case AUCTION_UPDATE_EVENT:
                        // 1. Ép kiểu payload
                        payload.response.AuctionUpdateEvent updateEvent = msg.getDataAs(payload.response.AuctionUpdateEvent.class);

                        // 2. Bắt buộc phải xin phép luồng UI
                        javafx.application.Platform.runLater(() -> {
                            // Tạm thời in ra Console để check
                            System.out.println("🔥 [HOT] Phiên #" + updateEvent.getAuctionId() +
                                    " | Dẫn đầu: " + updateEvent.getHighestBidder() +
                                    " | Giá mới: " + updateEvent.getCurrentBid());

                            // TODO: Sau này có giao diện Chi tiết đấu giá, bạn sẽ gọi lệnh update text ở đây
                            // Ví dụ: lblHighestBidder.setText(updateEvent.getHighestBidder());
                            //        lblCurrentPrice.setText(String.valueOf(updateEvent.getCurrentBid()));
                        });
                        break;
                    case CREATE_ACCOUNT_RESPONSE:
                        // 1. Bóc hộp kết quả Server trả về
                        payload.response.CreateAccountResponse regRes = msg.getDataAs(payload.response.CreateAccountResponse.class);

                        // 2. Xin phép luồng UI để cập nhật giao diện
                        javafx.application.Platform.runLater(() -> {
                            if (regRes.isSuccess()) {
                                // Gọi hàm showInfoAlert
                                // (Trong hàm showSuccessAlert đã có sẵn logic chuyển màn hình nếu người dùng bấm "Tiếp tục" hoặc "Đăng nhập")
                                showInfoAlert("Đăng ký thành công", regRes.getMessage());
                            } else {
                                // Hiện bảng báo lỗi (Màu đỏ)
                                showErrorAlert("Đăng ký thất bại", regRes.getMessage());

                                // Mở khóa lại nút Đăng ký để người dùng có thể sửa thông tin và bấm lại
                                // (Nếu ở RegisterController bạn chưa có hàm mở khóa thì có thể bỏ qua dòng này)
                            }
                        });
                        break;
                    case REGISTRATION_CONFIRMATION:
                        //
                        break;

                    case CANCEL_CONFIRMATION:
                        //
                        break;
                    case BID_CONFIRMATION:
                        //
                        break;
                    case AUCTION_LIST_RESPONSE:
                        //
                        break;
                    case AUCTION_DATA_RESPONSE:
                        //
                        break;
                    case HIGHEST_BID_RESPONSE:
                        //
                        break;
                    case ERROR_RESPONSE:

                        // Giả sử Server gửi String báo lỗi:
                        String errorMsg = msg.getDataAs(String.class);
                        System.out.println("\n❌ [TỪ CHỐI]: " + errorMsg);
                        javafx.application.Platform.runLater(() -> {
                            showErrorAlert("Lỗi hệ thống", errorMsg);
                        });
                        break;
                    case COUNTDOWN_START_EVENT:
                        //
                        break;
                    case COUNTDOWN_TICK_EVENT:
                        payload.response.CountdownTickEvent tickEvent = msg.getDataAs(payload.response.CountdownTickEvent.class);
                        // Bỏ ký tự \r đi và dùng println thay vì print
                        System.out.println("⏳ Thời gian còn lại: " + tickEvent.getSecondsLeft() + " giây");
                        break;

                    case NO_WINNER_NOTIFICATION:
                        payload.response.NoWinnerNotification noWinnerEvent = msg.getDataAs(payload.response.NoWinnerNotification.class);
                        System.out.println("\n🔔 [Thông báo] Phiên đấu giá mã " + noWinnerEvent.getAuctionId() + " đã kết thúc mà KHÔNG CÓ AI mua (Ế)!");
                        System.out.print("👉 Lựa chọn của bạn: "); // In lại menu dở dang
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Lost connection to the server.");
        }
    }
    private void showErrorAlert(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        // 1. Tự tạo một nhãn (Label) chứa câu thông báo để dễ ép màu
        javafx.scene.control.Label contentLabel = new javafx.scene.control.Label(content);
        // Ép màu chữ xanh ngọc (khớp với nút bấm) và chỉnh size chữ to rõ ràng hơn
        contentLabel.setStyle("-fx-text-fill: #00B981; -fx-font-size: 15px; -fx-font-weight: bold;");
        // Nhét cái nhãn này vào làm nội dung của Alert
        alert.getDialogPane().setContent(contentLabel);
        alert.getDialogPane().setStyle("-fx-background-color: #111C2D;");
        alert.getDialogPane().setGraphic(null);
        alert.show(); // Dùng show() thay vì showAndWait() để không treo luồng
    }


    private void showInfoAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);

        // 1. Ép màu nền cho hộp thoại (Màu xanh đen giống app)
        alert.getDialogPane().setStyle("-fx-background-color: #111C2D;");
        alert.getDialogPane().setGraphic(null); // Bỏ đi cái icon (i) mặc định cho đỡ quê

        // 2. Tùy chỉnh chữ nội dung (Màu trắng, chữ to rõ)
        Label contentLabel = new Label(content);
        contentLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-wrap-text: true;");
        alert.getDialogPane().setContent(contentLabel);

        // 3. Xóa các nút mặc định và tạo 2 nút mới theo Option B
        ButtonType btnContinue = new ButtonType("Tiếp tục", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnLoginOther = new ButtonType("Đăng nhập tài khoản khác", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnContinue, btnLoginOther);

        // 4. Ép CSS cho NÚT CHÍNH (Tiếp tục) - Nền xanh ngọc, chữ trắng
        Button nodeContinue = (Button) alert.getDialogPane().lookupButton(btnContinue);
        if (nodeContinue != null) {
            nodeContinue.setStyle(
                    "-fx-background-color: #00B981; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-cursor: hand; " +
                            "-fx-background-radius: 5px; " +
                            "-fx-padding: 8px 15px;"
            );
        }

        // 5. Ép CSS cho NÚT PHỤ (Đăng nhập khác) - Nền trong suốt, viền xanh nhạt
        Button nodeLoginOther = (Button) alert.getDialogPane().lookupButton(btnLoginOther);
        if (nodeLoginOther != null) {
            nodeLoginOther.setStyle(
                    "-fx-background-color: transparent; " +
                            "-fx-text-fill: #3498db; " +
                            "-fx-border-color: #3498db; " +
                            "-fx-border-radius: 5px; " +
                            "-fx-cursor: hand; " +
                            "-fx-padding: 8px 15px;"
            );
        }

        // 6. Hiển thị hộp thoại và chờ người dùng click
        Optional<ButtonType> result = alert.showAndWait();

        // 7. Xử lý hành động sau khi click
        if (result.isPresent()) {
            if (result.get() == btnContinue) {
                System.out.println("👉 Người dùng chọn: Tiếp tục (Vào thẳng sảnh đợi)");
                client.utils.SceneManager.switchScene("/client/view/LobbyView.fxml");

            } else if (result.get() == btnLoginOther) {
                System.out.println("👉 Người dùng chọn: Đăng nhập tài khoản khác");
                client.utils.SceneManager.switchScene("/client/view/ConnectView.fxml");
            }
        }
    }

}