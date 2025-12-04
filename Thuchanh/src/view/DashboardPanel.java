// view/DashboardPanel.java
package view;

import java.awt.*;
import controller.POSController;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DashboardPanel extends Panel {
    private POSController controller;
    
    public DashboardPanel(POSController controller) {
        this.controller = controller;
        setupUI();
    }
    
    private void setupUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(236, 240, 241));
        
        // Welcome panel
        Panel welcomePanel = new Panel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        welcomePanel.setBackground(new Color(236, 240, 241));
        
        String userName = controller.getCurrentUser() != null ? 
            controller.getCurrentUser().getFullName() : "Guest";
        
        Label welcomeLabel = new Label("Xin chào, " + userName + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(52, 73, 94));
        welcomePanel.add(welcomeLabel);
        
        Label dateLabel = new Label("" + LocalDate.now().format(
            DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy")));
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateLabel.setForeground(new Color(127, 140, 141));
        welcomePanel.add(dateLabel);
        
        add(welcomePanel, BorderLayout.NORTH);
        
        // Statistics cards
        Panel statsPanel = new Panel(new GridLayout(2, 3, 15, 15));
        statsPanel.setBackground(new Color(236, 240, 241));
        
        // Card 1: Doanh thu hôm nay
        Panel card1 = createStatCard(
            "DOANH THU HÔM NAY",
            "12,500,000 VNĐ",
            "+15.3% so với hôm qua",
            new Color(46, 204, 113)
        );
        
        // Card 2: Số đơn hàng
        Panel card2 = createStatCard(
            "ĐƠN HÀNG",
            "142 đơn",
            "23 đơn trong giờ qua",
            new Color(52, 152, 219)
        );
        
        // Card 3: Sản phẩm bán ra
        Panel card3 = createStatCard(
            "SẢN PHẨM BÁN",
            "567 sản phẩm",
            "18 loại khác nhau",
            new Color(155, 89, 182)
        );
        
        // Card 4: Tổng sản phẩm
        Panel card4 = createStatCard(
            "TỒN KHO",
            "1,234 sản phẩm",
            "Trên 50 danh mục",
            new Color(241, 196, 15)
        );
        
        // Card 5: Khách hàng
        Panel card5 = createStatCard(
            "KHÁCH HÀNG",
            "89 khách",
            "12 khách hàng mới",
            new Color(230, 126, 34)
        );
        
        // Card 6: Doanh thu tháng
        Panel card6 = createStatCard(
            "DOANH THU THÁNG",
            "385,000,000 VNĐ",
            "Đạt 76% mục tiêu",
            new Color(26, 188, 156)
        );
        
        statsPanel.add(card1);
        statsPanel.add(card2);
        statsPanel.add(card3);
        statsPanel.add(card4);
        statsPanel.add(card5);
        statsPanel.add(card6);
        
        add(statsPanel, BorderLayout.CENTER);
        
        // ĐÃ XÓA: Quick actions panel (3 nút bên dưới)
        // Không còn cần panel này nữa vì đã có 4 nút ở MainFrame
    }
    
    private Panel createStatCard(String title, String value, String description, Color color) {
        Panel card = new Panel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        
        // Color bar on left
        Panel colorBar = new Panel();
        colorBar.setBackground(color);
        colorBar.setPreferredSize(new Dimension(8, 0));
        card.add(colorBar, BorderLayout.WEST);
        
        // Content panel
        Panel contentPanel = new Panel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        Label titleLabel = new Label(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 13));
        titleLabel.setForeground(new Color(127, 140, 141));
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(titleLabel, gbc);
        
        // Value
        Label valueLabel = new Label(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(new Color(52, 73, 94));
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 15, 5, 15);
        contentPanel.add(valueLabel, gbc);
        
        // Description
        Label descLabel = new Label(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        descLabel.setForeground(new Color(149, 165, 166));
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 15, 15, 15);
        contentPanel.add(descLabel, gbc);
        
        card.add(contentPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    // ĐÃ XÓA: Method createActionButton() không còn cần thiết
}