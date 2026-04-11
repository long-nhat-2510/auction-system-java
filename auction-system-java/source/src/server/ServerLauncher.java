package server;

public class ServerLauncher {

    // Config cổng ở đây
    private static final int PORT = 1234;

    public static void main(String[] args) {
        System.out.println("=== HỆ THỐNG ĐẤU GIÁ AUCTIONLIVE ===");
        System.out.println("Đang nạp cấu hình...");

        // Chỉ việc gọi ông Quản lý (AuctionServer) ra và bắt đầu làm việc
        AuctionServer server = new AuctionServer();
        server.start(PORT);
    }
}