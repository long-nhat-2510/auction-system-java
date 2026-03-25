package Server.ServerException;

public class AlreadyHasHighestBidException extends RuntimeException {
    public AlreadyHasHighestBidException(String message) {
        super(message);
    }
}
