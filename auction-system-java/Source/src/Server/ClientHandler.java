package Server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private AuctionServer server;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName = "Anonymous"; //ten client sau khi log in

    public ClientHandler(Socket socket, AuctionServer server){
        this.socket = socket;
        this.server = server;

    }

    @Override
    public void run(){
        try {
            // thiết lập luồng vào/ra dữ liệu
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            String inputLine;

            // cập nhật tin nhắn từ clients
            inputLine = in.readLine();
            while (inputLine != null) {
                System.out.println("[ClientHandler] Received: " + inputLine);
                handleProtocol(inputLine);
                inputLine = in.readLine();
            }
        }
        catch (IOException e){
            System.out.println("[ClientHandler] Connection error with client: " + e.getMessage());
        }
        finally {
            //dọn dẹp khi clients ngat ket noi
            closeConnection();
        }
    }
    private void handleProtocol(String request){
        if (request == null || request.trim().isEmpty()) return;
        try{
            String[] parts = request.split("\\|");
            if (parts.length<1){
                return;
            }
            String command = parts[0];

            switch (command){
                case "LOGIN":
                    if (parts.length<2){
                        sendMessage("ERROR|Thiếu tên đăng nhập!");
                        return;
                    }
                    this.clientName = parts[1];
                    sendMessage("LOGIN_SUCCESS|Welcome " + clientName);
                    break;

                case "BID": //Định dạng: BID|ID_San_Pham|Gia_Thau

                    if (parts.length<3){
                        sendMessage("ERROR| Dữ liệu đặt giá không đầy đủ!");
                        return;
                    }
                    // Kiểm tra xem đã Login chưa trước khi cho Bid
                    if (this.clientName.equals("Anonymous")) {
                        sendMessage("ERROR|Bạn cần đăng nhập trước khi đấu giá!");
                        return;
                    }
                    int itemId = Integer.parseInt(parts[1]);
                    double bidAmount = Double.parseDouble(parts[2]);

                    //gọi sang AuctionServer để xử lý
                    boolean success = server.processBid(itemId, clientName, bidAmount);

                    if (!success){
                        sendMessage("BID_FAILED|Giá không hợp lệ hoặc phiên đã đóng!");
                    }
                    break;
                // nếu muốn thêm chức năng gì thì viết vào đây
                default:
                    sendMessage("ERROR|" + command + " không tồn tại!");
            }

        }
        catch (NumberFormatException e){
            sendMessage("ERROR|ID sản phẩm hoặc Giá tiền phải là con số!");
        }
        catch (Exception e){
            sendMessage("ERROR|Dữ liệu gửi lên sai định dạng!");
        }

    }
    //gửi tin nhắn cho clients
    public void sendMessage(String message) {
        if (out != null){
            out.println(message);
        }
    }
    //dọn dẹp
    private void closeConnection(){
        try{
            server.removeClient(this);
            if (in!=null){
                in.close();
            }
            if (out != null){
                out.close();
            }
            if (socket != null){
                socket.close();
                System.out.println("[ClientHandler] Resources cleaned up for: " + clientName);
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
