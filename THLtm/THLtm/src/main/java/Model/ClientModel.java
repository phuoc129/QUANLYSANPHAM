package Model;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientModel {
    private String serverIP;
    private int serverPort;

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public ClientModel(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }


    public void connect() throws IOException {
        if (socket == null || socket.isClosed()) {
            socket = new Socket(serverIP, serverPort);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());
        }
    }


    public String login(String username, String password) {
        try {
            connect();
            oos.writeObject("LOGIN");
            oos.writeObject(username);
            oos.writeObject(password);
            oos.flush();

            Object response = ois.readObject();
            return response.toString();

        } catch (SocketException se) {
            System.out.println("Server đã đóng kết nối: " + se.getMessage());
            closeConnection();
            return "FAIL";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }


    public void send(String msg) {
        try {
            if (socket != null && !socket.isClosed()) {
                oos.writeObject(msg);
                oos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            closeConnection();
        }
    }


    public Object receive() {
        try {
            if (socket != null && !socket.isClosed()) {
                return ois.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
            closeConnection();
        }
        return null;
    }

    // Logout
    public void logout() {
        send("LOGOUT");
        closeConnection();
    }


    public void closeConnection() {
        try {
            if (oos != null) oos.close();
            if (ois != null) ois.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket = null;
            oos = null;
            ois = null;
        }
    }
}
