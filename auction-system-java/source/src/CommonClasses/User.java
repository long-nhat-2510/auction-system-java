package CommonClasses;

public class User {
    private String user_id;
    private String username;
    private String password;
    private double balance; // Số dư
    private String fullname;

    // CONSTRUCTOR 0: BẮT BUỘC DÀNH CHO THƯ VIỆN JSON
    // (Gson/Jackson sẽ dùng cái này để ép kiểu)

    public User() {
    }


    public User(String user_id, String username, String password, String fullname, double balance) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.fullname = fullname;
    }

    // ----------------------------------------------------
    // CONSTRUCTOR 2: CHE MẬT KHẨU (Dùng để trả về Client)
    // ----------------------------------------------------
    public User(String user_id, String username, String fullname, double balance) {
        this.user_id = user_id;
        this.username = username;
        this.password = "*****"; // Giấu mật khẩu khi truyền qua mạng
        this.fullname = fullname;
        this.balance = balance;
    }

    // ----------------------------------------------------
    // GETTERS & SETTERS (Bắt buộc phải có để JSON bơm dữ liệu)
    // ----------------------------------------------------
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + user_id + '\'' +
                ", name='" + username + '\'' +
                ", balance=" + balance +
                '}';
    }
}