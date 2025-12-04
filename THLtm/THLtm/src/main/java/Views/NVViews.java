package Views;

import Controller.ClientController;

import javax.swing.*;
import java.awt.*;

public class NVViews extends JFrame {

    private JButton btnBanHang;
    private JButton btnQuanLyDonHang;
    private JButton btnXemSanPham;
    private JButton btnDangXuat;
    private JLabel lblWelcome; // label chào mừng

    public NVViews(ClientController mainController) {

        setTitle("Dashboard Nhân Viên");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        // ===================== SIDEBAR ======================
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 150, 136),
                        0, getHeight(), new Color(0, 121, 107));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("<html><center><br><font size=5 color='white'>NHÂN VIÊN</font></center></html>");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(title);
        sidebar.add(Box.createVerticalStrut(15));

        // Label chào mừng
        lblWelcome = new JLabel("<html><center>Chào mừng!</center></html>");
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblWelcome);
        sidebar.add(Box.createVerticalStrut(20));

        btnDangXuat = new JButton("🚪 Đăng Xuất");
        styleSidebarButton(btnDangXuat);
        btnDangXuat.addActionListener(e -> mainController.logout(this));
        sidebar.add(btnDangXuat);
        sidebar.add(Box.createVerticalGlue());

        add(sidebar, BorderLayout.WEST);

        // ===================== CONTENT ======================
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(245, 245, 245));

        JLabel header = new JLabel("Bảng điều khiển Nhân Viên", SwingConstants.LEFT);
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.add(header, BorderLayout.NORTH);

        JPanel cardPanel = new JPanel(new GridLayout(1, 3, 30, 30));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        cardPanel.setOpaque(false);

        btnBanHang = createCard("💰", "Bán Hàng");
        btnQuanLyDonHang = createCard("🧾", "Quản Lý\nĐơn Hàng");
        btnXemSanPham = createCard("📦", "Xem Sản Phẩm");

        cardPanel.add(btnBanHang);
        cardPanel.add(btnQuanLyDonHang);
        cardPanel.add(btnXemSanPham);

        content.add(cardPanel, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);
    }

    // ===================== HIỂN THỊ TÊN NHÂN VIÊN ======================
    public void setUsername(String username) {
        if (username != null && !username.isEmpty()) {
            lblWelcome.setText("<html><center>Chào mừng nhân viên  "+username  + "!</center></html>");
        } else {
            lblWelcome.setText("<html><center>Chào mừng!</center></html>");
        }
    }

    private JButton createCard(String icon, String text) {
        JButton btn = new JButton("<html><center>" + icon + "<br><br>" + text.replace("\n", "<br>") + "</center></html>");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(230, 230, 250));
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.WHITE);
            }
        });

        return btn;
    }

    private void styleSidebarButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0, 150, 136));
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(180, 50));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0, 121, 107));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0, 150, 136));
            }
        });
    }

    // getters nếu cần
    public JButton getBtnBanHang() { return btnBanHang; }
    public JButton getBtnQuanLyDonHang() { return btnQuanLyDonHang; }
    public JButton getBtnXemSanPham() { return btnXemSanPham; }
    public JButton getBtnDangXuat() { return btnDangXuat; }
}
