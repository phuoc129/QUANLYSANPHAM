package network;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TCPClient {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private String serverHost;
    private int serverPort;
    private boolean connected;
    
    // Timeout settings
    private static final int CONNECTION_TIMEOUT = 5000; // 5 seconds
    private static final int READ_TIMEOUT = 10000; // 10 seconds
    
    /**
     * Constructor
     * @param host Server hostname or IP
     * @param port Server port
     */
    public TCPClient(String host, int port) {
        this.serverHost = host;
        this.serverPort = port;
        this.connected = false;
    }
    
    /**
     * Kết nối đến server
     * @return true nếu kết nối thành công
     */
    public boolean connect() {
        try {
            // Create socket with timeout
            socket = new Socket();
            socket.connect(new java.net.InetSocketAddress(serverHost, serverPort), CONNECTION_TIMEOUT);
            socket.setSoTimeout(READ_TIMEOUT);
            
            // Create streams
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            
            connected = true;
            logInfo("Connected to server: " + serverHost + ":" + serverPort);
            return true;
            
        } catch (SocketTimeoutException e) {
            logError("Connection timeout: " + e.getMessage());
            return false;
        } catch (IOException e) {
            logError("Connection failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Gửi request đến server và nhận response
     * @param request Request string
     * @return Response string, hoặc null nếu có lỗi
     */
    public String sendRequest(String request) {
        if (!connected) {
            logError("Not connected to server");
            return null;
        }
        
        try {
            // Send request
            out.writeUTF(request);
            out.flush();
            logInfo("Sent: " + truncate(request, 100));
            
            // Receive response
            String response = in.readUTF();
            logInfo("Received: " + truncate(response, 100));
            
            return response;
            
        } catch (SocketTimeoutException e) {
            logError("Request timeout: " + e.getMessage());
            // Try to reconnect
            reconnect();
            return null;
        } catch (IOException e) {
            logError("Communication error: " + e.getMessage());
            connected = false;
            return null;
        }
    }
    
    /**
     * Gửi request với retry
     * @param request Request string
     * @param maxRetries Maximum number of retries
     * @return Response string, hoặc null nếu thất bại sau maxRetries
     */
    public String sendRequestWithRetry(String request, int maxRetries) {
        int attempts = 0;
        
        while (attempts < maxRetries) {
            String response = sendRequest(request);
            
            if (response != null) {
                return response;
            }
            
            attempts++;
            logWarning("Retry attempt " + attempts + " of " + maxRetries);
            
            // Wait before retry
            try {
                Thread.sleep(1000); // 1 second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            
            // Try to reconnect before retry
            if (!connected) {
                reconnect();
            }
        }
        
        logError("Failed after " + maxRetries + " retries");
        return null;
    }
    
    /**
     * Thử kết nối lại
     * @return true nếu reconnect thành công
     */
    public boolean reconnect() {
        logInfo("Attempting to reconnect...");
        disconnect();
        
        try {
            Thread.sleep(2000); // Wait 2 seconds before reconnect
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return connect();
    }
    
    /**
     * Đóng kết nối
     */
    public void disconnect() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            
            connected = false;
            logInfo("Disconnected from server");
            
        } catch (IOException e) {
            logError("Error during disconnect: " + e.getMessage());
        }
    }
    
    /**
     * Kiểm tra kết nối
     * @return true nếu đang kết nối
     */
    public boolean isConnected() {
        return connected && socket != null && !socket.isClosed();
    }
    
    /**
     * Ping server để kiểm tra kết nối
     * @return true nếu server phản hồi
     */
    public boolean ping() {
        if (!connected) {
            return false;
        }
        
        try {
            String response = sendRequest("PING");
            return response != null && response.startsWith("PONG");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get server info
     */
    public String getServerHost() {
        return serverHost;
    }
    
    public int getServerPort() {
        return serverPort;
    }
    
    /**
     * Set timeout values
     */
    public void setConnectionTimeout(int timeout) throws IOException {
        if (socket != null) {
            socket.connect(socket.getRemoteSocketAddress(), timeout);
        }
    }
    
    public void setReadTimeout(int timeout) throws IOException {
        if (socket != null) {
            socket.setSoTimeout(timeout);
        }
    }
    
    // =================== UTILITY METHODS ===================
    
    /**
     * Truncate long strings for logging
     */
    private String truncate(String str, int maxLength) {
        if (str == null) return "null";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength) + "...";
    }
    
    /**
     * Log info message
     */
    private void logInfo(String message) {
        System.out.println("[TCPClient] [INFO] " + 
            java.time.LocalTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) + 
            " - " + message);
    }
    
    /**
     * Log error message
     */
    private void logError(String message) {
        System.err.println("[TCPClient] [ERROR] " + 
            java.time.LocalTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) + 
            " - " + message);
    }
    
    /**
     * Log warning message
     */
    private void logWarning(String message) {
        System.out.println("[TCPClient] [WARN] " + 
            java.time.LocalTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) + 
            " - " + message);
    }
    
    /**
     * Get connection statistics
     */
    public String getConnectionStats() {
        StringBuilder stats = new StringBuilder();
        stats.append("═══════════════════════════════════════════════\n");
        stats.append("        TCP CLIENT CONNECTION STATS\n");
        stats.append("═══════════════════════════════════════════════\n");
        stats.append("Server: ").append(serverHost).append(":").append(serverPort).append("\n");
        stats.append("Status: ").append(connected ? "Connected ✓" : "Disconnected ✗").append("\n");
        
        if (socket != null) {
            stats.append("Local: ").append(socket.getLocalAddress()).append(":").append(socket.getLocalPort()).append("\n");
            stats.append("Closed: ").append(socket.isClosed() ? "Yes" : "No").append("\n");
        }
        
        stats.append("═══════════════════════════════════════════════\n");
        return stats.toString();
    }
    
    /**
     * Finalize - đảm bảo đóng kết nối khi object bị destroy
     */
    @Override
    protected void finalize() throws Throwable {
        try {
            disconnect();
        } finally {
            super.finalize();
        }
    }
}