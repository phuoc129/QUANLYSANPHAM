package utils;

import java.awt.*;

public class ErrorHandler {
    
    /**
     * Hiển thị dialog lỗi với icon và message
     */
    public static void showError(Frame parent, String message) {
        Dialog dialog = new Dialog(parent, "Lỗi", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setBackground(Color.WHITE);
        
        // Content panel with icon and message
        Panel contentPanel = new Panel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE);
        
        // Error icon
        Label iconLabel = new Label("⚠️", Label.CENTER);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        iconLabel.setForeground(new Color(231, 76, 60));
        contentPanel.add(iconLabel, BorderLayout.WEST);
        
        // Message area
        TextArea messageArea = new TextArea(message, 5, 40, TextArea.SCROLLBARS_VERTICAL_ONLY);
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 12));
        messageArea.setBackground(Color.WHITE);
        contentPanel.add(messageArea, BorderLayout.CENTER);
        
        dialog.add(contentPanel, BorderLayout.CENTER);
        
        // Button panel
        Button okBtn = new Button("OK");
        okBtn.setPreferredSize(new Dimension(80, 30));
        okBtn.setBackground(new Color(231, 76, 60));
        okBtn.setForeground(Color.WHITE);
        okBtn.setFont(new Font("Arial", Font.BOLD, 12));
        okBtn.addActionListener(e -> dialog.dispose());
        
        Panel btnPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(okBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
    
    /**
     * Hiển thị dialog thành công
     */
    public static void showSuccess(Frame parent, String message) {
        Dialog dialog = new Dialog(parent, "Thành công", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setBackground(Color.WHITE);
        
        Panel contentPanel = new Panel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE);
        
        // Success icon
        Label iconLabel = new Label("✓", Label.CENTER);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        iconLabel.setForeground(new Color(46, 204, 113));
        contentPanel.add(iconLabel, BorderLayout.WEST);
        
        // Message area
        TextArea messageArea = new TextArea(message, 4, 35, TextArea.SCROLLBARS_VERTICAL_ONLY);
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 12));
        messageArea.setBackground(Color.WHITE);
        contentPanel.add(messageArea, BorderLayout.CENTER);
        
        dialog.add(contentPanel, BorderLayout.CENTER);
        
        Button okBtn = new Button("OK");
        okBtn.setPreferredSize(new Dimension(80, 30));
        okBtn.setBackground(new Color(46, 204, 113));
        okBtn.setForeground(Color.WHITE);
        okBtn.setFont(new Font("Arial", Font.BOLD, 12));
        okBtn.addActionListener(e -> dialog.dispose());
        
        Panel btnPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(okBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
    
    /**
     * Hiển thị dialog warning
     */
    public static void showWarning(Frame parent, String message) {
        Dialog dialog = new Dialog(parent, "Cảnh báo", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setBackground(Color.WHITE);
        
        Panel contentPanel = new Panel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE);
        
        // Warning icon
        Label iconLabel = new Label("⚠️", Label.CENTER);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        iconLabel.setForeground(new Color(241, 196, 15));
        contentPanel.add(iconLabel, BorderLayout.WEST);
        
        // Message area
        TextArea messageArea = new TextArea(message, 4, 35, TextArea.SCROLLBARS_VERTICAL_ONLY);
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 12));
        messageArea.setBackground(Color.WHITE);
        contentPanel.add(messageArea, BorderLayout.CENTER);
        
        dialog.add(contentPanel, BorderLayout.CENTER);
        
        Button okBtn = new Button("OK");
        okBtn.setPreferredSize(new Dimension(80, 30));
        okBtn.setBackground(new Color(241, 196, 15));
        okBtn.setForeground(Color.WHITE);
        okBtn.setFont(new Font("Arial", Font.BOLD, 12));
        okBtn.addActionListener(e -> dialog.dispose());
        
        Panel btnPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(okBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
    
    /**
     * Hiển thị dialog thông tin
     */
    public static void showInfo(Frame parent, String title, String message) {
        Dialog dialog = new Dialog(parent, title, true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setBackground(Color.WHITE);
        
        Panel contentPanel = new Panel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE);
        
        // Info icon
        Label iconLabel = new Label("ℹ️", Label.CENTER);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        iconLabel.setForeground(new Color(52, 152, 219));
        contentPanel.add(iconLabel, BorderLayout.WEST);
        
        // Message area
        TextArea messageArea = new TextArea(message, 6, 40, TextArea.SCROLLBARS_VERTICAL_ONLY);
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 12));
        messageArea.setBackground(Color.WHITE);
        contentPanel.add(messageArea, BorderLayout.CENTER);
        
        dialog.add(contentPanel, BorderLayout.CENTER);
        
        Button okBtn = new Button("OK");
        okBtn.setPreferredSize(new Dimension(80, 30));
        okBtn.setBackground(new Color(52, 152, 219));
        okBtn.setForeground(Color.WHITE);
        okBtn.setFont(new Font("Arial", Font.BOLD, 12));
        okBtn.addActionListener(e -> dialog.dispose());
        
        Panel btnPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(okBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
    
    /**
     * Hiển thị dialog xác nhận (Yes/No)
     */
    public static boolean showConfirm(Frame parent, String message) {
        Dialog dialog = new Dialog(parent, "Xác nhận", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setBackground(Color.WHITE);
        
        // Message
        Label messageLabel = new Label(message, Label.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 13));
        dialog.add(messageLabel, BorderLayout.CENTER);
        
        // Button panel
        Panel btnPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(Color.WHITE);
        
        final boolean[] result = {false};
        
        Button yesBtn = new Button("Có");
        yesBtn.setPreferredSize(new Dimension(90, 35));
        yesBtn.setBackground(new Color(46, 204, 113));
        yesBtn.setForeground(Color.WHITE);
        yesBtn.setFont(new Font("Arial", Font.BOLD, 12));
        yesBtn.addActionListener(e -> {
            result[0] = true;
            dialog.dispose();
        });
        
        Button noBtn = new Button("Không");
        noBtn.setPreferredSize(new Dimension(90, 35));
        noBtn.setBackground(new Color(231, 76, 60));
        noBtn.setForeground(Color.WHITE);
        noBtn.setFont(new Font("Arial", Font.BOLD, 12));
        noBtn.addActionListener(e -> {
            result[0] = false;
            dialog.dispose();
        });
        
        btnPanel.add(yesBtn);
        btnPanel.add(noBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.setSize(380, 130);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        
        return result[0];
    }
    
    /**
     * Log lỗi ra console
     */
    public static void logError(String context, Exception e) {
        System.err.println("[ERROR] " + context);
        System.err.println("Time: " + java.time.LocalDateTime.now());
        System.err.println("Message: " + e.getMessage());
        e.printStackTrace();
        System.err.println("---");
    }
    
    /**
     * Log thông tin ra console
     */
    public static void logInfo(String message) {
        System.out.println("[INFO] " + java.time.LocalTime.now() + " - " + message);
    }
    
    /**
     * Log warning ra console
     */
    public static void logWarning(String message) {
        System.out.println("[WARN] " + java.time.LocalTime.now() + " - " + message);
    }
}