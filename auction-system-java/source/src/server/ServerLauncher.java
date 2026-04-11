package server;

import com.google.gson.Gson;
import packets.NetworkMessage;
import packets.RequestType;
import payload.AuctionIdRequest;

import CommonClasses.AuctionEntity;
import CommonClasses.AuctionItem;
import packets.NetworkMessage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerLauncher {

    private static final int PORT = 1234;
    // Lưu danh sách tất cả các Client đang kết nối
    public static final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    // Kho chứa các phiên đấu giá (Dùng ConcurrentHashMap để an toàn với đa luồng)
    public static final Map<Integer, AuctionEntity> auctions = new ConcurrentHashMap<>();


    public static void main(String[] args) {
        System.out.println("🚀 Server starting...");
        //tạo 1 phiên đấu giá ảo để test
        AuctionItem item1 = new AuctionItem("A01", "Áo trinh sát đoàn", "Hàng real limited", "Eren Yeager", "Quần áo", 500000);
        //AuctionItem item2 = new AuctionItem("A02", "Áo CR7", "Có chữ ký của Ronaldo", "Cristiano Ronaldo", "Quần áo", 1000000);
        AuctionEntity auction1 = new AuctionEntity(1, item1, 600000, System.currentTimeMillis(), System.currentTimeMillis() + 120000);
        auctions.put(auction1.getAuctionId(), auction1);

        //Bật công tắc đồng hồ
        Thread timerThread = new Thread(new AuctionTimer());
        timerThread.start();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("🔌 Client connected" + socket.getInetAddress());

                ClientHandler handler = new ClientHandler(socket);
                clients.add(handler); // thêm khách vào danh sách
                new Thread(handler).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Tính năng broadcast(thông báo cho tất cả các Bidder)
    public static void broadcast(NetworkMessage message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
}