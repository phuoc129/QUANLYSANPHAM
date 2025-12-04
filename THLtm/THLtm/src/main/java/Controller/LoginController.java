package Controller;

import Model.ClientModel;
import Views.LoginViews;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController {

    private LoginViews view;
    private ClientModel model;
    private ClientController mainController;

    public LoginController(LoginViews view, ClientModel model, ClientController mainController) {
        this.view = view;
        this.model = model;
        this.mainController = mainController;

        this.view.addLoginListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
    }

    private void login() {
        String username = view.getUsername();
        String password = view.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            view.showMessage("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        String result = model.login(username, password);

        if (result == null || result.startsWith("ERROR")) {
            view.showMessage("Không thể kết nối server hoặc server đã ngắt kết nối!");
            return;
        }

        if (result.startsWith("SUCCESS")) {
            String role = result.split(":")[1];
            if (role.equalsIgnoreCase("admin")) {
                view.showMessage("Đăng nhập thành công với tư cách Người quản lý!");
            } else if (role.equalsIgnoreCase("nhanvien")) {
                view.showMessage("Đăng nhập thành công với tư cách Nhân viên!");
            } else {
                view.showMessage("Đăng nhập thành công với tư cách: " + role);
            }
            mainController.navigateAfterLogin(role, view);
        } else if (result.equals("FAIL")) {
            view.showMessage("Sai username hoặc mật khẩu!");
        } else {
            view.showMessage("Không đăng nhập được!");
        }
    }
}
