package payload.request;

public class CreateAuctionRequest {

    private String title;          // tên sản phẩm
    private String description;    // mô tả
    private double startingPrice;  // giá khởi điểm
    private double minIncrement;   // bước giá tối thiểu
    private long startTime;        // thời điểm bắt đầu (timestamp)
    private long endTime;          // thời điểm kết thúc (timestamp)
    private String sellerId;       // người tạo (user id)

    public CreateAuctionRequest() {
    }

    public CreateAuctionRequest(String title, String description, double startingPrice,
                                double minIncrement, long startTime, long endTime, String sellerId) {
        this.title = title;
        this.description = description;
        this.startingPrice = startingPrice;
        this.minIncrement = minIncrement;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sellerId = sellerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(double startingPrice) {
        this.startingPrice = startingPrice;
    }

    public double getMinIncrement() {
        return minIncrement;
    }

    public void setMinIncrement(double minIncrement) {
        this.minIncrement = minIncrement;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    @Override
    public String toString() {
        return "CreateAuctionRequest{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startingPrice=" + startingPrice +
                ", minIncrement=" + minIncrement +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", sellerId='" + sellerId + '\'' +
                '}';
    }
}