package payload;

public class FinishAuctionRequest {

    private String auctionId;   // ID phiên đấu giá cần kết thúc
    private String requesterId; // ai yêu cầu (admin/seller/system)
    private String reason;      // lý do kết thúc (optional)

    public FinishAuctionRequest() {
    }

    public FinishAuctionRequest(String auctionId, String requesterId, String reason) {
        this.auctionId = auctionId;
        this.requesterId = requesterId;
        this.reason = reason;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "FinishAuctionRequest{" +
                "auctionId='" + auctionId + '\'' +
                ", requesterId='" + requesterId + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}