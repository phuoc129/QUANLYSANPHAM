// view/LoginPanel.java
package view;

import java.awt.*;
import java.awt.event.*;
import controller.POSController;
import utils.Validator;

public class LoginPanel extends Panel {
    private POSController controller;
    private TextField usernameField;
    private TextField passwordField;
    private Frame parentFrame;
    
    public LoginPanel(POSController controller, Frame parent) {
        this.controller = controller;
        this.parentFrame = parent;
        
        setupUI();
        connectToServer();
    }
    
    private void connectToServer() {
        if (!controller.connectToServer()) {
            showError("Không thể kết nối đến server!\n\n" +
                     "Vui lòng kiểm tra:\n" +
                     "1. Server đã chạy chưa?\n" +
                     "2. Địa chỉ và port đúng chưa? (localhost:8888)\n" +
                     "3. Firewall có chặn kết nối không?");
        }
    }
    
    private void setupUI() {
        setLayout(new GridBagLayout());
        setBackground(new Color(236, 240, 241));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Login container
        Panel loginContainer = new Panel();
        loginContainer.setLayout(new GridBagLayout());
        loginContainer.setBackground(Color.WHITE);
        
        GridBagConstraints containerGbc = new GridBagConstraints();
        containerGbc.insets = new Insets(15, 20, 15, 20);
        containerGbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Icon/Logo
        Label iconLabel = new Label("", Label.CENTER);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        containerGbc.gridx = 0;
        containerGbc.gridy = 0;
        containerGbc.gridwidth = 2;
        containerGbc.anchor = GridBagConstraints.CENTER;
        loginContainer.add(iconLabel, containerGbc);
        
        // Title
        Label titleLabel = new Label("HỆ THỐNG QUẢN LÝ BÁN HÀNG");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(41, 128, 185));
        containerGbc.gridy = 1;
        loginContainer.add(titleLabel, containerGbc);
        
        // Subtitle
        Label subtitleLabel = new Label("Point of Sale System");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        containerGbc.gridy = 2;
        loginContainer.add(subtitleLabel, containerGbc);
        
        // Spacer
        containerGbc.gridy = 3;
        containerGbc.insets = new Insets(25, 20, 15, 20);
        loginContainer.add(new Label(""), containerGbc);
        
        // Username label
        Label usernameLabel = new Label("Tên đăng nhập:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        containerGbc.gridx = 0;
        containerGbc.gridy = 4;
        containerGbc.gridwidth = 1;
        containerGbc.anchor = GridBagConstraints.WEST;
        containerGbc.insets = new Insets(5, 20, 5, 20);
        loginContainer.add(usernameLabel, containerGbc);
        
        // Username field
        usernameField = new TextField(25);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setText("admin"); // Default for testing
        containerGbc.gridx = 0;
        containerGbc.gridy = 5;
        containerGbc.gridwidth = 2;
        loginContainer.add(usernameField, containerGbc);
        
        // Password label
        Label passwordLabel = new Label("Mật khẩu:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        containerGbc.gridy = 6;
        containerGbc.gridwidth = 1;
        loginContainer.add(passwordLabel, containerGbc);
        
        // Password field
        passwordField = new TextField(25);
        passwordField.setEchoChar('●');
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setText("admin123"); // Default for testing
        containerGbc.gridy = 7;
        containerGbc.gridwidth = 2;
        loginContainer.add(passwordField, containerGbc);
        
        // Button panel
        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        Button loginButton = new Button("Đăng nhập");
        loginButton.setPreferredSize(new Dimension(130, 38));
        loginButton.setFont(new Font("Arial", Font.BOLD, 13));
        loginButton.setBackground(new Color(46, 204, 113));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(e -> performLogin());
        
        Button cancelButton = new Button("Hủy");
        cancelButton.setPreferredSize(new Dimension(130, 38));
        cancelButton.setFont(new Font("Arial", Font.BOLD, 13));
        cancelButton.setBackground(new Color(149, 165, 166));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> clearFields());
        
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        
        containerGbc.gridy = 8;
        containerGbc.gridwidth = 2;
        containerGbc.anchor = GridBagConstraints.CENTER;
        containerGbc.insets = new Insets(20, 20, 15, 20);
        loginContainer.add(buttonPanel, containerGbc);
        
        // Info text
        Label infoLabel = new Label("Tài khoản mặc định: admin / admin123");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        infoLabel.setForeground(new Color(127, 140, 141));
        containerGbc.gridy = 9;
        containerGbc.insets = new Insets(5, 20, 15, 20);
        loginContainer.add(infoLabel, containerGbc);
        
        // Footer
        Label footerLabel = new Label("© 2025 Point of Sale System");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        footerLabel.setForeground(new Color(149, 165, 166));
        containerGbc.gridy = 10;
        containerGbc.insets = new Insets(10, 20, 20, 20);
        loginContainer.add(footerLabel, containerGbc);
        
        // Add container to main panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(loginContainer, gbc);
        
        // Add Enter key listener
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });
        
        usernameField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });
    }
    
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        // Validation
        if (Validator.isEmpty(username)) {
            showError("Vui lòng nhập tên đăng nhập!");
            usernameField.requestFocus();
            return;
        }
        
        if (Validator.isEmpty(password)) {
            showError("Vui lòng nhập mật khẩu!");
            passwordField.requestFocus();
            return;
        }
        
        // Attempt login
        try {
            boolean success = controller.login(username, password);
            
            if (success) {
                showSuccess("Đăng nhập thành công!\n\n" +
                          "Chào mừng " + controller.getCurrentUser().getFullName() + "!\n" +
                          "Vai trò: " + controller.getCurrentUser().getRole().toUpperCase());
                
                // Chuyển sang MainFrame
                parentFrame.dispose();
                new MainFrame(controller);
            } else {
                showError("Đăng nhập thất bại!\n\n" +
                         "Tên đăng nhập hoặc mật khẩu không đúng.\n\n" +
                         "Vui lòng thử lại.");
                passwordField.setText("");
                passwordField.requestFocus();
            }
        } catch (Exception ex) {
            showError("Lỗi kết nối đến server!\n\n" + 
                     ex.getMessage() + "\n\n" +
                     "Vui lòng kiểm tra kết nối mạng.");
            ex.printStackTrace();
        }
    }
    
    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        usernameField.requestFocus();
    }
    
    private void showError(String message) {
        Dialog dialog = new Dialog(parentFrame, "Lỗi", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setBackground(Color.WHITE);
        
        Panel contentPanel = new Panel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE);
        
        Label iconLabel = new Label("⚠️", Label.CENTER);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        iconLabel.setForeground(new Color(231, 76, 60));
        contentPanel.add(iconLabel, BorderLayout.WEST);
        
        TextArea messageArea = new TextArea(message, 5, 35, TextArea.SCROLLBARS_VERTICAL_ONLY);
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 12));
        messageArea.setBackground(Color.WHITE);
        contentPanel.add(messageArea, BorderLayout.CENTER);
        
        dialog.add(contentPanel, BorderLayout.CENTER);
        
        Button okButton = new Button("OK");
        okButton.setPreferredSize(new Dimension(80, 30));
        okButton.setBackground(new Color(231, 76, 60));
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(e -> dialog.dispose());
        
        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(okButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }
    
    private void showSuccess(String message) {
        Dialog dialog = new Dialog(parentFrame, "Thành công", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setBackground(Color.WHITE);
        
        Panel contentPanel = new Panel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE);
        
        Label iconLabel = new Label("✓", Label.CENTER);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        iconLabel.setForeground(new Color(46, 204, 113));
        contentPanel.add(iconLabel, BorderLayout.WEST);
        
        TextArea messageArea = new TextArea(message, 4, 30, TextArea.SCROLLBARS_NONE);
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 12));
        messageArea.setBackground(Color.WHITE);
        contentPanel.add(messageArea, BorderLayout.CENTER);
        
        dialog.add(contentPanel, BorderLayout.CENTER);
        
        Button okButton = new Button("OK");
        okButton.setPreferredSize(new Dimension(80, 30));
        okButton.setBackground(new Color(46, 204, 113));
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(e -> dialog.dispose());
        
        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(okButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }
}