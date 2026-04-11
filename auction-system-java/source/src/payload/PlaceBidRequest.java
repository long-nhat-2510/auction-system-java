package payload;

public class PlaceBidRequest {

    private int auctionId;  // ID phiên đấu giá
    private String bidderName;   // người đặt giá
    private double bidAmount;  // số tiền đặt giá


    public PlaceBidRequest() {
    }

    public PlaceBidRequest(int auctionId, String bidderName, double bidAmount) {
        this.auctionId = auctionId;
        this.bidderName = bidderName;
        this.bidAmount = bidAmount;

    }

    public int getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }

    public String getBidderName() {
        return bidderName;
    }

    public void setBidderId(String bidderName) {
        this.bidderName = bidderName;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }


    @Override
    public String toString() {
        return "PlaceBidRequest{" +
                "auctionId='" + auctionId + '\'' +
                ", bidderId='" + bidderName + '\'' +
                ", bidAmount=" + bidAmount +
                '}';
    }
}