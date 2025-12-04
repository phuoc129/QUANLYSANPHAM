package Views;

import Controller.AccountController;
import Controller.ClientController;
import Model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class AccountViews extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete, btnBack;
    private AccountController controller;
    private ClientController mainController;

    public AccountViews(AccountController controller, ClientController mainController) {
        this.controller = controller;
        this.mainController = mainController;

        initUI();
        loadTable();
        initEvents();
        setVisible(true);
    }

    private void initUI() {
        setTitle("Quản lý tài khoản");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color headerColor = new Color(52, 152, 219);   // xanh header
        Color btnBlue     = new Color(41, 128, 185);
        Color btnGreen    = new Color(39, 174, 96);
        Color btnRed      = new Color(231, 76, 60);
        Color btnGray     = new Color(127, 140, 141);

        setLayout(new BorderLayout());


        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(headerColor);
        header.setPreferredSize(new Dimension(900, 70));

        JLabel title = new JLabel("QUẢN LÝ TÀI KHOẢN", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.add(title, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);


        tableModel = new DefaultTableModel(
                new Object[]{"Username", "Password", "Vai trò", "Phone", "Mã NV"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setGridColor(new Color(220, 220, 220));
        table.setShowHorizontalLines(true);

        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 16));
        th.setBackground(new Color(52, 73, 94));
        th.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(scrollPane, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        btnAdd = createButton("Thêm", btnGreen);
        btnEdit = createButton("Sửa", btnBlue);
        btnDelete = createButton("Xóa", btnRed);
        btnBack = createButton("Quay lại", btnGray);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnBack);

        add(buttonPanel, BorderLayout.SOUTH);
    }


    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return btn;
    }

    private void initEvents() {
        btnAdd.addActionListener(e -> {
            User user = inputUserData(null);
            if (user != null && controller.addAccount(user)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!\nMã NV: " + user.getEmployeeCode());
                loadTable();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String username = (String) tableModel.getValueAt(row, 0);
                User user = inputUserData(username);

                if (user != null && controller.editAccount(user)) {
                    JOptionPane.showMessageDialog(this, "Sửa thành công!");
                    loadTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Sửa thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản để sửa!");
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String username = (String) tableModel.getValueAt(row, 0);
                if (controller.deleteAccount(username)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản để xóa!");
            }
        });

        btnBack.addActionListener(e -> {
            mainController.backToAdmin();
            dispose();
        });
    }

    private User inputUserData(String username) {
        JTextField txtUsername = new JTextField();
        if (username != null) {
            txtUsername.setText(username);
            txtUsername.setEnabled(false);
        }

        JTextField txtPassword = new JTextField();
        JTextField txtPhone = new JTextField();

        String[] roles = {"admin", "nhanvien"};
        JComboBox<String> roleBox = new JComboBox<>(roles);

        Object[] inputs = {
                "Username:", txtUsername,
                "Password:", txtPassword,
                "Vai trò:", roleBox,
                "Phone:", txtPhone
        };

        int result = JOptionPane.showConfirmDialog(this, inputs,
                username == null ? "Thêm tài khoản" : "Sửa tài khoản",
                JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) return null;

        String u = txtUsername.getText().trim();
        String p = txtPassword.getText().trim();
        String r = (String) roleBox.getSelectedItem();
        String ph = txtPhone.getText().trim();

        if (u.isEmpty() || p.isEmpty() || ph.isEmpty()) return null;

        String employeeCode = username == null ? controller.generateEmployeeCode() : null;

        return new User(
                u, p, r, ph,
                username == null ? employeeCode : tableModel.getValueAt(table.getSelectedRow(), 4).toString()
        );
    }

    public void loadTable() {
        tableModel.setRowCount(0);
        List<User> users = controller.loadAccounts();
        for (User user : users) {
            tableModel.addRow(new Object[]{
                    user.getUsername(),
                    user.getPassword(),
                    user.getRole(),
                    user.getPhone(),
                    user.getEmployeeCode()
            });
        }
    }
}
