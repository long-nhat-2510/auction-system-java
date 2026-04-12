package payload.response;

public class AuctionUpdateEvent {

    private String auctionId;
    private double currentBid;
    private String highestBidder;
    private long timeStamp = System.currentTimeMillis();

    // Constructor đầy đủ
    public AuctionUpdateEvent(String auctionId, double currentBid, String highestBidder) {
        this.auctionId = auctionId;
        this.currentBid = currentBid;
        this.highestBidder = highestBidder;


    }

    // Getter và Setter
    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }

    public String getHighestBidder() {
        return highestBidder;
    }

    public void setHighestBidder(String highestBidder) {
        this.highestBidder = highestBidder;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    // Enum trạng thái đấu giá
    public enum AuctionStatus {
        ONGOING,
        ENDED,
        PAUSED
    }

    @Override
    public String toString() {
        return "AuctionUpdateEvent{" +
                "auctionId=" + auctionId +
                ", currentBid=" + currentBid +
                ", highestBidder='" + highestBidder + '\'' +
                ", timestamp=" + timeStamp +
                '}';
    }
}