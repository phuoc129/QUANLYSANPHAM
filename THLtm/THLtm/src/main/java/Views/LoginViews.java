package Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginViews extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginViews() {
        setTitle("Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);


        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(labelFont);
        panel.add(lblUser, gbc);


        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtUsername = new JTextField(15);
        txtUsername.setFont(inputFont);
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                txtUsername.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.add(txtUsername, gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(labelFont);
        panel.add(lblPass, gbc);


        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtPassword = new JPasswordField(15);
        txtPassword.setFont(inputFont);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                txtPassword.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.add(txtPassword, gbc);


        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        btnLogin = new JButton("Login");
        btnLogin.setPreferredSize(new Dimension(120, 35));
        btnLogin.setBackground(new Color(33, 150, 243));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));


        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(30, 136, 229));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(33, 150, 243));
            }
        });

        panel.add(btnLogin, gbc);

        add(panel);

        getRootPane().setDefaultButton(btnLogin);

        setVisible(true);
    }


    public String getUsername() {
        return txtUsername.getText();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public void addLoginListener(ActionListener listener) {
        btnLogin.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
