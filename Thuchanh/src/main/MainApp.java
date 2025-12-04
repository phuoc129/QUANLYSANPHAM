package main;

import java.awt.*;
import java.awt.event.*;
import controller.POSController;
import view.LoginPanel;
import view.MainFrame;

public class MainApp extends Frame {
    private POSController controller;
    private LoginPanel loginPanel;
    
    public MainApp() {
        controller = new POSController();
        setupLoginUI();
    }
    
    private void setupLoginUI() {
        setTitle("Point of Sale System - Đăng nhập");
        setSize(550, 450);
        setLayout(new BorderLayout());
        
        loginPanel = new LoginPanel(controller, this);
        add(loginPanel, BorderLayout.CENTER);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                controller.disconnect();
                System.exit(0);
            }
        });
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public void onLoginSuccess() {
        dispose();
        new MainFrame(controller);
    }
    
    public static void main(String[] args) {
        try {
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new MainApp();
    }
}