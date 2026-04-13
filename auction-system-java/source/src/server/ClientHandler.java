package server;

import packets.NetworkMessage;
import packets.RequestType;
import payload.request.AuctionIdRequest;
import CommonClasses.AuctionEntity;
import payload.request.PlaceBidRequest;
import payload.response.AuctionUpdateEvent;
import CommonClasses.User;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private CommonClasses.User loggedInUser;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("❌ Lỗi luồng I/O khi Client kết nối.");
        }
    }

    @Override
    public void run() {
        try {
            String jsonReceived;
            // Liên tục lắng nghe tin nhắn từ Client gửi lên
            while ((jsonReceived = in.readLine()) != null) {

                NetworkMessage msg = NetworkMessage.fromJson(jsonReceived);
                if (msg == null) continue;

                // Xử lý logic dựa trên Enum RequestType
                switch (msg.getAction()) {
                    case LOGIN_REQUEST:
                        // 1. BÓC HỘP: Lấy tờ khai của khách
                        payload.request.LoginRequest req = msg.getDataAs(payload.request.LoginRequest.class);
                        System.out.println("🔐 Đang kiểm tra tài khoản: " + req.getName());

                        // 2. NHỜ ÔNG QUẢN LÝ CHECK (Gọi hàm ở AuctionServer)
                        CommonClasses.User validUser = server.AuctionServer.authenticate(req.getName(), req.getPassword());

                        // 3. ĐÓNG GÓI TRẢ LỜI
                        if (validUser != null) {
                            // Lưu lại CCCD vào túi áo của anh bồi bàn này để dùng về sau
                            this.loggedInUser = validUser;

                            // Tạo gói thành công, nhét kèm cái CCCD (validUser) gửi về cho khách
                            payload.response.LoginResponse res = new payload.response.LoginResponse(true, "Đăng nhập thành công!", validUser);
                            sendMessage(new packets.NetworkMessage(packets.RequestType.LOGIN_RESPONSE, res));
                            System.out.println("✅ Khách hàng " + validUser.getUsername() + " đã vào mạng!");

                        } else {
                            // Tạo gói thất bại
                            payload.response.LoginResponse res = new payload.response.LoginResponse(false, "Sai tài khoản hoặc mật khẩu!", null);
                            sendMessage(new packets.NetworkMessage(packets.RequestType.LOGIN_RESPONSE, res));
                            System.out.println("❌ " + req.getName() + " đăng nhập thất bại.");
                        }
                        break;


                    case PLACE_BID_REQUEST:
                        PlaceBidRequest bidReq = msg.getDataAs(PlaceBidRequest.class);

                        if (bidReq == null) {
                            sendMessage(new NetworkMessage(RequestType.ERROR_RESPONSE, "Dữ liệu gói tin bị lỗi!"));
                            break;
                        }

                        AuctionEntity auction = AuctionServer.auctions.get(bidReq.getAuctionId());

                        if (auction == null) {
                            sendMessage(new NetworkMessage(RequestType.ERROR_RESPONSE, "Phiên đấu giá không tồn tại!"));
                            break;
                        }

                        // Thực hiện logic đặt giá
                        boolean success = auction.placeBid(bidReq.getBidderName(), bidReq.getBidAmount());

                        if (success) {
                            System.out.println("🔥 " + bidReq.getBidderName() + " đặt giá: " + bidReq.getBidAmount());

                            AuctionUpdateEvent updateEvent = new AuctionUpdateEvent(
                                    auction.getAuctionId(),
                                    auction.getCurrentPrice(),
                                    auction.getHighestBidder()
                            );

                            // Bắn thông báo cập nhật giá cho TẤT CẢ mọi người trong phòng
                            AuctionServer.broadcast(new NetworkMessage(RequestType.AUCTION_UPDATE_EVENT, updateEvent));
                        } else {
                            sendMessage(new NetworkMessage(RequestType.ERROR_RESPONSE, "❌ Giá phải cao hơn giá hiện tại hoặc phiên đã đóng!"));
                        }
                        break;

                    case REGISTER_CLIENT_REQUEST:
                        System.out.println("📝 Có người muốn đăng ký vào phòng!");
                        // TODO: Lấy payload Username/Password ra và xử lý
                        break;

                    case AUCTION_ID_REQUEST:
                        System.out.println("🔍 Nhận yêu cầu tìm kiếm ID");
                        AuctionIdRequest request = msg.getDataAs(AuctionIdRequest.class);
                        int id = request.getAuctionId();
                        System.out.println("Auction Id cần tìm: " + id);

                        NetworkMessage response = new NetworkMessage(RequestType.AUCTION_DATA_RESPONSE, "Server received id: " + id);
                        sendMessage(response);
                        break;

                    // Các case khác chưa xử lý...
                    case UNREGISTER_CLIENT_REQUEST:
                    case CREATE_AUCTION_REQUEST:
                    case CANCEL_AUCTION_REQUEST:
                    case FINISH_AUCTION_REQUEST:
                    case HIGHEST_BID_REQUEST:
                        System.out.println("Tính năng " + msg.getAction() + " đang được xây dựng.");
                        break;

                    default:
                        System.out.println("⚠️ Hành động chưa được hỗ trợ: " + msg.getAction());
                }
            }
        } catch (IOException e) {
            // Khi Client tắt cửa sổ hoặc mất mạng, hàm readLine() sẽ ném ra Exception nhảy vào đây
            System.out.println("⚠️ Client " + socket.getInetAddress() + " đã ngắt kết nối đột ngột.");
        } finally {
            // QUAN TRỌNG: Dọn dẹp bộ nhớ và xóa Client khỏi danh sách
            AuctionServer.removeClient(this);
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