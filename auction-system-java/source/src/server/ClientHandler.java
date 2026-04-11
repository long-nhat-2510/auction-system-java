package server;

import packets.NetworkMessage;
import packets.RequestType;
import payload.AuctionIdRequest;
import CommonClasses.AuctionEntity;


import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            // Khởi tạo luồng đọc/ghi dữ liệu với Client này
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String jsonReceived;
            // Liên tục lắng nghe tin nhắn từ Client gửi lên
            while ((jsonReceived = in.readLine()) != null) {

                // Dịch chuỗi JSON thành đối tượng NetworkMessage
                NetworkMessage msg = NetworkMessage.fromJson(jsonReceived);
                if (msg == null) continue;

                // Xử lý logic dựa trên Enum RequestType
                switch (msg.getAction()) {
                    case PLACE_BID_REQUEST:
                        payload.PlaceBidRequest bidReq = msg.getDataAs(payload.PlaceBidRequest.class);

                        if (bidReq == null) {
                            System.out.println("❌ Payload lỗi!");
                            break;
                        }

                        AuctionEntity auction = ServerLauncher.auctions.get(bidReq.getAuctionId());

                        if (auction == null) {
                            sendMessage(new NetworkMessage(RequestType.ERROR_RESPONSE, "Auction không tồn tại"));
                            break;
                        }

                        boolean success = auction.placeBid(
                                bidReq.getBidderName(),
                                bidReq.getBidAmount()
                        );

                        if (success) {
                            System.out.println("🔥 " + bidReq.getBidderName() +
                                    " đặt giá: " + bidReq.getBidAmount());

                            payload.AuctionUpdateEvent updateEvent =
                                    new payload.AuctionUpdateEvent(
                                            auction.getAuctionId(),
                                            auction.getCurrentPrice(),
                                            auction.getHighestBidder()
                                    );

                            NetworkMessage updateMsg =
                                    new NetworkMessage(RequestType.AUCTION_UPDATE_EVENT, updateEvent);

                            ServerLauncher.broadcast(updateMsg);

                        } else {
                            sendMessage(new NetworkMessage(
                                    RequestType.ERROR_RESPONSE,
                                    "❌ Giá phải cao hơn giá hiện tại hoặc auction đã đóng"
                            ));
                        }
                        break;

                    case REGISTER_CLIENT_REQUEST:
                        System.out.println("Có người muốn đăng ký vào phòng!");
                        break;
                    case UNREGISTER_CLIENT_REQUEST:
                        //
                        break;
                    case CREATE_AUCTION_REQUEST:
                        //
                        break;
                    case CANCEL_AUCTION_REQUEST:
                        //
                        break;
                    case FINISH_AUCTION_REQUEST:
                        //
                        break;
                    case HIGHEST_BID_REQUEST:
                        //
                        break;
                    case AUCTION_ID_REQUEST:
                        System.out.println("Receive Id request");
                        AuctionIdRequest request = msg.getDataAs(AuctionIdRequest.class);
                        int id = request.getAuctionId();
                        System.out.println("Auction Id: " + id);
                        //Gửi phản hồi về client
                        NetworkMessage response = new NetworkMessage(RequestType.AUCTION_DATA_RESPONSE, "Server received id: " + id);
                        sendMessage(response);
                        break;


                    default:
                        System.out.println("Hành động chưa được hỗ trợ: " + msg.getAction());
                }
            }
        } catch (IOException e) {
            System.out.println("Client đã ngắt kết nối.");
        } finally {
            closeEverything();
        }
    }

    // Hàm dùng để gửi tin nhắn về lại cho đúng Client này
    public void sendMessage(NetworkMessage message) {
        if (out != null) {
            out.println(message.toJson());
        }
    }

    private void closeEverything() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}