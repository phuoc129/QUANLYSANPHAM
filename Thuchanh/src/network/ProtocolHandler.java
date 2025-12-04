package network;

public class ProtocolHandler {
    
    // ===================== AUTHENTICATION =====================
    public static final String CMD_LOGIN = "LOGIN";
    public static final String CMD_LOGOUT = "LOGOUT";
    public static final String CMD_CHANGE_PASSWORD = "CHANGE_PASSWORD";
    
    // ===================== PRODUCT MANAGEMENT =====================
    public static final String CMD_GET_PRODUCTS = "GET_PRODUCTS";
    public static final String CMD_GET_PRODUCT_BY_ID = "GET_PRODUCT_BY_ID";
    public static final String CMD_SEARCH_PRODUCT = "SEARCH_PRODUCT";
    public static final String CMD_ADD_PRODUCT = "ADD_PRODUCT";
    public static final String CMD_UPDATE_PRODUCT = "UPDATE_PRODUCT";
    public static final String CMD_DELETE_PRODUCT = "DELETE_PRODUCT";
    public static final String CMD_UPDATE_STOCK = "UPDATE_STOCK";
    
    // ===================== INVOICE MANAGEMENT =====================
    public static final String CMD_CREATE_INVOICE = "CREATE_INVOICE";
    public static final String CMD_GET_INVOICES = "GET_INVOICES";
    public static final String CMD_GET_INVOICE_BY_ID = "GET_INVOICE_BY_ID";
    public static final String CMD_SEARCH_INVOICE = "SEARCH_INVOICE";
    public static final String CMD_CANCEL_INVOICE = "CANCEL_INVOICE";
    public static final String CMD_PRINT_INVOICE = "PRINT_INVOICE";
    
    // ===================== USER MANAGEMENT =====================
    public static final String CMD_GET_USERS = "GET_USERS";
    public static final String CMD_GET_USER_BY_ID = "GET_USER_BY_ID";
    public static final String CMD_CREATE_USER = "CREATE_USER";
    public static final String CMD_UPDATE_USER = "UPDATE_USER";
    public static final String CMD_DELETE_USER = "DELETE_USER";
    public static final String CMD_TOGGLE_USER_STATUS = "TOGGLE_USER_STATUS";
    
    // ===================== REPORTS =====================
    public static final String CMD_GET_REVENUE_REPORT = "GET_REVENUE_REPORT";
    public static final String CMD_GET_PRODUCT_REPORT = "GET_PRODUCT_REPORT";
    public static final String CMD_GET_DAILY_REPORT = "GET_DAILY_REPORT";
    public static final String CMD_GET_MONTHLY_REPORT = "GET_MONTHLY_REPORT";
    
    // ===================== RESPONSE STATUS =====================
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_ERROR = "ERROR";
    public static final String STATUS_NOT_FOUND = "NOT_FOUND";
    public static final String STATUS_UNAUTHORIZED = "UNAUTHORIZED";
    public static final String STATUS_INVALID = "INVALID";
    
    // ===================== MESSAGE DELIMITER =====================
    public static final String DELIMITER = "|";
    public static final String ITEM_SEPARATOR = ";";
    
    /**
     * Tạo request message với format: COMMAND|param1|param2|...
     */
    public static String createRequest(String command, String... params) {
        StringBuilder request = new StringBuilder(command);
        for (String param : params) {
            String escapedParam = param.replace(DELIMITER, "\\|");
            request.append(DELIMITER).append(escapedParam);
        }
        return request.toString();
    }
    
    /**
     * Parse response từ server
     * Format: STATUS|data1|data2|...
     */
    public static String[] parseResponse(String response) {
        if (response == null || response.isEmpty()) {
            return new String[]{STATUS_ERROR, "Empty response"};
        }
        
        response = response.replace("\\|", DELIMITER);
        return response.split("\\" + DELIMITER);
    }
    
    /**
     * Kiểm tra response có thành công không
     */
    public static boolean isSuccess(String response) {
        String[] parts = parseResponse(response);
        return parts.length > 0 && STATUS_SUCCESS.equals(parts[0]);
    }
    
    /**
     * Lấy status từ response
     */
    public static String getStatus(String response) {
        String[] parts = parseResponse(response);
        return parts.length > 0 ? parts[0] : STATUS_ERROR;
    }
    
    /**
     * Lấy message lỗi từ response
     */
    public static String getErrorMessage(String response) {
        String[] parts = parseResponse(response);
        if (parts.length > 1 && !STATUS_SUCCESS.equals(parts[0])) {
            return parts[1];
        }
        return "";
    }
    
    /**
     * Tạo success response
     */
    public static String createSuccessResponse(String... data) {
        return createRequest(STATUS_SUCCESS, data);
    }
    
    /**
     * Tạo error response
     */
    public static String createErrorResponse(String errorMessage) {
        return createRequest(STATUS_ERROR, errorMessage);
    }
}