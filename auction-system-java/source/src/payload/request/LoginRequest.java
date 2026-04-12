package payload.request;

public class LoginRequest {
    private String name;
    private String password;
    public LoginRequest(String name, String password) {
        this.name = name;
        this.password= password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
    // Khi payload được niêm phong thì k ai được phép sửa trên đường vận chuyển--> giúp code an toàn hơn

}
