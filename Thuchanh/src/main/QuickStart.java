package main;

import java.awt.*;
import controller.POSController;
import view.MainFrame;

/**
 * File này để test frontend mà không cần login
 * Sử dụng khi chưa có backend/database
 */
public class QuickStart {
    public static void main(String[] args) {
        // Set system properties cho font rendering tốt hơn
        try {
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("Đang khởi tạo controller...");
        
        // Tạo controller (không kết nối server)
        POSController controller = new POSController();
        
        System.out.println("Đang mở giao diện chính...");
        
        // Mở MainFrame trực tiếp
        EventQueue.invokeLater(() -> {
            new MainFrame(controller);
            System.out.println("✓ Giao diện đã sẵn sàng!");
            System.out.println();
            System.out.println("LƯU Ý:");
            System.out.println("- Các chức năng kết nối server sẽ không hoạt động");
            System.out.println("- Chỉ dùng để test giao diện frontend");
            System.out.println("- Để sử dụng đầy đủ, hãy chạy MainApp.java và đăng nhập");
        });
    }
}