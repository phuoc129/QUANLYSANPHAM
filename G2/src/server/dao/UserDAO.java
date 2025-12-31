package server.dao;

import server.database.DatabaseConnection;
import server.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// ============================================================
// USER DAO - Truy xuất MySQL
// ============================================================
public class UserDAO {

    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, DatabaseConnection.hashPassword(password));
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("full_name"));
                user.setRole(rs.getString("role"));
                
                Timestamp created = rs.getTimestamp("created_at");
                if (created != null) {
                    user.setCreatedAt(created.toLocalDateTime());
                }
                
                return user;
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi login: " + e.getMessage());
        }
        
        return null;
    }
}
