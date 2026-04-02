package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerLauncher {
    private static final int PORT = 2222;

    // Sử dụng CachedThreadPool để tối ưu hóa việc tạo và tái sử dụng Thread
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();
    public static void main(String[] args) {
        //khoi tao AuctionServer (Singleton)
        AuctionServer auctionServer = AuctionServer.getInstance();

        System.out.println("[Server] Đang khởi động hệ thống đấu giá...");

        // Mở cổng chào ServiceSocket
        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("[Server] Đang lắng nghe tại cổng: "+PORT);
            System.out.println("[Server] Sẵn sàng đón tiếp các Bidder!");

            //vòng lặp vô tận để chấp nhận cacs kết nối
            while (true){
                // chờ và chấp nhận kếtnooisi từ clients
                Socket clientSocket = serverSocket.accept();
                System.out.println("[Server] Có kết nối mới từ: " + clientSocket.getInetAddress());

                //tạo ng phục vụ
                ClientHandler handler = new ClientHandler(clientSocket, auctionServer);

                // đưa khách vào ds quản lý chung
                auctionServer.addClient(handler);

                // giao việc cho threadpool
                threadPool.execute(handler);
            }


        }
        catch (IOException e){
            System.err.println("[Server] Lỗi không thể mở cổng " + PORT);
            e.printStackTrace();
        }
        finally {
            // đóng threadpool
            threadPool.shutdown();
        }

    }
}
