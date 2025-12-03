package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3307/grocery_store";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Kết nối database thành công!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy MySQL JDBC Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối database!");
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Đóng kết nối database!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tạo bảng tự động nếu chưa có
    public static void initDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Tạo bảng categories
            String createCategoriesTable = "CREATE TABLE IF NOT EXISTS categories (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(100) NOT NULL UNIQUE" +
                    ")";
            stmt.executeUpdate(createCategoriesTable);
            System.out.println("Khởi tạo bảng categories thành công!");
            
            // Tạo bảng products
            String createProductsTable = "CREATE TABLE IF NOT EXISTS products (" +
                    "id VARCHAR(50) PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "category VARCHAR(100) NOT NULL," +
                    "price DOUBLE NOT NULL," +
                    "quantity INT NOT NULL" +
                    ")";
            stmt.executeUpdate(createProductsTable);
            System.out.println("Khởi tạo bảng products thành công!");
            
            // Thêm dữ liệu mẫu cho categories nếu chưa có
            String checkCategories = "SELECT COUNT(*) as count FROM categories";
            var rs = stmt.executeQuery(checkCategories);
            if (rs.next() && rs.getInt("count") == 0) {
                String[] defaultCategories = {
                    "Thực phẩm khô",
                    "Đồ uống",
                    "Rau củ quả",
                    "Sữa và sản phẩm từ sữa",
                    "Gia vị",
                    "Đồ ăn nhanh",
                    "Bánh kẹo",
                    "Đồ dùng cá nhân"
                };
                
                for (String cat : defaultCategories) {
                    stmt.executeUpdate("INSERT INTO categories (name) VALUES ('" + cat + "')");
                }
                System.out.println("Đã thêm các danh mục mặc định!");
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi khởi tạo database!");
            e.printStackTrace();
        }
    }
}