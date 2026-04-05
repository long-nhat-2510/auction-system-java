package payload;

public class CancelAuctionRequest {

    private Long auctionId;
    private String reason; // lý do hủy, có thể null

    public CancelAuctionRequest() {
    }

    public CancelAuctionRequest(Long auctionId, String reason) {
        this.auctionId = auctionId;
        this.reason = reason;
    }

    // Getter và Setter
    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "CancelAuctionRequest{" +
                "auctionId=" + auctionId +
                ", reason='" + reason + '\'' +
                '}';
    }
}