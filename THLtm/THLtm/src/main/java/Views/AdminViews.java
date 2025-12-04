package Views;

import Controller.ClientController;

import javax.swing.*;
import java.awt.*;

public class AdminViews extends JFrame {

    private JButton btnQuanLyTaiKhoan;
    private JButton btnQuanLySanPham;
    private JButton btnQuanLyDonHang;
    private JButton btnBackLogin;
    private JLabel welcomeLabel;

    public AdminViews(ClientController mainController, String username) {

        setTitle("Admin Dashboard");
        setSize(930, 560);
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
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(52, 152, 219),
                        0, getHeight(), new Color(41, 128, 185)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setPreferredSize(new Dimension(230, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel adminTitle = new JLabel("<html><center><br><font size=6 color='white'>ADMIN</font></center></html>");
        adminTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(adminTitle);
        sidebar.add(Box.createVerticalStrut(25));

        // Avatar tròn
        ImageIcon avatarIcon = new ImageIcon(new ImageIcon("src/main/resources/admin.png")
                .getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH));
        JLabel avatar = new JLabel(avatarIcon);
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(avatar);
        sidebar.add(Box.createVerticalStrut(20));

        // Welcome với tên admin
        welcomeLabel = new JLabel();
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        setUsername(username); // hiển thị tên admin khi mở view
        sidebar.add(welcomeLabel);
        sidebar.add(Box.createVerticalStrut(35));

        // Logout
        btnBackLogin = new JButton("🚪 Đăng xuất");
        styleSidebarButton(btnBackLogin);
        btnBackLogin.addActionListener(e -> mainController.logout(this));
        sidebar.add(btnBackLogin);
        sidebar.add(Box.createVerticalGlue());

        add(sidebar, BorderLayout.WEST);

        // ===================== CONTENT ======================
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(245, 247, 250));

        JLabel header = new JLabel("Bảng Điều Khiển Quản Trị", SwingConstants.LEFT);
        header.setFont(new Font("Segoe UI", Font.BOLD, 32));
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        content.add(header, BorderLayout.NORTH);

        JPanel cardPanel = new JPanel(new GridLayout(1, 3, 30, 20));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        cardPanel.setOpaque(false);

        // ===================== CARDS ======================
        btnQuanLyTaiKhoan = createCard("👤", "Quản Lý\nTài Khoản");
        btnQuanLySanPham = createCard("📦", "Quản Lý\nSản Phẩm");
        btnQuanLyDonHang = createCard("🧾", "Quản Lý\nĐơn Hàng");

        // Gắn sự kiện mở AccountViews
        btnQuanLyTaiKhoan.addActionListener(e -> mainController.openAccountManager());

        cardPanel.add(btnQuanLyTaiKhoan);
        cardPanel.add(btnQuanLySanPham);
        cardPanel.add(btnQuanLyDonHang);

        content.add(cardPanel, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);
    }

    // ===================== SET USERNAME ======================
    public void setUsername(String username) {
        if (username != null && !username.isEmpty()) {
            welcomeLabel.setText("<html><center><font color='white'>Chào mừng quản trị viên " + username + "!</font></center></html>");
        } else {
            welcomeLabel.setText("<html><center><font color='white'>Chào mừng trở lại!</font></center></html>");
        }
    }

    // ===================== TẠO CARD BUTTON ======================
    private JButton createCard(String icon, String text) {
        JButton btn = new JButton("<html><center>" + icon + "<br><br>" + text.replace("\n", "<br>") + "</center></html>");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(28, 20, 28, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(240, 240, 255));
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(150, 150, 255), 2),
                        BorderFactory.createEmptyBorder(28, 20, 28, 20)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.WHITE);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                        BorderFactory.createEmptyBorder(28, 20, 28, 20)
                ));
            }
        });
        return btn;
    }

    // ===================== STYLE SIDEBAR BUTTON ======================
    private void styleSidebarButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(52, 152, 219));
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(190, 50));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(52, 152, 219));
            }
        });
    }

    // ===================== GETTER BUTTONS ======================
    public JButton getBtnQuanLyTaiKhoan() { return btnQuanLyTaiKhoan; }
    public JButton getBtnQuanLySanPham() { return btnQuanLySanPham; }
    public JButton getBtnQuanLyDonHang() { return btnQuanLyDonHang; }
    public JButton getBtnBackLogin() { return btnBackLogin; }
}
