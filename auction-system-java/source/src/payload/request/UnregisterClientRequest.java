package payload.request;

public class UnregisterClientRequest {

    private String clientId;  // ID client muốn ngắt kết nối
    private String reason;    // lý do rời (optional)

    public UnregisterClientRequest() {
    }

    public UnregisterClientRequest(String clientId, String reason) {
        this.clientId = clientId;
        this.reason = reason;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "UnregisterClientRequest{" +
                "clientId='" + clientId + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}