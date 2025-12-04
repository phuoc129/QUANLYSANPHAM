package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Usertxt {
    private String file_name = "src/main/java/users.txt";


    public List<User> readUsers() {
        List<User> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+");

                // CHỈ USERNAME PASSWORD ROLE
                if (parts.length == 3) {
                    String username = parts[0];
                    String password = parts[1];
                    String role = parts[2];
                    list.add(new User(username, password, role, "", ""));
                }

                // ĐỦ 5 TRƯỜNG
                else if (parts.length == 5) {
                    String username = parts[0];
                    String password = parts[1];
                    String role = parts[2];
                    String phone = parts[3];
                    String code = parts[4];
                    list.add(new User(username, password, role, phone, code));
                }

                // DÒNG LỖI
                else {
                    System.out.println("Dòng không hợp lệ: " + line);
                }
            }

        } catch (Exception e) {
            System.out.println("Không thể đọc file users.txt");
        }
        return list;
    }


    private void writeUsers(List<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file_name))) {
            for (User u : users) {
                // Ghi tất cả thông tin trên cùng một dòng, cách nhau bởi khoảng trắng
                bw.write(u.getUsername() + " " + u.getPassword() + " " + u.getRole() + " " + u.getPhone() + " " + u.getEmployeeCode());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean addUser(User user) {
        List<User> users = readUsers();
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) return false; // username trùng
        }
        users.add(user);
        writeUsers(users);
        return true;
    }


    public boolean deleteUser(String username) {
        List<User> users = readUsers();
        boolean removed = users.removeIf(u -> u.getUsername().equals(username));
        if (removed) writeUsers(users);
        return removed;
    }

    public boolean updateUser(User updatedUser) {
        List<User> users = readUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(updatedUser.getUsername())) {
                users.set(i, updatedUser);
                writeUsers(users);
                return true;
            }
        }
        return false;
    }
}
