package payload.request;
public class CreateAccountRequest {
    private String name;
    private String email;
    private String username;
    private String phone;
    private String password;

    // Constructor đầy đủ tham số để Client tạo đối tượng trước khi chuyển thành JSON
    public CreateAccountRequest(String name, String email, String username, String phone, String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.password = password;
    }

    // Các Getters để Server lấy thông tin ra xử lý sau khi dịch từ JSON về Object
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }
}