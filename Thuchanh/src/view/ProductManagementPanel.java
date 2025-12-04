// view/ProductManagementPanel.java
package view;

import java.awt.*;
import java.awt.event.*;
import controller.POSController;
import model.Product;
import utils.Validator;
import utils.ErrorHandler;

public class ProductManagementPanel extends Panel {
    private POSController controller;
    private java.awt.List productList;
    private TextField searchField;
    private Choice categoryChoice;
    private boolean isAdmin;
    
    public ProductManagementPanel(POSController controller) {
        this.controller = controller;
        this.isAdmin = controller.isAdmin();
        setupUI();
        loadProducts();
    }
    
    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Top panel - Tìm kiếm và thêm mới
        Panel topPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(new Color(52, 152, 219));
        
        Label titleLabel = new Label(" QUẢN LÝ SẢN PHẨM");
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
        
        Label categoryLabel = new Label("Danh mục:");
        categoryLabel.setForeground(Color.WHITE);
        topPanel.add(categoryLabel);
        categoryChoice = new Choice();
        categoryChoice.add("Tất cả");
        categoryChoice.add("Thực phẩm");
        categoryChoice.add("Đồ uống");
        categoryChoice.add("Văn phòng phẩm");
        categoryChoice.add("Gia dụng");
        categoryChoice.add("Khác");
        topPanel.add(categoryChoice);
        
        Button searchBtn = new Button("Tìm kiếm");
        searchBtn.setBackground(new Color(46, 204, 113));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.addActionListener(e -> searchProducts());
        topPanel.add(searchBtn);
        
        Button refreshBtn = new Button("Làm mới");
        refreshBtn.setBackground(new Color(241, 196, 15));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.addActionListener(e -> loadProducts());
        topPanel.add(refreshBtn);
        
        // Only admin can add products
        if (isAdmin) {
            Button addBtn = new Button("+ Thêm sản phẩm");
            addBtn.setBackground(new Color(46, 204, 113));
            addBtn.setForeground(Color.WHITE);
            addBtn.addActionListener(e -> showAddProductDialog());
            topPanel.add(addBtn);
        }
        
        add(topPanel, BorderLayout.NORTH);
        
        // Center - Danh sách sản phẩm
        Panel centerPanel = new Panel(new BorderLayout());
        
        Panel headerPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(236, 240, 241));
        Label headerLabel = new Label("Mã SP   | Tên sản phẩm              | Danh mục      | Giá         | Tồn kho | Đơn vị");
        headerLabel.setFont(new Font("Monospaced", Font.BOLD, 12));
        headerPanel.add(headerLabel);
        centerPanel.add(headerPanel, BorderLayout.NORTH);
        
        productList = new java.awt.List(22, false);
        productList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        centerPanel.add(productList, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Bottom - Buttons
        Panel bottomPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(new Color(236, 240, 241));
        
        Button viewBtn = new Button("Xem chi tiết");
        viewBtn.setPreferredSize(new Dimension(130, 35));
        viewBtn.setBackground(new Color(52, 152, 219));
        viewBtn.setForeground(Color.WHITE);
        viewBtn.addActionListener(e -> viewProductDetail());
        bottomPanel.add(viewBtn);
        
        if (isAdmin) {
            Button editBtn = new Button("Sửa");
            editBtn.setPreferredSize(new Dimension(130, 35));
            editBtn.setBackground(new Color(241, 196, 15));
            editBtn.setForeground(Color.WHITE);
            editBtn.addActionListener(e -> editProduct());
            bottomPanel.add(editBtn);
            
            Button deleteBtn = new Button("Xóa");
            deleteBtn.setPreferredSize(new Dimension(130, 35));
            deleteBtn.setBackground(new Color(231, 76, 60));
            deleteBtn.setForeground(Color.WHITE);
            deleteBtn.addActionListener(e -> deleteProduct());
            bottomPanel.add(deleteBtn);
        }
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void loadProducts() {
        productList.removeAll();
        java.util.List<Product> products = controller.getAllProducts();
        
        if (products.isEmpty()) {
            productList.add("Chưa có sản phẩm nào trong hệ thống.");
            return;
        }
        
        for (Product product : products) {
            String item = String.format("%-7s | %-27s | %-13s | %,10.0f₫ | %-7d | %s",
                product.getProductCode(),
                truncate(product.getProductName(), 27),
                truncate(product.getCategory(), 13),
                product.getPrice(),
                product.getStockQuantity(),
                product.getUnit()
            );
            productList.add(item);
        }
    }
    
    private void searchProducts() {
        String keyword = searchField.getText().trim();
        
        productList.removeAll();
        java.util.List<Product> products;
        
        if (keyword.isEmpty()) {
            products = controller.getAllProducts();
        } else {
            products = controller.searchProducts(keyword);
        }
        
        // Filter by category if needed
        String selectedCategory = categoryChoice.getSelectedItem();
        if (!"Tất cả".equals(selectedCategory)) {
            products.removeIf(p -> !p.getCategory().equals(selectedCategory));
        }
        
        if (products.isEmpty()) {
            productList.add("Không tìm thấy sản phẩm phù hợp.");
            return;
        }
        
        for (Product product : products) {
            String item = String.format("%-7s | %-27s | %-13s | %,10.0f₫ | %-7d | %s",
                product.getProductCode(),
                truncate(product.getProductName(), 27),
                truncate(product.getCategory(), 13),
                product.getPrice(),
                product.getStockQuantity(),
                product.getUnit()
            );
            productList.add(item);
        }
    }
    
    private void showAddProductDialog() {
        showProductDialog("Thêm sản phẩm mới", null, false);
    }
    
    private void showProductDialog(String title, Product product, boolean isEdit) {
        Frame parentFrame = getParentFrame();
        Dialog dialog = new Dialog(parentFrame, title, true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Mã sản phẩm
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new Label("Mã sản phẩm: *"), gbc);
        
        gbc.gridx = 1;
        TextField codeField = new TextField(30);
        if (product != null) codeField.setText(product.getProductCode());
        dialog.add(codeField, gbc);
        
        // Tên sản phẩm
        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new Label("Tên sản phẩm: *"), gbc);
        
        gbc.gridx = 1;
        TextField nameField = new TextField(30);
        if (product != null) nameField.setText(product.getProductName());
        dialog.add(nameField, gbc);
        
        // Danh mục
        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new Label("Danh mục: *"), gbc);
        
        gbc.gridx = 1;
        Choice catChoice = new Choice();
        catChoice.add("Thực phẩm");
        catChoice.add("Đồ uống");
        catChoice.add("Văn phòng phẩm");
        catChoice.add("Gia dụng");
        catChoice.add("Khác");
        if (product != null) catChoice.select(product.getCategory());
        dialog.add(catChoice, gbc);
        
        // Giá
        gbc.gridx = 0;
        gbc.gridy = 3;
        dialog.add(new Label("Giá: *"), gbc);
        
        gbc.gridx = 1;
        TextField priceField = new TextField(30);
        if (product != null) priceField.setText(String.valueOf(product.getPrice()));
        dialog.add(priceField, gbc);
        
        // Số lượng tồn kho
        gbc.gridx = 0;
        gbc.gridy = 4;
        dialog.add(new Label("Tồn kho: *"), gbc);
        
        gbc.gridx = 1;
        TextField stockField = new TextField(30);
        if (product != null) stockField.setText(String.valueOf(product.getStockQuantity()));
        dialog.add(stockField, gbc);
        
        // Đơn vị
        gbc.gridx = 0;
        gbc.gridy = 5;
        dialog.add(new Label("Đơn vị: *"), gbc);
        
        gbc.gridx = 1;
        Choice unitChoice = new Choice();
        unitChoice.add("cái");
        unitChoice.add("hộp");
        unitChoice.add("kg");
        unitChoice.add("lít");
        unitChoice.add("gói");
        unitChoice.add("chai");
        if (product != null) unitChoice.select(product.getUnit());
        dialog.add(unitChoice, gbc);
        
        // Mô tả
        gbc.gridx = 0;
        gbc.gridy = 6;
        dialog.add(new Label("Mô tả:"), gbc);
        
        gbc.gridx = 1;
        TextArea descArea = new TextArea(title, 3, 30, TextArea.SCROLLBARS_VERTICAL_ONLY);
        if (product != null && product.getDescription() != null) {
            descArea.setText(product.getDescription());
        }
        dialog.add(descArea, gbc);
        
        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        Button saveBtn = new Button("Lưu");
        saveBtn.setPreferredSize(new Dimension(100, 35));
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> {
            if (saveProduct(product, codeField, nameField, catChoice, 
                          priceField, stockField, unitChoice, descArea, isEdit)) {
                dialog.dispose();
                loadProducts();
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
        
        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }
    
    private boolean saveProduct(Product existingProduct, TextField codeField, 
                               TextField nameField, Choice catChoice,
                               TextField priceField, TextField stockField,
                               Choice unitChoice, TextArea descArea, boolean isEdit) {
        // Validation
        if (Validator.isEmpty(codeField.getText())) {
            showError("Vui lòng nhập mã sản phẩm!");
            return false;
        }
        
        if (Validator.isEmpty(nameField.getText())) {
            showError("Vui lòng nhập tên sản phẩm!");
            return false;
        }
        
        if (Validator.isEmpty(priceField.getText())) {
            showError("Vui lòng nhập giá!");
            return false;
        }
        
        if (Validator.isEmpty(stockField.getText())) {
            showError("Vui lòng nhập số lượng tồn kho!");
            return false;
        }
        
        try {
            double price = Double.parseDouble(priceField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());
            
            if (price < 0 || stock < 0) {
                showError("Giá và số lượng phải là số dương!");
                return false;
            }
            
            // Create/Update product
            Product product = isEdit ? existingProduct : new Product();
            product.setProductCode(codeField.getText().trim());
            product.setProductName(nameField.getText().trim());
            product.setCategory(catChoice.getSelectedItem());
            product.setPrice(price);
            product.setStockQuantity(stock);
            product.setUnit(unitChoice.getSelectedItem());
            product.setDescription(descArea.getText().trim());
            
            boolean success;
            if (isEdit) {
                success = controller.updateProduct(product);
            } else {
                success = controller.addProduct(product);
            }
            
            if (success) {
                showSuccess(isEdit ? "Cập nhật sản phẩm thành công!" : "Thêm sản phẩm thành công!");
                return true;
            } else {
                showError(isEdit ? "Cập nhật sản phẩm thất bại!" : "Thêm sản phẩm thất bại!");
                return false;
            }
        } catch (NumberFormatException ex) {
            showError("Giá và số lượng phải là số hợp lệ!");
            return false;
        }
    }
    
    private void viewProductDetail() {
        int selectedIndex = productList.getSelectedIndex();
        if (selectedIndex < 0) {
            showError("Vui lòng chọn một sản phẩm!");
            return;
        }
        
        String selectedItem = productList.getItem(selectedIndex);
        if (selectedItem.contains("Chưa có") || selectedItem.contains("Không tìm thấy")) {
            return;
        }
        
        showMessage("Chi tiết sản phẩm", selectedItem);
    }
    
    private void editProduct() {
        int selectedIndex = productList.getSelectedIndex();
        if (selectedIndex < 0) {
            showError("Vui lòng chọn một sản phẩm để sửa!");
            return;
        }
        
        String selectedItem = productList.getItem(selectedIndex);
        if (selectedItem.contains("Chưa có") || selectedItem.contains("Không tìm thấy")) {
            return;
        }
        
        try {
            // Extract product code
            String code = selectedItem.substring(0, 7).trim();
            
            // Get product details
            java.util.List<Product> products = controller.getAllProducts();
            Product product = null;
            for (Product p : products) {
                if (p.getProductCode().equals(code)) {
                    product = p;
                    break;
                }
            }
            
            if (product != null) {
                showProductDialog("Sửa thông tin sản phẩm", product, true);
            }
        } catch (Exception e) {
            showError("Lỗi: " + e.getMessage());
        }
    }
    
    private void deleteProduct() {
        int selectedIndex = productList.getSelectedIndex();
        if (selectedIndex < 0) {
            showError("Vui lòng chọn một sản phẩm để xóa!");
            return;
        }
        
        String selectedItem = productList.getItem(selectedIndex);
        if (selectedItem.contains("Chưa có") || selectedItem.contains("Không tìm thấy")) {
            return;
        }
        
        if (confirmAction("Bạn có chắc muốn xóa sản phẩm này?")) {
            try {
                // Extract product code
                String code = selectedItem.substring(0, 7).trim();
                
                // Get product ID
                java.util.List<Product> products = controller.getAllProducts();
                for (Product p : products) {
                    if (p.getProductCode().equals(code)) {
                        boolean success = controller.deleteProduct(p.getProductId());
                        if (success) {
                            showSuccess("Xóa sản phẩm thành công!");
                            loadProducts();
                        } else {
                            showError("Xóa sản phẩm thất bại!");
                        }
                        break;
                    }
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
        
        confirmDialog.setSize(350, 120);
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