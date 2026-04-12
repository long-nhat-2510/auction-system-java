package CommonClasses;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BidRecord {
    private String bidId;
    private String auctionId;
    private String bidder;
    private double amount;


    private LocalDateTime time;

    public BidRecord() {}


    public BidRecord(String bidId, String auctionId,
                     String bidder, double amount, LocalDateTime time) {
        this.bidId = bidId;
        this.auctionId = auctionId;
        this.bidder = bidder;
        this.amount = amount;
        this.time = time;
    }

    // ================== GETTER & SETTER ==================
    public String getBidId() { return bidId; }
    public String getAuctionId() { return auctionId; }
    public String getBidder() { return bidder; }
    public double getAmount() { return amount; }


    public LocalDateTime getTime() { return time; }

    // ================== DEBUG ==================
    @Override
    public String toString() {
        // Format thời gian cho dễ nhìn khi in log (VD: 2023-10-25 15:30:00)
        String formattedTime = "Unknown";
        if (time != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            formattedTime = time.format(formatter);
        }

        return bidder + " bid " + amount + " at " + formattedTime;
    }
}