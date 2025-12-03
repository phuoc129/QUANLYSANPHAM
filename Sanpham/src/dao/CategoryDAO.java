package dao;

import database.DatabaseConnection;
import model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    // Lấy tất cả danh mục
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Category category = new Category(
                    rs.getInt("id"),
                    rs.getString("name")
                );
                categories.add(category);
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách danh mục!");
            e.printStackTrace();
        }
        
        return categories;
    }

    // Thêm danh mục mới
    public boolean insertCategory(Category category) {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category.getName());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm danh mục!");
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật danh mục
    public boolean updateCategory(Category category) {
        String sql = "UPDATE categories SET name = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category.getName());
            pstmt.setInt(2, category.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật danh mục!");
            e.printStackTrace();
            return false;
        }
    }

    // Xóa danh mục
    public boolean deleteCategory(int id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa danh mục!");
            e.printStackTrace();
            return false;
        }
    }

    // Kiểm tra danh mục đã tồn tại
    public boolean categoryExists(String name) {
        String sql = "SELECT COUNT(*) as count FROM categories WHERE name = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra danh mục!");
            e.printStackTrace();
        }
        
        return false;
    }

    // Tìm danh mục theo ID
    public Category findById(int id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Category(
                    rs.getInt("id"),
                    rs.getString("name")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm danh mục!");
            e.printStackTrace();
        }
        
        return null;
    }

    // Đếm số lượng danh mục
    public int getCategoryCount() {
        String sql = "SELECT COUNT(*) as total FROM categories";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi đếm danh mục!");
            e.printStackTrace();
        }
        
        return 0;
    }
}