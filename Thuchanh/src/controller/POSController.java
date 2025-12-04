package controller;

import network.TCPClient;
import network.ProtocolHandler;
import model.*;
import java.util.ArrayList;
import java.util.List;

public class POSController {
 private TCPClient client;
 private User currentUser;
 
 public POSController() {
     client = new TCPClient("localhost", 8888);
 }
 
 // ===================== CONNECTION =====================
 
 public boolean connectToServer() {
     try {
         return client.connect();
     } catch (Exception e) {
         System.err.println("Lỗi kết nối server: " + e.getMessage());
         return false;
     }
 }
 
 public void disconnect() {
     try {
         client.disconnect();
     } catch (Exception e) {
         System.err.println("Lỗi đóng kết nối: " + e.getMessage());
     }
 }
 
 // ===================== AUTHENTICATION =====================
 
 public boolean login(String username, String password) {
     try {
         String request = ProtocolHandler.createRequest(
             ProtocolHandler.CMD_LOGIN, username, password
         );
         String response = client.sendRequest(request);
         
         if (response == null) {
             return false;
         }
         
         String[] parts = ProtocolHandler.parseResponse(response);
         if ("SUCCESS".equals(parts[0])) {
             if (parts.length >= 5) {
                 currentUser = new User();
                 currentUser.setUserId(Integer.parseInt(parts[1]));
                 currentUser.setUsername(parts[2]);
                 currentUser.setFullName(parts[3]);
                 currentUser.setRole(parts[4]);
                 return true;
             }
         }
         return false;
     } catch (Exception e) {
         System.err.println("Lỗi đăng nhập: " + e.getMessage());
         return false;
     }
 }
 
 public void logout() {
     try {
         String request = ProtocolHandler.createRequest(ProtocolHandler.CMD_LOGOUT);
         client.sendRequest(request);
         this.currentUser = null;
     } catch (Exception e) {
         System.err.println("Lỗi đăng xuất: " + e.getMessage());
     }
 }
 
 public User getCurrentUser() {
     return currentUser;
 }
 
 public boolean isAdmin() {
     return currentUser != null && currentUser.isAdmin();
 }
 
 // ===================== PRODUCT MANAGEMENT =====================
 
 public List<Product> getAllProducts() {
     try {
         String request = ProtocolHandler.createRequest(
             ProtocolHandler.CMD_GET_PRODUCTS
         );
         String response = client.sendRequest(request);
         
         if (response == null) {
             return new ArrayList<>();
         }
         
         return parseProductList(response);
     } catch (Exception e) {
         System.err.println("Lỗi lấy danh sách sản phẩm: " + e.getMessage());
         return new ArrayList<>();
     }
 }
 
 public List<Product> searchProducts(String keyword) {
     try {
         String request = ProtocolHandler.createRequest(
             ProtocolHandler.CMD_SEARCH_PRODUCT, 
             keyword != null ? keyword : ""
         );
         String response = client.sendRequest(request);
         
         if (response == null) {
             return new ArrayList<>();
         }
         
         return parseProductList(response);
     } catch (Exception e) {
         System.err.println("Lỗi tìm kiếm sản phẩm: " + e.getMessage());
         return new ArrayList<>();
     }
 }
 
 public boolean addProduct(Product product) {
     try {
         String request = ProtocolHandler.createRequest(
             ProtocolHandler.CMD_ADD_PRODUCT,
             product.getProductCode(),
             product.getProductName(),
             product.getCategory(),
             String.valueOf(product.getPrice()),
             String.valueOf(product.getStockQuantity()),
             product.getUnit(),
             product.getDescription() != null ? product.getDescription() : ""
         );
         String response = client.sendRequest(request);
         
         if (response == null) {
             return false;
         }
         
         String[] parts = ProtocolHandler.parseResponse(response);
         return "SUCCESS".equals(parts[0]);
     } catch (Exception e) {
         System.err.println("Lỗi thêm sản phẩm: " + e.getMessage());
         return false;
     }
 }
 
 public boolean updateProduct(Product product) {
     try {
         String request = ProtocolHandler.createRequest(
             ProtocolHandler.CMD_UPDATE_PRODUCT,
             String.valueOf(product.getProductId()),
             product.getProductCode(),
             product.getProductName(),
             product.getCategory(),
             String.valueOf(product.getPrice()),
             String.valueOf(product.getStockQuantity()),
             product.getUnit(),
             product.getDescription() != null ? product.getDescription() : ""
         );
         String response = client.sendRequest(request);
         
         if (response == null) {
             return false;
         }
         
         String[] parts = ProtocolHandler.parseResponse(response);
         return "SUCCESS".equals(parts[0]);
     } catch (Exception e) {
         System.err.println("Lỗi cập nhật sản phẩm: " + e.getMessage());
         return false;
     }
 }
 
 public boolean deleteProduct(int productId) {
     try {
         String request = ProtocolHandler.createRequest(
             ProtocolHandler.CMD_DELETE_PRODUCT,
             String.valueOf(productId)
         );
         String response = client.sendRequest(request);
         
         if (response == null) {
             return false;
         }
         
         String[] parts = ProtocolHandler.parseResponse(response);
         return "SUCCESS".equals(parts[0]);
     } catch (Exception e) {
         System.err.println("Lỗi xóa sản phẩm: " + e.getMessage());
         return false;
     }
 }
 
 private List<Product> parseProductList(String response) {
     List<Product> products = new ArrayList<>();
     
     if (response == null || response.isEmpty()) {
         return products;
     }
     
     String[] parts = ProtocolHandler.parseResponse(response);
     
     if (!"SUCCESS".equals(parts[0])) {
         return products;
     }
     
     for (int i = 1; i < parts.length; i++) {
         try {
             String[] productData = parts[i].split(",");
             if (productData.length >= 7) {
                 Product product = new Product();
                 product.setProductId(Integer.parseInt(productData[0].trim()));
                 product.setProductCode(productData[1].trim());
                 product.setProductName(productData[2].trim());
                 product.setCategory(productData[3].trim());
                 product.setPrice(Double.parseDouble(productData[4].trim()));
                 product.setStockQuantity(Integer.parseInt(productData[5].trim()));
                 product.setUnit(productData[6].trim());
                 products.add(product);
             }
         } catch (Exception e) {
             System.err.println("Lỗi parse product data: " + e.getMessage());
         }
     }
     return products;
 }
 
 // ===================== INVOICE MANAGEMENT =====================
 
 public String createInvoice(Invoice invoice) {
     try {
         StringBuilder itemsStr = new StringBuilder();
         for (int i = 0; i < invoice.getItems().size(); i++) {
             Invoice.InvoiceItem item = invoice.getItems().get(i);
             if (i > 0) itemsStr.append(";");
             itemsStr.append(item.getProductId())
                    .append(":")
                    .append(item.getQuantity())
                    .append(":")
                    .append(item.getUnitPrice());
         }
         
         String request = ProtocolHandler.createRequest(
             ProtocolHandler.CMD_CREATE_INVOICE,
             invoice.getCustomerName() != null ? invoice.getCustomerName() : "",
             invoice.getCustomerPhone() != null ? invoice.getCustomerPhone() : "",
             String.valueOf(invoice.getTotalAmount()),
             String.valueOf(invoice.getDiscount()),
             String.valueOf(invoice.getFinalAmount()),
             invoice.getPaymentMethod(),
             itemsStr.toString()
         );
         String response = client.sendRequest(request);
         return response != null ? response : "ERROR|Không thể tạo hóa đơn";
     } catch (Exception e) {
         System.err.println("Lỗi tạo hóa đơn: " + e.getMessage());
         return "ERROR|" + e.getMessage();
     }
 }
 
 public List<Invoice> getAllInvoices() {
     try {
         String request = ProtocolHandler.createRequest(
             ProtocolHandler.CMD_GET_INVOICES
         );
         String response = client.sendRequest(request);
         
         if (response == null) {
             return new ArrayList<>();
         }
         
         return parseInvoiceList(response);
     } catch (Exception e) {
         System.err.println("Lỗi lấy danh sách hóa đơn: " + e.getMessage());
         return new ArrayList<>();
     }
 }
 
 private List<Invoice> parseInvoiceList(String response) {
     List<Invoice> invoices = new ArrayList<>();
     
     if (response == null || response.isEmpty()) {
         return invoices;
     }
     
     String[] parts = ProtocolHandler.parseResponse(response);
     
     if (!"SUCCESS".equals(parts[0])) {
         return invoices;
     }
     
     for (int i = 1; i < parts.length; i++) {
         try {
             String[] invoiceData = parts[i].split(",");
             if (invoiceData.length >= 6) {
                 Invoice invoice = new Invoice();
                 invoice.setInvoiceId(Integer.parseInt(invoiceData[0].trim()));
                 invoice.setInvoiceCode(invoiceData[1].trim());
                 invoice.setCustomerName(invoiceData[3].trim());
                 invoice.setFinalAmount(Double.parseDouble(invoiceData[4].trim()));
                 invoice.setStatus(invoiceData[5].trim());
                 invoices.add(invoice);
             }
         } catch (Exception e) {
             System.err.println("Lỗi parse invoice data: " + e.getMessage());
         }
     }
     return invoices;
 }
 
 // ===================== USER MANAGEMENT =====================
 
 public List<User> getAllUsers() {
     try {
         String request = ProtocolHandler.createRequest(
             ProtocolHandler.CMD_GET_USERS
         );
         String response = client.sendRequest(request);
         
         if (response == null) {
             return new ArrayList<>();
         }
         
         return parseUserList(response);
     } catch (Exception e) {
         System.err.println("Lỗi lấy danh sách tài khoản: " + e.getMessage());
         return new ArrayList<>();
     }
 }
 
 public boolean createUser(User user) {
     try {
         String request = ProtocolHandler.createRequest(
             ProtocolHandler.CMD_CREATE_USER,
             user.getUsername(),
             user.getPassword(),
             user.getFullName(),
             user.getRole(),
             user.getEmail() != null ? user.getEmail() : "",
             user.getPhoneNumber() != null ? user.getPhoneNumber() : ""
         );
         String response = client.sendRequest(request);
         
         if (response == null) {
             return false;
         }
         
         String[] parts = ProtocolHandler.parseResponse(response);
         return "SUCCESS".equals(parts[0]);
     } catch (Exception e) {
         System.err.println("Lỗi tạo tài khoản: " + e.getMessage());
         return false;
     }
 }
 
 public boolean updateUser(User user) {
     try {
         String request = ProtocolHandler.createRequest(
             ProtocolHandler.CMD_UPDATE_USER,
             String.valueOf(user.getUserId()),
             user.getUsername(),
             user.getFullName(),
             user.getRole(),
             user.getEmail() != null ? user.getEmail() : "",
             user.getPhoneNumber() != null ? user.getPhoneNumber() : ""
         );
         String response = client.sendRequest(request);
         
         if (response == null) {
             return false;
         }
         
         String[] parts = ProtocolHandler.parseResponse(response);
         return "SUCCESS".equals(parts[0]);
     } catch (Exception e) {
         System.err.println("Lỗi cập nhật tài khoản: " + e.getMessage());
         return false;
     }
 }
 
 public boolean deleteUser(int userId) {
     try {
         String request = ProtocolHandler.createRequest(
             ProtocolHandler.CMD_DELETE_USER,
             String.valueOf(userId)
         );
         String response = client.sendRequest(request);
         
         if (response == null) {
             return false;
         }
         
         String[] parts = ProtocolHandler.parseResponse(response);
         return "SUCCESS".equals(parts[0]);
     } catch (Exception e) {
         System.err.println("Lỗi xóa tài khoản: " + e.getMessage());
         return false;
     }
 }
 
 private List<User> parseUserList(String response) {
     List<User> users = new ArrayList<>();
     
     if (response == null || response.isEmpty()) {
         return users;
     }
     
     String[] parts = ProtocolHandler.parseResponse(response);
     
     if (!"SUCCESS".equals(parts[0])) {
         return users;
     }
     
     for (int i = 1; i < parts.length; i++) {
         try {
             String[] userData = parts[i].split(",");
             if (userData.length >= 7) {
                 User user = new User();
                 user.setUserId(Integer.parseInt(userData[0].trim()));
                 user.setUsername(userData[1].trim());
                 user.setFullName(userData[2].trim());
                 user.setRole(userData[3].trim());
                 user.setEmail(userData[4].trim());
                 user.setPhoneNumber(userData[5].trim());
                 user.setActive(Boolean.parseBoolean(userData[6].trim()));
                 users.add(user);
             }
         } catch (Exception e) {
             System.err.println("Lỗi parse user data: " + e.getMessage());
         }
     }
     return users;
 }
 
 // ===================== REPORTS =====================
 
 public String getRevenueReport(String startDate, String endDate) {
     try {
         String request = ProtocolHandler.createRequest(
             ProtocolHandler.CMD_GET_REVENUE_REPORT,
             startDate,
             endDate
         );
         String response = client.sendRequest(request);
         return response != null ? response : "ERROR|Không thể lấy báo cáo";
     } catch (Exception e) {
         System.err.println("Lỗi lấy báo cáo doanh thu: " + e.getMessage());
         return "ERROR|" + e.getMessage();
     }
 }
 
 public String getDailyReport() {
     try {
         String request = ProtocolHandler.createRequest(
             ProtocolHandler.CMD_GET_DAILY_REPORT
         );
         String response = client.sendRequest(request);
         return response != null ? response : "ERROR|Không thể lấy báo cáo";
     } catch (Exception e) {
         System.err.println("Lỗi lấy báo cáo ngày: " + e.getMessage());
         return "ERROR|" + e.getMessage();
     }
 }
}