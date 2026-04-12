package client;

import packets.NetworkMessage;
import java.io.*;
import java.net.Socket;
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

                                // TODO: Lệnh chuyển sang màn hình AuctionTableView
                                // Ví dụ: SceneManager.switchScene("AuctionView.fxml");

                            } else {
                                // Nếu sai mật khẩu, hiện bảng thông báo lỗi cho khách
                                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                                alert.setTitle("Đăng nhập thất bại");
                                alert.setHeaderText(null);
                                alert.setContentText(res.getMessage());
                                alert.showAndWait();
                            }
                        });
                        break;




                    case WINNER_NOTIFICATION:
                        // Trong ServerConnection.java

                        payload.response.WinnerNotification winnerEvent = msg.getDataAs(payload.response.WinnerNotification.class);

                        System.out.println("\n=========================================");
                        System.out.println("🎉 [KẾT QUẢ ĐẤU GIÁ] Phiên ID: " + winnerEvent.getAuctionId() + " đã khép lại!");
                        System.out.println("🏆 NGƯỜI CHIẾN THẮNG: " + winnerEvent.getWinnerName());
                        System.out.println("💰 MỨC GIÁ CHỐT ĐƠN: " + winnerEvent.getWinningPrice() + " VNĐ");
                        System.out.println("=========================================\n");

                        System.out.print("👉 Lựa chọn của bạn: "); // In lại menu để người dùng gõ tiếp
                        break;

                    case AUCTION_UPDATE_EVENT:
                        // ép kiểu payload
                        payload.response.AuctionUpdateEvent updateEvent = msg.getDataAs(payload.response.AuctionUpdateEvent.class);

                        System.out.println("\n [News] Giá của mã đấu giá " + updateEvent.getAuctionId() + " vừa thay đổi!");
                        System.out.println(" Người dẫn đầu: " + updateEvent.getHighestBidder());
                        System.out.println(" Mức giá mới: " + updateEvent.getCurrentBid() + " VND");

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
                        // Lấy nội dung lỗi Server gửi về (Tùy thuộc bạn đang lưu dữ liệu dạng String hay ErrorPayload)
                        // Giả sử Server gửi String báo lỗi:
                        String errorMsg = msg.getDataAs(String.class);
                        System.out.println("\n❌ [TỪ CHỐI]: " + errorMsg);
                        System.out.print("👉 Lựa chọn của bạn: ");
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
}