package server.ServerException;

public class ClientAlreadyRegisteredException extends RuntimeException {
    public ClientAlreadyRegisteredException(String message) {
        super(message);
    }
}
