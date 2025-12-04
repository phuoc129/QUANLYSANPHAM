package Controller;

import Model.User;
import Model.Usertxt;
import Database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AccountController {

    private Usertxt txtModel;
    private Database db;

    public AccountController(Usertxt txtModel) {
        this.txtModel = txtModel;
        this.db = new Database();
    }


    public List<User> loadAccounts() {
        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(txtModel.readUsers());

        String sql = "SELECT username, password, role, phone, employee_code FROM users";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");
                String phone = rs.getString("phone");
                String employeeCode = rs.getString("employee_code");

                boolean exists = allUsers.stream()
                        .anyMatch(u -> u.getUsername().equals(username));
                if (!exists) {
                    allUsers.add(new User(username, password, role, phone, employeeCode));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allUsers;
    }


    public boolean addAccount(User user) {
        boolean success = txtModel.addUser(user);

        String insertSql = "INSERT INTO users(username, password, role, phone, employee_code) VALUES(?,?,?,?,?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(insertSql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getEmployeeCode());

            if (ps.executeUpdate() <= 0) success = false;

        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }


    public boolean editAccount(User user) {
        boolean success = txtModel.updateUser(user);

        String sql = "UPDATE users SET password=?, role=?, phone=? WHERE username=?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getPassword());
            ps.setString(2, user.getRole());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getUsername());

            if (ps.executeUpdate() <= 0) success = false;

        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }


    public boolean deleteAccount(String username) {
        boolean success = txtModel.deleteUser(username);

        String deleteSql = "DELETE FROM users WHERE username=?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(deleteSql)) {

            ps.setString(1, username);
            if (ps.executeUpdate() <= 0) success = false;

        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }


    public String generateEmployeeCode() {
        Random random = new Random();
        String code;
        do {
            code = "NV" + (1000 + random.nextInt(9000));
        } while (checkEmployeeCodeExists(code));
        return code;
    }

    private boolean checkEmployeeCodeExists(String code) {
        String sql = "SELECT COUNT(*) FROM users WHERE employee_code=?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
