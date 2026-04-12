package payload.response;

public class CountdownTickEvent {

    private String auctionId;
    private int secondsLeft;

    // Constructor rỗng (BẮT BUỘC để Gson/Jackson có thể dịch ngược từ JSON)
    public CountdownTickEvent() {
    }

    // Constructor đầy đủ để Server đóng gói dữ liệu gửi đi
    public CountdownTickEvent(String auctionId, int secondsLeft) {
        this.auctionId = auctionId;
        this.secondsLeft = secondsLeft;
    }

    // ================== GETTERS & SETTERS ==================

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }

    public void setSecondsLeft(int secondsLeft) {
        this.secondsLeft = secondsLeft;
    }

    // ================== DEBUG ==================

    @Override
    public String toString() {
        return "CountdownTickEvent{" +
                "auctionId=" + auctionId +
                ", secondsLeft=" + secondsLeft +
                '}';
    }
}