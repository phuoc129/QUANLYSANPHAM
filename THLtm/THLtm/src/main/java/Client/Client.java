package Client;

import Model.ClientModel;
import Controller.ClientController;

public class Client {
    public static void main(String[] args) {
        ClientModel model = new ClientModel("localhost", 9999);
        new ClientController(model);
    }
}
