package Server;

import Controller.ServerController;
import Model.ServerModel;
import Views.ServerViews;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static List<Socket> clientSockets = new ArrayList<>();

    public static void main(String[] args) {
        ServerModel model = new ServerModel();
        ServerViews view = new ServerViews();
        ServerController controller = new ServerController(model, view);

        try (ServerSocket serverSocket = new ServerSocket(9999)) {
            view.showMessage("Server đang chạy trên cổng 9999...");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                controller.setServerShuttingDown(true);
                view.showMessage("Server đang tắt, ngắt kết nối tất cả client...");
                for (Socket s : clientSockets) {
                    try {
                        if (s != null && !s.isClosed()) s.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientSockets.add(clientSocket);
                view.showMessage("Kết nối mới từ: " + clientSocket.getInetAddress().toString() + ":" + clientSocket.getPort());

                new Thread(() -> {
                    controller.handleClient(clientSocket);
                    clientSockets.remove(clientSocket);
                }).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
