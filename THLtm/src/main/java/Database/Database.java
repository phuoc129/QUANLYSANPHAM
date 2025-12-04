package Database;

import java.sql.*;

public class Database {


    String url = "jdbc:mysql://localhost:3306/ze?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private String user = "root";
    private String password = "18004huyhio";

    public Database() {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Kết nối MySQL thành công!");

            String check = "SELECT * FROM users WHERE username='admin'";
            try (PreparedStatement ps = conn.prepareStatement(check);
                 ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    String insert = "INSERT INTO users(username,password,role) VALUES('admin','123','admin')";
                    try (PreparedStatement psInsert = conn.prepareStatement(insert)) {
                        psInsert.executeUpdate();
                        System.out.println("Đã tạo admin mặc định");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Kết nối MySQL thất bại!");
        }
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
