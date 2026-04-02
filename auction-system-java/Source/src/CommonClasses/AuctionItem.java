 package CommonClasses;

import java.time.LocalDateTime;
public class AuctionItem extends AuctionEntity {
    private String name;
    private String description;
    private double startingPrice;
    private double currentPrice;
    private String currentWinner;

    // Thời gian bắt đầu và kết thúc
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Trạng thái: OPEN -> RUNNING -> FINISHED -> CANCELED
    private String status;

    public AuctionItem(int id, String name, String description, double startingPrice, LocalDateTime endTime) {
        super(id);
        this.name = name;
        this.description = description;
        this.startingPrice = startingPrice;
        this.currentPrice = startingPrice;
        this.currentWinner = "None";
        this.startTime = LocalDateTime.now();
        this.endTime = endTime;
        this.status = "RUNNING"; // Mặc định khi tạo là đang chạy
    }
    //getter/setter
    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public double getCurrentPrice(){
        return currentPrice;
    }

    // Dùng synchronized ở mức độ thuộc tính để đảm bảo an toàn đa luồng
    public synchronized void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getCurrentWinner() {
        return currentWinner;
    }
    public synchronized void setCurrentWinner(String currentWinner) {
        this.currentWinner = currentWinner;
    }

    public LocalDateTime getEndTime() { return endTime; }

    //cập nhật tgian kết thúc
    public synchronized void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
    }
    //ktra xem coòn tgian đấu giá không
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.endTime);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + this.getId() +
                ", name='" + name + '\'' +
                ", price=" + currentPrice +
                ", winner='" + currentWinner + '\'' +
                ", status='" + status + '\'' +
                '}';
        }
    }

