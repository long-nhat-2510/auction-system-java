package payload.response;

public class HighestBidResponse {

    private String auctionId;   // ID phiên đấu giá
    private double highestBid;  // giá cao nhất hiện tại
    private String bidderId;    // người đang dẫn đầu
    private long bidTime;       // thời điểm đặt giá (timestamp)

    public HighestBidResponse() {
    }

    public HighestBidResponse(String auctionId, double highestBid, String bidderId, long bidTime) {
        this.auctionId = auctionId;
        this.highestBid = highestBid;
        this.bidderId = bidderId;
        this.bidTime = bidTime;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public double getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(double highestBid) {
        this.highestBid = highestBid;
    }

    public String getBidderId() {
        return bidderId;
    }

    public void setBidderId(String bidderId) {
        this.bidderId = bidderId;
    }

    public long getBidTime() {
        return bidTime;
    }

    public void setBidTime(long bidTime) {
        this.bidTime = bidTime;
    }

    @Override
    public String toString() {
        return "HighestBidResponse{" +
                "auctionId='" + auctionId + '\'' +
                ", highestBid=" + highestBid +
                ", bidderId='" + bidderId + '\'' +
                ", bidTime=" + bidTime +
                '}';
    }
}