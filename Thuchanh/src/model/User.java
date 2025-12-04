package model;

public class User {
    private int userId;
    private String username;
    private String password;
    private String fullName;
    private String role;
    private String email;
    private String phoneNumber;
    private boolean isActive;
    
    public User() {
    }
    
    public User(int userId, String username, String fullName, String role) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
        this.isActive = true;
    }
    
    // Getters
    public int getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public String getRole() {
        return role;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    // Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    // Helper methods
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }
    
    public boolean isStaff() {
        return "staff".equalsIgnoreCase(role);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}