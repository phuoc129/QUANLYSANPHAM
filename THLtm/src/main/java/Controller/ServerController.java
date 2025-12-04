package Controller;

import Model.ServerModel;
import Model.User;
import Views.ServerViews;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerController {
    private ServerModel model;
    private ServerViews view;
    private boolean isServerShuttingDown = false;

    public ServerController(ServerModel model, ServerViews view) {
        this.model = model;
        this.view = view;
    }

    public void setServerShuttingDown(boolean flag) {
        this.isServerShuttingDown = flag;
    }

    public void handleClient(Socket clientSocket) {
        String clientName = clientSocket.getInetAddress().toString() + ":" + clientSocket.getPort();

        try (ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {

            oos.flush(); // flush ngay sau tạo ObjectOutputStream
            view.showMessage("Kết nối mới từ: " + clientName);

            boolean isConnected = true;
            while (isConnected) {
                try {
                    Object requestObj = ois.readObject();
                    if (requestObj == null) continue;

                    String command = requestObj.toString().trim();

                    if (command.equalsIgnoreCase("LOGIN")) {
                        String username = (String) ois.readObject();
                        String password = (String) ois.readObject();
                        User user = model.loginDatabase(username, password);
                        if (user == null) user = model.loginTxt(username, password);

                        if (user == null) {
                            oos.writeObject("FAIL");
                            oos.flush();
                            view.showMessage(username + " đăng nhập thất bại!");
                        } else {
                            oos.writeObject("SUCCESS:" + user.getRole());
                            oos.flush();
                            view.showMessage(username + " đăng nhập thành công (" + user.getRole() + ")");
                        }

                    } else if (command.equalsIgnoreCase("LOGOUT")) {
                        view.showMessage("Client đã logout: " + clientName);
                        isConnected = false; // thoát vòng lặp
                    } else {
                        view.showMessage("Nhận từ client: " + command);
                        oos.writeObject("Server nhận: " + command);
                        oos.flush();
                    }
                } catch (Exception e) {
                    if (isServerShuttingDown) {
                        view.showMessage(clientName + " ngắt kết nối do server tắt.");
                    } else {
                        view.showMessage(clientName + " ngắt kết nối bất ngờ!");
                    }
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            view.showMessage("Lỗi kết nối với client " + clientName);
        } finally {
            try {
                if (!clientSocket.isClosed()) clientSocket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
