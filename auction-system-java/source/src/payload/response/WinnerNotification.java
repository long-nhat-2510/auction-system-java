package payload.response;

public class WinnerNotification {

    private String auctionId;
    private String winnerName;
    private double winningPrice;

    // Constructor rỗng (Bắt buộc cho quá trình dịch JSON - Deserialization)
    public WinnerNotification() {
    }

    // Constructor đầy đủ để Server đóng gói dữ liệu
    public WinnerNotification(String auctionId, String winnerName, double winningPrice) {
        this.auctionId = auctionId;
        this.winnerName = winnerName;
        this.winningPrice = winningPrice;
    }

    // ================== GETTERS & SETTERS ==================

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public double getWinningPrice() {
        return winningPrice;
    }

    public void setWinningPrice(double winningPrice) {
        this.winningPrice = winningPrice;
    }

    // ================== DEBUG ==================

    @Override
    public String toString() {
        return "WinnerNotification{" +
                "auctionId=" + auctionId +
                ", winnerName='" + winnerName + '\'' +
                ", winningPrice=" + winningPrice +
                '}';
    }
}