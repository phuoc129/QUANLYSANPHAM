// view/RevenueReportPanel.java
package view;

import java.awt.*;
import java.awt.event.*;
import controller.POSController;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RevenueReportPanel extends Panel {
    private POSController controller;
    private Choice reportTypeChoice;
    private TextField startDateField, endDateField;
    private TextArea reportArea;
    
    public RevenueReportPanel(POSController controller) {
        this.controller = controller;
        setupUI();
    }
    
    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Top panel - Controls
        Panel topPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(new Color(52, 152, 219));
        
        Label titleLabel = new Label("BÁO CÁO DOANH THU");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);
        
        Label spacer = new Label("     ");
        topPanel.add(spacer);
        
        Label typeLabel = new Label("Loại báo cáo:");
        typeLabel.setForeground(Color.WHITE);
        topPanel.add(typeLabel);
        
        reportTypeChoice = new Choice();
        reportTypeChoice.add("Báo cáo theo ngày");
        reportTypeChoice.add("Báo cáo theo khoảng thời gian");
        reportTypeChoice.add("Báo cáo theo tháng");
        reportTypeChoice.add("Báo cáo sản phẩm bán chạy");
        reportTypeChoice.addItemListener(e -> updateDateFields());
        topPanel.add(reportTypeChoice);
        
        Label startLabel = new Label("Từ ngày:");
        startLabel.setForeground(Color.WHITE);
        topPanel.add(startLabel);
        startDateField = new TextField(10);
        startDateField.setText(LocalDate.now().toString());
        topPanel.add(startDateField);
        
        Label endLabel = new Label("Đến ngày:");
        endLabel.setForeground(Color.WHITE);
        topPanel.add(endLabel);
        endDateField = new TextField(10);
        endDateField.setText(LocalDate.now().toString());
        topPanel.add(endDateField);
        
        Button generateBtn = new Button("Tạo báo cáo");
        generateBtn.setBackground(new Color(46, 204, 113));
        generateBtn.setForeground(Color.WHITE);
        generateBtn.addActionListener(e -> generateReport());
        topPanel.add(generateBtn);
        
        Button exportBtn = new Button("Xuất Excel");
        exportBtn.setBackground(new Color(241, 196, 15));
        exportBtn.setForeground(Color.WHITE);
        exportBtn.addActionListener(e -> exportReport());
        topPanel.add(exportBtn);
        
        Button printBtn = new Button("In báo cáo");
        printBtn.setBackground(new Color(149, 165, 166));
        printBtn.setForeground(Color.WHITE);
        printBtn.addActionListener(e -> printReport());
        topPanel.add(printBtn);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Center - Report display
        Panel centerPanel = new Panel(new BorderLayout());
        
        Panel headerPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(236, 240, 241));
        Label headerLabel = new Label("Kết quả báo cáo:");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 12));
        headerPanel.add(headerLabel);
        centerPanel.add(headerPanel, BorderLayout.NORTH);
        
        reportArea = new TextArea(28, 100);
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        centerPanel.add(reportArea, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Show initial guide
        showInitialGuide();
    }
    
    private void showInitialGuide() {
        StringBuilder guide = new StringBuilder();
        guide.append("╔════════════════════════════════════════════════════════════════════════════════╗\n");
        guide.append("║               HỆ THỐNG BÁO CÁO DOANH THU - QUẢN LÝ BÁN HÀNG                  ║\n");
        guide.append("╚════════════════════════════════════════════════════════════════════════════════╝\n\n");
        guide.append("📋 HƯỚNG DẪN SỬ DỤNG:\n\n");
        guide.append("1. Chọn loại báo cáo từ dropdown menu\n");
        guide.append("2. Nhập khoảng thời gian (nếu cần)\n");
        guide.append("3. Nhấn 'Tạo báo cáo' để xem kết quả\n");
        guide.append("4. Sử dụng 'Xuất Excel' hoặc 'In báo cáo' để lưu/in\n\n");
        guide.append("📊 CÁC LOẠI BÁO CÁO:\n\n");
        guide.append("  • Báo cáo theo ngày        - Doanh thu trong ngày hiện tại\n");
        guide.append("  • Báo cáo theo khoảng TG   - Doanh thu trong khoảng thời gian\n");
        guide.append("  • Báo cáo theo tháng       - Tổng hợp doanh thu theo tháng\n");
        guide.append("  • Sản phẩm bán chạy        - Top sản phẩm có doanh số cao\n\n");
        guide.append("📅 Format ngày: YYYY-MM-DD (ví dụ: 2025-01-15)\n\n");
        guide.append("💡 MẸO:\n");
        guide.append("  - Báo cáo theo ngày không cần nhập khoảng thời gian\n");
        guide.append("  - Để xem doanh thu tuần này, nhập từ thứ 2 đến chủ nhật\n");
        guide.append("  - Để xem doanh thu tháng, chọn ngày đầu và cuối tháng\n\n");
        guide.append("╔════════════════════════════════════════════════════════════════════════════════╗\n");
        guide.append("║  Nhấn 'Tạo báo cáo' để bắt đầu                                                ║\n");
        guide.append("╚════════════════════════════════════════════════════════════════════════════════╝\n");
        
        reportArea.setText(guide.toString());
    }
    
    private void updateDateFields() {
        String reportType = reportTypeChoice.getSelectedItem();
        
        // Báo cáo ngày không cần chọn thời gian
        if ("Báo cáo theo ngày".equals(reportType)) {
            startDateField.setEnabled(false);
            endDateField.setEnabled(false);
        } else {
            startDateField.setEnabled(true);
            endDateField.setEnabled(true);
        }
    }
    
    private void generateReport() {
        String reportType = reportTypeChoice.getSelectedItem();
        String startDate = startDateField.getText().trim();
        String endDate = endDateField.getText().trim();
        
        StringBuilder report = new StringBuilder();
        report.append("╔════════════════════════════════════════════════════════════════════════════════╗\n");
        report.append("║                    ").append(centerText(reportType.toUpperCase(), 56)).append("║\n");
        report.append("╚════════════════════════════════════════════════════════════════════════════════╝\n");
        report.append("Ngày tạo: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        report.append("                                     Người tạo: ").append(controller.getCurrentUser().getFullName()).append("\n");
        
        try {
            if ("Báo cáo theo ngày".equals(reportType)) {
                report.append("Ngày báo cáo: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
                report.append("────────────────────────────────────────────────────────────────────────────────\n\n");
                
                String data = controller.getDailyReport();
                report.append(formatDailyReport(data));
                
            } else if ("Báo cáo theo khoảng thời gian".equals(reportType)) {
                if (startDate.isEmpty() || endDate.isEmpty()) {
                    showMessage("Vui lòng nhập đầy đủ khoảng thời gian!");
                    return;
                }
                report.append("Khoảng thời gian: ").append(startDate).append(" đến ").append(endDate).append("\n");
                report.append("────────────────────────────────────────────────────────────────────────────────\n\n");
                
                String data = controller.getRevenueReport(startDate, endDate);
                report.append(formatRevenueReport(data));
                
            } else if ("Báo cáo theo tháng".equals(reportType)) {
                report.append("────────────────────────────────────────────────────────────────────────────────\n\n");
                report.append(formatMonthlyReport());
                
            } else if ("Báo cáo sản phẩm bán chạy".equals(reportType)) {
                if (startDate.isEmpty() || endDate.isEmpty()) {
                    showMessage("Vui lòng nhập đầy đủ khoảng thời gian!");
                    return;
                }
                report.append("Khoảng thời gian: ").append(startDate).append(" đến ").append(endDate).append("\n");
                report.append("────────────────────────────────────────────────────────────────────────────────\n\n");
                report.append(formatTopProductsReport());
            }
            
            report.append("\n╔════════════════════════════════════════════════════════════════════════════════╗\n");
            report.append("║                              KẾT THÚC BÁO CÁO                                  ║\n");
            report.append("╚════════════════════════════════════════════════════════════════════════════════╝\n");
            
            reportArea.setText(report.toString());
            
        } catch (Exception e) {
            showMessage("Lỗi khi tạo báo cáo: " + e.getMessage());
        }
    }
    
    private String formatDailyReport(String data) {
        StringBuilder formatted = new StringBuilder();
        
        if (data == null || data.startsWith("ERROR")) {
            formatted.append("Không có dữ liệu cho ngày hôm nay.\n");
            return formatted.toString();
        }
        
        String[] parts = data.split("\\|");
        
        if (parts.length > 0 && "SUCCESS".equals(parts[0])) {
            formatted.append("┌──────────────────────────────────────────────────────────────────────────────┐\n");
            formatted.append(String.format("│ %-30s │ %15s │ %20s │\n", 
                "Danh mục", "Số đơn hàng", "Doanh thu"));
            formatted.append("├──────────────────────────────────────────────────────────────────────────────┤\n");
            
            double totalRevenue = 0;
            int totalOrders = 0;
            
            for (int i = 1; i < parts.length; i++) {
                String[] detail = parts[i].split(",");
                if (detail.length >= 3) {
                    int orders = Integer.parseInt(detail[1]);
                    double revenue = Double.parseDouble(detail[2]);
                    
                    formatted.append(String.format("│ %-30s │ %15d │ %,18.0f₫ │\n",
                        detail[0], orders, revenue));
                    
                    totalRevenue += revenue;
                    totalOrders += orders;
                }
            }
            
            formatted.append("├──────────────────────────────────────────────────────────────────────────────┤\n");
            formatted.append(String.format("│ %-30s │ %15d │ %,18.0f₫ │\n",
                "TỔNG CỘNG", totalOrders, totalRevenue));
            formatted.append("└──────────────────────────────────────────────────────────────────────────────┘\n");
        } else {
            formatted.append("Không có dữ liệu doanh thu.\n");
        }
        
        return formatted.toString();
    }
    
    private String formatRevenueReport(String data) {
        StringBuilder formatted = new StringBuilder();
        
        String[] parts = data.split("\\|");
        
        if (parts.length > 0 && "SUCCESS".equals(parts[0])) {
            formatted.append("┌──────────────────────────────────────────────────────────────────────────────┐\n");
            formatted.append(String.format("│ %-20s │ %15s │ %15s │ %15s │\n",
                "Ngày", "Số hóa đơn", "Số sản phẩm", "Doanh thu"));
            formatted.append("├──────────────────────────────────────────────────────────────────────────────┤\n");
            
            double totalRevenue = 0;
            int totalInvoices = 0;
            
            for (int i = 1; i < parts.length; i++) {
                String[] detail = parts[i].split(",");
                if (detail.length >= 4) {
                    int invoices = Integer.parseInt(detail[1]);
                    int products = Integer.parseInt(detail[2]);
                    double revenue = Double.parseDouble(detail[3]);
                    
                    formatted.append(String.format("│ %-20s │ %15d │ %15d │ %,13.0f₫ │\n",
                        detail[0], invoices, products, revenue));
                    
                    totalRevenue += revenue;
                    totalInvoices += invoices;
                }
            }
            
            formatted.append("├──────────────────────────────────────────────────────────────────────────────┤\n");
            formatted.append(String.format("│ %-20s │ %15d │ %15s │ %,13.0f₫ │\n",
                "TỔNG CỘNG", totalInvoices, "-", totalRevenue));
            formatted.append("└──────────────────────────────────────────────────────────────────────────────┘\n");
            
            // Thống kê thêm
            formatted.append("\n📈 THỐNG KÊ:\n");
            formatted.append(String.format("  • Tổng doanh thu:        %,0f VNĐ\n", totalRevenue));
            formatted.append(String.format("  • Tổng số hóa đơn:       %d hóa đơn\n", totalInvoices));
            if (totalInvoices > 0) {
                formatted.append(String.format("  • Trung bình/hóa đơn:   %,0f VNĐ\n", totalRevenue / totalInvoices));
            }
        } else {
            formatted.append("Không có dữ liệu doanh thu trong khoảng thời gian này.\n");
        }
        
        return formatted.toString();
    }
    
    private String formatMonthlyReport() {
        StringBuilder formatted = new StringBuilder();
        
        formatted.append("┌──────────────────────────────────────────────────────────────────────────────┐\n");
        formatted.append(String.format("│ %-20s │ %15s │ %15s │ %15s │\n",
            "Tháng", "Số hóa đơn", "Số sản phẩm", "Doanh thu"));
        formatted.append("├──────────────────────────────────────────────────────────────────────────────┤\n");
        
        // Sample data - sẽ được thay thế bằng dữ liệu thực từ server
        String[] months = {"Tháng 1/2025", "Tháng 2/2025", "Tháng 3/2025"};
        int[] invoices = {120, 135, 158};
        int[] products = {450, 520, 610};
        double[] revenues = {45000000, 52000000, 61000000};
        
        double totalRevenue = 0;
        int totalInvoices = 0;
        
        for (int i = 0; i < months.length; i++) {
            formatted.append(String.format("│ %-20s │ %15d │ %15d │ %,13.0f₫ │\n",
                months[i], invoices[i], products[i], revenues[i]));
            totalRevenue += revenues[i];
            totalInvoices += invoices[i];
        }
        
        formatted.append("├──────────────────────────────────────────────────────────────────────────────┤\n");
        formatted.append(String.format("│ %-20s │ %15d │ %15s │ %,13.0f₫ │\n",
            "TỔNG CỘNG", totalInvoices, "-", totalRevenue));
        formatted.append("└──────────────────────────────────────────────────────────────────────────────┘\n");
        
        formatted.append("\n💡 Lưu ý: Đây là dữ liệu mẫu. Cần kết nối với server để lấy dữ liệu thực.\n");
        
        return formatted.toString();
    }
    
    private String formatTopProductsReport() {
        StringBuilder formatted = new StringBuilder();
        
        formatted.append("TOP 10 SẢN PHẨM BÁN CHẠY\n\n");
        formatted.append("┌────────────────────────────────────────────────────────────────────────────┐\n");
        formatted.append(String.format("│ %-4s │ %-35s │ %10s │ %15s │\n",
            "Hạng", "Tên sản phẩm", "Đã bán", "Doanh thu"));
        formatted.append("├────────────────────────────────────────────────────────────────────────────┤\n");
        
        // Sample data
        String[] products = {
            "Coca Cola lon 330ml",
            "Bánh mì sandwich",
            "Café sữa đá",
            "Nước suối Lavie 500ml",
            "Snack khoai tây Lay's"
        };
        int[] quantities = {450, 380, 320, 290, 250};
        double[] revenues = {4500000, 3800000, 3200000, 2900000, 2500000};
        
        for (int i = 0; i < products.length; i++) {
            formatted.append(String.format("│ %-4d │ %-35s │ %10d │ %,13.0f₫ │\n",
                (i + 1), truncate(products[i], 35), quantities[i], revenues[i]));
        }
        
        formatted.append("└────────────────────────────────────────────────────────────────────────────┘\n");
        
        formatted.append("\n💡 Lưu ý: Đây là dữ liệu mẫu. Cần kết nối với server để lấy dữ liệu thực.\n");
        
        return formatted.toString();
    }
    
    private String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            result.append(" ");
        }
        result.append(text);
        while (result.length() < width) {
            result.append(" ");
        }
        return result.toString();
    }
    
    private String truncate(String str, int length) {
        if (str == null) return "";
        if (str.length() <= length) return str;
        return str.substring(0, length - 3) + "...";
    }
    
    private void exportReport() {
        if (reportArea.getText().contains("HƯỚNG DẪN SỬ DỤNG")) {
            showMessage("Vui lòng tạo báo cáo trước khi xuất!");
            return;
        }
        
        showMessage("Chức năng xuất Excel đang được phát triển.\n\n" +
                   "Báo cáo sẽ được lưu tại: reports/report_" + 
                   LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".xlsx");
    }
    
    private void printReport() {
        if (reportArea.getText().contains("HƯỚNG DẪN SỬ DỤNG")) {
            showMessage("Vui lòng tạo báo cáo trước khi in!");
            return;
        }
        
        showMessage("Đã gửi lệnh in báo cáo!");
    }
    
    private void showMessage(String message) {
        Frame parentFrame = (Frame)getParent().getParent().getParent();
        Dialog dialog = new Dialog(parentFrame, "Thông báo", true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        TextArea textArea = new TextArea(message, 5, 50, TextArea.SCROLLBARS_VERTICAL_ONLY);
        textArea.setEditable(false);
        dialog.add(textArea, BorderLayout.CENTER);
        
        Button okButton = new Button("OK");
        okButton.addActionListener(e -> dialog.dispose());
        Panel btnPanel = new Panel();
        btnPanel.add(okButton);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }
}