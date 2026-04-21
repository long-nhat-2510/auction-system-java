package payload.response;

import CommonClasses.User; // Import class User của bạn vào đây

public class LoginResponse {
    private boolean isSuccess;
    private String message;
    private User userProfile;
    // cho JSON
    public LoginResponse() {}

    // Constructor
    public LoginResponse(boolean isSuccess, String message, User userProfile) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.userProfile = userProfile;
    }

    // Các hàm Getters
    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public User getUserProfile() {
        return userProfile;
    }
}