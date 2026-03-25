package Server.ServerException;

public class ClientAlreadyRegisteredException extends RuntimeException {
    public ClientAlreadyRegisteredException(String message) {
        super(message);
    }
}
