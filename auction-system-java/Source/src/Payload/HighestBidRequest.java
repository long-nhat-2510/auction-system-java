package payload;

public class HighestBidRequest {

    private String auctionId;

    public HighestBidRequest() {
    }

    public HighestBidRequest(String auctionId) {
        this.auctionId = auctionId;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    @Override
    public String toString() {
        return "HighestBidRequest{" +
                "auctionId='" + auctionId + '\'' +
                '}';
    }
}