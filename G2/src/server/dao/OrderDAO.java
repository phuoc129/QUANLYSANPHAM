package server.dao;

import server.model.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ORDER DAO - Lưu trữ vào JSON File (Dữ liệu KHÔNG có cấu trúc)
 */
public class OrderDAO {
    private static final String ORDERS_FILE = "data/orders.json";
    private static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private Gson gson;

    public OrderDAO() {
        // Cấu hình Gson để xử lý LocalDateTime
        gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .setPrettyPrinting()
            .create();
        
        // Tạo thư mục data nếu chưa có
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
            System.out.println("✓ Đã tạo thư mục data/");
        }
        
        // Tạo file orders.json nếu chưa có
        File ordersFile = new File(ORDERS_FILE);
        if (!ordersFile.exists()) {
            try {
                ordersFile.createNewFile();
                saveOrders(new ArrayList<>());
                System.out.println("✓ Đã tạo file orders.json");
            } catch (IOException e) {
                System.err.println("✗ Lỗi tạo file orders.json: " + e.getMessage());
            }
        }
    }

    /**
     * Tạo đơn hàng mới
     */
    public boolean createOrder(Order order) {
        try {
            List<Order> orders = loadOrders();
            
            // Tạo ID tự động
            int newId = orders.isEmpty() ? 1 : 
                orders.stream().mapToInt(Order::getId).max().getAsInt() + 1;
            
            order.setId(newId);
            order.setOrderDate(LocalDateTime.now());
            order.setStatus("PENDING");
            
            orders.add(order);
            saveOrders(orders);
            
            System.out.println("  ✓ Đã lưu Order #" + newId + " vào JSON file");
            return true;
            
        } catch (Exception e) {
            System.err.println("✗ Lỗi createOrder: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy đơn hàng theo user ID
     */
    public List<Order> getOrdersByUserId(int userId) {
        try {
            List<Order> allOrders = loadOrders();
            return allOrders.stream()
                .filter(o -> o.getUserId() == userId)
                .sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()))
                .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("✗ Lỗi getOrdersByUserId: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Lấy tất cả đơn hàng (Admin)
     */
    public List<Order> getAllOrders() {
        try {
            List<Order> orders = loadOrders();
            return orders.stream()
                .sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()))
                .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("✗ Lỗi getAllOrders: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Đếm tổng số đơn hàng
     */
    public int getOrderCount() {
        try {
            return loadOrders().size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Tính tổng doanh thu
     */
    public double getTotalRevenue() {
        try {
            List<Order> orders = loadOrders();
            return orders.stream()
                .filter(o -> "COMPLETED".equals(o.getStatus()))
                .mapToDouble(Order::getTotalAmount)
                .sum();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Load orders từ JSON file
     */
    private List<Order> loadOrders() {
        try (Reader reader = new FileReader(ORDERS_FILE)) {
            Type listType = new TypeToken<ArrayList<Order>>(){}.getType();
            List<Order> orders = gson.fromJson(reader, listType);
            return orders != null ? orders : new ArrayList<>();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("✗ Lỗi đọc file JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Lưu orders vào JSON file
     */
    private void saveOrders(List<Order> orders) {
        try (Writer writer = new FileWriter(ORDERS_FILE)) {
            gson.toJson(orders, writer);
        } catch (IOException e) {
            System.err.println("✗ Lỗi ghi file JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ============================================================
    // Adapter cho LocalDateTime
    // ============================================================
    private class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, 
                                                   JsonDeserializer<LocalDateTime> {
        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, 
                                    JsonSerializationContext context) {
            return new JsonPrimitive(src.format(formatter));
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, 
                                        JsonDeserializationContext context) 
                                        throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(), formatter);
        }
    }
}