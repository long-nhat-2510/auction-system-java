package server;

import CommonClasses.AuctionEntity;
import CommonClasses.AuctionItem;
import packets.NetworkMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuctionServer {

    // Kho chứa danh sách Client và các Phiên đấu giá
    public static final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    public static final Map<Integer, AuctionEntity> auctions = new ConcurrentHashMap<>();

    // [BẢN NÂNG CẤP]: Sử dụng ThreadPool tự co giãn để quản lý Client thay vì new Thread() bừa bãi
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();

    public void start(int port) {
        System.out.println("🚀 AuctionServer starting on port " + port + "...");

        // 1. Khởi tạo dữ liệu ảo (Sau này chuyển sang lấy từ Database)
        initMockData();

        // 2. Bật công tắc đồng hồ đếm ngược
        Thread timerThread = new Thread(new AuctionTimer());
        timerThread.start();

        // 3. Mở cửa đón khách
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("🔌 Client connected from: " + socket.getInetAddress());

                ClientHandler handler = new ClientHandler(socket);
                clients.add(handler);

                // Giao việc cho ThreadPool thay vì dùng: new Thread(handler).start();
                threadPool.execute(handler);
            }
        } catch (IOException e) {
            System.err.println("❌ Lỗi khởi tạo Server: " + e.getMessage());
        }
    }

    // Tách phần tạo dữ liệu ảo ra một hàm riêng cho sạch sẽ
    private void initMockData() {
        AuctionItem item1 = new AuctionItem("A01", "Áo trinh sát đoàn", "Hàng real limited", "Eren Yeager", "Quần áo", 500000);
        AuctionEntity auction1 = new AuctionEntity(1, item1, 600000, System.currentTimeMillis(), System.currentTimeMillis() + 120000);
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
}