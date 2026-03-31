package Server;

import CommonClasses.AuctionItem;
import CommonClasses.BidRecord;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AuctionServer {
    private static volatile AuctionServer instance;
    //ds cac clients dang ket noi
    private final List<ClientHandler> clients;
    private final List<AuctionItem> auctionItems;
    //danh sách lưu lịch sử để vẽ biểu đồ
    private final List<CommonClasses.BidRecord> bidHistory = new CopyOnWriteArrayList<>();
    //bộ đếm giờ hệ thống
    private final ScheduledExecutorService timerService = Executors.newScheduledThreadPool(1);

    //constructor
    private AuctionServer(){ //ngăn khởi tạo tự do
        this.clients = new CopyOnWriteArrayList<>();
        this.auctionItems = new CopyOnWriteArrayList<>();
        loadInitialData();
        System.out.println("[Server] Auction System initialized successfully.");
        startGlobalTimer();
    }
    //hàm quét thời gian tự động đóng phiên
    private void startGlobalTimer(){
        timerService.scheduleAtFixedRate(() -> {
            LocalDateTime now = LocalDateTime.now();
            for (AuctionItem item: auctionItems){
                if (item.getStatus().equals("RUNNING")&& now.isAfter(item.getEndTime())){
                    item.setStatus("FINISHED");
                    broadcast("AUCTION_FINISHED|" + item.getId() + "|" + item.getCurrentWinner());

                }
            }
        },0,1, TimeUnit.SECONDS);
    }

    //phương thức lấy thực thể duy nhất (Singleton pattern)
    public static AuctionServer getInstance(){
        if (instance == null){
            synchronized (AuctionServer.class) {
                if (instance == null) {
                    instance = new AuctionServer();
                }
            }
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
                //kiem tra phien dau con mo khong
                if (!item.getStatus().equals("RUNNING")){
                    return false;
                }

                //kiem tra tinh hop le cua gia dau
                if (bidAmount > item.getCurrentPrice()){
                    //Anti-snipping
                    long secondsLeft = Duration.between(LocalDateTime.now(),item.getEndTime()).getSeconds();
                    if (secondsLeft>0 && secondsLeft <30){
                        item.setEndTime(item.getEndTime().plusMinutes(1));
                        broadcast("TIME_EXTENDED|" + itemId + "|" + item.getEndTime());
                    }
                    //cap nhat thong tin moi
                    item.setCurrentPrice(bidAmount);
                    item.setCurrentWinner(bidderName);

                    // inform
                    // Định dạng: UPDATE|ID_Sản_Phẩm|Giá_Mới|Tên_Người_Đấu
                    String updateMessage = "UPDATE_PRICE|" + item.getId() + "|" + bidAmount + "|" + bidderName;
                    broadcast(updateMessage);

                    //lưu lịch sử
                    bidHistory.add(new BidRecord(bidHistory.size()+1,itemId, bidderName, bidAmount));

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
