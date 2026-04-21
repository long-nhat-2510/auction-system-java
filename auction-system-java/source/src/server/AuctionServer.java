package server;

import CommonClasses.AuctionEntity;
import CommonClasses.AuctionItem;
import CommonClasses.User;
import packets.NetworkMessage;
import payload.request.CreateAccountRequest;
import server.dao.UserDAO; // BỔ SUNG: Import ông Thủ kho tài khoản
import server.dao.AuctionDAO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuctionServer {

    // Kho chứa danh sách Client và các Phiên đấu giá
    public static final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    public static final Map<String, AuctionEntity> auctions = new ConcurrentHashMap<>();

    // Sử dụng ThreadPool tự co giãn để quản lý Client
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();

    // [KẾT NỐI DATABASE]: Triệu hồi các ông Thủ kho
    private static final UserDAO userDAO = new UserDAO();
    private static final AuctionDAO auctionDAO = new AuctionDAO();

    public void start(int port) {
        System.out.println("🚀 AuctionServer starting on port " + port + "...");

        // 1. Tải danh sách phiên đấu giá
        loadAuctions();

        // 2. Bật công tắc đồng hồ đếm ngược
//        Thread timerThread = new Thread(new AuctionTimer());
//        timerThread.start();

        // 3. Mở cửa đón khách
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("🔌 Client connected from: " + socket.getInetAddress());

                ClientHandler handler = new ClientHandler(socket);
                clients.add(handler);

                // Giao việc cho ThreadPool
                threadPool.execute(handler);
            }
        } catch (IOException e) {
            System.err.println("❌ Lỗi khởi tạo Server: " + e.getMessage());
        }
    }

    // Hàm lấy danh sách phiên đấu giá (Chuẩn bị sẵn cho Database)
    private void loadAuctions() {
        System.out.println("📦 Đang nạp dữ liệu phiên đấu giá...");

        // Chỉ việc gọi thẳng hàm, KHÔNG CẦN try-catch nữa vì AuctionDAO đã tự lo rồi!
        java.util.List<CommonClasses.AuctionEntity> dbAuctions = auctionDAO.getAllAuctions();

        // Đổ từ List vào cái Map của Server
        if (dbAuctions != null) {
            for (CommonClasses.AuctionEntity auction : dbAuctions) {
                auctions.put(auction.getAuctionId(), auction);
            }
        }
        System.out.println("✅ Đã tải " + auctions.size() + " phiên đấu giá từ DB.");

        // Tạm thời vẫn giữ 1 sản phẩm ảo để test chức năng đấu giá
        CommonClasses.AuctionItem item1 = new CommonClasses.AuctionItem("A01", "Áo trinh sát đoàn", "Hàng real limited", "Eren Yeager", "Quần áo", 500000);
        CommonClasses.AuctionEntity auction1 = new CommonClasses.AuctionEntity("1", item1, 600000, java.time.LocalDateTime.now(), java.time.LocalDateTime.now().plusMinutes(1));
        auctions.put(auction1.getAuctionId(), auction1);
    }

    // Tính năng broadcast (thông báo cho tất cả các Bidder)
    public static void broadcast(NetworkMessage message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    // Hàm xóa Client khỏi danh sách khi họ ngắt kết nối
    public static void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        System.out.println("📉 Một Client đã thoát. Tổng số user hiện tại: " + clients.size());
    }

    // ====================================================================
    // [KẾT NỐI DATABASE]: HÀM XÁC THỰC NGƯỜI DÙNG CHUẨN REAL
    // ====================================================================
    public static CommonClasses.User authenticate(String username, String password) {
        // AuctionServer không tự làm nữa, giao thẳng cho UserDAO quét MySQL
        return userDAO.login(username, password);
    }
    public static boolean registerNewUser(CreateAccountRequest req) {
        // Sau này nếu có logic kiểm tra gì thêm (ví dụ check tên có chứa từ cấm không) thì viết ở đây
        // ...
        User newUser = new User();
        newUser.setUsername(req.getUsername());
        newUser.setFullname(req.getName());
        UserDAO dao = new UserDAO();
        // Giao việc cho UserDAO xử lý lưu vào MySQL
        return dao.register(newUser, req.getPassword());
    }
}