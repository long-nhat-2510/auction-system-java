package client;

import packets.NetworkMessage;
import java.io.*;
import java.net.Socket;

public class ServerConnection {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public void connect(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connected to the auction server!");

            // Tạo một luồng riêng chỉ để lắng nghe Server gửi gì về
            new Thread(this::listenForMessages).start();

        } catch (IOException e) {
            System.err.println("Cannot connected to the auction server: " + e.getMessage());
        }
    }

    // Gửi gói tin lên Server
    public void sendRequest(NetworkMessage message) {
        if (out != null) {
            out.println(message.toJson());
        }
    }

    // Luồng liên tục nhận phản hồi từ Server
    private void listenForMessages() {
        try {
            String response;
            while ((response = in.readLine()) != null) {

                NetworkMessage msg = NetworkMessage.fromJson(response);
                if (msg == null) continue;

                // Cập nhật giao diện dựa trên thông báo từ Server
                switch (msg.getAction()) {
                    case WINNER_NOTIFICATION:
                        // Trong ServerConnection.java

                        payload.WinnerNotification winnerEvent = msg.getDataAs(payload.WinnerNotification.class);

                        System.out.println("\n=========================================");
                        System.out.println("🎉 [KẾT QUẢ ĐẤU GIÁ] Phiên ID: " + winnerEvent.getAuctionId() + " đã khép lại!");
                        System.out.println("🏆 NGƯỜI CHIẾN THẮNG: " + winnerEvent.getWinnerName());
                        System.out.println("💰 MỨC GIÁ CHỐT ĐƠN: " + winnerEvent.getWinningPrice() + " VNĐ");
                        System.out.println("=========================================\n");

                        System.out.print("👉 Lựa chọn của bạn: "); // In lại menu để người dùng gõ tiếp
                        break;

                    case AUCTION_UPDATE_EVENT:
                        // ép kiểu payload
                        payload.AuctionUpdateEvent updateEvent = msg.getDataAs(payload.AuctionUpdateEvent.class);

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
                        payload.CountdownTickEvent tickEvent = msg.getDataAs(payload.CountdownTickEvent.class);
                        // Bỏ ký tự \r đi và dùng println thay vì print
                        System.out.println("⏳ Thời gian còn lại: " + tickEvent.getSecondsLeft() + " giây");
                        break;

                    case NO_WINNER_NOTIFICATION:
                        payload.NoWinnerNotification noWinnerEvent = msg.getDataAs(payload.NoWinnerNotification.class);
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