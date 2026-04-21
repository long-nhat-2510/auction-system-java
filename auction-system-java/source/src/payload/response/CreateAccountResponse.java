package payload.response;

public class CreateAccountResponse {
    private boolean isSuccess;
    private String message;

    // CONSTRUCTOR RỖNG (Bắt buộc cho JSON)
    public CreateAccountResponse() {
    }

    public CreateAccountResponse(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public boolean isSuccess() { return isSuccess; }
    public void setSuccess(boolean success) { isSuccess = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}