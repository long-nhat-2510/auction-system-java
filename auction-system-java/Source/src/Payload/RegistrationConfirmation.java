package payload;

public class RegistrationConfirmation {

    private boolean success;     // đăng ký thành công hay không
    private String clientId;     // ID đã được server xác nhận
    private String message;      // thông báo (optional)

    public RegistrationConfirmation() {
    }

    public RegistrationConfirmation(boolean success, String clientId, String message) {
        this.success = success;
        this.clientId = clientId;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RegistrationConfirmation{" +
                "success=" + success +
                ", clientId='" + clientId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}