package server.ServerException;

public class NotClientOwnerException extends RuntimeException {
    public NotClientOwnerException(String message) {
        super(message);
    }
}
