package Server.AuctionException;

public class NotAuctionOwnerException extends RuntimeException {
    public NotAuctionOwnerException(String message) {
        super(message);
    }
}
