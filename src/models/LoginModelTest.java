package models;

public class LoginModelTest {
    // script need to be runned when startup to create test users
    public static void main(String[] args) {
        LoginModel login = new LoginModel();
        AdminModel admin = new AdminModel();
        login.initTable();
        System.out.println(login.register("user1", "user1"));
        // set user1 as admin
        admin.promoteRegularUserToAdmin(1);
        // all users created below are regular user
        System.out.println(login.register("user2", "user2"));
        System.out.println(login.register("user3", "user3"));
        System.out.println(login.register("user4", "user4"));
        System.out.println(login.register("user5", "user5"));
        System.out.println(login.register("user6", "user6"));
        System.out.println(login.register("user7", "user7"));
        System.out.println(login.register("user8", "user8"));
        System.out.println(login.register("user9", "user9"));
        System.out.println(login.register("user10", "user10"));
        System.out.println(login.register("user11", "user11"));
        System.out.println(login.register("user12", "user12"));
        System.out.println(login.register("user13", "user13"));
        System.out.println(login.register("user14", "user14"));
        System.out.println(login.register("user15", "user15"));
        System.out.println(login.getCredentials("user2", "user2"));
    }
    
}
