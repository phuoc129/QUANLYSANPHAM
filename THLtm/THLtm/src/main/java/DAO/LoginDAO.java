package DAO;

import Model.User;
import Database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginDAO {

    private Database db;

    public LoginDAO() {
        db = new Database();
    }

    public User login(String username, String password) {
        String sql = "SELECT username, password, role FROM users WHERE username=? AND password=?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String uname = rs.getString("username");
                String pass = rs.getString("password");
                String role = rs.getString("role");


                return new User(uname, pass, role, "", "");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
