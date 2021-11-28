package models;

public class ClientModelTest {
    public static void main(String args[]) {
        // simulate check in, approve checkout and checkout
        ClientModel client = new ClientModel();
        AdminModel admin = new AdminModel();
        client.initTable();
        for (int i =2; i < 15; i ++ ) {
            client.checkIn(2);
            admin.askClientToPay(2);
            client.checkOut(2);
        }

    }
}