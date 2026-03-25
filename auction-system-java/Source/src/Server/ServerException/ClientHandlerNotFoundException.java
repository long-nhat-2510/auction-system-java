package Server.ServerException;

public class ClientHandlerNotFoundException extends RuntimeException {
    public ClientHandlerNotFoundException(String message) {
        super(message);
    }
}
