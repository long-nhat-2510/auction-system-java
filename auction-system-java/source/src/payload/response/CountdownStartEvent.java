package payload.response;

import java.time.Instant;

public class CountdownStartEvent {

    private Long auctionId;
    private int countdownSeconds;
    private String message;
    private Instant startTime;

    public CountdownStartEvent() {
    }

    public CountdownStartEvent(Long auctionId, int countdownSeconds, String message) {
        this.auctionId = auctionId;
        this.countdownSeconds = countdownSeconds;
        this.message = message;
        this.startTime = Instant.now(); // thời gian bắt đầu countdown
    }

    // Getter và Setter
    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public int getCountdownSeconds() {
        return countdownSeconds;
    }

    public void setCountdownSeconds(int countdownSeconds) {
        this.countdownSeconds = countdownSeconds;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "CountdownStartEvent{" +
                "auctionId=" + auctionId +
                ", countdownSeconds=" + countdownSeconds +
                ", message='" + message + '\'' +
                ", startTime=" + startTime +
                '}';
    }
}