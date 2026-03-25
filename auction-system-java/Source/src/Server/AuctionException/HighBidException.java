package Server.AuctionException;

public class HighBidException extends RuntimeException {
    public HighBidException(String message) {
        super(message);
    }
}
