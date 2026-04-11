package server.AuctionException;

public class LowBidException extends RuntimeException {
    public LowBidException(String message) {
        super(message);
    }
}
