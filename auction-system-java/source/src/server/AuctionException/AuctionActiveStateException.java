package server.AuctionException;

public class AuctionActiveStateException extends RuntimeException {
    public AuctionActiveStateException(String message) {
        super(message);
    }
}
