package payload.response;

import java.math.BigDecimal;
import java.time.Instant;

public class BidConfirmation {

    private Long auctionId;
    private BigDecimal bidAmount;
    private boolean successful;
    private String message;
    private BigDecimal currentHighestBid;
    private String highestBidder;
    private Instant timestamp;

    public BidConfirmation(Long auctionId, BigDecimal bidAmount, boolean successful, String message,
                           BigDecimal currentHighestBid, String highestBidder) {
        this.auctionId = auctionId;
        this.bidAmount = bidAmount;
        this.successful = successful;
        this.message = message;
        this.currentHighestBid = currentHighestBid;
        this.highestBidder = highestBidder;
        this.timestamp = Instant.now();
    }

    // Getter và Setter
    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public BigDecimal getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(BigDecimal bidAmount) {
        this.bidAmount = bidAmount;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getCurrentHighestBid() {
        return currentHighestBid;
    }

    public void setCurrentHighestBid(BigDecimal currentHighestBid) {
        this.currentHighestBid = currentHighestBid;
    }

    public String getHighestBidder() {
        return highestBidder;
    }

    public void setHighestBidder(String highestBidder) {
        this.highestBidder = highestBidder;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BidConfirmation{" +
                "auctionId=" + auctionId +
                ", bidAmount=" + bidAmount +
                ", successful=" + successful +
                ", message='" + message + '\'' +
                ", currentHighestBid=" + currentHighestBid +
                ", highestBidder='" + highestBidder + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}