package models;

public class AdminModelTest {
    public static void main(String[] args) {
        AdminModel admin = new AdminModel();
        admin.promoteRegularUserToAdmin(5);
        System.out.println( admin.getUnpayedUserIds());
        System.out.println(admin.checkUserPaymentStatus(1));
        System.out.println(admin.getAllRentalRecords());
    }
    
}
