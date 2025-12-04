package Model;

import DAO.LoginDAO;

public class ServerModel {
    private LoginDAO DAO;
    private Usertxt usertxt;

    public ServerModel() {
        DAO = new LoginDAO();
        usertxt = new Usertxt();
    }


    public User loginDatabase(String username, String password) {
        return DAO.login(username, password);
    }

    public User loginTxt(String username, String password) {
        return usertxt.readUsers().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public User login(String username, String password) {
        User user = loginDatabase(username, password);
        if(user != null) return user;
        return loginTxt(username, password);
    }
}
