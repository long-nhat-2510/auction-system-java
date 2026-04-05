package packets;

import com.google.gson.Gson;
import com.google.gson.JsonElement; // Thêm import này

public class NetworkMessage {

    private RequestType action;
    private JsonElement data; // Đổi Object thành JsonElement

    private static final Gson gson = new Gson();

    public NetworkMessage() {}

    // Khi khởi tạo, Gson sẽ tự động ép payload của bạn thành JsonElement
    public NetworkMessage(RequestType action, Object payload) {
        this.action = action;
        this.data = gson.toJsonTree(payload);
    }

    public RequestType getAction() { return action; }
    public void setAction(RequestType action) { this.action = action; }

    public JsonElement getData() { return data; }
    public void setData(JsonElement data) { this.data = data; }

    public String toJson() { return gson.toJson(this); }

    public static NetworkMessage fromJson(String json) {
        return gson.fromJson(json, NetworkMessage.class);
    }

    // Viết thêm hàm tiện ích này để lấy dữ liệu ra cực nhanh
    public <T> T getDataAs(Class<T> clazz) {
        return gson.fromJson(this.data, clazz);
    }
}