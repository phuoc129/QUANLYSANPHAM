// view/MainFrame.java
package view;

import java.awt.*;
import java.awt.event.*;
import controller.POSController;

public class MainFrame extends Frame {
    private POSController controller;
    private Panel mainPanel;
    private CardLayout cardLayout;
    private Label statusLabel;
    
    public MainFrame(POSController controller) {
        this.controller = controller;
        setupUI();
    }
    
    private void setupUI() {
        setTitle("Hệ thống Quản lý Bán hàng - Point of Sale");
        setSize(1450, 850);
        setLayout(new BorderLayout());
        
        // Menu bar
        MenuBar menuBar = createMenuBar();
        setMenuBar(menuBar);
        
        // Top panel - Title and user info
        Panel topPanel = new Panel(new BorderLayout());
        topPanel.setBackground(new Color(41, 128, 185));
        
        Label titleLabel = new Label("HỆ THỐNG QUẢN LÝ BÁN HÀNG", Label.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        // User info panel
        Panel userPanel = new Panel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(new Color(41, 128, 185));
        
        String userName = controller.getCurrentUser() != null ? 
            controller.getCurrentUser().getFullName() : "Guest";
        String userRole = controller.getCurrentUser() != null ? 
            controller.getCurrentUser().getRole().toUpperCase() : "N/A";
        
        Label userLabel = new Label("" + userName + " (" + userRole + ")  ");
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
        userLabel.setForeground(Color.WHITE);
        userPanel.add(userLabel);
        topPanel.add(userPanel, BorderLayout.NORTH);
        
        // Status panel
        statusLabel = new Label("Sẵn sàng", Label.RIGHT);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        statusLabel.setForeground(Color.WHITE);
        Panel statusPanel = new Panel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setBackground(new Color(41, 128, 185));
        statusPanel.add(statusLabel);
        topPanel.add(statusPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Main panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new Panel(cardLayout);
        
        // Add panels
        mainPanel.add(new DashboardPanel(controller), "dashboard");
        mainPanel.add(new SalesPanel(controller), "sales");
        mainPanel.add(new ProductManagementPanel(controller), "products");
        mainPanel.add(new RevenueReportPanel(controller), "reports");
        
        // Only add user management for admin
        if (controller.isAdmin()) {
            mainPanel.add(new UserManagementPanel(controller), "users");
        }
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Bottom panel - Quick actions
        Panel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Show dashboard by default
        cardLayout.show(mainPanel, "dashboard");
        
        // Window listener
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (confirmExit()) {
                    controller.logout();
                    controller.disconnect();
                    System.exit(0);
                }
            }
        });
        
        // Center the window
        setLocationRelativeTo(null);
        setVisible(true);
        
        updateStatus("Đã sẵn sàng");
    }
    
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        
        // Menu System
        Menu systemMenu = new Menu("Hệ thống");
        
        MenuItem logoutItem = new MenuItem("Đăng xuất");
        MenuItem exitItem = new MenuItem("Thoát");
        
        logoutItem.addActionListener(e -> logout());
        exitItem.addActionListener(e -> {
            if (confirmExit()) {
                controller.logout();
                controller.disconnect();
                System.exit(0);
            }
        });
        
        systemMenu.add(logoutItem);
        systemMenu.addSeparator();
        systemMenu.add(exitItem);
        
        // Menu Business
        Menu businessMenu = new Menu("Nghiệp vụ");
        
        MenuItem dashboardItem = new MenuItem("Trang chủ");
        MenuItem salesItem = new MenuItem("Bán hàng");
        MenuItem productsItem = new MenuItem("Quản lý sản phẩm");
        MenuItem reportsItem = new MenuItem("Báo cáo doanh thu");
        
        // Shortcuts
        dashboardItem.setShortcut(new MenuShortcut(KeyEvent.VK_H, false));
        salesItem.setShortcut(new MenuShortcut(KeyEvent.VK_B, false));
        productsItem.setShortcut(new MenuShortcut(KeyEvent.VK_P, false));
        reportsItem.setShortcut(new MenuShortcut(KeyEvent.VK_R, false));
        
        // Action listeners
        dashboardItem.addActionListener(e -> navigateTo("dashboard", "Trang chủ"));
        salesItem.addActionListener(e -> navigateTo("sales", "Bán hàng"));
        productsItem.addActionListener(e -> navigateTo("products", "Quản lý sản phẩm"));
        reportsItem.addActionListener(e -> navigateTo("reports", "Báo cáo doanh thu"));
        
        businessMenu.add(dashboardItem);
        businessMenu.add(salesItem);
        businessMenu.add(productsItem);
        businessMenu.addSeparator();
        businessMenu.add(reportsItem);
        
        // Menu Admin (only for admin)
        if (controller.isAdmin()) {
            Menu adminMenu = new Menu("Quản trị");
            
            MenuItem usersItem = new MenuItem("Quản lý tài khoản");
            usersItem.setShortcut(new MenuShortcut(KeyEvent.VK_U, false));
            usersItem.addActionListener(e -> navigateTo("users", "Quản lý tài khoản"));
            
            adminMenu.add(usersItem);
            menuBar.add(adminMenu);
        }
        
        menuBar.add(systemMenu);
        menuBar.add(businessMenu);
        
        return menuBar;
    }
    
    private Panel createBottomPanel() {
        Panel bottomPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 20, 12));
        bottomPanel.setBackground(new Color(236, 240, 241));
        
        Button dashboardBtn = createQuickButton("Trang chủ", "dashboard", new Color(52, 152, 219));
        Button salesBtn = createQuickButton("Bán hàng", "sales", new Color(46, 204, 113));
        Button productsBtn = createQuickButton("Sản phẩm", "products", new Color(241, 196, 15));
        Button reportsBtn = createQuickButton("Báo cáo", "reports", new Color(230, 126, 34));
        
        bottomPanel.add(dashboardBtn);
        bottomPanel.add(salesBtn);
        bottomPanel.add(productsBtn);
        bottomPanel.add(reportsBtn);
        
        if (controller.isAdmin()) {
            Button usersBtn = createQuickButton("Tài khoản", "users", new Color(142, 68, 173));
            bottomPanel.add(usersBtn);
        }
        
        return bottomPanel;
    }
    
    private Button createQuickButton(String label, String panelName, Color color) {
        Button btn = new Button(label);
        btn.setPreferredSize(new Dimension(150, 45));
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        
        btn.addActionListener(e -> navigateTo(panelName, label));
        
        return btn;
    }
    
    private void navigateTo(String panelName, String label) {
        cardLayout.show(mainPanel, panelName);
        updateStatus("Đang xem " + label);
    }
    
    private void logout() {
        if (confirmAction("Bạn có chắc muốn đăng xuất?")) {
            controller.logout();
            dispose();
            // Return to login screen
            new main.MainApp();
        }
    }
    
    private boolean confirmExit() {
        return confirmAction("Bạn có chắc muốn thoát khỏi ứng dụng?");
    }
    
    private boolean confirmAction(String message) {
        Dialog confirmDialog = new Dialog(this, "Xác nhận", true);
        confirmDialog.setLayout(new BorderLayout(10, 10));
        
        Label label = new Label(message, Label.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        confirmDialog.add(label, BorderLayout.CENTER);
        
        Panel btnPanel = new Panel(new FlowLayout());
        final boolean[] result = {false};
        
        Button yesBtn = new Button("Có");
        yesBtn.setPreferredSize(new Dimension(90, 35));
        yesBtn.setBackground(new Color(46, 204, 113));
        yesBtn.setForeground(Color.WHITE);
        yesBtn.addActionListener(e -> {
            result[0] = true;
            confirmDialog.dispose();
        });
        
        Button noBtn = new Button("Không");
        noBtn.setPreferredSize(new Dimension(90, 35));
        noBtn.setBackground(new Color(231, 76, 60));
        noBtn.setForeground(Color.WHITE);
        noBtn.addActionListener(e -> {
            result[0] = false;
            confirmDialog.dispose();
        });
        
        btnPanel.add(yesBtn);
        btnPanel.add(noBtn);
        confirmDialog.add(btnPanel, BorderLayout.SOUTH);
        
        confirmDialog.setSize(380, 130);
        confirmDialog.setLocationRelativeTo(this);
        confirmDialog.setVisible(true);
        
        return result[0];
    }
    
    private void updateStatus(String status) {
        statusLabel.setText(status + " | " + 
            java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
}