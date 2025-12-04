package model;

public class Product {
    private int productId;
    private String productCode;
    private String productName;
    private String category;
    private double price;
    private int stockQuantity;
    private String unit;
    private String description;
    
    public Product() {
    }
    
    public Product(int productId, String productCode, String productName, 
                   String category, double price, int stockQuantity, String unit) {
        this.productId = productId;
        this.productCode = productCode;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.unit = unit;
    }
    
    // Getters
    public int getProductId() {
        return productId;
    }
    
    public String getProductCode() {
        return productCode;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public String getCategory() {
        return category;
    }
    
    public double getPrice() {
        return price;
    }
    
    public int getStockQuantity() {
        return stockQuantity;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public String getDescription() {
        return description;
    }
    
    // Setters
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", unit='" + unit + '\'' +
                '}';
    }
}