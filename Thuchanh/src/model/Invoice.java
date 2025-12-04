package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private int invoiceId;
    private String invoiceCode;
    private LocalDateTime invoiceDate;
    private String customerName;
    private String customerPhone;
    private double totalAmount;
    private double discount;
    private double finalAmount;
    private String paymentMethod;
    private String status;
    private String createdBy;
    private List<InvoiceItem> items;
    
    public Invoice() {
        this.items = new ArrayList<>();
    }
    
    public int getInvoiceId() {
        return invoiceId;
    }
    
    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }
    
    public String getInvoiceCode() {
        return invoiceCode;
    }
    
    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }
    
    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }
    
    public void setInvoiceDate(LocalDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerPhone() {
        return customerPhone;
    }
    
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public double getDiscount() {
        return discount;
    }
    
    public void setDiscount(double discount) {
        this.discount = discount;
    }
    
    public double getFinalAmount() {
        return finalAmount;
    }
    
    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public List<InvoiceItem> getItems() {
        return items;
    }
    
    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }
    
    public void addItem(InvoiceItem item) {
        this.items.add(item);
    }
    
    // Inner class cho chi tiết hóa đơn
    public static class InvoiceItem {
        private int itemId;
        private int productId;
        private String productName;
        private int quantity;
        private double unitPrice;
        private double subtotal;
        
        public InvoiceItem() {
        }
        
        public InvoiceItem(int productId, String productName, int quantity, 
                          double unitPrice) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.subtotal = quantity * unitPrice;
        }
        
        public int getItemId() {
            return itemId;
        }
        
        public void setItemId(int itemId) {
            this.itemId = itemId;
        }
        
        public int getProductId() {
            return productId;
        }
        
        public void setProductId(int productId) {
            this.productId = productId;
        }
        
        public String getProductName() {
            return productName;
        }
        
        public void setProductName(String productName) {
            this.productName = productName;
        }
        
        public int getQuantity() {
            return quantity;
        }
        
        public void setQuantity(int quantity) {
            this.quantity = quantity;
            this.subtotal = quantity * unitPrice;
        }
        
        public double getUnitPrice() {
            return unitPrice;
        }
        
        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
            this.subtotal = quantity * unitPrice;
        }
        
        public double getSubtotal() {
            return subtotal;
        }
        
        public void setSubtotal(double subtotal) {
            this.subtotal = subtotal;
        }
    }
}