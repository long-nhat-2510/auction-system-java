package CommonClasses;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class AuctionEntity {

    private String auctionId;
    private AuctionItem item;

    private double startingPrice;
    private double currentPrice;

    private String highestBidder;

    // Thay thế boolean active bằng Enum AuctionStatus
    private AuctionStatus status;

    // Thay thế long bằng LocalDateTime
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private List<BidRecord> bidHistory;

    // Constructor rỗng (bắt buộc cho Gson)
    public AuctionEntity() {
        this.bidHistory = new ArrayList<>();
        this.status = AuctionStatus.WAITING; // Trạng thái mặc định
    }

    // Constructor đầy đủ
    public AuctionEntity(String auctionId, AuctionItem item,
                         double startingPrice,
                         LocalDateTime startTime, LocalDateTime endTime) {

        this.auctionId = auctionId;
        this.item = item;
        this.startingPrice = startingPrice;
        this.currentPrice = startingPrice;

        this.startTime = startTime;
        this.endTime = endTime;

        this.bidHistory = new ArrayList<>();

        // Kiểm tra và gán trạng thái chuẩn ngay khi khởi tạo
        updateStatus();
    }

    // ================== LOGIC CHÍNH ==================

    public synchronized boolean placeBid(String bidder, double amount) {

        updateStatus(); // 🔥 luôn cập nhật trạng thái hiện tại trước khi xử lý bid

        // Chỉ cho phép bid nếu phiên đấu giá đang diễn ra
        if (this.status != AuctionStatus.RUNNING) return false;

        if (amount <= currentPrice) return false;

        currentPrice = amount;
        highestBidder = bidder;


        BidRecord bid = new BidRecord(
                "BID-" + LocalDate.now(),
                auctionId,
                bidder,
                amount,
                LocalDateTime.now()
        );

        bidHistory.add(bid);

        return true;
    }

    // Nâng cấp từ checkExpired: Kiểm tra và cập nhật trạng thái dựa vào thời gian thực
    public void updateStatus() {
        // Nếu đã bị đóng thủ công từ trước thì không thay đổi nữa
        if (this.status == AuctionStatus.FINISHED) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(startTime)) {
            this.status = AuctionStatus.WAITING;
        } else if (now.isAfter(endTime)) {
            this.status = AuctionStatus.FINISHED;
        } else {
            this.status = AuctionStatus.RUNNING;
        }
    }

    // Giữ lại hàm này để tương thích ngược nếu các class khác đang gọi
    public void checkExpired() {
        updateStatus();
    }

    // Đóng auction thủ công
    public void closeAuction() {
        this.status = AuctionStatus.FINISHED;
    }

    // Lấy bid cao nhất
    public BidRecord getHighestBid() {
        if (bidHistory.isEmpty()) return null;
        return bidHistory.get(bidHistory.size() - 1);
    }

    // ================== GETTER ==================

    public String getAuctionId() { return auctionId; }
    public AuctionItem getItem() { return item; }
    public double getStartingPrice() { return startingPrice; }
    public double getCurrentPrice() { return currentPrice; }
    public String getHighestBidder() { return highestBidder; }

    // Cập nhật Getter cho status
    public AuctionStatus getStatus() { return status; }

    public List<BidRecord> getBidHistory() { return bidHistory; }

    // Cập nhật Getter cho thời gian
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }

    // ================== DEBUG ==================

    @Override
    public String toString() {
        return "AuctionID: " + auctionId +
                " | Item: " + (item != null ? item.getName() : "Unknown") +
                " | Current Price: " + currentPrice +
                " | Highest Bidder: " + (highestBidder != null ? highestBidder : "None") +
                " | Status: " + status; // In ra enum
    }
}