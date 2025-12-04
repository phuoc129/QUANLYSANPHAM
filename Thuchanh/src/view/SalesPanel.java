// view/SalesPanel.java
package view;

import java.awt.*;
import java.awt.event.*;
import controller.POSController;
import model.Product;
import model.Invoice;
import utils.Validator;
import java.util.ArrayList;

public class SalesPanel extends Panel {
    private POSController controller;
    private java.awt.List cartList;
    private TextField searchProductField;
    private TextField customerNameField;
    private TextField customerPhoneField;
    private TextField discountField;
    private Label totalLabel;
    private Label finalLabel;
    private Choice paymentChoice;
    private ArrayList<CartItem> cartItems;
    
    public SalesPanel(POSController controller) {
        this.controller = controller;
        this.cartItems = new ArrayList<>();
        setupUI();
    }
    
    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Top panel - Title
        Panel topPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(46, 204, 113));
        Label titleLabel = new Label("BÁN HÀNG - TẠO HÓA ĐƠN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);
        add(topPanel, BorderLayout.NORTH);
        
        // Center - Split into left (products) and right (cart)
        Panel centerPanel = new Panel(new GridLayout(1, 2, 10, 0));
        
        // Left - Product selection
        Panel leftPanel = new Panel(new BorderLayout(5, 5));
        
        Panel searchPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new Label("Tìm sản phẩm:"));
        searchProductField = new TextField(20);
        searchPanel.add(searchProductField);
        Button searchBtn = new Button("Tìm");
        searchBtn.addActionListener(e -> searchProduct());
        searchPanel.add(searchBtn);
        leftPanel.add(searchPanel, BorderLayout.NORTH);
        
        java.awt.List productSearchList = new java.awt.List(20);
        productSearchList.setFont(new Font("Monospaced", Font.PLAIN, 11));
        leftPanel.add(productSearchList, BorderLayout.CENTER);
        
        Button addToCartBtn = new Button("Thêm vào giỏ →");
        addToCartBtn.setBackground(new Color(52, 152, 219));
        addToCartBtn.setForeground(Color.WHITE);
        addToCartBtn.setPreferredSize(new Dimension(150, 35));
        addToCartBtn.addActionListener(e -> addToCart(productSearchList));
        Panel addBtnPanel = new Panel(new FlowLayout());
        addBtnPanel.add(addToCartBtn);
        leftPanel.add(addBtnPanel, BorderLayout.SOUTH);
        
        centerPanel.add(leftPanel);
        
        // Right - Shopping cart
        Panel rightPanel = new Panel(new BorderLayout(5, 5));
        
        Panel cartHeaderPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        cartHeaderPanel.setBackground(new Color(236, 240, 241));
        Label cartHeaderLabel = new Label("GIỎ HÀNG");
        cartHeaderLabel.setFont(new Font("Arial", Font.BOLD, 13));
        cartHeaderPanel.add(cartHeaderLabel);
        rightPanel.add(cartHeaderPanel, BorderLayout.NORTH);
        
        cartList = new java.awt.List(15);
        cartList.setFont(new Font("Monospaced", Font.PLAIN, 11));
        rightPanel.add(cartList, BorderLayout.CENTER);
        
        Panel cartButtonPanel = new Panel(new FlowLayout());
        Button removeBtn = new Button("Xóa");
        removeBtn.setBackground(new Color(231, 76, 60));
        removeBtn.setForeground(Color.WHITE);
        removeBtn.addActionListener(e -> removeFromCart());
        cartButtonPanel.add(removeBtn);
        
        Button clearBtn = new Button("Xóa tất cả");
        clearBtn.setBackground(new Color(149, 165, 166));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.addActionListener(e -> clearCart());
        cartButtonPanel.add(clearBtn);
        rightPanel.add(cartButtonPanel, BorderLayout.SOUTH);
        
        centerPanel.add(rightPanel);
        add(centerPanel, BorderLayout.CENTER);
        
        // Bottom - Customer info and payment
        Panel bottomPanel = new Panel(new BorderLayout(10, 10));
        bottomPanel.setBackground(new Color(236, 240, 241));
        
        // Customer info
        Panel customerPanel = new Panel(new GridLayout(3, 2, 5, 5));
        customerPanel.add(new Label("Tên khách hàng:"));
        customerNameField = new TextField(20);
        customerPanel.add(customerNameField);
        
        customerPanel.add(new Label("Số điện thoại:"));
        customerPhoneField = new TextField(20);
        customerPanel.add(customerPhoneField);
        
        customerPanel.add(new Label("Giảm giá (VNĐ):"));
        discountField = new TextField(20);
        discountField.setText("0");
        discountField.addTextListener(e -> updateTotal());
        customerPanel.add(discountField);
        
        bottomPanel.add(customerPanel, BorderLayout.WEST);
        
        // Total and payment
        Panel paymentPanel = new Panel(new GridLayout(5, 1, 5, 5));
        
        totalLabel = new Label("Tổng tiền: 0 VNĐ");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalLabel.setForeground(new Color(52, 73, 94));
        paymentPanel.add(totalLabel);
        
        finalLabel = new Label("Thành tiền: 0 VNĐ");
        finalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        finalLabel.setForeground(new Color(231, 76, 60));
        paymentPanel.add(finalLabel);
        
        Panel methodPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        methodPanel.add(new Label("Thanh toán:"));
        paymentChoice = new Choice();
        paymentChoice.add("Tiền mặt");
        paymentChoice.add("Chuyển khoản");
        paymentChoice.add("Thẻ");
        methodPanel.add(paymentChoice);
        paymentPanel.add(methodPanel);
        
        Button createInvoiceBtn = new Button("TẠO HÓA ĐƠN");
        createInvoiceBtn.setFont(new Font("Arial", Font.BOLD, 14));
        createInvoiceBtn.setPreferredSize(new Dimension(200, 45));
        createInvoiceBtn.setBackground(new Color(46, 204, 113));
        createInvoiceBtn.setForeground(Color.WHITE);
        createInvoiceBtn.addActionListener(e -> createInvoice());
        paymentPanel.add(createInvoiceBtn);
        
        Button printBtn = new Button("IN HÓA ĐƠN");
        printBtn.setPreferredSize(new Dimension(200, 35));
        printBtn.setBackground(new Color(52, 152, 219));
        printBtn.setForeground(Color.WHITE);
        printBtn.addActionListener(e -> printInvoice());
        paymentPanel.add(printBtn);
        
        bottomPanel.add(paymentPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Load initial products
        loadAllProducts(productSearchList);
    }
    
    private void loadAllProducts(java.awt.List list) {
        list.removeAll();
        java.util.List<Product> products = controller.getAllProducts();
        
        for (Product p : products) {
            String item = String.format("%-8s | %-30s | %,10.0f₫",
                p.getProductCode(),
                truncate(p.getProductName(), 30),
                p.getPrice()
            );
            list.add(item);
        }
    }
    
    private void searchProduct() {
        // Implementation for searching products
        String keyword = searchProductField.getText().trim();
        if (keyword.isEmpty()) {
            showError("Vui lòng nhập từ khóa tìm kiếm!");
            return;
        }
        
        showMessage("Tìm kiếm", "Chức năng tìm kiếm sẽ được triển khai");
    }
    
    private void addToCart(java.awt.List productList) {
        int selectedIndex = productList.getSelectedIndex();
        if (selectedIndex < 0) {
            showError("Vui lòng chọn sản phẩm để thêm vào giỏ!");
            return;
        }
        
        String selectedItem = productList.getItem(selectedIndex);
        String productCode = selectedItem.substring(0, 8).trim();
        
        // Find product
        java.util.List<Product> products = controller.getAllProducts();
        Product selectedProduct = null;
        for (Product p : products) {
            if (p.getProductCode().equals(productCode)) {
                selectedProduct = p;
                break;
            }
        }
        
        if (selectedProduct == null) {
            showError("Không tìm thấy sản phẩm!");
            return;
        }
        
        // Ask for quantity
        String qtyStr = showInputDialog("Nhập số lượng cho " + selectedProduct.getProductName() + ":");
        if (qtyStr == null || qtyStr.trim().isEmpty()) {
            return;
        }
        
        try {
            int quantity = Integer.parseInt(qtyStr.trim());
            if (quantity <= 0) {
                showError("Số lượng phải lớn hơn 0!");
                return;
            }
            
            if (quantity > selectedProduct.getStockQuantity()) {
                showError("Không đủ hàng trong kho!\nTồn kho: " + selectedProduct.getStockQuantity());
                return;
            }
            
            // Check if product already in cart
            CartItem existingItem = null;
            for (CartItem item : cartItems) {
                if (item.product.getProductId() == selectedProduct.getProductId()) {
                    existingItem = item;
                    break;
                }
            }
            
            if (existingItem != null) {
                existingItem.quantity += quantity;
            } else {
                cartItems.add(new CartItem(selectedProduct, quantity));
            }
            
            updateCartDisplay();
            updateTotal();
            
        } catch (NumberFormatException ex) {
            showError("Số lượng không hợp lệ!");
        }
    }
    
    private void removeFromCart() {
        int selectedIndex = cartList.getSelectedIndex();
        if (selectedIndex < 0) {
            showError("Vui lòng chọn sản phẩm để xóa!");
            return;
        }
        
        if (selectedIndex < cartItems.size()) {
            cartItems.remove(selectedIndex);
            updateCartDisplay();
            updateTotal();
        }
    }
    
    private void clearCart() {
        if (confirmAction("Bạn có chắc muốn xóa tất cả sản phẩm trong giỏ?")) {
            cartItems.clear();
            updateCartDisplay();
            updateTotal();
        }
    }
    
    private void updateCartDisplay() {
        cartList.removeAll();
        
        if (cartItems.isEmpty()) {
            cartList.add("Giỏ hàng trống");
            return;
        }
        
        for (CartItem item : cartItems) {
            String line = String.format("%-25s x%-3d | %,10.0f₫ | %,12.0f₫",
                truncate(item.product.getProductName(), 25),
                item.quantity,
                item.product.getPrice(),
                item.quantity * item.product.getPrice()
            );
            cartList.add(line);
        }
    }
    
    private void updateTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.quantity * item.product.getPrice();
        }
        
        double discount = 0;
        try {
            discount = Double.parseDouble(discountField.getText().trim());
        } catch (NumberFormatException e) {
            discount = 0;
        }
        
        double finalAmount = total - discount;
        if (finalAmount < 0) finalAmount = 0;
        
        totalLabel.setText(String.format("Tổng tiền: %,0f VNĐ", total));
        finalLabel.setText(String.format("Thành tiền: %,0f VNĐ", finalAmount));
    }
    
    private void createInvoice() {
        if (cartItems.isEmpty()) {
            showError("Giỏ hàng trống! Vui lòng thêm sản phẩm.");
            return;
        }
        
        // Validate customer info (optional)
        String customerName = customerNameField.getText().trim();
        String customerPhone = customerPhoneField.getText().trim();
        
        if (!customerPhone.isEmpty() && !Validator.isValidPhoneNumber(customerPhone)) {
            showError("Số điện thoại không hợp lệ!");
            return;
        }
        
        try {
            double total = 0;
            for (CartItem item : cartItems) {
                total += item.quantity * item.product.getPrice();
            }
            
            double discount = Double.parseDouble(discountField.getText().trim());
            double finalAmount = total - discount;
            
            if (finalAmount < 0) {
                showError("Giảm giá không được lớn hơn tổng tiền!");
                return;
            }
            
            // Create invoice object
            Invoice invoice = new Invoice();
            invoice.setCustomerName(customerName.isEmpty() ? "Khách lẻ" : customerName);
            invoice.setCustomerPhone(customerPhone);
            invoice.setTotalAmount(total);
            invoice.setDiscount(discount);
            invoice.setFinalAmount(finalAmount);
            invoice.setPaymentMethod(getPaymentMethod());
            invoice.setCreatedBy(controller.getCurrentUser().getUsername());
            
            // Add items
            for (CartItem cartItem : cartItems) {
                Invoice.InvoiceItem item = new Invoice.InvoiceItem(
                    cartItem.product.getProductId(),
                    cartItem.product.getProductName(),
                    cartItem.quantity,
                    cartItem.product.getPrice()
                );
                invoice.addItem(item);
            }
            
            // Send to server
            String response = controller.createInvoice(invoice);
            
            if (response != null && response.startsWith("SUCCESS")) {
                String[] parts = response.split("\\|");
                String invoiceCode = parts.length > 1 ? parts[1] : "N/A";
                
                showSuccess("Tạo hóa đơn thành công!\n\n" +
                          "Mã hóa đơn: " + invoiceCode + "\n" +
                          "Tổng tiền: " + String.format("%,0f VNĐ", finalAmount));
                
                // Clear form
                clearCart();
                customerNameField.setText("");
                customerPhoneField.setText("");
                discountField.setText("0");
                updateTotal();
            } else {
                showError("Tạo hóa đơn thất bại!\n" + response);
            }
            
        } catch (NumberFormatException e) {
            showError("Giảm giá không hợp lệ!");
        } catch (Exception e) {
            showError("Lỗi: " + e.getMessage());
        }
    }
    
    private void printInvoice() {
        if (cartItems.isEmpty()) {
            showError("Không có hóa đơn để in!");
            return;
        }
        
        showMessage("In hóa đơn", "Chức năng in hóa đơn sẽ được triển khai");
    }
    
    private String getPaymentMethod() {
        String selected = paymentChoice.getSelectedItem();
        switch (selected) {
            case "Tiền mặt": return "cash";
            case "Chuyển khoản": return "transfer";
            case "Thẻ": return "card";
            default: return "cash";
        }
    }
    
    private String showInputDialog(String message) {
        Frame parentFrame = getParentFrame();
        Dialog dialog = new Dialog(parentFrame, "Nhập thông tin", true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        Label label = new Label(message);
        dialog.add(label, BorderLayout.NORTH);
        
        TextField textField = new TextField(20);
        dialog.add(textField, BorderLayout.CENTER);
        
        Panel buttonPanel = new Panel(new FlowLayout());
        final String[] result = {null};
        
        Button okBtn = new Button("OK");
        okBtn.addActionListener(e -> {
            result[0] = textField.getText();
            dialog.dispose();
        });
        
        Button cancelBtn = new Button("Hủy");
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(okBtn);
        buttonPanel.add(cancelBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
        
        return result[0];
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
        confirmDialog.add(label, BorderLayout.CENTER);
        
        Panel btnPanel = new Panel(new FlowLayout());
        final boolean[] result = {false};
        
        Button yesBtn = new Button("Có");
        yesBtn.addActionListener(e -> {
            result[0] = true;
            confirmDialog.dispose();
        });
        
        Button noBtn = new Button("Không");
        noBtn.addActionListener(e -> confirmDialog.dispose());
        
        btnPanel.add(yesBtn);
        btnPanel.add(noBtn);
        confirmDialog.add(btnPanel, BorderLayout.SOUTH);
        
        confirmDialog.setSize(300, 100);
        confirmDialog.setLocationRelativeTo(parentFrame);
        confirmDialog.setVisible(true);
        
        return result[0];
    }
    
    private void showError(String message) {
        showMessage("Lỗi", message);
    }
    
    private void showSuccess(String message) {
        showMessage("Thành công", message);
    }
    
    private void showMessage(String title, String message) {
        Frame parentFrame = getParentFrame();
        Dialog dialog = new Dialog(parentFrame, title, true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        TextArea textArea = new TextArea(message, 6, 40, TextArea.SCROLLBARS_VERTICAL_ONLY);
        textArea.setEditable(false);
        dialog.add(textArea, BorderLayout.CENTER);
        
        Button okButton = new Button("OK");
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
    
    // Inner class for cart items
    private static class CartItem {
        Product product;
        int quantity;
        
        CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
    }
}