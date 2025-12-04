// view/UserManagementPanel.java - ĐÃ FIX LỖI IMPORT
package view;

import java.awt.*;
import java.awt.event.*;
import controller.POSController;
import model.User;
import utils.Validator;
import utils.ErrorHandler;

public class UserManagementPanel extends Panel {
    private POSController controller;
    private List userList;  // java.awt.List - UI component
    private TextField searchField;
    private Choice roleFilterChoice;
    private boolean isEditMode = false;
    private int editingUserId = -1;
    
    public UserManagementPanel(POSController controller) {
        this.controller = controller;
        
        // Check if user is admin
        if (!controller.isAdmin()) {
            showAccessDenied();
            return;
        }
        
        setupUI();
        loadUsers();
    }
    
    private void showAccessDenied() {
        setLayout(new BorderLayout());
        Panel panel = new Panel(new GridBagLayout());
        
        Label iconLabel = new Label("");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 72));
        iconLabel.setForeground(new Color(231, 76, 60));
        
        Label messageLabel = new Label("TRUY CẬP BỊ TỪ CHỐI");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 24));
        messageLabel.setForeground(new Color(231, 76, 60));
        
        Label infoLabel = new Label("Chỉ quản trị viên (Admin) mới có quyền truy cập chức năng này.");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        panel.add(iconLabel, gbc);
        
        gbc.gridy = 1;
        panel.add(messageLabel, gbc);
        
        gbc.gridy = 2;
        panel.add(infoLabel, gbc);
        
        add(panel, BorderLayout.CENTER);
    }
    
    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Top panel
        Panel topPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(new Color(142, 68, 173));
        
        Label titleLabel = new Label("QUẢN LÝ TÀI KHOẢN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);
        
        Label spacer = new Label("     ");
        topPanel.add(spacer);
        
        Label searchLabel = new Label("Tìm kiếm:");
        searchLabel.setForeground(Color.WHITE);
        topPanel.add(searchLabel);
        searchField = new TextField(20);
        topPanel.add(searchField);
        
        Label roleLabel = new Label("Vai trò:");
        roleLabel.setForeground(Color.WHITE);
        topPanel.add(roleLabel);
        roleFilterChoice = new Choice();
        roleFilterChoice.add("Tất cả");
        roleFilterChoice.add("Admin");
        roleFilterChoice.add("Nhân viên");
        topPanel.add(roleFilterChoice);
        
        Button searchBtn = new Button("Tìm kiếm");
        searchBtn.setBackground(new Color(46, 204, 113));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.addActionListener(e -> searchUsers());
        topPanel.add(searchBtn);
        
        Button refreshBtn = new Button("Làm mới");
        refreshBtn.setBackground(new Color(241, 196, 15));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.addActionListener(e -> loadUsers());
        topPanel.add(refreshBtn);
        
        Button addBtn = new Button("+ Thêm tài khoản");
        addBtn.setBackground(new Color(46, 204, 113));
        addBtn.setForeground(Color.WHITE);
        addBtn.addActionListener(e -> showAddUserDialog());
        topPanel.add(addBtn);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Center - User list
        Panel centerPanel = new Panel(new BorderLayout());
        
        Panel headerPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(236, 240, 241));
        Label headerLabel = new Label("ID   | Username      | Họ tên                    | Vai trò    | Email                | Trạng thái");
        headerLabel.setFont(new Font("Monospaced", Font.BOLD, 12));
        headerPanel.add(headerLabel);
        centerPanel.add(headerPanel, BorderLayout.NORTH);
        
        userList = new List(22, false);  // java.awt.List
        userList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        centerPanel.add(userList, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Bottom - Action buttons
        Panel bottomPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(new Color(236, 240, 241));
        
        Button viewBtn = new Button("Xem chi tiết");
        viewBtn.setPreferredSize(new Dimension(130, 35));
        viewBtn.setBackground(new Color(52, 152, 219));
        viewBtn.setForeground(Color.WHITE);
        viewBtn.addActionListener(e -> viewUserDetail());
        bottomPanel.add(viewBtn);
        
        Button editBtn = new Button("Sửa");
        editBtn.setPreferredSize(new Dimension(130, 35));
        editBtn.setBackground(new Color(241, 196, 15));
        editBtn.setForeground(Color.WHITE);
        editBtn.addActionListener(e -> editUser());
        bottomPanel.add(editBtn);
        
        Button resetPwdBtn = new Button("Đặt lại MK");
        resetPwdBtn.setPreferredSize(new Dimension(130, 35));
        resetPwdBtn.setBackground(new Color(52, 152, 219));
        resetPwdBtn.setForeground(Color.WHITE);
        resetPwdBtn.addActionListener(e -> resetPassword());
        bottomPanel.add(resetPwdBtn);
        
        Button deleteBtn = new Button("Xóa");
        deleteBtn.setPreferredSize(new Dimension(130, 35));
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> deleteUser());
        bottomPanel.add(deleteBtn);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void loadUsers() {
        userList.removeAll();
        java.util.List<User> users = controller.getAllUsers();  // EXPLICIT: java.util.List
        
        if (users.isEmpty()) {
            userList.add("Chưa có tài khoản nào trong hệ thống.");
            return;
        }
        
        for (User user : users) {
            String status = user.isActive() ? "Hoạt động" : "Khóa";
            String role = user.getRole().equals("admin") ? "Admin" : "Nhân viên";
            
            String item = String.format("%-4d | %-13s | %-25s | %-10s | %-20s | %s",
                user.getUserId(),
                truncate(user.getUsername(), 13),
                truncate(user.getFullName(), 25),
                role,
                truncate(user.getEmail() != null ? user.getEmail() : "N/A", 20),
                status
            );
            userList.add(item);
        }
    }
    
    private void searchUsers() {
        String keyword = searchField.getText().trim();
        String roleFilter = roleFilterChoice.getSelectedItem();
        
        userList.removeAll();
        java.util.List<User> users = controller.getAllUsers();  // EXPLICIT: java.util.List
        
        // Create new list for filtered results
        java.util.List<User> filteredUsers = new java.util.ArrayList<>();  // EXPLICIT
        
        for (User u : users) {
            boolean matchKeyword = keyword.isEmpty() || 
                u.getUsername().toLowerCase().contains(keyword.toLowerCase()) ||
                u.getFullName().toLowerCase().contains(keyword.toLowerCase());
            
            boolean matchRole = roleFilter.equals("Tất cả") ||
                (roleFilter.equals("Admin") && u.getRole().equals("admin")) ||
                (roleFilter.equals("Nhân viên") && u.getRole().equals("staff"));
            
            if (matchKeyword && matchRole) {
                filteredUsers.add(u);
            }
        }
        
        if (filteredUsers.isEmpty()) {
            userList.add("Không tìm thấy tài khoản phù hợp.");
            return;
        }
        
        for (User user : filteredUsers) {
            String status = user.isActive() ? "Hoạt động" : "Khóa";
            String role = user.getRole().equals("admin") ? "Admin" : "Nhân viên";
            
            String item = String.format("%-4d | %-13s | %-25s | %-10s | %-20s | %s",
                user.getUserId(),
                truncate(user.getUsername(), 13),
                truncate(user.getFullName(), 25),
                role,
                truncate(user.getEmail() != null ? user.getEmail() : "N/A", 20),
                status
            );
            userList.add(item);
        }
    }
    
    private void showAddUserDialog() {
        isEditMode = false;
        editingUserId = -1;
        showUserDialog("Thêm tài khoản mới", null);
    }
    
    private void showUserDialog(String title, User user) {
        Frame parentFrame = getParentFrame();
        Dialog dialog = new Dialog(parentFrame, title, true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        Label usernameLabel = new Label("Tên đăng nhập: *");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dialog.add(usernameLabel, gbc);
        
        gbc.gridx = 1;
        TextField usernameField = new TextField(30);
        if (user != null) {
            usernameField.setText(user.getUsername());
            usernameField.setEnabled(false); // Không cho sửa username
        }
        dialog.add(usernameField, gbc);
        
        // Password (only for new user)
        TextField passwordField = null;
        if (user == null) {  // Only show password field for new user
            gbc.gridx = 0;
            gbc.gridy = 1;
            Label passwordLabel = new Label("Mật khẩu: *");
            passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
            dialog.add(passwordLabel, gbc);
            
            gbc.gridx = 1;
            passwordField = new TextField(30);
            passwordField.setEchoChar('●');
            dialog.add(passwordField, gbc);
        }
        
        // Full name
        gbc.gridx = 0;
        gbc.gridy = user == null ? 2 : 1;
        Label fullNameLabel = new Label("Họ tên: *");
        fullNameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dialog.add(fullNameLabel, gbc);
        
        gbc.gridx = 1;
        TextField fullNameField = new TextField(30);
        if (user != null) fullNameField.setText(user.getFullName());
        dialog.add(fullNameField, gbc);
        
        // Role
        gbc.gridx = 0;
        gbc.gridy = user == null ? 3 : 2;
        Label roleLabel = new Label("Vai trò: *");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dialog.add(roleLabel, gbc);
        
        gbc.gridx = 1;
        Choice roleChoice = new Choice();
        roleChoice.add("staff");
        roleChoice.add("admin");
        if (user != null) roleChoice.select(user.getRole());
        dialog.add(roleChoice, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = user == null ? 4 : 3;
        Label emailLabel = new Label("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dialog.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        TextField emailField = new TextField(30);
        if (user != null && user.getEmail() != null) emailField.setText(user.getEmail());
        dialog.add(emailField, gbc);
        
        // Phone
        gbc.gridx = 0;
        gbc.gridy = user == null ? 5 : 4;
        Label phoneLabel = new Label("Số điện thoại:");
        phoneLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dialog.add(phoneLabel, gbc);
        
        gbc.gridx = 1;
        TextField phoneField = new TextField(30);
        if (user != null && user.getPhoneNumber() != null) phoneField.setText(user.getPhoneNumber());
        dialog.add(phoneField, gbc);
        
        // Store password field in final variable for lambda
        final TextField finalPasswordField = passwordField;
        
        // Buttons
        gbc.gridx = 0;
        gbc.gridy = user == null ? 6 : 5;
        gbc.gridwidth = 2;
        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        Button saveBtn = new Button("Lưu");
        saveBtn.setPreferredSize(new Dimension(100, 35));
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> {
            if (saveUser(user, usernameField, finalPasswordField, fullNameField, 
                        roleChoice, emailField, phoneField)) {
                dialog.dispose();
                loadUsers();
            }
        });
        buttonPanel.add(saveBtn);
        
        Button cancelBtn = new Button("Hủy");
        cancelBtn.setPreferredSize(new Dimension(100, 35));
        cancelBtn.setBackground(new Color(149, 165, 166));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelBtn);
        
        dialog.add(buttonPanel, gbc);
        
        // Info label
        gbc.gridy = user == null ? 7 : 6;
        Label infoLabel = new Label("* Trường bắt buộc");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        infoLabel.setForeground(new Color(231, 76, 60));
        dialog.add(infoLabel, gbc);
        
        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }
    
    private boolean saveUser(User existingUser, TextField usernameField, 
                           TextField passwordField, TextField fullNameField,
                           Choice roleChoice, TextField emailField, 
                           TextField phoneField) {
        // Validation
        if (Validator.isEmpty(usernameField.getText())) {
            showError("Vui lòng nhập tên đăng nhập!");
            return false;
        }
        
        if (existingUser == null && Validator.isEmpty(passwordField.getText())) {
            showError("Vui lòng nhập mật khẩu!");
            return false;
        }
        
        if (Validator.isEmpty(fullNameField.getText())) {
            showError("Vui lòng nhập họ tên!");
            return false;
        }
        
        // Validate email if provided
        String email = emailField.getText().trim();
        if (!email.isEmpty() && !Validator.isValidEmail(email)) {
            showError("Email không hợp lệ!");
            return false;
        }
        
        // Validate phone if provided
        String phone = phoneField.getText().trim();
        if (!phone.isEmpty() && !Validator.isValidPhoneNumber(phone)) {
            showError("Số điện thoại không hợp lệ!");
            return false;
        }
        
        try {
            User user = (existingUser != null) ? existingUser : new User();
            user.setUsername(usernameField.getText().trim());
            if (existingUser == null) {
                user.setPassword(passwordField.getText());
            }
            user.setFullName(fullNameField.getText().trim());
            user.setRole(roleChoice.getSelectedItem());
            user.setEmail(email.isEmpty() ? null : email);
            user.setPhoneNumber(phone.isEmpty() ? null : phone);
            
            boolean success;
            if (existingUser != null) {
                success = controller.updateUser(user);
            } else {
                success = controller.createUser(user);
            }
            
            if (success) {
                showSuccess(existingUser != null ? "Cập nhật tài khoản thành công!" : "Thêm tài khoản thành công!");
                return true;
            } else {
                showError(existingUser != null ? "Cập nhật tài khoản thất bại!" : "Thêm tài khoản thất bại!");
                return false;
            }
        } catch (Exception e) {
            showError("Lỗi: " + e.getMessage());
            return false;
        }
    }
    
    private void viewUserDetail() {
        int selectedIndex = userList.getSelectedIndex();
        if (selectedIndex < 0) {
            showError("Vui lòng chọn một tài khoản!");
            return;
        }
        
        String selectedItem = userList.getItem(selectedIndex);
        if (selectedItem.contains("Chưa có") || selectedItem.contains("Không tìm thấy")) {
            return;
        }
        
        showMessage("Chi tiết tài khoản", selectedItem);
    }
    
    private void editUser() {
        int selectedIndex = userList.getSelectedIndex();
        if (selectedIndex < 0) {
            showError("Vui lòng chọn một tài khoản để sửa!");
            return;
        }
        
        String selectedItem = userList.getItem(selectedIndex);
        if (selectedItem.contains("Chưa có") || selectedItem.contains("Không tìm thấy")) {
            return;
        }
        
        try {
            // Extract user ID
            String idStr = selectedItem.substring(0, 4).trim();
            int userId = Integer.parseInt(idStr);
            
            // Get user details
            java.util.List<User> users = controller.getAllUsers();  // EXPLICIT
            User user = null;
            for (User u : users) {
                if (u.getUserId() == userId) {
                    user = u;
                    break;
                }
            }
            
            if (user != null) {
                isEditMode = true;
                editingUserId = userId;
                showUserDialog("Sửa thông tin tài khoản", user);
            }
        } catch (Exception e) {
            showError("Lỗi: " + e.getMessage());
        }
    }
    
    private void resetPassword() {
        int selectedIndex = userList.getSelectedIndex();
        if (selectedIndex < 0) {
            showError("Vui lòng chọn một tài khoản!");
            return;
        }
        
        String selectedItem = userList.getItem(selectedIndex);
        if (selectedItem.contains("Chưa có") || selectedItem.contains("Không tìm thấy")) {
            return;
        }
        
        if (confirmAction("Bạn có chắc muốn đặt lại mật khẩu cho tài khoản này?\nMật khẩu mới sẽ là: 123456")) {
            showMessage("Đặt lại mật khẩu", "Chức năng đặt lại mật khẩu sẽ được triển khai");
        }
    }
    
    private void deleteUser() {
        int selectedIndex = userList.getSelectedIndex();
        if (selectedIndex < 0) {
            showError("Vui lòng chọn một tài khoản để xóa!");
            return;
        }
        
        String selectedItem = userList.getItem(selectedIndex);
        if (selectedItem.contains("Chưa có") || selectedItem.contains("Không tìm thấy")) {
            return;
        }
        
        if (confirmAction("Bạn có chắc muốn xóa tài khoản này?")) {
            try {
                // Extract user ID
                String idStr = selectedItem.substring(0, 4).trim();
                int userId = Integer.parseInt(idStr);
                
                boolean success = controller.deleteUser(userId);
                if (success) {
                    showSuccess("Xóa tài khoản thành công!");
                    loadUsers();
                } else {
                    showError("Xóa tài khoản thất bại!");
                }
            } catch (Exception e) {
                showError("Lỗi: " + e.getMessage());
            }
        }
    }
    
    private String truncate(String str, int length) {
        if (str == null) return "";
        if (str.length() <= length) return str;
        return str.substring(0, length - 3) + "...";
    }
    
    private boolean confirmAction(String message) {
        Frame parentFrame = getParentFrame();
        Dialog confirmDialog = new Dialog(parentFrame, "Xác nhận", true);
        confirmDialog.setLayout(new BorderLayout(10, 10));
        
        Label label = new Label(message, Label.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        confirmDialog.add(label, BorderLayout.CENTER);
        
        Panel btnPanel = new Panel(new FlowLayout());
        final boolean[] result = {false};
        
        Button yesBtn = new Button("Có");
        yesBtn.setPreferredSize(new Dimension(80, 30));
        yesBtn.setBackground(new Color(46, 204, 113));
        yesBtn.setForeground(Color.WHITE);
        yesBtn.addActionListener(e -> {
            result[0] = true;
            confirmDialog.dispose();
        });
        
        Button noBtn = new Button("Không");
        noBtn.setPreferredSize(new Dimension(80, 30));
        noBtn.setBackground(new Color(231, 76, 60));
        noBtn.setForeground(Color.WHITE);
        noBtn.addActionListener(e -> confirmDialog.dispose());
        
        btnPanel.add(yesBtn);
        btnPanel.add(noBtn);
        confirmDialog.add(btnPanel, BorderLayout.SOUTH);
        
        confirmDialog.setSize(400, 120);
        confirmDialog.setLocationRelativeTo(parentFrame);
        confirmDialog.setVisible(true);
        
        return result[0];
    }
    
    private void showError(String message) {
        Frame parentFrame = getParentFrame();
        if (parentFrame != null) {
            ErrorHandler.showError(parentFrame, message);
        }
    }
    
    private void showSuccess(String message) {
        showMessage("Thành công", message);
    }
    
    private void showMessage(String title, String message) {
        Frame parentFrame = getParentFrame();
        Dialog dialog = new Dialog(parentFrame, title, true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        TextArea textArea = new TextArea(message, 6, 50, TextArea.SCROLLBARS_VERTICAL_ONLY);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        dialog.add(textArea, BorderLayout.CENTER);
        
        Button okButton = new Button("OK");
        okButton.setPreferredSize(new Dimension(80, 30));
        okButton.addActionListener(e -> dialog.dispose());
        Panel btnPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(okButton);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }
    
    private Frame getParentFrame() {
        Container parent = getParent();
        while (parent != null && !(parent instanceof Frame)) {
            parent = parent.getParent();
        }
        return (Frame) parent;
    }
}