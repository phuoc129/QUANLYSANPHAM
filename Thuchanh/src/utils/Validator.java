package utils;

import java.util.regex.Pattern;

public class Validator {
    
    // Regex patterns
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^0\\d{9}$");
    
    private static final Pattern DATE_PATTERN = 
        Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    
    private static final Pattern USERNAME_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    
    private static final Pattern PRODUCT_CODE_PATTERN = 
        Pattern.compile("^[A-Z0-9]{3,10}$");
    
    /**
     * Kiểm tra chuỗi có rỗng hoặc null không
     */
    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }
    
    /**
     * Kiểm tra email hợp lệ
     * Format: example@domain.com
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Kiểm tra số điện thoại hợp lệ (Vietnam)
     * Format: 0xxxxxxxxx (10 chữ số)
     */
    public static boolean isValidPhoneNumber(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }
    
    /**
     * Kiểm tra ngày hợp lệ
     * Format: YYYY-MM-DD
     */
    public static boolean isValidDate(String date) {
        if (isEmpty(date)) {
            return false;
        }
        
        if (!DATE_PATTERN.matcher(date.trim()).matches()) {
            return false;
        }
        
        // Additional validation for date values
        try {
            String[] parts = date.trim().split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);
            
            if (year < 1900 || year > 2100) return false;
            if (month < 1 || month > 12) return false;
            if (day < 1 || day > 31) return false;
            
            // Check days in month
            if (month == 2) {
                boolean isLeap = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
                if (day > (isLeap ? 29 : 28)) return false;
            } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                if (day > 30) return false;
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Kiểm tra username hợp lệ
     * 3-20 ký tự, chỉ chữ, số và dấu gạch dưới
     */
    public static boolean isValidUsername(String username) {
        if (isEmpty(username)) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username.trim()).matches();
    }
    
    /**
     * Kiểm tra mật khẩu hợp lệ
     * Tối thiểu 6 ký tự
     */
    public static boolean isValidPassword(String password) {
        if (isEmpty(password)) {
            return false;
        }
        return password.length() >= 6;
    }
    
    /**
     * Kiểm tra mật khẩu mạnh
     * Tối thiểu 8 ký tự, có chữ hoa, chữ thường, số
     */
    public static boolean isStrongPassword(String password) {
        if (isEmpty(password) || password.length() < 8) {
            return false;
        }
        
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isLowerCase(c)) hasLower = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        
        return hasUpper && hasLower && hasDigit;
    }
    
    /**
     * Kiểm tra mã sản phẩm hợp lệ
     * 3-10 ký tự, chỉ chữ IN HOA và số
     */
    public static boolean isValidProductCode(String code) {
        if (isEmpty(code)) {
            return false;
        }
        return PRODUCT_CODE_PATTERN.matcher(code.trim()).matches();
    }
    
    /**
     * Kiểm tra số nguyên dương
     */
    public static boolean isPositiveInteger(String text) {
        if (isEmpty(text)) {
            return false;
        }
        
        try {
            int value = Integer.parseInt(text.trim());
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Kiểm tra số thực dương
     */
    public static boolean isPositiveDouble(String text) {
        if (isEmpty(text)) {
            return false;
        }
        
        try {
            double value = Double.parseDouble(text.trim());
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Kiểm tra số nguyên không âm (>= 0)
     */
    public static boolean isNonNegativeInteger(String text) {
        if (isEmpty(text)) {
            return false;
        }
        
        try {
            int value = Integer.parseInt(text.trim());
            return value >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Kiểm tra số thực không âm (>= 0)
     */
    public static boolean isNonNegativeDouble(String text) {
        if (isEmpty(text)) {
            return false;
        }
        
        try {
            double value = Double.parseDouble(text.trim());
            return value >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Kiểm tra độ dài chuỗi
     */
    public static boolean isValidLength(String text, int minLength, int maxLength) {
        if (text == null) {
            return false;
        }
        
        int length = text.trim().length();
        return length >= minLength && length <= maxLength;
    }
    
    /**
     * Kiểm tra giá trị nằm trong khoảng
     */
    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }
    
    /**
     * Làm sạch chuỗi (trim và loại bỏ ký tự đặc biệt)
     */
    public static String sanitize(String text) {
        if (text == null) {
            return "";
        }
        
        // Trim and remove multiple spaces
        text = text.trim().replaceAll("\\s+", " ");
        
        // Remove special characters that might cause issues
        text = text.replaceAll("[<>\"']", "");
        
        return text;
    }
    
    /**
     * Format số điện thoại
     * Input: 0123456789
     * Output: 012-345-6789
     */
    public static String formatPhoneNumber(String phone) {
        if (isEmpty(phone) || !isValidPhoneNumber(phone)) {
            return phone;
        }
        
        phone = phone.trim();
        return phone.substring(0, 3) + "-" + 
               phone.substring(3, 6) + "-" + 
               phone.substring(6);
    }
    
    /**
     * Format giá tiền
     * Input: 1000000
     * Output: 1,000,000 VNĐ
     */
    public static String formatCurrency(double amount) {
        return String.format("%,.0f VNĐ", amount);
    }
    
    /**
     * Parse giá tiền từ chuỗi
     * Input: "1,000,000 VNĐ"
     * Output: 1000000.0
     */
    public static double parseCurrency(String text) {
        if (isEmpty(text)) {
            return 0;
        }
        
        // Remove currency symbol and commas
        text = text.replaceAll("[^0-9.]", "");
        
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * Kiểm tra CMND/CCCD hợp lệ (9 hoặc 12 chữ số)
     */
    public static boolean isValidIdCard(String idCard) {
        if (isEmpty(idCard)) {
            return false;
        }
        
        String cleaned = idCard.trim().replaceAll("\\s", "");
        return cleaned.matches("^\\d{9}$") || cleaned.matches("^\\d{12}$");
    }
    
    /**
     * Validate form data và trả về error message
     * @return null nếu valid, error message nếu invalid
     */
    public static String validateLoginForm(String username, String password) {
        if (isEmpty(username)) {
            return "Vui lòng nhập tên đăng nhập!";
        }
        
        if (isEmpty(password)) {
            return "Vui lòng nhập mật khẩu!";
        }
        
        if (!isValidUsername(username)) {
            return "Tên đăng nhập không hợp lệ!\n3-20 ký tự, chỉ chữ, số và dấu gạch dưới.";
        }
        
        if (!isValidPassword(password)) {
            return "Mật khẩu phải có ít nhất 6 ký tự!";
        }
        
        return null; // Valid
    }
    
    /**
     * Validate product data
     */
    public static String validateProduct(String code, String name, String price, String stock) {
        if (isEmpty(code)) {
            return "Vui lòng nhập mã sản phẩm!";
        }
        
        if (!isValidProductCode(code)) {
            return "Mã sản phẩm không hợp lệ!\n3-10 ký tự, chỉ chữ IN HOA và số.";
        }
        
        if (isEmpty(name)) {
            return "Vui lòng nhập tên sản phẩm!";
        }
        
        if (!isValidLength(name, 3, 100)) {
            return "Tên sản phẩm phải từ 3-100 ký tự!";
        }
        
        if (isEmpty(price) || !isPositiveDouble(price)) {
            return "Giá sản phẩm phải là số dương!";
        }
        
        if (isEmpty(stock) || !isNonNegativeInteger(stock)) {
            return "Số lượng tồn kho phải là số không âm!";
        }
        
        return null; // Valid
    }
    
    /**
     * Validate user data
     */
    public static String validateUser(String username, String fullName, String email, String phone) {
        if (isEmpty(username)) {
            return "Vui lòng nhập tên đăng nhập!";
        }
        
        if (!isValidUsername(username)) {
            return "Tên đăng nhập không hợp lệ!";
        }
        
        if (isEmpty(fullName)) {
            return "Vui lòng nhập họ tên!";
        }
        
        if (!isValidLength(fullName, 3, 50)) {
            return "Họ tên phải từ 3-50 ký tự!";
        }
        
        if (!isEmpty(email) && !isValidEmail(email)) {
            return "Email không hợp lệ!";
        }
        
        if (!isEmpty(phone) && !isValidPhoneNumber(phone)) {
            return "Số điện thoại không hợp lệ!\nĐịnh dạng: 0xxxxxxxxx (10 chữ số)";
        }
        
        return null; // Valid
    }
}