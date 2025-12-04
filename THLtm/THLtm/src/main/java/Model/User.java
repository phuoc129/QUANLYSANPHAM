package Model;

public class User {
    private int id;
    private String username;
    private String password;
    private String role;
    private String phone;
    private String employeeCode;

    public User(int id, String username, String password, String role, String phone, String employeeCode) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.employeeCode = employeeCode;
    }


    public User(String username, String password, String role, String phone, String employeeCode) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.employeeCode = employeeCode;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }
}
