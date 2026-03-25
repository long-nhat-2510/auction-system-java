package Server.ServerException;

public class ShutdownAlreadyInProgressException extends RuntimeException {
    public ShutdownAlreadyInProgressException(String message) {
        super(message);
    }
}
