package payload.response;

public class CancelConfirmation {

    private Long auctionId;
    private boolean canceled;
    private String message;
    private String reason;

    public CancelConfirmation() {
    }

    public CancelConfirmation(Long auctionId, boolean canceled, String message, String reason) {
        this.auctionId = auctionId;
        this.canceled = canceled;
        this.message = message;
        this.reason = reason;
    }

    // Getter và Setter
    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "CancelConfirmation{" +
                "auctionId=" + auctionId +
                ", canceled=" + canceled +
                ", message='" + message + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}