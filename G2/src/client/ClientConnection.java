package client;

import server.model.*;
import java.io.*;
import java.net.Socket;
import java.util.Map;

/**
 * CLIENT - Kết nối và giao tiếp với Server
 */
public class ClientConnection {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8888;
    
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private User currentUser;

    /**
     * Kết nối tới server
     */
    public boolean connect() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            
            System.out.println("✓ Đã kết nối tới server: " + SERVER_HOST + ":" + SERVER_PORT);
            return true;
            
        } catch (IOException e) {
            System.err.println("✗ Không thể kết nối tới server!");
            System.err.println("  Kiểm tra xem server đã chạy chưa?");
            return false;
        }
    }

    /**
     * Gửi request và nhận response
     */
    public Response sendRequest(Request request) {
        try {
            out.writeObject(request);
            out.flush();
            
            Response response = (Response) in.readObject();
            return response;
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("✗ Lỗi giao tiếp với server: " + e.getMessage());
            return new Response(false, "Lỗi kết nối server", null);
        }
    }

    /**
     * Đăng nhập
     */
    public Response login(String username, String password) {
        Map<String, Object> data = Map.of(
            "username", username,
            "password", password
        );
        
        Request request = new Request("LOGIN", data);
        Response response = sendRequest(request);
        
        if (response.isSuccess()) {
            currentUser = (User) response.getData().get("user");
        }
        
        return response;
    }

    /**
     * Đăng xuất
     */
    public Response logout() {
        Request request = new Request("LOGOUT", null);
        Response response = sendRequest(request);
        
        if (response.isSuccess()) {
            currentUser = null;
        }
        
        return response;
    }

    /**
     * Lấy tất cả sản phẩm
     */
    public Response getAllProducts() {
        Request request = new Request("GET_ALL_PRODUCTS", null);
        return sendRequest(request);
    }

    /**
     * Tìm kiếm sản phẩm
     */
    public Response searchProducts(String keyword) {
        Map<String, Object> data = Map.of("keyword", keyword);
        Request request = new Request("SEARCH_PRODUCTS", data);
        return sendRequest(request);
    }

    /**
     * Thêm sản phẩm (Admin only)
     */
    public Response addProduct(Product product) {
        Map<String, Object> data = Map.of("product", product);
        Request request = new Request("ADD_PRODUCT", data);
        return sendRequest(request);
    }

    /**
     * Cập nhật sản phẩm (Admin only)
     */
    public Response updateProduct(Product product) {
        Map<String, Object> data = Map.of("product", product);
        Request request = new Request("UPDATE_PRODUCT", data);
        return sendRequest(request);
    }

    /**
     * Xóa sản phẩm (Admin only)
     */
    public Response deleteProduct(String productId) {
        Map<String, Object> data = Map.of("productId", productId);
        Request request = new Request("DELETE_PRODUCT", data);
        return sendRequest(request);
    }

    /**
     * Tạo đơn hàng
     */
    public Response createOrder(Order order) {
        Map<String, Object> data = Map.of("order", order);
        Request request = new Request("CREATE_ORDER", data);
        return sendRequest(request);
    }

    /**
     * Lấy đơn hàng của user hiện tại
     */
    public Response getUserOrders() {
        Request request = new Request("GET_USER_ORDERS", null);
        return sendRequest(request);
    }

    /**
     * Lấy tất cả đơn hàng (Admin only)
     */
    public Response getAllOrders() {
        Request request = new Request("GET_ALL_ORDERS", null);
        return sendRequest(request);
    }

    /**
     * Lấy thống kê (Admin only)
     */
    public Response getStatistics() {
        Request request = new Request("GET_STATISTICS", null);
        return sendRequest(request);
    }

    /**
     * Đóng kết nối
     */
    public void disconnect() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("✓ Đã ngắt kết nối server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters
    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
}