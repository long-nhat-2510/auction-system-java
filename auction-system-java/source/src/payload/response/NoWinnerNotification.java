package payload.response;

public class NoWinnerNotification {

    private String auctionId;

    // Constructor rỗng (Cực kỳ quan trọng: Bắt buộc phải có để các thư viện như Gson/Jackson có thể dịch ngược từ JSON sang Object)
    public NoWinnerNotification() {
    }

    // Constructor để Server sử dụng khi đóng gói dữ liệu
    public NoWinnerNotification(String auctionId) {
        this.auctionId = auctionId;
    }

    // Getter và Setter
    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    // Dùng để in ra log cho dễ nhìn (Debug)
    @Override
    public String toString() {
        return "NoWinnerNotification{" +
                "auctionId=" + auctionId +
                '}';
    }
}