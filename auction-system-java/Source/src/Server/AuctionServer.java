package Server;

import CommonClasses.AuctionItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AuctionServer {
    public static AuctionServer instance;
    //ds cac clients dang ket noi
    private final List<ClientHandler> clients;
    private final List<AuctionItem> auctionItems;

    //constructor
    private AuctionServer(){ //ngăn khởi tạo tự do
        this.clients = new CopyOnWriteArrayList<>();
        this.auctionItems = new CopyOnWriteArrayList<>();
        loadInitialData();
        System.out.println("[Server] Auction System initialized successfully.");
    }

    //phương thức lấy thực thể duy nhất
    public static synchronized AuctionServer getInstance(){
        if (instance == null){
            instance = new AuctionServer();
        }
        return instance;
    }
    //Logic dau gia
    private void loadInitialData() {
        //goi DAO
    }
    public synchronized boolean processBid(int itemId, String bidderName, double bidAmount){
        for (AuctionItem item: auctionItems){
            if (item.getId() == itemId) {
                //kiem tra tinh hop le cua gia dau
                if (bidAmount > item.getCurrentPrice()){
                    //cap nhat thong tin moi
                    item.setCurrentPrice(bidAmount);

                    // inform
                    // Định dạng: UPDATE|ID_Sản_Phẩm|Giá_Mới|Tên_Người_Đấu
                    String updateMessage = "UPDATE_PRICE|" + item.getId() + "|" + bidAmount + "|" + bidderName;
                    broadcast(updateMessage);

                    System.out.println("[Bid] Success: " + bidderName + " bid $" + bidAmount + " on Item ID: " + itemId);
                    return true; // Đặt giá thành công

                }
                else{
                    System.out.println("[Bid] Rejected: Bid amount $" + bidAmount + " is too low for Item ID: " + itemId);
                    return false; // Giá quá thấp
                }
            }
        }
        return false; //khong tim thay san pham
    }


    //gui tin nhan toi toan bo clients
    public void broadcast(String message){
        for (ClientHandler client: clients){
            client.sendMessage(message);
        }
    }

    //Clients
    //them clients
    public void addClient(ClientHandler client){
        clients.add(client);
        System.out.println("[Server] New client connected. Total: " + clients.size());
    }

    // xoa client
    public void removeClient(ClientHandler client){
        clients.remove(client);
        System.out.println("[Server] Client disconnected. Total: " + clients.size());
    }




}
