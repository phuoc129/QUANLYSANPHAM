package Views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class ServerViews extends JFrame {

    private JTextArea txtLog;

    public ServerViews() {
        setTitle("Server Log");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo panel chính với background gradient
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(58, 123, 213);
                Color color2 = new Color(0, 210, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // Header
        JLabel header = new JLabel("SERVER LOG", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setForeground(Color.WHITE);
        header.setBorder(new EmptyBorder(10, 0, 10, 0));
        mainPanel.add(header, BorderLayout.NORTH);

        // TextArea log
        txtLog = new JTextArea();
        txtLog.setEditable(false);
        txtLog.setLineWrap(true);
        txtLog.setWrapStyleWord(true);
        txtLog.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtLog.setBackground(new Color(30, 30, 30));
        txtLog.setForeground(new Color(200, 200, 200));
        txtLog.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
                new EmptyBorder(5, 5, 5, 5)
        ));

        // Auto-scroll
        DefaultCaret caret = (DefaultCaret) txtLog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane scrollPane = new JScrollPane(txtLog);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer (tùy chọn)
        JLabel footer = new JLabel("Server is running...", SwingConstants.RIGHT);
        footer.setFont(new Font("Arial", Font.ITALIC, 12));
        footer.setForeground(Color.WHITE);
        footer.setBorder(new EmptyBorder(5, 5, 5, 5));
        mainPanel.add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void showMessage(String message) {
        txtLog.append(message + "\n");
    }
}
