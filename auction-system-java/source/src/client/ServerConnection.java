package client;

import packets.NetworkMessage;
import java.io.*;
import java.net.Socket;

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

    // --------------------------------------------------------
    // GIỮ NGUYÊN HÀM listenForMessages()
    // --------------------------------------------------------
    private void listenForMessages() {
        // ... code cũ của bạn ...
    }
}