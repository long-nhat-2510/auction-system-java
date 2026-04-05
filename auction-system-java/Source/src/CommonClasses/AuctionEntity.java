package CommonClasses;

import java.util.ArrayList;
import java.util.List;

public class AuctionEntity {

    private int auctionId;
    private AuctionItem item;

    private double startingPrice;
    private double currentPrice;

    private String highestBidder;
    private boolean active;

    private long startTime;
    private long endTime;

    private List<BidRecord> bidHistory;

    // Constructor rỗng (bắt buộc cho Gson)
    public AuctionEntity() {
        this.bidHistory = new ArrayList<>();
    }

    // Constructor đầy đủ
    public AuctionEntity(int auctionId, AuctionItem item,
                         double startingPrice,
                         long startTime, long endTime) {

        this.auctionId = auctionId;
        this.item = item;
        this.startingPrice = startingPrice;
        this.currentPrice = startingPrice;

        this.startTime = startTime;
        this.endTime = endTime;

        this.active = true;
        this.bidHistory = new ArrayList<>();
    }

    // ================== LOGIC CHÍNH ==================

    public synchronized boolean placeBid(String bidder, double amount) {

        checkExpired(); // 🔥 luôn kiểm tra trước

        if (!active) return false;

        if (amount <= currentPrice) return false;

        currentPrice = amount;
        highestBidder = bidder;

        BidRecord bid = new BidRecord(
                "BID-" + System.currentTimeMillis(),
                auctionId,
                bidder,
                amount,
                System.currentTimeMillis()
        );

        bidHistory.add(bid);

        return true;
    }

    // Kiểm tra hết hạn
    public void checkExpired() {
        if (System.currentTimeMillis() > endTime) {
            active = false;
        }
    }

    // Đóng auction thủ công
    public void closeAuction() {
        this.active = false;
    }

    // Lấy bid cao nhất
    public BidRecord getHighestBid() {
        if (bidHistory.isEmpty()) return null;
        return bidHistory.get(bidHistory.size() - 1);
    }

    // ================== GETTER ==================

    public int getAuctionId() { return auctionId; }
    public AuctionItem getItem() { return item; }
    public double getStartingPrice() { return startingPrice; }
    public double getCurrentPrice() { return currentPrice; }
    public String getHighestBidder() { return highestBidder; }
    public boolean isActive() { return active; }
    public List<BidRecord> getBidHistory() { return bidHistory; }
    public long getStartTime() { return startTime; }
    public long getEndTime() { return endTime; }

    // ================== DEBUG ==================

    @Override
    public String toString() {
        return "AuctionID: " + auctionId +
                " | Item: " + (item != null ? item.getName() : "Unknown") +
                " | Current Price: " + currentPrice +
                " | Highest Bidder: " + (highestBidder != null ? highestBidder : "None") +
                " | Active: " + active;
    }
}