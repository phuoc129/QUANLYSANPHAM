package server.database;

import java.sql.*;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * DỮ LIỆU CÓ CẤU TRÚC: MySQL Database
 */
public class DatabaseConnection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/client_server_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Không tìm thấy MySQL JDBC Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("✗ Lỗi kết nối database!");
            e.printStackTrace();
        }
        return connection;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            return password;
        }
    }

    /**
     * Khởi tạo database và bảng
     */
    public static void initDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            System.out.println("\n═══════════════════════════════════════");
            System.out.println("  KHỞI TẠO DATABASE (MySQL)");
            System.out.println("═══════════════════════════════════════");
            
            // Tạo bảng users
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "username VARCHAR(50) NOT NULL UNIQUE," +
                    "password VARCHAR(255) NOT NULL," +
                    "full_name VARCHAR(100) NOT NULL," +
                    "role VARCHAR(20) NOT NULL," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            stmt.executeUpdate(createUsersTable);
            System.out.println("✓ Bảng 'users' đã sẵn sàng");
            
            // Tạo bảng products
            String createProductsTable = "CREATE TABLE IF NOT EXISTS products (" +
                    "id VARCHAR(50) PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "category VARCHAR(100) NOT NULL," +
                    "price DOUBLE NOT NULL," +
                    "quantity INT NOT NULL," +
                    "description TEXT" +
                    ")";
            stmt.executeUpdate(createProductsTable);
            System.out.println("✓ Bảng 'products' đã sẵn sàng");
            
            // Thêm user mặc định
            var rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users");
            if (rs.next() && rs.getInt("count") == 0) {
                String hashedAdmin = hashPassword("admin123");
                String hashedUser = hashPassword("user123");
                
                stmt.executeUpdate("INSERT INTO users (username, password, full_name, role) " +
                        "VALUES ('admin', '" + hashedAdmin + "', 'Administrator', 'ADMIN')");
                stmt.executeUpdate("INSERT INTO users (username, password, full_name, role) " +
                        "VALUES ('user', '" + hashedUser + "', 'Normal User', 'USER')");
                
                System.out.println("✓ Đã tạo tài khoản mặc định:");
                System.out.println("  - admin / admin123 (ADMIN)");
                System.out.println("  - user / user123 (USER)");
            }
            
            // Thêm sản phẩm mẫu
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM products");
            if (rs.next() && rs.getInt("count") == 0) {
                stmt.executeUpdate("INSERT INTO products VALUES " +
                        "('P001', 'Laptop Dell', 'Electronics', 15000000, 10, 'Laptop hiệu năng cao')," +
                        "('P002', 'iPhone 15', 'Electronics', 25000000, 5, 'Điện thoại thông minh')," +
                        "('P003', 'Bàn phím cơ', 'Accessories', 1500000, 20, 'Bàn phím gaming')");
                System.out.println("✓ Đã thêm 3 sản phẩm mẫu");
            }
            
            System.out.println("═══════════════════════════════════════\n");
            
        } catch (SQLException e) {
            System.err.println("✗ Lỗi khởi tạo database!");
            e.printStackTrace();
        }
    }
}