package CommonClasses;

public class BidRecord {
    private String bidId;
    private int auctionId;     // ✅ đổi sang int
    private String bidder;
    private double amount;
    private long time;         // ✅ đổi sang long

    public BidRecord() {}

    public BidRecord(String bidId, int auctionId,
                     String bidder, double amount, long time) {
        this.bidId = bidId;
        this.auctionId = auctionId;
        this.bidder = bidder;
        this.amount = amount;
        this.time = time;
    }

    // Getter & Setter
    public String getBidId() { return bidId; }
    public int getAuctionId() { return auctionId; }
    public String getBidder() { return bidder; }
    public double getAmount() { return amount; }
    public long getTime() { return time; }

    @Override
    public String toString() {
        return bidder + " bid " + amount + " at " + time;
    }
}